package com.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取，写入文件等操作
 * */
public class FileIO {
	/**
	 * @throws IOException 
	 * 读取训练样本
	 * @throws InvalidInputDataException 
	 * */
	public static Problem readProblem(File file, double bias) throws IOException, InvalidInputDataException {
		BufferedReader fp = new BufferedReader(new FileReader(file));
		List<int[]> vy = new ArrayList<int[]>();
		List<DataPoint[]> vx = new ArrayList<DataPoint[]>();
		
		int max_index = 0;
		
		int lineNr = 0;
		
		try {
			while(true) {
				String line = fp.readLine();
				if(line == null) break;
				lineNr++;
				
				String[] st = line.split("\\s|\t|\n|\r|\f|:");
				if(st.length <= 1) {
					System.out.println("检查输入样本格式");
					return null;
				}
				
				String label = st[0];					
				String[] labels = label.split(",");
				int[] labs = new int[labels.length];		//样本对应标签
				for(int i = 0; i < labs.length; i++) {
					labs[i] = Integer.parseInt(labels[i]);
				}
				vy.add(labs);
				
				int m = st.length / 2;
				DataPoint[] x;
				if(bias >= 0) {
					x = new DataPoint[m+1];
				} else {
					x = new DataPoint[m];
				}
				
				int indexBefore = 0;
				String token;
				for(int j = 0; j < m; j++) {
					token = st[2 * j + 1];
					int index;
					try {
						index = Integer.parseInt(token);
					} catch (NumberFormatException e) {
						throw new InvalidInputDataException("无效的index:" + token, file, lineNr, e);
					}
					
					if(index < 0) throw new InvalidInputDataException("无效的index:"+index, file, lineNr);
					if(index <= indexBefore) throw new InvalidInputDataException("index 应该以递增方式排列", file, lineNr);
					indexBefore = index;
				
					token  = st[2 * j + 2];
					try {
						double value = Double.parseDouble(token);
						x[j] = new DataPoint(index, value);
					} catch (NumberFormatException e) {
						throw new InvalidInputDataException("无效的value:" + token, file, lineNr);
					}
				}
				
				if(m > 0) {
					max_index = Math.max(max_index, x[m-1].index);
				}
				vx.add(x);
			}
			
			return constructProblem(vy, vx, max_index, bias);
		} finally {
			fp.close();
		}
	}

	/**
	 * 
	 * */
	public static Problem constructProblem(List<int[]> vy, List<DataPoint[]> vx, int max_index, double bias) {
		Problem prob = new Problem();
		prob.bias = bias;
		prob.l = vy.size();
		prob.n = max_index;
		
		if(bias >= 0) {
			prob.n++;
		}
		prob.x = new DataPoint[prob.l][];
		for(int i = 0; i < prob.l; i++) {
			prob.x[i] = vx.get(i);
			if(bias >= 0) {
				assert prob.x[i][prob.x[i].length - 1] == null;
				prob.x[i][prob.x[i].length - 1] = new DataPoint(max_index + 1, bias);
			}
		}
		
		prob.y = new int[prob.l][];
		for(int i = 0; i < prob.l; i++) {
			prob.y[i] = vy.get(i);
		}
		
		return prob;
	}
}
