package com.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 类别结构，节点用0到totleVertex-1的整数表示
 * */

public class Structure {
	private int totleVertex;		//目录结构中节点总数
	private Vertex[] vertex;
	
	public Structure(int totleVertex) {
		this.totleVertex = totleVertex;
		vertex = new Vertex[totleVertex];
	}
	
	/**
	 * 添加节点
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
	 * 获得某个节点的自己点
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
	 * 获得某个节点的所有父节点
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
	 * 获得某个节点的一个父节点
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
	 * 获得某一节点到根节点的路径,暂时只使用于树形结构，图的话返回众多路径中的某一条路径
	 * */
	public int[] getPathToRoot(int id) {
		return null;
	}
	
	/**
	 * 返回所有子节点编号
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
	 * 返回所有中间节点
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
