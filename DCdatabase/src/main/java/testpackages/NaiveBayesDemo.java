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
package testpackages;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.LabeledPoint;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import spark.utils.SparkManager;

public class NaiveBayesDemo {

	public static void main(String[] args) {

		SparkSession spark = SparkManager.getSession();
		Logger.getLogger("org.apache.spark.SparkContext").setLevel(Level.WARN);
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);

		Dataset<Row> autoDf = spark.read().option("header", "false").csv("data/input-0.csv").toDF("label", "file",
				"features");
		autoDf.show(5);
		autoDf.printSchema();

		StructType schemaForFrame = new StructType(
				new StructField[] { new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
						new StructField("features", new VectorUDT(), false, Metadata.empty()),
						new StructField("file", DataTypes.DoubleType, false, Metadata.empty()) });
		JavaRDD<Row> rdd1 = autoDf.toJavaRDD().repartition(2);
		// Function to map.
		JavaRDD<Row> rdd2 = rdd1.map(new Function<Row, Row>() {

			@Override
			public Row call(Row iRow) throws Exception {

				String featureString[] = iRow.getString(2).trim().split(" ");
				String fileString = iRow.getString(1).trim().substring(0, iRow.getString(1).indexOf("."));
				// System.out.println(fileString);
				double[] v = new double[featureString.length];
				int i = 0;
				for (String s : featureString) {
					if (s.trim().equals("") || s.trim().isEmpty())
						continue;
					v[i++] = Double.parseDouble(s.trim());
				}

				Row retRow = RowFactory.create(Double.valueOf(iRow.getString(0)), Vectors.dense(v),
						Double.valueOf(fileString));

				return retRow;
			}

		});

		// Create Data Frame back.
		Dataset<Row> autoCleansedDf = spark.createDataFrame(rdd2, schemaForFrame);
		System.out.println("Transformed Data :");
		autoCleansedDf.show(5);
		/*--------------------------------------------------------------------------
		Prepare for Machine Learning. 
		--------------------------------------------------------------------------*/

		// Convert data to labeled Point structure
		JavaRDD<Row> rdd3 = autoCleansedDf.toJavaRDD().repartition(2);
		JavaRDD<LabeledPoint> rdd4 = rdd3.map(new Function<Row, LabeledPoint>() {

			@Override
			public LabeledPoint call(Row iRow) throws Exception {

				LabeledPoint lp = new LabeledPoint(iRow.getDouble(0), iRow.getAs(1));

				return lp;
			}

		});

		Dataset<Row> autoLp = spark.createDataFrame(rdd4, LabeledPoint.class);
		autoLp.show(5);

		// Split the data into training and test sets (10% held out for
		// testing).
		Dataset<Row>[] splits = autoLp.randomSplit(new double[] { 0.9, 0.1 });
		Dataset<Row> trainingData = splits[0];
		Dataset<Row> testData = splits[1];

		/*--------------------------------------------------------------------------
		Perform machine learning.
		--------------------------------------------------------------------------*/

		// create the trainer and set its parameters
		NaiveBayes nb = new NaiveBayes();
		// train the model
		NaiveBayesModel model = nb.fit(trainingData);
		Dataset<Row> predictions = model.transform(testData);
		// predictions.show();
		// View results
		predictions.select("label", "prediction", "features").show(5);
		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator().setLabelCol("label")
				.setPredictionCol("prediction").setMetricName("accuracy");
		double accuracy = evaluator.evaluate(predictions);
		System.out.println("Test set accuracy = " + accuracy);
		///
		///
		///
		///
		///
		///
		// Other set of unknow data
		Dataset<Row> testDf = spark.read().option("header", "false").csv("data/inputTest-0.csv").toDF("label", "file",
				"features");
		testDf.show();

		JavaRDD<Row> testrdd1 = testDf.toJavaRDD().repartition(2);
		// Function to map.
		JavaRDD<Row> testrdd2 = testrdd1.map(new Function<Row, Row>() {

			@Override
			public Row call(Row iRow) throws Exception {

				String featureString[] = iRow.getString(2).trim().split(" ");
				String fileString = iRow.getString(1).trim().substring(0, iRow.getString(1).indexOf("-"));
				System.out.println(fileString);
				double[] v = new double[featureString.length];
				int i = 0;
				for (String s : featureString) {
					if (s.trim().equals("") || s.trim().isEmpty())
						continue;
					v[i++] = Double.parseDouble(s.trim());
				}

				Row retRow = RowFactory.create(Double.valueOf(iRow.getString(0)), Vectors.dense(v),
						Double.valueOf(fileString));

				return retRow;
			}

		});

		// Create Data Frame back.
		Dataset<Row> cleansedTestDf = spark.createDataFrame(testrdd2, schemaForFrame);
		System.out.println("Transformed Data :");
		cleansedTestDf.show(5);
		/*--------------------------------------------------------------------------
		Prepare for Machine Learning.
		--------------------------------------------------------------------------*/

		// Convert data to labeled Point structure
		JavaRDD<Row> testrdd3 = cleansedTestDf.toJavaRDD().repartition(2);
		JavaRDD<LabeledPoint> testrdd4 = testrdd3.map(new Function<Row, LabeledPoint>() {

			@Override
			public LabeledPoint call(Row iRow) throws Exception {
				System.out.println("a value " + iRow.getAs(0));
				LabeledPoint lp = new LabeledPoint(iRow.getDouble(0), iRow.getAs(1));

				return lp;
			}

		});

		Dataset<Row> finalTestDF = spark.createDataFrame(testrdd4, LabeledPoint.class);
		finalTestDF.show(5);
		Dataset<Row> predictionsUnknow = model.transform(finalTestDF);
		predictionsUnknow.show();

		// JavaRDD<LabeledPoint> testrdd2 = trialrdd3.map(new Function<Row,
		// LabeledPoint>() {
		//
		// @Override
		// public LabeledPoint call(Row iRow) throws Exception {
		// String featureString[] = iRow.getString(2).trim().split(" ");
		// double[] v = new double[featureString.length];
		// int i = 0;
		// for (String s : featureString) {
		// if (s.trim().equals("") || s.trim().isEmpty())
		// continue;
		// v[i++] = Double.parseDouble(s.trim());
		// }
		//
		// LabeledPoint lp = new LabeledPoint(Double.valueOf(iRow.getString(0)),
		// Vectors.dense(v));
		//
		// return lp;
		// }
		//
		// });

		// Dataset<Row> finalTestrdd = spark.createDataFrame(testrdd2,
		// LabeledPoint.class);
		// finalTestrdd.show(5);
		// Dataset<Row> predictionsUnknow = model.transform(finalTestrdd);
		// predictionsUnknow.show();

		///

	}

}
