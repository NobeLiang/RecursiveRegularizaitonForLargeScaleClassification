package com.tools;

import java.io.File;
import java.io.IOException;

public class TestMethods {
	public static void main(String[] main) throws IOException, InvalidInputDataException {

		
		String filename = "F:/java/RecursiveRegularizaitonForLargeScaleClassification/test3.txt";
		Parameter param = new Parameter(100, 1000, 0.001);
		Problem problem = FileIO.readProblem(new File(filename), 1);
		Structure tree = new Structure(7);
		tree.addChild(0, 1);
		tree.addChild(0, 2);
		tree.addChild(1, 3);
		tree.addChild(1, 4);
		tree.addChild(2, 5);
		tree.addChild(2, 6);
//		
		
		RecursiveSVM rs = new RecursiveSVM(tree, problem, param, 0.001);
		DataPoint[][] w = rs.train(problem, param);
System.out.println("w's length = " + w.length);
		for(int i = 0; i < w.length; i++) {
			System.out.print(i + " --> ");
			SparseVector.showVector(w[i]);
		}
		
		int[][] predict = rs.predict(w, problem.x);
		for(int i = 0; i < predict.length; i++) {
			System.out.print(i + " --> ");
			showV(predict[i]);
		}
	}
	
	public static void showV(int[] v) {
		if(v == null) {
			return;
		}
		for(int i = 0; i < v.length; i++) {
			System.out.print(v[i] + " ");
		}
		System.out.println();
	}
}
