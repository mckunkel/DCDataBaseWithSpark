package spark.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkMySQLConnection {

	public static Map<String, String> jdbcOptions() {
		Map<String, String> jdbcOptions = new HashMap<String, String>();
		jdbcOptions.put("url", "jdbc:mysql://localhost:3306/test");
		jdbcOptions.put("driver", "com.mysql.jdbc.Driver");
		jdbcOptions.put("dbtable", "status_change");
		jdbcOptions.put("user", "root");
		jdbcOptions.put("password", "");

		return jdbcOptions;
	}

	public static Dataset<Row> mySqlDataset() {
		SparkSession spSession = SparkManager.getSession();

		Dataset<Row> demoDf = spSession.read().format("jdbc").options(SparkMySQLConnection.jdbcOptions()).load();

		return demoDf;
	}

}
