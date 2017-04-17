package com.tools;

/**
 * �����������
 * */
public class RandomSequence {

	/**
	 * ����0��n֮�䲻�ظ��������
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
