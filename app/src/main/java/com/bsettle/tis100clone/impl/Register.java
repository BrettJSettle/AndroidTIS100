package com.bsettle.tis100clone.impl;

public class Register{
	protected Integer value = null;
	
	public Register(Integer value) {
		this.value = value;
	}
	
	public Integer read(){
		return value;
	}
	
	public void write(Integer value) {
		this.value = value;
	}
}
