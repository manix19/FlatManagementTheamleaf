package com.chainsys.flatmanagement.model;

public class Message {
    private int id;
    private int userId;
    private String message;
    private String time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", userId=" + userId + ", message=" + message + ", time=" + time + "]";
	}
    
}

