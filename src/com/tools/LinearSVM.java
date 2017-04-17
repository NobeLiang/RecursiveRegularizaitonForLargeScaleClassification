package com.tools;

import java.util.Random;

/**
 *  A Dual Coordinate Descent Method for Large-scale Linear SVM
 *  Recursive Regularization for Large-scale Classification
 *  with Hierarchical and Graphical Dependencies
 * */
public class LinearSVM {
	
	/**
	 * 输入：
	 * 	x-训练样本
	 *  y-对应类别{-1, 1}
	 *  param-训练参数
	 *  parent-父节点对应权值向量
	 * 
	 * 输出：
	 *  训练所得分类面
	 * */
	public static DataPoint[] train(DataPoint[][] x, int[] y, Parameter param, DataPoint[] parent, double[] loss) {
		DataPoint[] a = null;
		DataPoint[] weight = SparseVector.copyScaleVector(parent, 1);
		
		int numOfSamples = x.length;
		
		double[] alpha = new double[numOfSamples];
		
		int[] updateIndex;
		
		
		double alpha_old;
		double alpha_new;
		boolean exit = false;
		int i;
		double tempE;
		
		int counter = 0;
		for(i = 0; i < param.getMaxIteration(); i++) {
			if(exit) {
				break;
			}
			
			
			int index;
			DataPoint[] samplei;
			int labeli;
			updateIndex = RandomSequence.randomSequence(numOfSamples);
			counter = 0;
			
			for(int j = 0; j < numOfSamples; j++) {                 //样本集上的一次更新
				index = updateIndex[j];
				alpha_old = alpha[index];
				samplei = x[index];
				labeli = y[index];
				
				double gradient = (labeli * SparseVector.innerProduct(a, samplei)) - 1 + labeli * SparseVector.innerProduct(parent, samplei);
				double pg = 0;
				if(alpha_old == 0) {
					pg = Math.min(gradient, 0);
				} else if(alpha_old == param.getC()) {
					pg = Math.max(gradient, 0);
				} else if(alpha_old > 0 && alpha_old < param.getC()) {
					pg = gradient;
				}
				double innerX = SparseVector.innerProduct(samplei, samplei);
				if(pg != 0) {
					if(innerX != 0) {
						alpha_new = Math.min(Math.max(alpha_old - (gradient / innerX), 0), param.getC());
					} else {
						alpha_new = param.getC();
					}
					a = SparseVector.addVector(a, SparseVector.copyScaleVector(samplei, (alpha_new - alpha_old)* labeli));
				} else {
					alpha_new = alpha_old;
				}

				alpha[index] = alpha_new;
			}
	
			DataPoint[] sum = SparseVector.addVector(a, parent);
			weight = SparseVector.copyScaleVector(sum, 1);
			
			double output = 0;
			double alphai;
			double totleLoss = 0;
			for(int k = 0; k < numOfSamples; k++) {
				samplei = x[k];
				labeli = y[k];
				alphai = alpha[k];
				output = labeli * SparseVector.innerProduct(weight, samplei);
				totleLoss += Math.max(0, 1 - output);
//System.out.println("alpha " + k + " = " + alphai + " ,output " + k + " = " + output);
				if(alphai == 0 && output >= (1 - param.getEps())) {
					counter++;
				} else if((alphai > 0 && alphai < param.getC()) && Math.abs(output- 1) <= param.getEps()) {
					counter++;
				} else if ((alphai == param.getC()) && (output <= 1 + param.getEps())) {
					counter++;
				}	
			}
			if(counter == numOfSamples) {
				if(loss != null) {
					loss[0] = totleLoss;
				}
				exit = true;
			}
		}
		
//		if(i == param.getMaxIteration()) {
//			System.out.println("reach max iteration");
//		} else {
//			System.out.println("iteration " + i + " reach expected precision.");
//		}
		
		return weight;
	}
	
	/**
	 * sum(alpha[i] * y[i] * x[i])
	 * */
	public static DataPoint[] sumAYX(double[] alpha, int[] labels, DataPoint[][] x) {
		DataPoint[] result = null;
		
		DataPoint[] samplei;
		int labeli;
		double s;
		for(int i = 0; i < alpha.length; i++) {
			samplei = x[i];
			labeli = labels[i];
			s = alpha[i];
			result = SparseVector.addVector(result, SparseVector.copyScaleVector(samplei, s * labeli));	
		}
		return result;
	}

	/**
	 * 交叉验证
	 * */
	public static void crossValidation(DataPoint[][] x, int[] y, Parameter param, int nr_fold, int[] target, DataPoint[] parent) {
		int i;
		int l = x.length;
		int[] perm = new int[l];
		Random random = new Random();
		
		if(nr_fold > l) {
			nr_fold = l;
			System.out.println("");
		}
		
		int[] fold_start = new int[nr_fold + 1];
		for(i = 0; i < l; i++) {
			perm[i] = i;
		}
		for(i = 0; i < l; i++) {
			int j = i + random.nextInt(l - i);
			swap(perm,i,j);
		}
		for(i = 0; i <= nr_fold; i++) {
			fold_start[i] = i * l / nr_fold;
		}
		
		for(i = 0; i < nr_fold; i++) {
			int begin = fold_start[i];
			int end = fold_start[i+1];
			int j, k;
			
			int subl = l - (end - begin);
			DataPoint[][] subx = new DataPoint[subl][];
			int[] suby = new int[subl];
			k = 0;
			for(j = 0; j < begin; j++) {
				subx[k] = x[perm[j]];
				suby[k] = y[perm[j]];
				++k;
			}
			for(j = end; j < l; j++) {
				subx[k] = x[perm[j]];
				suby[k] = y[perm[j]];
				++k;
			}
			
			DataPoint[] weight = train(subx, suby, param, parent, null);
			for(j = begin; j < end; j++) {
				target[perm[j]] = predictSingleSample(weight, x[perm[j]]);
			}
		}
	}
	
	/**
	 * 预测样本分类
	 * */
	public static int predictSingleSample(DataPoint[] weight, DataPoint[] sample) {
		return SparseVector.innerProduct(weight, sample) >= 0 ? 1 : -1;
	}
	
	/**
	 * 预测
	 * */
	public static int[] predict(DataPoint[] weight, DataPoint[][] samples) {
		int[] result = new int[samples.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = predictSingleSample(weight, samples[i]);
		}
		return result;
	}
	
	public static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	public static double accuracy(int[] y, int[] target) {
		double counter = 0;
		for(int i = 0; i < y.length; i++) {
			if(y[i] == target[i]) {
				counter++;
			}
		}
		return counter / y.length;
	}
}
