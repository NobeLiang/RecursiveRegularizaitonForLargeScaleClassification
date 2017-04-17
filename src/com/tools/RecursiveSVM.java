package com.tools;

import java.util.ArrayList;
import java.util.List;

public class RecursiveSVM {
	private Structure 		structure;
	private DataPoint[][] 	weights;
	private Problem 		prob;
	private Parameter 		param;
	private double 			precision;
	
	public RecursiveSVM(Structure structure, Problem prob, Parameter param, double precision) {
		this.structure = structure;
		this.param = param;
		this.prob = prob;
		this.weights = new DataPoint[structure.getTotleVertex()][];
		this.precision = precision;
	}
	
	public DataPoint[][] train(Problem prob, Parameter param) {
		int[] nodes = this.structure.getAllNodes();
		double obj = 0;
		double[] loss = new double[1];
		double totleLoss = 0;
		
		DataPoint[][] w = new DataPoint[nodes.length][];
		
		int id;
		double delta = 0;
		double lastObj = 0;
		while(true) {
			totleLoss = 0;
			obj = 0;
			for(int i = 0; i < nodes.length; i++) {
				id = nodes[i];
				if(!structure.isLeaf(id)) {
					updataInnerNode(w, id);
				} else {
					updateLeafNode(w, id, prob, param, loss);
					totleLoss += loss[0];
				}
			}
			
			for(int i = 0; i < nodes.length; i++) {
				id = nodes[i];
				obj += getRegularTerm(w, id);
			}
System.out.println("regular = " + obj + " ,loss = " + totleLoss + " ,c * loss = " + param.getC() * totleLoss);
			obj += param.getC() * totleLoss;
			delta = Math.abs(obj - lastObj);
			if(delta <= this.precision) {
				return w;
			}
			lastObj = obj;
		}
	}
	
	/**
	 * 构造叶节点id的标签
	 * */
	public int[] getLabel(int[][] y, int id) {
		int[] result = new int[y.length];
		int[] temp;
		for(int i = 0; i < result.length; i++) {
			temp = y[i];
			result[i] = -1;
			for(int j = 0; j < temp.length; j++) {
				if(temp[j] == id) {
					result[i] = 1;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 更新中间节点权值
	 * */
	public void updataInnerNode(DataPoint[][] w, int id) {
		int pid = structure.getParent(id);
		int[] childId = structure.getChildren(id);
		
		double totleNieghbour = 0;
		DataPoint[] sum = null;
		if(pid != -1) {
			totleNieghbour++;
			sum = SparseVector.copyScaleVector(w[pid], 1);
		}
		
		if(childId != null && childId.length != 0) {
			for(int i = 0; i < childId.length; i++) {
				totleNieghbour++;
				sum = SparseVector.addVector(sum, w[childId[i]]);
			}
		}
		w[id] = SparseVector.copyScaleVector(sum, (1 / totleNieghbour));
		
	}

	/**
	 * 更新叶节点
	 * */
	public void updateLeafNode(DataPoint[][] w, int id, Problem prob, Parameter param, double[] loss) {
		int[] label = getLabel(prob.y, id);
		int pid = structure.getParent(id);
		DataPoint[] parent = w[pid];
		w[id] = LinearSVM.train(prob.x, label, param, parent, loss);
	}

	/**
	 * 求节点与父节点权值差的模
	 * */
	public double getRegularTerm(DataPoint[][] w, int id) {
		double result = 0;
		int pid = this.structure.getParent(id);
		DataPoint[] parent = null;
		if(pid != -1) {
			parent = w[pid];
		}
		DataPoint[] wid = w[id];
		DataPoint[] sub = SparseVector.subVector(wid, parent);
		result = 0.5 * SparseVector.innerProduct(sub, sub);
		return result;
	}

	/**
	 * 预测单个样本类别
	 * */
	public int[] predictSingelSample(DataPoint[][] weight, DataPoint[] sample) {
		List<Integer> list = new ArrayList<Integer>();
		int[] leaves = this.structure.getLeaves();
		double predicti = 0;
		for(int i = 0; i < leaves.length; i++) {
			predicti = SparseVector.innerProduct(weight[leaves[i]], sample);
			if(predicti >= 0) {
				list.add(leaves[i]);
			}
		}
		int[] result = new int[list.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = list.get(i);
		}
		return result;
	}
	
	/**
	 * */
	public int[][] predict(DataPoint[][] weight, DataPoint[][] samples) {
		int[][] result = new int[samples.length][];
		DataPoint[] sample = null;
		for(int i = 0; i < samples.length; i++) {
			sample = samples[i];
			result[i] = predictSingelSample(weight, sample);
		}
		return result;
	}
}
