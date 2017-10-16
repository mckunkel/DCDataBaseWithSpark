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
package spark.utils.decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.inference.TestUtils;
import org.jlab.groot.data.H2F;
import org.jlab.groot.math.F1D;

public class HVChannelDecision {

	private static List<Pair<Integer, Integer>> hvChannelSegmentation = null;
	private static Map<Integer, Pair<Integer, Integer>> returnMap = null;
	private static F1D f1;
	private static double[] yValues;
	private static double[] xValues;

	public static Map<Integer, Pair<Integer, Integer>> BadHVChannel(H2F aH2F) {
		hvChannelSegmentation = new ArrayList<Pair<Integer, Integer>>();
		returnMap = new HashMap<Integer, Pair<Integer, Integer>>();
		yValues = new double[8];
		xValues = new double[8];
		sumByChannel(aH2F);

		return returnMap;

	}

	private static void setuphvPinSegmentation() {
		hvChannelSegmentation.add(Pair.of(1, 8));
		hvChannelSegmentation.add(Pair.of(9, 16));
		hvChannelSegmentation.add(Pair.of(17, 24));
		hvChannelSegmentation.add(Pair.of(25, 32));
		hvChannelSegmentation.add(Pair.of(33, 48));
		hvChannelSegmentation.add(Pair.of(49, 64));
		hvChannelSegmentation.add(Pair.of(65, 80));
		hvChannelSegmentation.add(Pair.of(81, 112));
	}

	private static void sumByChannel(H2F aH2f) {
		setuphvPinSegmentation();
		int bundle = 0;
		// first by bundle in each layer

		for (Pair<Integer, Integer> pair : hvChannelSegmentation) {// wire
			// bundles
			double sum = 0;
			double average = 0;
			int wiresInBundle = pair.getRight() - pair.getLeft() + 1;
			double midPoint = (pair.getRight() + pair.getLeft()) / 2.0;
			System.out.println(wiresInBundle + "  " + midPoint);
			int layer = 0;
			for (int j = 0; j < 6; j++) {// layers
				layer = j;
				for (int i = pair.getLeft() - 1; i < pair.getRight(); i++) {
					sum = sum + aH2f.getBinContent(i, j);
				}
			}
			average = sum / wiresInBundle;
			yValues[bundle] = average;
			xValues[bundle] = midPoint;

			bundle++;

			System.out.println("average in layer " + layer + " is " + average);
		}
		findBadPoints(xValues, yValues);
		// yValues[8] = yValues[0];
		// xValues[8] = (xValues[7] + xValues[0]);
		for (double d : xValues) {
			System.out.println(d);
		}
		splineValue(xValues, yValues);
	}

	private static void findBadPoints(double[] xValues, double[] yValues) {
		double[] rValuesLeft = new double[yValues.length];
		double[] rValuesRight = new double[yValues.length];

		for (int i = 0; i < yValues.length - 1; i++) {
			rValuesRight[i] = yValues[i + 1] / yValues[i];
			if (rValuesRight[i] > 1) {
				rValuesRight[i] = 1. / rValuesRight[i];
			}
			System.out.println(rValuesRight[i] + " rValuesRight");
		}
		for (int i = yValues.length - 1; i > 0; i--) {
			rValuesLeft[i] = yValues[i - 1] / yValues[i];
			if (rValuesLeft[i] > 1) {
				rValuesLeft[i] = 1. / rValuesLeft[i];
			}
			System.out.println(rValuesLeft[i] + " rValuesLeft");

		}
	}

	private static void splineValue(double[] xValues, double[] yValues) {
		SplineInterpolator si = new SplineInterpolator();
		LoessInterpolator loess = new LoessInterpolator(0.25, 1);
		PolynomialSplineFunction spline = si.interpolate(xValues, yValues);
		PolynomialFunction[] aFunctions = spline.getPolynomials();

		final WeightedObservedPoints obs = new WeightedObservedPoints();

		for (int i = 0; i < xValues.length; i++) {
			obs.add(xValues[i], yValues[i]);
		}

		final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(4);
		final double[] coeff = fitter.fit(obs.toList());

		// F1D f1 = new F1D("f1", "[p0] + [p1]*x + [p2]*x*x + [p3]*x*x*x",
		// xValues[0], xValues[xValues.length - 1]);
		// f1.setParameters(coeff);

		setFunc(coeff, xValues);
		System.out.println("#############################################");
		for (double d : coeff) {
			System.out.println("coeff " + d);
		}

		System.out.println("#############################################");

		for (PolynomialFunction polynomialFunction : aFunctions) {
			System.out.println(polynomialFunction.degree() + "  " + polynomialFunction.getCoefficients()[0] + "  "
					+ polynomialFunction.getCoefficients()[1] + "  " + polynomialFunction.getCoefficients()[2] + "  "
					+ polynomialFunction.getCoefficients()[3] + "  ratio 2/3 "
					+ polynomialFunction.getCoefficients()[2] / polynomialFunction.getCoefficients()[3] + "  ratio 1/2 "
					+ polynomialFunction.getCoefficients()[1] / polynomialFunction.getCoefficients()[2] + "  ratio 1/3 "
					+ polynomialFunction.getCoefficients()[1] / polynomialFunction.getCoefficients()[3]);
			System.out.println(polynomialFunction.derivative());

		}
		long[] observed = new long[yValues.length];
		for (int i = 0; i < yValues.length; i++) {
			System.out.println(
					"xvalue " + xValues[i] + " yvalue " + yValues[i] + " spline value " + spline.value(xValues[i]));
			System.out.println(f1.evaluate(xValues[i]) + " f1 evaluation. Ration of fit to value "
					+ yValues[i] / f1.evaluate(xValues[i]) + " Percent Error "
					+ Math.abs(yValues[i] - f1.evaluate(xValues[i])) / f1.evaluate(xValues[i]));
			observed[i] = (long) f1.evaluate(xValues[i]);
		}
		double chisqr = TestUtils.chiSquare(yValues, observed);
		System.out.println(chisqr + " chisqr");

	}

	private static void setFunc(double[] coeff, double[] xValues) {
		f1 = new F1D("f1", "[p0] + [p1]*x + [p2]*x*x + [p3]*x*x*x + [p4]*x*x*x*x", xValues[0],
				xValues[xValues.length - 1]);// + [p5]*x*x*x*x*x
		f1.setParameters(coeff);

	}

	public static F1D getFunc() {
		return f1;
	}

	public static double[] getXValues() {
		return xValues;
	}

	public static double[] getYValues() {
		return yValues;
	}
}
