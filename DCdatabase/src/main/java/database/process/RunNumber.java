package database.process;

import org.jlab.io.hipo.HipoDataSource;

public class RunNumber {

	public static int getRunNumber(HipoDataSource reader) {

		return reader.gotoEvent(0).getBank("RUN::config").getInt("run", 0);
	}

	public static void main(String[] args) {
		HipoDataSource chain = new HipoDataSource();
		chain.open("/Users/Mike/Google\\ Drive/Work/CLAS/CLAS12/DCdatabaseV/out_clas12_000762_a00001.hipo");
		int runNumber = RunNumber.getRunNumber(chain);
		System.out.println(runNumber);
	}

}
