package database.query;

import java.util.List;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import database.objects.StatusChangeDB;
import spark.utils.SparkManager;

public class DBQuery {

	public DBQuery() {
	}

	private Dataset<StatusChangeDB> getBadWires(String str) {
		Dataset<Row> tempDF = SparkManager.mySqlDataset();
		Dataset<StatusChangeDB> typedDataset = tempDF.as(SparkManager.statusChangeDBEncoder())
				.filter((FilterFunction<StatusChangeDB>) response -> response.getRunno() == Integer.parseInt(str))
				.filter((FilterFunction<StatusChangeDB>) response -> response.getStatus_change_type().equals("broke"));
		return typedDataset;

	}

	private Dataset<StatusChangeDB> getBadWires(int runnoNumber) {
		Dataset<Row> tempDF = SparkManager.mySqlDataset();
		Dataset<StatusChangeDB> typedDataset = tempDF.as(SparkManager.statusChangeDBEncoder())
				.filter((FilterFunction<StatusChangeDB>) response -> response.getRunno() == runnoNumber)
				.filter((FilterFunction<StatusChangeDB>) response -> response.getStatus_change_type().equals("broke"));
		return typedDataset;

	}

	private Dataset<StatusChangeDB> getFixedWires(String str) {
		Dataset<Row> tempDF = SparkManager.mySqlDataset();
		Dataset<StatusChangeDB> typedDataset = tempDF.as(SparkManager.statusChangeDBEncoder())
				.filter((FilterFunction<StatusChangeDB>) response -> response.getRunno() == Integer.parseInt(str))
				.filter((FilterFunction<StatusChangeDB>) response -> response.getStatus_change_type().equals("fixed"));
		return typedDataset;

	}

	private Dataset<StatusChangeDB> getFixedWires(int runno) {
		Dataset<Row> tempDF = SparkManager.mySqlDataset();
		Dataset<StatusChangeDB> typedDataset = tempDF.as(SparkManager.statusChangeDBEncoder())
				.filter((FilterFunction<StatusChangeDB>) response -> response.getRunno() == runno)
				.filter((FilterFunction<StatusChangeDB>) response -> response.getStatus_change_type().equals("fixed"));
		return typedDataset;

	}

	public List<StatusChangeDB> getBadWireList(String str) {
		List<StatusChangeDB> alist = getBadWires(str).collectAsList();
		return alist;
	}

	public List<StatusChangeDB> getBadWireList(int runno) {
		List<StatusChangeDB> alist = getBadWires(runno).collectAsList();
		return alist;
	}

	public List<StatusChangeDB> getFixedWireList(String str) {
		List<StatusChangeDB> alist = getFixedWires(str).collectAsList();
		return alist;
	}

	public List<StatusChangeDB> getFixedWireList(int runno) {
		List<StatusChangeDB> alist = getFixedWires(runno).collectAsList();
		return alist;
	}
}
