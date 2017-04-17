package com.tools;

/**
 * 随机数产生类
 * */
public class RandomSequence {

	/**
	 * 产生0到n之间不重复随机序列
	 * */
	public static int[] randomSequence(int n) {
		int[] result = new int[n];
		for(int i = 0; i < n; i++) {
			result[i] = i;
		}
		
		int index;
		int temp;
		for(int i = 0; i < n; i++) {
			index = (int)(Math.random() * n);
			temp = result[i];
			result[i] = result[index];
			result[index] = temp;
		}
		return result;
	}
}
