package com.tools;
/**
 * 训练样本等数据
 * */
public class Problem {
	/** 训练样本数量 */
	public int				l;
	
	/** 特征维数 */
	public int 				n;
	
	/**  类别标签 */
	public int[][] 			y;
	
	/**	样本特征矩阵 */
	public DataPoint[][]	x;
	
	/** bias*/
	public double        	bias;
	
}
