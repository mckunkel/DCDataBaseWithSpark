/*  +__^_________,_________,_____,________^-.-------------------,
 *  | |||||||||   `--------'     |          |                   O
 *  `+-------------USMC----------^----------|___________________|
 *    `\_,---------,---------,--------------'
 *      / X MK X /'|       /'
 *     / X MK X /  `\    /'
 *    / X MK X /`-------'
 *   / X MK X /
 *  / X MK X /
 * (________(                @author m.c.kunkel
 *  `------'
*/
package database.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

import database.objects.StatusChangeDB;
import database.utils.MainFrameServiceManager;
import spark.utils.SparkManager;

public class InsertMYSqlQueryImpl implements InsertMYSqlQuery {
	private MainFrameService mainFrameService = null;

	public InsertMYSqlQueryImpl() {
		this.mainFrameService = MainFrameServiceManager.getSession();
	}

	@Override
	public void prepareMYSQLQuery() {
		List<StatusChangeDB> aList = new ArrayList<>();
		Timestamp timestamp = java.sql.Timestamp.from(java.time.Instant.now());
		System.out.println(timestamp);
		for (StatusChangeDB statusChangeDB : this.mainFrameService.getCompleteSQLList()) {
			statusChangeDB.setStatchangeid(0);
			statusChangeDB.setDateofentry(timestamp);

		}

		aList.addAll(this.mainFrameService.getCompleteSQLList());
		Dataset<Row> changeDF = SparkManager.getSession().createDataset(aList, SparkManager.statusChangeDBEncoder())
				.toDF();
		insertMYSQLQuery(changeDF);
	}

	private void insertMYSQLQuery(Dataset<Row> changeDF) {
		try {
			changeDF.write().mode(SaveMode.Append).jdbc(SparkManager.jdbcAppendOptions(), "status_change",
					new java.util.Properties());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
