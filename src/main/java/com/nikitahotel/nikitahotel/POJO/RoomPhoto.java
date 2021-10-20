package com.nikitahotel.nikitahotel.POJO;

public class RoomPhoto {

	private int roomId;
	String photopath;

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getPhotopath() {
		return photopath;
	}

	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}

	@Override
	public String toString() {
		return "RoomPhoto [roomId=" + roomId + ", photopath=" + photopath + "]";
	}

	

}
