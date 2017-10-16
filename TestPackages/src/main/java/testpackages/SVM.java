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

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import scala.Tuple2;
import spark.utils.SparkManager;

public class SVM {

	public static void main(String[] args) {

		// Create Java spark context
		JavaSparkContext sc = SparkManager.getContext();

		// RDD training = MLUtils.loadLabeledData(sc, args[0]);
		// RDD test = MLUtils.loadLabeledData(sc, args[1]); // test set

		String testFolder = "data/input-0.csv";
		String trainingFolder = "data/inputTest-0.csv";

		String maxIterations = "100";

		JavaRDD<LabeledPoint> training = sc.textFile(testFolder).cache().map(new Function<String, LabeledPoint>() {

			@Override
			public LabeledPoint call(String v1) throws Exception {
				double label = Double.parseDouble(v1.substring(0, v1.indexOf(",")));
				String featureString[] = v1.substring(v1.indexOf(",", v1.indexOf(",") + 1) + 1).trim().split(" ");
				double[] v = new double[featureString.length];
				int i = 0;
				// System.out.println(label + " " + featureString[0]);
				for (String s : featureString) {
					if (s.trim().equals("") || s.trim().isEmpty())
						continue;
					v[i++] = Double.parseDouble(s.trim());
				}
				return new LabeledPoint(label, Vectors.dense(v));
			}

		});
		System.out.println(training.count());
		JavaRDD test = sc.textFile(trainingFolder).cache().map(new Function<String, LabeledPoint>() {

			@Override
			public LabeledPoint call(String v1) throws Exception {
				double label = Double.parseDouble(v1.substring(0, v1.indexOf(",")));
				String featureString[] = v1.substring(v1.indexOf(",", v1.indexOf(",") + 1) + 1).trim().split(" ");
				double[] v = new double[featureString.length];
				int i = 0;
				for (String s : featureString) {
					if (s.trim().equals(""))
						continue;
					v[i++] = Double.parseDouble(s.trim());
				}
				return new LabeledPoint(label, Vectors.dense(v));
			}

		});
		System.out.println(test.count());
		final NaiveBayesModel model = NaiveBayes.train(training.rdd(), 1.0);

		JavaPairRDD<Double, Double> predictionAndLabel = test
				.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
					@Override
					public Tuple2<Double, Double> call(LabeledPoint p) {
						return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
					}
				});
		double accuracy = 1.0 * predictionAndLabel.filter(new Function<Tuple2<Double, Double>, Boolean>() {
			@Override
			public Boolean call(Tuple2<Double, Double> pl) {
				System.out.println(pl._1() + " -- " + pl._2());
				return pl._1().intValue() == pl._2().intValue();
			}
		}).count() / (double) test.count();
		System.out.println("navie bayes accuracy : " + accuracy);

		// final SVMModel svmModel = SVMWithSGD.train(training.rdd(),
		// Integer.parseInt(maxIterations));
		//
		// JavaPairRDD<Double, Double> predictionAndLabelSVM = test
		// .mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
		// @Override
		// public Tuple2<Double, Double> call(LabeledPoint p) {
		// return new Tuple2<Double, Double>(svmModel.predict(p.features()),
		// p.label());
		// }
		// });
		// double accuracySVM = 1.0 * predictionAndLabelSVM.filter(new
		// Function<Tuple2<Double, Double>, Boolean>() {
		// @Override
		// public Boolean call(Tuple2<Double, Double> pl) {
		// // System.out.println(pl._1() + " -- " + pl._2());
		// return pl._1().intValue() == pl._2().intValue();
		// }
		// }).count() / (double) test.count();
		// System.out.println("svm accuracy : " + accuracySVM);

	}
}
