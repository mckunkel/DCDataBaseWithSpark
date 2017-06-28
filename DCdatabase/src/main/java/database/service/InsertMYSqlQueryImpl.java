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
import database.query.DBQuery;
import database.utils.DCConversions;
import database.utils.MainFrameServiceManager;
import spark.utils.SparkManager;

public class InsertMYSqlQueryImpl implements InsertMYSqlQuery {
	private MainFrameService mainFrameService = null;
	private DBQuery dbQuery = null;

	public InsertMYSqlQueryImpl() {
		this.mainFrameService = MainFrameServiceManager.getSession();
		this.dbQuery = new DBQuery();

	}

	@Override
	public void prepareMYSQLQuery() {
		// well we need to "trick" the system. The Spark encoder does not handle
		// the enum properly. So we
		// will copy a generic StatusChangeDB from the MYSQL database with the
		// appropriate call for Status_change_type
		// this is beta for only the "broken" wires
		// need to do the ""fixed" wires at some point
		List<StatusChangeDB> aList = new ArrayList<>();
		Dataset<StatusChangeDB> dbQuery = this.dbQuery.compareRunII("0");

		Timestamp timestamp = java.sql.Timestamp.from(java.time.Instant.now());
		System.out.println(timestamp);
		for (StatusChangeDB statusChangeDB : this.mainFrameService.getCompleteSQLList()) {
			StatusChangeDB aChangeDBTest = dbQuery.first();
			aChangeDBTest.setStatchangeid(0);
			aChangeDBTest.setDateofentry(timestamp);
			aChangeDBTest.setRegion(DCConversions.getRegion(statusChangeDB.getSuperlayer()));

			aChangeDBTest.setProblem_type(statusChangeDB.getProblem_type());
			aChangeDBTest.setRunno(statusChangeDB.getRunno());
			aChangeDBTest.setSector(statusChangeDB.getSector());
			aChangeDBTest.setSuperlayer(statusChangeDB.getSuperlayer());
			aChangeDBTest.setLoclayer(statusChangeDB.getLoclayer());
			aChangeDBTest.setLocwire(statusChangeDB.getLocwire());
			// aChangeDBTest.setStatus_change_type(statusChangeDB.getStatus_change_type());
			aList.add(aChangeDBTest);

		}

		// aList.addAll(this.mainFrameService.getCompleteSQLList());
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
