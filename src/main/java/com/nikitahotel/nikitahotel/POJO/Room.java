package com.nikitahotel.nikitahotel.POJO;

import java.util.List;

public class Room {

	private int roomId;
	private String roomtype;
	private int roomarea;
	private int noofoccupants;
	private int noofbeds;
	private double roomrate;
	List<String> photopath;

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}

	public int getRoomarea() {
		return roomarea;
	}

	public void setRoomarea(int roomarea) {
		this.roomarea = roomarea;
	}

	public int getNoofoccupants() {
		return noofoccupants;
	}

	public void setNoofoccupants(int noofoccupants) {
		this.noofoccupants = noofoccupants;
	}

	public int getNoofbeds() {
		return noofbeds;
	}

	public void setNoofbeds(int noofbeds) {
		this.noofbeds = noofbeds;
	}

	public double getRoomrate() {
		return roomrate;
	}

	public void setRoomrate(double roomrate) {
		this.roomrate = roomrate;
	}

	public List<String> getPhotopath() {
		return photopath;
	}

	public void setPhotopath(List<String> photopath) {
		this.photopath = photopath;
	}

	@Override
	public String toString() {
		return "Room [roomtype=" + roomtype + ", roomarea=" + roomarea + ", noofoccupants=" + noofoccupants
				+ ", noofbeds=" + noofbeds + ", roomrate=" + roomrate + "]";
	}

}
