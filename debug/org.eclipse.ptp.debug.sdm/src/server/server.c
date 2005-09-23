/*
 * server() has two roles. The first is to launch the process being debugged 
 * (debuggee) under the control of a debugger (us). The second is to manage 
 * communication with the client process.
 *
 * Note that there will be num_procs+1 [0..num_procs] processes in our 
 * communicator, where num_procs is the number of processes in the parallel 
 * job being debugged. To simplify the accounting, we use the task id of
 * num_procs as the client task id and [0..num_procs-1] for the server
 * task ids.
 */

#include <mpi.h>
#include <stdlib.h>
#include <string.h>

#include "hash.h"
#include "backend.h"

extern int	svr_init(dbg_backend *, void (*)(dbg_event *, void *), void *);
extern int	svr_dispatch(dbg_backend *, char *);
extern int	svr_progress(dbg_backend *);

static void
event_callback(dbg_event *e, void *data)
{
	int		task_id = *((int *)data);
	int		len;
	char *	reply_buf;
	unsigned int	hdr[2];
		
	if (proxy_tcp_event_to_str(e, &reply_buf) < 0)
		reply_buf = strdup("ERROR");
	
	len = strlen(reply_buf);

	hdr[0] = HashCompute(reply_buf, len);
	hdr[1] = len;
	
	MPI_Send(hdr, 2, MPI_UNSIGNED, task_id, 0, MPI_COMM_WORLD);
	MPI_Send(reply_buf, strlen(reply_buf), MPI_CHAR, task_id, 0, MPI_COMM_WORLD);
	
	free(reply_buf);
}

/*
 * Receive and process a command from the client. Return the reponse to the client.
 * 
 * @return	0 for normal command dispatch
 * 			1 for server shutdown
 * 			-1 for other errors
 */
static int
do_commands(dbg_backend *dbgr, int client_task_id, int my_task_id)
{
	int			len;
	int			flag;
	int			ret = 0;
	char *		cmd_buf;
	MPI_Status	stat;

	MPI_Iprobe(client_task_id, 0, MPI_COMM_WORLD, &flag, &stat);
	
	if (flag == 0)
		return 0;
		
	MPI_Get_count(&stat, MPI_CHAR, &len);
	
	cmd_buf = (char *)malloc(len + 1);
	MPI_Recv(cmd_buf, len, MPI_CHAR, client_task_id, 0, MPI_COMM_WORLD, &stat);
	cmd_buf[len] = '\0';

	ret = svr_dispatch(dbgr, cmd_buf);
	
	free(cmd_buf);
	
	return ret;
}

/*
 * Debug server implementation
 * 
 * NOTE: currently the debug server does not support multiple simulutaneous command execution. That is, it
 * will only process one command at at time, and will not accept another command until a reply
 * has been sent. The client may send multiple commands, but it is the underlying transport
 * that is providing the command buffering. 
 * 
 * This approach should be sufficient for all debugging tasks if we assume that the debugger operates
 * in one of two states: ACCEPTING_COMMANDS and PROCESSING_COMMAND and will only accept additional commands
 * when it is in the ACCEPTING_COMMANDS state. E.g.
 * 
 * Command		Debugger State		GDB State
 * 
 * --SLB-->		ACCEPTING_COMMANDS	SUSPENDED
 * 				PROCESSING_COMMAND	SUSPENDED
 * <--OK---		ACCEPTING_COMMANDS	SUSPENDED
 * 
 * --GOP-->		ACCEPTING_COMMANDS	SUSPENDED
 * 				PROCESSING_COMMAND	RUNNING
 * 				PROCESSING_COMMAND	SUSPENDED
 * <--OK---		ACCEPTING_COMMANDS	SUSPENDED
 * 
 * The exception to this is the INT command which can be sent at any time. This works by sending
 * with a special MPI tag used of OOB communication. It will be implemented later...
 * 
 */
void
server(int client_task_id, int my_task_id, dbg_backend *dbgr)
{
	//int signal;
	//char status;
	//char **args;
	
	printf("starting server on [%d]\n", my_task_id);
	
	//unpack_executable(&args);
	
	//initalize_low();
	
	//signal = start_inferior(&args, &status);
	srand(my_task_id);
	
	svr_init(dbgr, event_callback, (void *)&client_task_id);
	
	for (;;) {
		do_commands(dbgr, client_task_id, my_task_id);
		if (svr_progress(dbgr) < 0)
			break;
	}
}