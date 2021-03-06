#!/usr/bin/perl -w
#*******************************************************************************
#* Copyright (c) 2012 Forschungszentrum Juelich GmbH.
#* All rights reserved. This program and the accompanying materials
#* are made available under the terms of the Eclipse Public License v1.0
#* which accompanies this distribution, and is available at
#* http://www.eclipse.org/legal/epl-v10.html
#*
#* Contributors:
#*    Wolfgang Frings (Forschungszentrum Juelich GmbH) 
#*******************************************************************************/ 
use strict;

use FindBin;
use lib "$FindBin::RealBin/../../lib";
use LML_da_util;

my $patint="([\\+\\-\\d]+)";   # Pattern for Integer number
my $patfp ="([\\+\\-\\d.E]+)"; # Pattern for Floating Point number
my $patwrd="([\^\\s]+)";       # Pattern for Work (all noblank characters)
my $patbl ="\\s+";             # Pattern for blank space (variable length)

#####################################################################
# get user info / check system 
#####################################################################
my $UserID = getpwuid($<);
my $Hostname = `hostname`;
my $verbose=1;
my ($line,%blocks,%nodenr,$key,$value,$count,%notmappedkeys,%notfoundkeys);

#unless( ($Hostname =~ /jugenes\d/) && ($UserID =~ /llstat/) ) {
#  die "da_blocks_info_LML.pl can only be used as llstat on jugenesX!";
#}

#####################################################################
# get command line parameter
#####################################################################
if ($#ARGV != 0) {
  die " Usage: $0 <filename> $#ARGV\n";
}
my $filename = $ARGV[0];

my $system_sysprio=-1;
my $maxtopdogs=-1;

my %mapping = (
    "Block_Status"                           => "status",
    "Booter"                                 => "booter",
    "Connectivity"                           => "connect",
    "Description"                            => "description",
    "IOLinks_/_Midplane"                     => "IOlinks",
    "Midplane_List"                          => "midplanes",
    "MloaderImage"                           => "loaderimage",
    "Node_Configuration"                     => "nodeconf",
    "Owner"                                  => "owner",
    "Shape"                                  => "shape",
    "Size"                                   => "size",
    "Small_Blocks"                           => "smallblock",
    "Users"                                  => "users",
    "Node_Board_List"                        => "nodeboards",
    "Number_of_IOLinks"                      => "numiolinks",
    "id"                                     => "id",
    # unknown attributes
    );


my $cmd="/usr/bin/llbgstatus";
$cmd=$ENV{"CMD_BLOCKINFO"} if($ENV{"CMD_BLOCKINFO"}); 

open(IN," $cmd -B all |");
my $blockid="-";
my $lastkey="-";


while($line=<IN>) {
    chomp($line);
    next if ($line=~/^\-+$/);
    next if ($line=~/^\=+$/);

    if($line=~/^\s*Block Name\s*[:]\s*$patwrd/) {
	$blockid=$1;
	$blocks{$blockid}{id}=$blockid;
#	print "line $line\n";
    } elsif($line=~/^\s*([^\:]+)\s*\:\s+(.*)$/) {
	($key,$value)=($1,$2);
	$key=~s/\s/_/gs;
	$lastkey=$key;
	$blocks{$blockid}{$key}=$value;
    } elsif($line=~/^\s*([^\:]+)\s*\:\s*$/) {
	($key)=($1);
	$key=~s/\s/_/gs;
	$lastkey=$key;
	$blocks{$blockid}{$key}="";
    } else {
	$line=~s/^\s*//gs;
	$blocks{$blockid}{$lastkey}.=$line;
    }
}
close(IN);

# add unknown but manatory attributes to blocks
foreach $blockid (keys(%blocks)) {
    my($key,$value,$pair);
    if(exists($blocks{$blockid}{status})) {
	foreach $pair (split(/,/,$blocks{$blockid}{status})) {
	    ($key,$value)=split(/=/,$pair);
	    $blocks{$blockid}{$key}=$value;
	}
    } 

}

open(OUT,"> $filename") || die "cannot open file $filename";
printf(OUT "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
printf(OUT "<lml:lgui xmlns:lml=\"http://eclipse.org/ptp/lml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
printf(OUT "	xsi:schemaLocation=\"http://eclipse.org/ptp/lml http://eclipse.org/ptp/schemas/v1.1/lgui.xsd\"\n");
printf(OUT "	version=\"1.1\"\>\n");
printf(OUT "<objects>\n");
$count=0;
foreach $blockid (sort(keys(%blocks))) {
    next if($blocks{$blockid}{"Block_Status"} ne "Initialized");
    $count++;$nodenr{$blockid}=$count;
    printf(OUT "<object id=\"bgj%06d\" name=\"%s\" type=\"partition\"/>\n",$count,&LML_da_util::escapeForXML($blockid));
}
printf(OUT "</objects>\n");
printf(OUT "<information>\n");
foreach $blockid (sort(keys(%blocks))) {
    next if($blocks{$blockid}{"Block_Status"} ne "Initialized");
    printf(OUT "<info oid=\"bgj%06d\" type=\"short\">\n",$nodenr{$blockid});
    foreach $key (sort(keys(%{$blocks{$blockid}}))) {
	if(exists($mapping{$key})) {
	    if($mapping{$key} ne "") {
		$value=&modify($key,$mapping{$key},$blocks{$blockid}{$key});
		if($value) {
		    printf(OUT " <data %-20s value=\"%s\"/>\n","key=\"".$mapping{$key}."\"",&LML_da_util::escapeForXML($value));
		}
	    } else {
		$notmappedkeys{$key}++;
	    }
	} else {
	    $notfoundkeys{$key}++;
	}
    }
    printf(OUT "</info>\n");
}
printf(OUT "</information>\n");
 
printf(OUT "</lml:lgui>\n");

close(OUT);

foreach $key (sort(keys(%notfoundkeys))) {
    printf("%-40s => \"\",\n","\"".$key."\"",$notfoundkeys{$key});
}



sub modify {
    my($key,$mkey,$value)=@_;
    my $ret=$value;

    if($mkey eq "owner") {
	$ret=~s/\@.*//gs;
    }

    if($mkey eq "state") {
	$ret="Running"  if ($value eq "Running");
	$ret="Down"     if ($value eq "Down");
	$ret="Idle"     if ($value eq "Idle");
	$ret="unknown"  if ($value eq "unknown");
    }

    if(($mkey eq "wall") || ($mkey eq "wallsoft")) {
	if($value=~/\($patint seconds\)/) {
	    $ret=$1;
	}
	if($value=~/$patint minutes/) {
	    $ret=$1*60;
	}
	if($value=~/^$patint[:]$patint[:]$patint$/) {
	    $ret=$1*60*60+$2*60+$3;
	}
    }

    if($mkey eq "nodelist") {
	if($ret ne "-") {
	    $ret=~s/\//,/gs;
	    my @blocks = split(/\+/,$ret);
	    $ret="(".join(')(',@blocks).")";
	}
    }

    if($mkey eq "totalcores") {
	if($ret=~/$patint[:]ppn=$patint/) {
	    $ret=$1*$2;
	}
    }
    if($mkey eq "totaltasks") {
	if($ret=~/$patint[:]ppn=$patint/) {
	    $ret=$1*$2;
	}
    }

    if(($mkey eq "comment")) {
	$ret=~s/\"//gs;
    }
    if(($mkey eq "bgp_state")) {
	$ret=~s/\<unknown\>/unknown/gs;
    }

    return($ret);
}
