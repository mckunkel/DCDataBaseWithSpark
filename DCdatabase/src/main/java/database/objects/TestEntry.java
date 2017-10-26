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
package database.objects;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class TestEntry {
	private static final String MYSQL_CONNECTION_URL = jdbcOptions().get("url") + "&host="
			+ jdbcOptions().get("hostname") + "?user=" + jdbcOptions().get("user") + "&password="
			+ jdbcOptions().get("password");

	private static Map<String, String> jdbcOptions() {
		Map<String, String> jdbcOptions = new HashMap<String, String>();
		jdbcOptions.put("url", "jdbc:mysql://clasdb:3306/dc_chan_status?jdbcCompliantTruncation=false");
		jdbcOptions.put("driver", "com.mysql.jdbc.Driver");
		jdbcOptions.put("dbtable", "status_change");
		jdbcOptions.put("user", "clasuser");
		jdbcOptions.put("password", "");

		return jdbcOptions;
	}

	// private static Map<String, String> jdbcOptions() {
	// Map<String, String> jdbcOptions = new HashMap<String, String>();
	// jdbcOptions.put("url",
	// "jdbc:mysql://localhost:3306/test?jdbcCompliantTruncation=false");
	// jdbcOptions.put("driver", "com.mysql.jdbc.Driver");
	// jdbcOptions.put("dbtable", "status_change");
	// jdbcOptions.put("user", "root");
	// jdbcOptions.put("password", "");
	// jdbcOptions.put("hostname", "");
	//
	// return jdbcOptions;
	// }

	public static void main(String[] args) {
		System.out.println(MYSQL_CONNECTION_URL);
		try {

			SparkSession sparkSession = SparkSession.builder().appName("Testint").master("local[*]").getOrCreate();
			sparkSession.sql("set spark.sql.caseSensitive=false");

			Dataset<Row> testDF = sparkSession.read().format("jdbc").options(jdbcOptions()).option("inferSchema", true)
					.option("header", true).option("comment", "#").load();
			testDF.createOrReplaceTempView("DataView");
			testDF.show();

		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}

}
