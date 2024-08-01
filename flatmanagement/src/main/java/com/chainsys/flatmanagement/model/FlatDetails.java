package com.chainsys.flatmanagement.model;

public class FlatDetails {
	private int id;
    private int ownerId;
    private String flatType;
    private int flatFloor;
    private int noOfRooms;
    private String type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public String getFlatType() {
		return flatType;
	}
	public void setFlatType(String flatType) {
		this.flatType = flatType;
	}
	public int getFlatFloor() {
		return flatFloor;
	}
	public void setFlatFloor(int flatFloor) {
		this.flatFloor = flatFloor;
	}
	public int getNoOfRooms() {
		return noOfRooms;
	}
	public void setNoOfRooms(int noOfRooms) {
		this.noOfRooms = noOfRooms;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "FlatDetails [id=" + id + ", ownerId=" + ownerId + ", flatType=" + flatType + ", flatFloor=" + flatFloor
				+ ", noOfRooms=" + noOfRooms + ", type=" + type + "]";
	}
	
}
