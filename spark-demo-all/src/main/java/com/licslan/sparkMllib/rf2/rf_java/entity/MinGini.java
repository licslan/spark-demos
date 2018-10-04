package com.licslan.sparkMllib.rf2.rf_java.entity;

public class MinGini {
	private double minGini;
	private int minIndex;
	
	public double getMingini(){
		return minGini;
	}
	public void setMingini(double minGini){
		this.minGini=minGini;
	}
	public int getMinindex(){
		return minIndex;
	}
	public void setMinindex(int minIndex){
		this.minIndex=minIndex;
	}

}
