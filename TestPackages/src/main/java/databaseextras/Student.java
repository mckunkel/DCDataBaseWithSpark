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
package databaseextras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import spark.utils.SparkManager;

public class Student implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String country;
	private int zip_code;
	private int age;
	private String name;
	private String mycolumn;

	public Student() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getZip_code() {
		return zip_code;
	}

	public void setZip_code(int zip_code) {
		this.zip_code = zip_code;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMycolumn() {
		return mycolumn;
	}

	public void setMycolumn(String mycolumn) {
		this.mycolumn = mycolumn;
	}

	public static Map<String, String> jdbcOptions() {
		Map<String, String> jdbcOptions = new HashMap<String, String>();
		jdbcOptions.put("url", "jdbc:mysql://localhost:3306/udemy");
		// jdbcOptions.put("driver", "com.mysql.jdbc.Driver");
		jdbcOptions.put("dbtable", "student");
		jdbcOptions.put("user", "root");
		jdbcOptions.put("password", "");

		return jdbcOptions;
	}

	public static void main(String[] args) {
		String MYSQL_CONNECTION_URL = "jdbc:mysql://localhost:3306/udemy?user=root&password=";
		SparkSession spSession = SparkManager.getSession();
		List<Student> list = new ArrayList<Student>();
		Student student = new Student();
		student.setAge(96);
		student.setCountry("Poopy Land");
		student.setName("EnumPersonC");
		student.setZip_code(90210);
		student.setMycolumn("c");
		list.add(student);

		// JavaRDD<Student> personsRDD =
		// SparkManager.getContext().parallelize(list);
		// Dataset<Row> userDf = spSession.createDataFrame(personsRDD,
		// Student.class);
		// userDf.write().mode(SaveMode.Append).jdbc(MYSQL_CONNECTION_URL,
		// "student", new java.util.Properties());

		Encoder<Student> StudentEncoder = Encoders.bean(Student.class);
		Dataset<Row> changeDF = SparkManager.getSession().createDataset(list, StudentEncoder).toDF();
		changeDF.write().mode(SaveMode.Append).jdbc(MYSQL_CONNECTION_URL, "student", new java.util.Properties());
		// changeDF.write().mode(SaveMode.Append).options(jdbcOptions());

	}
}
