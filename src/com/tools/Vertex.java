package com.tools;

/**
 * 结构树或图中的节点
 * */
public class Vertex {
	private int 		id;
	private String 		info;
	private int 		level;
	
	public VertexPoint 	prior;
	public VertexPoint 	next;
	
	public Vertex(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}	
}
