#!/apps/bin/perl -w
use strict;
use warnings;
use feature qw{ say };
use XML::Twig;

#Global Variable
	my $scriptDir  = "target/";
	my $javaScript = $scriptDir."DCFaultFinderApp-jar-with-dependencies.jar";
	my $envFile    = "environment.cshrc";

#Ok, I dont understand how to use POD:Usage along with other options, so I will hard code the usage here

if ( defined $ARGV[0] && $ARGV[0] eq "--help" ) {
	print STDOUT "Options:\n",
	  "--help				this \n \n",
	  "Usage:\n",
	  "./runDCFaultFinder.pl  \n",
	  "Notes:\n",
"Running this will set your environment according to the CLASTAG set in the default  \n",
	  "setting in the /group/clas12/gemc/environment.csh script",
	  ;
}
if ( CheckFiles() ) {

	system("source $envFile");
	system("java -Xms1024m -jar $javaScript");
}

sub CheckFiles {


	if ( !( -d $scriptDir ) ) {
		print STDERR "Script not running in parent directory \n",
		  "or package is not built";
		return 0;
	}
	elsif ( !( -e $javaScript ) ) {
		print STDERR "It appears that the package is not built \n", return 0;
	}
	elsif ( !( -e $envFile ) ) {
		print STDERR
		  "It appears that the environment.cshrc is ",
		  "missing from this directory \n";
		return 0;
	}
	else {
		return 1;
	}

}

