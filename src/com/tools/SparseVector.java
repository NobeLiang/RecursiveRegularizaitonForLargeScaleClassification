package com.tools;

import java.util.ArrayList;

/**
 * 稀疏矩阵求和，差，内积等操作
 **/
public class SparseVector {
	
	/**
	 * 两向量求内积
	 */
	public static double innerProduct(DataPoint[] a, DataPoint[] b) {
		double result = 0;
		
		if(a == null || b == null) {
			return result;
		}
		
		int pa = 0;
		int pb = 0;
		
		int indexa;
		int indexb;
		while(pa < a.length && pb < b.length) {
			indexa = a[pa].index;
			indexb = b[pb].index;
			
			if(indexa == indexb) {
				result += a[pa].value * b[pb].value;
				pa++;
				pb++;
			} else if(indexa < indexb) {
				pa++;
			} else {
				pb++;
			}
		}
		return result;
	}

	/**
	 * 对输入向量扩大scale倍，输入向量自身改变
	 **/
	
	public static void scaleVector(DataPoint[] a, double scale) {
		if(a != null) {
			int pa = 0;
			while(pa < a.length) {
				a[pa].value = a[pa].value * scale;
				pa++;
			}
		}
	}

	/**
	 * 向量相加，输入向量保持不变
	 * 
	 * */
	public static DataPoint[] addVector(DataPoint[] a, DataPoint[] b) {
		if(a == null  && b == null) {
			return null;
		}
		
		int lengtha = 0;
		int lengthb = 0;
		
		if(a == null && b != null) {
			lengtha = 0;
			lengthb = b.length;
		} else if(a != null && b == null) {
			lengtha = a.length;
			lengthb = 0;
		} else {
			lengtha = a.length;
			lengthb = b.length;
		}
		
		
		ArrayList<DataPoint> list = new ArrayList<DataPoint>();
		
		int pa = 0;
		int pb = 0;
		
		int indexa = 0;
		int indexb = 0;
		
		double sum;
		
		DataPoint temp;
		while(pa < lengtha && pb < lengthb) {
			
			indexa = a[pa].index;
			indexb = b[pb].index;
			
			if(indexa == indexb) {
				sum = a[pa].value + b[pb].value;
				temp = new DataPoint(indexa, sum);
				if(sum != 0) {
					list.add(temp);
				}
				pa++;
				pb++;
			} else if(indexa < indexb) {
				sum = a[pa].value;
				temp = new DataPoint(indexa, sum);
				if(sum != 0) {
					list.add(temp);
				}
				pa++;
			} else {
				sum = b[pb].value;
				temp = new DataPoint(indexb, sum);
				if(sum != 0) {
					list.add(temp);
				}
				pb++;
			}
		}
		
		while(pa < lengtha) {
			indexa = a[pa].index;
			sum = a[pa].value;
			temp = new DataPoint(indexa, sum);
			if(sum != 0) {
				list.add(temp);
			}
			pa++;
		}
		
		while(pb < lengthb) {
			indexb = b[pb].index;
			sum = b[pb].value;
			temp = new DataPoint(indexb, sum);
			if(sum != 0) {
				list.add(temp);
			}
			pb++;
		}
		
		DataPoint[] result = new DataPoint[list.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = list.get(i);
		}
		return result;
	} 

	
	/**
	 *  向量相减 a - b
	 * */
	public static DataPoint[] subVector(DataPoint[] a, DataPoint[] b) {
		if(a == null && b == null) {
			return null;
		}
		
		if(a != null && b == null) {
			return SparseVector.copyScaleVector(a, 1);
		} else if(a == null && b != null) {
			return SparseVector.copyScaleVector(b, -1);
		} else {
			DataPoint[] sub = SparseVector.copyScaleVector(b, -1);
			return SparseVector.addVector(a, sub); 
		}
		
	}
	
	
	/**
	 *  向量复制，并且做缩放。复制是深复制，返回一个新的向量。
	 * */
	public static DataPoint[] copyScaleVector(DataPoint[] a, double scale) {
		if(a == null) {
			return null;
		} 
		
		DataPoint[] result = new DataPoint[a.length];
		for(int i = 0; i < a.length; i++) {
			result[i] = new DataPoint(a[i].index, a[i].value * scale);
		}
		return result;
	}
	
	/**
	 * 打印向量
	 * */
	public static void showVector(DataPoint[] a) {
		if(a == null) {
			return;
		}
		
		for(int i = 0; i < a.length; i++) {
			System.out.print(a[i].index + ":" + a[i].value);
			if(i < a.length - 1) {
				System.out.print(" ");
			} else {
				System.out.println();
			}
		}
	}
}
