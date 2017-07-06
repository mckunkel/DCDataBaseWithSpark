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
package database.utils;

import org.jlab.groot.math.Func1D;

public class HVChannelFitter extends Func1D {
	private double[] fPars;
	private boolean isLinearFit;

	public HVChannelFitter(String name, double xmin, double xmax, double[] fPars, boolean isLinearFit) {
		super(name, xmax, xmax);
		this.initParameters();
		this.isLinearFit = isLinearFit;
		this.fPars = fPars;

	}

	private void initParameters() {
		this.addParameter("b0");
		this.addParameter("b1");
		this.addParameter("b2");
		this.addParameter("b3");
		double prevFitPars[] = { 0.0, 0.0, 0.0, 0.0 };
		this.setParameters(prevFitPars);
	}

	@Override
	public void setParameters(double[] params) {
		setParmLength(params.length);
		for (int i = 0; i < params.length; i++) {
			this.setParameter(i, params[i]);
			fPars[i] = params[i];
		}
	}

	private void setParmLength(int i) {
		this.fPars = new double[i + 1];
	}

	@Override
	public double evaluate(double x) {
		double fit = isLinearFit ? pol1(x) : pol3(x);
		return fit;
	}

	public double pol3(double x) {
		return fPars[0] + fPars[1] * x + fPars[2] * Math.pow(x, 2) + fPars[1] * Math.pow(x, 3);
	}

	public double pol1(double x) {

		return fPars[0] + fPars[1] * x;
	}
}
