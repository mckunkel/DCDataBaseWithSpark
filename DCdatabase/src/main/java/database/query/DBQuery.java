package database.query;

import static org.apache.spark.sql.functions.asc;
import static org.apache.spark.sql.functions.col;

import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

import spark.utils.SparkMySQLConnection;

public class DBQuery {

	public DBQuery() {
	}

	public List<String> getAllRuns() {
		Dataset<Row> dataDF = SparkMySQLConnection.mySqlDataset().select("runno").sort(asc("runno")).distinct();
		List<String> listTwo = dataDF.map(row -> row.mkString(), Encoders.STRING()).collectAsList();
		return listTwo;
	}

	public Dataset<Row> getAllRunsDataset() {
		return SparkMySQLConnection.mySqlDataset().select("runno").sort(asc("runno")).distinct();
	}

	public Dataset<Row> compareRun(String str) {
		return SparkMySQLConnection.mySqlDataset()
				.select("loclayer", "superLayer", "sector", "locwire", "status_change_type")
				.filter(col("runno").equalTo(str));
	}

	public static void main(String[] args) {
		// List<String> test = DBQuery.getAllRuns();
		// System.out.println("######################");
		// System.out.println(test.size());
		// for (String str : test) {
		// System.out.println(str + " is the run number");
		// }
		// System.out.println("######################");
		// Dataset<Row> dataDF = DBQuery.getAllRunsDataset();
		// dataDF.foreach((ForeachFunction<Row>) row -> System.out.println("Run
		// from Query " + row.get(0) + ""));
		// Dataset<Row> dataDF = DBQuery.compareRun("806");
		// dataDF.show();
		// dataDF.foreach((ForeachFunction<Row>) row -> System.out
		// .println("Run from Query " + row.get(0) + " " + row.get(1) + " " +
		// row.get(2) + " "));

	}
}
