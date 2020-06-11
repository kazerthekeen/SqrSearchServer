package com.ezplus.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name="solutions")
public class Solution {
	
	@Column
	private String result;
	@Column
	private String user;
	@Column
	private String status;
	@Column
	@Id
	private long range;
	@Column
	private int time;
	
	public Solution(){
		status = "Fake";
	}
	
	public Solution(long range){
		status = "Ready";
		this.range = range;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getRange() {
		return range;
	}
	public void setRange(long range) {
		this.range = range;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
