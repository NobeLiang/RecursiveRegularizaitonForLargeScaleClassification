package com.tools;

public class Parameter {
	
	private double 	C;
	private int 		maxIteration;
	private double 	eps;
	
	public Parameter(double C, int maxIteration, double eps) {
		this.C = C;
		this.maxIteration = maxIteration;
		this.eps = eps;
	}

	public double getC() {
		return C;
	}

	public void setC(double c) {
		C = c;
	}

	public int getMaxIteration() {
		return maxIteration;
	}

	public void setMaxIteration(int maxIteration) {
		this.maxIteration = maxIteration;
	}

	public double getEps() {
		return eps;
	}

	public void setEps(double eps) {
		this.eps = eps;
	}

}
