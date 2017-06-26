package database.service;

import static org.apache.spark.sql.functions.col;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import database.objects.StatusChangeDB;
import database.query.DBQuery;
import database.utils.Coordinate;
import database.utils.MainFrameServiceManager;
import spark.utils.SparkManager;

public class CompareRunFormServiceImpl implements CompareRunFormService {

	private DBQuery dbQuery = null;
	private MainFrameService mainFrameService = null;

	public CompareRunFormServiceImpl() {
		this.dbQuery = new DBQuery();
		this.mainFrameService = MainFrameServiceManager.getSession();
	}

	public List<String> getAllRuns() {
		return this.dbQuery.getAllRuns();
	}

	// public Dataset<Row> compareRun(String str) {
	// compareRunII(str);
	// return this.dbQuery.compareRun(str);
	// }

	public void compareRun(String str) {
		compareSets(str);
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

	private void compareSets(String str) {
		Dataset<StatusChangeDB> dbQuery = this.dbQuery.compareRunII(str);

		for (int i = 1; i < 7; i++) {// superLayer
			for (int j = 1; j < 7; j++) { // sector
				List<StatusChangeDB> dataList = new ArrayList<StatusChangeDB>();
				List<StatusChangeDB> sqlList = new ArrayList<StatusChangeDB>();

				dataList.addAll(this.mainFrameService.getDatasetByMap(i, j).collectAsList());

				sqlList.addAll(
						dbQuery.filter(col("superLayer").equalTo(i)).filter(col("sector").equalTo(j)).collectAsList());

				TreeSet<StatusChangeDB> listToRemove = new TreeSet<>();
				TreeSet<StatusChangeDB> listToSql = new TreeSet<>();

				for (StatusChangeDB ro : sqlList) {
					for (StatusChangeDB statusChangeDB : dataList) {
						if (statusChangeDB.getSector().equals(ro.getSector())
								&& statusChangeDB.getSuperlayer().equals(ro.getSuperlayer())
								&& statusChangeDB.getLoclayer().equals(ro.getLoclayer())
								&& statusChangeDB.getLocwire().equals(ro.getLocwire())) {
							listToRemove.add(statusChangeDB);
							ro.setRunno(this.mainFrameService.getRunNumber());
							listToSql.add(ro);

						}
					}
				}
				dataList.removeAll(listToRemove);
				this.mainFrameService.addToCompleteSQLList(listToSql);

				this.mainFrameService.getComparedDataSetMap().put(new Coordinate(i - 1, j - 1),
						SparkManager.getSession().createDataset(dataList, SparkManager.statusChangeDBEncoder()));
			}
		}

	}

}
