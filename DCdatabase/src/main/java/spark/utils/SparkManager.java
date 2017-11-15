package spark.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import database.objects.StatusChangeDB;
import database.utils.StringConstants;

public enum SparkManager {
	INSTANCE;

	// A name for the spark instance. Can be any string
	private static String appName = StringConstants.APP_NAME;
	// Pointer / URL to the Spark instance - embedded
	private static String sparkMaster = StringConstants.SPARK_MASTER;
	private static String tempDir = StringConstants.TEMP_DIR;

	private static SparkSession sparkSession = null;

	private static void getConnection() {

		if (sparkSession == null) {

			sparkSession = SparkSession.builder().appName(appName).master(sparkMaster)
					.config("spark.sql.warehouse.dir", tempDir).getOrCreate();

			// System.setProperty("hadoop.home.dir", ".");

		}

	}

	public static SparkSession getSession() {
		if (sparkSession == null) {
			getConnection();
		}
		return sparkSession;
	}

	private static Map<String, String> jdbcOptions() {
		return findDomain(getHostName());
	}

	public static String jdbcAppendOptions() {

		return SparkManager.jdbcOptions().get("url") + "&user=" + SparkManager.jdbcOptions().get("user") + "&password="
				+ SparkManager.jdbcOptions().get("password");

	}

	public static Dataset<Row> mySqlDataset() {
		SparkSession spSession = getSession();
		spSession.sql("set spark.sql.caseSensitive=false");
		// Dataset<Row> demoDf =
		// spSession.read().format("jdbc").options(jdbcOptions()).load();

		Dataset<Row> demoDf = spSession.read().format("jdbc").options(jdbcOptions()).option("inferSchema", true)
				.option("header", true).option("comment", "#").load();

		return demoDf;
	}

	public static void hold() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static Encoder<StatusChangeDB> statusChangeDBEncoder() {
		return Encoders.bean(StatusChangeDB.class);
	}

	private static String getHostName() {
		String retString = null;
		try {

			// run the Unix "hostname" command
			// using the Runtime exec method:
			Process p = Runtime.getRuntime().exec("hostname");

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			retString = stdInput.readLine();

		} catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
		return retString;
	}

	private static Map<String, String> findDomain(String str) {
		Map<String, String> jdbcOptions = new HashMap<String, String>();

		if (str.contains("ikp")) {
			jdbcOptions.put("url", "jdbc:mysql://localhost:3306/test?jdbcCompliantTruncation=false");
			jdbcOptions.put("driver", "com.mysql.jdbc.Driver");
			jdbcOptions.put("dbtable", "status_change");
			jdbcOptions.put("user", "root");
			jdbcOptions.put("password", "");
		} else if (str.contains("jlab.org")) {
			jdbcOptions.put("url", "jdbc:mysql://clasdb:3306/dc_chan_status?jdbcCompliantTruncation=false");
			jdbcOptions.put("driver", "com.mysql.jdbc.Driver");
			jdbcOptions.put("dbtable", "status_change");
			jdbcOptions.put("user", "clasuser");
			jdbcOptions.put("password", "");
		} else if (str.contains("Mike-Kunkels")) {
			jdbcOptions.put("url", "jdbc:mysql://localhost:3306/test?jdbcCompliantTruncation=false");
			jdbcOptions.put("driver", "com.mysql.jdbc.Driver");
			jdbcOptions.put("dbtable", "status_change");
			jdbcOptions.put("user", "clasuser");
			jdbcOptions.put("password", "");
		} else if (str.contains("pool")) {
			jdbcOptions.put("url", "jdbc:mysql://localhost:3306/test?jdbcCompliantTruncation=false");
			jdbcOptions.put("driver", "com.mysql.jdbc.Driver");
			jdbcOptions.put("dbtable", "status_change");
			jdbcOptions.put("user", "clasuser");
			jdbcOptions.put("password", "");
		} // pool-90-49-zam2168.wlan.kfa-juelich.de
		else {
			System.err.println("On an unknown server. Please use on ifarm1402 or ifarm1401");
			System.exit(-1);
		}
		return jdbcOptions;
	}

	public void shutdown() {
		SparkManager.sparkSession.stop();
	}

	public static void restart() {
		SparkSession.clearActiveSession();
	}
}
