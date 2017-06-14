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
package spark.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import scala.reflect.ClassTag;

public class Person {
	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public static void main(String[] args) {
		Logger.getLogger("org.apache.spark.SparkContext").setLevel(Level.WARN);
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
		SparkSession spark = SparkManager.getSession();
		// Create an RDD of Person objects from a text file
		JavaRDD<Person> peopleRDD = spark.read()
				.textFile("/usr/local/Cellar/apache-spark/2.1.1/libexec/examples/src/main/resources/people.txt")
				.javaRDD().map(new Function<String, Person>() {
					@Override
					public Person call(String line) throws Exception {
						String[] parts = line.split(",");
						Person person = new Person();
						person.setName(parts[0]);
						person.setAge(Integer.parseInt(parts[1].trim()));
						return person;
					}
				});

		// Apply a schema to an RDD of JavaBeans to get a DataFrame
		Dataset<Row> peopleDF = spark.createDataFrame(peopleRDD, Person.class);
		// Register the DataFrame as a temporary view
		peopleDF.createOrReplaceTempView("people");

		// SQL statements can be run by using the sql methods provided by spark
		Dataset<Row> teenagersDF = spark.sql("SELECT name, age FROM people WHERE age BETWEEN 13 AND 29");
		teenagersDF.show();
		// The columns of a row in the result can be accessed by field index
		Encoder<String> stringEncoder = Encoders.STRING();
		// ExpressionEncoder<Row> encoder =
		// RowEncoder.apply(teenagersDF.schema());
		Encoder<Row> encoder = new Encoder<Row>() {
			@Override
			public StructType schema() {
				return teenagersDF.schema();
				// return null;
			}

			@Override
			public ClassTag<Row> clsTag() {
				return null;
			}
		};
		System.out.println("#############################");
		teenagersDF.printSchema();
		Dataset<String> teenagerNamesByIndexDF = teenagersDF.map(new MapFunction<Row, String>() {
			@Override
			public String call(Row row) throws Exception {
				return "Name: " + row.getString(0) + "  " + row.getInt(1);
			}
		}, stringEncoder);
		teenagerNamesByIndexDF.show();
		System.out.println("#############################");
		// +------------+
		// | value|
		// +------------+
		// |Name: Justin|
		// +------------+

		// or by field name
		Dataset<String> teenagerNamesByFieldDF = teenagersDF.map(new MapFunction<Row, String>() {
			@Override
			public String call(Row row) throws Exception {
				return "Name: " + row.<String>getAs("name");
			}
		}, stringEncoder);
		teenagerNamesByFieldDF.show();
		// +------------+
		// | value|
		// +------------+
		// |Name: Justin|
		// +------------+

		// MK test
		// Dataset<Row> testDF = teenagerNamesByFieldDF.map(new
		// MapFunction<String, Row>() {
		// @Override
		// public Row call(String row) throws Exception {
		// return row.toLowerCase();
		// }
		// }, encoder);
		// testDF.show();
	}
}
