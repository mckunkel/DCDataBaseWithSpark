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
package standaloneSQLconnection;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import database.objects.StatusChangeDB;
import database.query.DBQuery;

public class Example {

	public static void main(String[] args) {
		Logger.getLogger("org.apache.spark.SparkContext").setLevel(Level.ERROR);
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
		DBQuery dbQuery = new DBQuery();

		List<StatusChangeDB> aChangeDBs = dbQuery.getBadWireList("762");
		for (StatusChangeDB statusChangeDB : aChangeDBs) {
			System.out.println("sector  " + statusChangeDB.getSector() + " local layer " + statusChangeDB.getLoclayer()
					+ " superlayer " + statusChangeDB.getSuperlayer() + " wire number " + statusChangeDB.getLocwire());
		}

	}
}
