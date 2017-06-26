package database.query;

import static org.apache.spark.sql.functions.asc;
import static org.apache.spark.sql.functions.col;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

import database.objects.StatusChangeDB;
import spark.utils.SparkManager;

public class DBQuery {

	public DBQuery() {
	}

	public List<String> getAllRuns() {
		Dataset<Row> dataDF = SparkManager.mySqlDataset().select("runno").sort(asc("runno")).distinct();
		return dataDF.map(row -> row.mkString(), Encoders.STRING()).collectAsList();
	}

	public Dataset<Row> getAllRunsDataset() {
		return SparkManager.mySqlDataset().select("runno").sort(asc("runno")).distinct();
	}

	public Dataset<Row> compareRun(String str) {
		return SparkManager.mySqlDataset().select("loclayer", "superLayer", "sector", "locwire", "status_change_type")
				.filter(col("runno").equalTo(str));
	}

	public Dataset<StatusChangeDB> compareRunII(String str) {
		Dataset<Row> tempDF = SparkManager.mySqlDataset().filter(col("runno").equalTo(str));
		return tempDF.as(SparkManager.statusChangeDBEncoder());

	}

	public List<String> getAllProblems() {
		Dataset<Row> dataDF = SparkManager.mySqlDataset().select("problem_type").sort(asc("problem_type")).distinct();
		return dataDF.map(row -> row.mkString(), Encoders.STRING()).collectAsList();
	}

	public Dataset<Row> getAllProblemsDataset() {
		return SparkManager.mySqlDataset().select("problem_type").sort(asc("problem_type")).distinct();

	}

	public static void main(String[] args) {
		Logger.getLogger("org.apache.spark.SparkContext").setLevel(Level.WARN);
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
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
		DBQuery dbQuery = new DBQuery();
		Dataset<Row> dataDF = dbQuery.compareRun("762");
		dataDF.show();

		Dataset<StatusChangeDB> dataset = dbQuery.compareRunII("762");
		dataset.show();
		// dataDF.foreach((ForeachFunction<Row>) row -> System.out
		// .println("Run from Query " + row.get(0) + " " + row.get(1) + " " +
		// row.get(2) + " "));

	}
}
