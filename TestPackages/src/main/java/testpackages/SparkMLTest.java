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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.ml.linalg.Vector;
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

public class SparkMLTest {

	public static void main(String[] args) {
		SparkSession spark = SparkManager.getSession();
		List<Row> dataTraining = createLists("data/input-0.csv");
		List<Row> unknowData = createLists("data/inputTest-0.csv");

		StructType schemaForFrame = new StructType(
				new StructField[] { new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
						new StructField("features", new VectorUDT(), false, Metadata.empty()) });
		Dataset<Row> training = spark.createDataFrame(dataTraining, schemaForFrame);
		Dataset<Row> unkownDF = spark.createDataFrame(unknowData, schemaForFrame);

		training.show();
		unkownDF.show();

		Dataset<Row>[] splits = training.randomSplit(new double[] { 0.6, 0.4 }, 1234L);
		Dataset<Row> train = splits[0];
		Dataset<Row> test = splits[1];
		Dataset<Row>[] testsplits = unkownDF.randomSplit(new double[] { 0.99, 0.01 }, 1234L);
		Dataset<Row> unkownDFII = testsplits[0];

		// create the trainer and set its parameters
		NaiveBayes nb = new NaiveBayes();
		// train the model
		NaiveBayesModel model = nb.fit(train);
		Dataset<Row> predictions = model.transform(test);
		predictions.show();
		//
		// compute accuracy on the test set
		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator().setLabelCol("label")
				.setPredictionCol("prediction").setMetricName("accuracy");
		double accuracy = evaluator.evaluate(predictions);
		System.out.println("Test set accuracy = " + accuracy);

		// now lets try to see what's in the unknownDF
		Dataset<Row> morepredictions = model.transform(unkownDFII);
		morepredictions.show();
	}

	private static List<Row> createLists(String file) {
		String line = "";
		String cvsSplitBy = ",";
		List<Row> aList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] country = line.split(cvsSplitBy);
				String label = line.substring(0, line.indexOf(","));

				String featureString[] = line.substring(line.indexOf(",", line.indexOf(",") + 1) + 1).trim().split(" ");
				double[] v = new double[featureString.length];
				int i = 0;
				for (String s : featureString) {
					if (s.trim().equals("") || s.trim().isEmpty())
						continue;
					v[i++] = Double.parseDouble(s.trim());
				}
				aList.add(RowFactory.create(Double.parseDouble(label), Vectors.dense(v)));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return aList;
	}

	public static Function<Row, Vector> rowToVector = new Function<Row, Vector>() {
		public Vector call(Row row) throws Exception {
			Object features = row.getAs(0);
			org.apache.spark.ml.linalg.DenseVector dense = null;

			if (features instanceof DenseVector) {
				dense = (DenseVector) features;
			} else if (features instanceof SparseVector) {
				SparseVector sparse = (SparseVector) features;
				dense = sparse.toDense();
			} else {
				RuntimeException e = new RuntimeException(
						"Cannot convert to " + features.getClass().getCanonicalName());
				System.out.println(e);
				throw e;
			}
			Vector vec = Vectors.dense(dense.toArray());
			return vec;
		}

	};

}
