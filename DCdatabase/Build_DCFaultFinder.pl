#!/usr/bin/perl
use strict;
use warnings;
use feature qw{ say };
use XML::Twig;
use Data::Dumper;
###!/apps/bin/perl -w
#Global variables
my $maven_Dir = "$ENV{HOME}/.m2";

#my $maven_Dir     = "$ENV{HOME}/TestMaven";
my $settings_File = "settings.xml";

#Check if you are building on jlab
my $onJlab   = 0;
my $hostName = `hostname`;

#if ( $hostName =~ m/.jlab.org/ ) {
ProcessJLab();
DeleteFile();

#}

sub ProcessJLab {
	if ( CheckMaven() ) {
		print("Maven directory is ready. \n");
		print("Checking COATJava distribution. \n");
		ProcessCOATJava();
	}
	else {
		print STDERR "There is an error, \n
         Maven directory is not ready. \n 
         Probably ~/.m2/settings.xml already exists with incorrect proxy settings. \n
         See hint above. \n";
	}
}

sub DeleteFile {

	if ( !( $hostName =~ m/.jlab.org/ ) ) {
		print("Deleting file, remove this method once beta testing is over");
		system( "rm " . $maven_Dir . "/" . $settings_File );
	}

}

sub CheckMaven {

	#No Maven directory, therefore no settings.xml
	if ( !-d $maven_Dir ) {
		system("mkdir $maven_Dir");
		system("cp $settings_File $maven_Dir");
		return 1;
	}

	#There is a .m2 directory, lets check if settings are there
	else {
		if ( !-e ( $maven_Dir . "/" . $settings_File ) ) {
			system("cp $settings_File $maven_Dir");
			return 1;
		} #There is a .m2 directory and settings.xml, lets check if settings have the correct Jlab settings
		else {
			return CheckSettingsFile();
		}
	}

}

sub CheckSettingsFile {

	my ( $PORT, $HOST, $PROTOCOL, $ACTIVE ) = ( 8082, 'jprox', 'http', 'true' );
	my $twig =
	  'XML::Twig'->new( pretty_print => 'indented', )
	  ->parsefile( $maven_Dir . "/" . $settings_File );

	my $root = $twig->root;
	my ($proxies) = $root->children('proxies');

	if ( !$proxies ) {
		print STDERR "In your settings.xml located in your .m2 folder,
         there appears to be no proxy settings.
         Adding Jlab proxy settings \n";

		my $proxies = $root->insert_new_elt('proxies');
		$proxies->set_inner_xml(
			    "<proxy><active>$ACTIVE</active><protocol>$PROTOCOL</protocol>"
			  . "<host>$HOST</host><port>$PORT</port></proxy>" );
		$root->print;

		my $outDir = $maven_Dir . "/" . $settings_File;
		open( my $fh_out, '>', $outDir )
		  or die "unable to open '$outDir' for writing: $!";
		print {$fh_out} $twig->sprint();
		return 1;

	}
	else {
		my $found;
		for my $proxy ( $proxies->children('proxy') ) {
			my $port     = $proxy->first_child_text('port');
			my $host     = $proxy->first_child_text('host');
			my $protocol = $proxy->first_child_text('protocol');
			my $active   = $proxy->first_child_text('active');

			$found = 1
			  if $port == $PORT
			  && $host eq $HOST
			  && $protocol eq $PROTOCOL
			  && $active eq $ACTIVE;
		}
		if ($found) {
			print STDERR "Correct proxy settings are set properly for Jlab \n";
			return 1;
		}
		else {
			print STDERR "Correct proxy settings missing:\n",
			  "port $PORT\nhost $HOST\nprotocol $PROTOCOL\nactive $ACTIVE\n";
			return 0;

		}
	}

}

sub ProcessCOATJava {

	my $coatDir         = $maven_Dir . "/repository/org/jlab/coat/coat-libs";
	my $coatJavaTag     = 1;
	my $getTag          = 0;
	my $coatJavaVersion = 5.1;
	if ( defined $ARGV[1] ) {
		$coatJavaVersion = $ARGV[1];
	}
	if ( defined $ARGV[2] ) {
		$coatJavaTag = $ARGV[2];
		$getTag      = 1;
	}

	if ( -d $coatDir . "/" . $coatJavaVersion . "-SNAPSHOT" ) {
		if(BuildNew( $coatJavaVersion, $coatJavaTag )){
			
		}else{
			BuildDCFaultFinder();
		}
	}
	my $gitCommand = '';

}

sub BuildNew {
	my ( $coatJavaVersion, $coatJavaTag ) = @_;

	print( $coatJavaVersion. "-SNAPSHOT already exist. " );
	print( "Build new " . $coatJavaVersion . "-SNAPSHOT?  (yes/no) " );
	my $input = <>;
	chomp($input);
	if ( $input eq "yes" ) {
		return 1;
	}
	elsif ( $input eq "no" ) {
		return 0;
	}
	else {
		BuildNew( $coatJavaVersion, $coatJavaTag );
	}

}
sub BuildDCFaultFinder{
	
}