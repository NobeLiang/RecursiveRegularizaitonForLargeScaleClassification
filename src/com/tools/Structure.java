package com.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ṹ���ڵ���0��totleVertex-1��������ʾ
 * */

public class Structure {
	private int totleVertex;		//Ŀ¼�ṹ�нڵ�����
	private Vertex[] vertex;
	
	public Structure(int totleVertex) {
		this.totleVertex = totleVertex;
		vertex = new Vertex[totleVertex];
	}
	
	/**
	 * ��ӽڵ�
	 * */
	public void addChild(int parent, int child) {
		if(vertex[parent] == null) {
			vertex[parent] = new Vertex(parent);
		}
		
		if(vertex[child] == null) {
			vertex[child] = new Vertex(child);
		}
		
		VertexPoint c = new VertexPoint(child);
		c.next = vertex[parent].next;
		vertex[parent].next = c;
		
		VertexPoint p = new VertexPoint(parent);
		p.next = vertex[child].prior;
		vertex[child].prior = p;
	}
	
	/**
	 * ���ĳ���ڵ���Լ���
	 * */
	public int[] getChildren(int id) {
		if(id >= totleVertex) {
//			System.out.println("");
			return null;
		}
		Vertex v = this.vertex[id];
		List<Integer> list = new ArrayList<Integer>();
		VertexPoint vp = v.next;
		
		while(vp != null) {
			list.add(vp.offset);
			vp = vp.next;
		}
		
		int[] result = new int[list.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	/**
	 * ���ĳ���ڵ�����и��ڵ�
	 * */
	public int[] getParents(int id) {
		if(id > this.totleVertex) {
			return null;
		}
		
		List<Integer> list = new ArrayList<Integer>();
		Vertex v = this.vertex[id];
		VertexPoint vp = v.prior;
		
		while(vp != null) {
			list.add(vp.offset);
			vp = vp.next;
		}
		
		int[] result = new int[list.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	
	/**
	 * ���ĳ���ڵ��һ�����ڵ�
	 * */
	public int getParent(int id) {
		int[] parents = getParents(id);
		if(parents != null && parents.length != 0) {
			return parents[0];
		} else {
			return -1;
		}
	}
	/**
	 * ���ĳһ�ڵ㵽���ڵ��·��,��ʱֻʹ�������νṹ��ͼ�Ļ������ڶ�·���е�ĳһ��·��
	 * */
	public int[] getPathToRoot(int id) {
		return null;
	}
	
	/**
	 * ���������ӽڵ���
	 * */
	public int[] getLeaves() {
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < this.vertex.length; i++) {
			if(this.vertex[i].next == null) {
				list.add(i);
			}
		}
		
		int[] result = new int[list.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = list.get(i);
		}
		return result;
	}
	
	/**
	 * ���������м�ڵ�
	 **/
	public int[] getInnerVertex() {
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < this.vertex.length; i++) {
			if(this.vertex[i].next != null) {
				list.add(i);
			}
		}
		
		int[] result = new int[list.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	public int getTotleVertex() {
		return totleVertex;
	}

	public void setTotleVertex(int totleVertex) {
		this.totleVertex = totleVertex;
	}

	public Vertex[] getVertex() {
		return vertex;
	}

	public void setVertex(Vertex[] vertex) {
		this.vertex = vertex;
	}
	
	public int[] getAllNodes() {
		int[] result = new int[this.vertex.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = i;
		}
		return result;
	}
	
	public boolean isLeaf(int id) {
		Vertex v = this.vertex[id];
		if(v.next == null) {
			return true;
		} else {
			return false;
		}
	}
}
