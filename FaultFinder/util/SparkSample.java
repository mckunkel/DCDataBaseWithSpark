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
package database.util;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSample {
	public static void main(String[] args) {
		SparkSession spark = SparkSession.builder().appName("SparkSample").master("local[*]").getOrCreate();
		// create df
		List<String> myList = Arrays.asList("one", "two", "three", "four", "five");
		Dataset<Row> df = spark.createDataset(myList, Encoders.STRING()).toDF();
		df.show();
		// using df.as
		List<String> listOne = df.as(Encoders.STRING()).collectAsList();
		System.out.println(listOne);
		// using df.map
		List<String> listTwo = df.map(row -> row.mkString(), Encoders.STRING()).collectAsList();
		System.out.println(listTwo);
	}
}
