package database.service;

import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import database.query.DBQuery;

public class CompareRunFormServiceImpl implements CompareRunFormService {

	private DBQuery dbQuery;

	public CompareRunFormServiceImpl() {
		this.dbQuery = new DBQuery();
	}

	public List<String> getAllRuns() {
		return this.dbQuery.getAllRuns();
	}

	public Dataset<Row> compareRun(String str) {
		return this.dbQuery.compareRun(str);
	}

	public Dataset<Row> getAllRunsDataset() {
		return this.dbQuery.getAllRunsDataset();
	}

	public List<String> getAllProblems() {
		return this.dbQuery.getAllProblems();

	}

	public Dataset<Row> getAllProblemsDataset() {
		return this.dbQuery.getAllProblemsDataset();

	}

}
