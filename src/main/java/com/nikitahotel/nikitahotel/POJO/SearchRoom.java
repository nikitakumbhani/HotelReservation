package com.nikitahotel.nikitahotel.POJO;

import java.io.Serializable;

public class SearchRoom  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roomId;
	private String arrivaldate;
	private String departuredate;
	private int rooms;
	private int adults;
	private int children;
	private long noOfDays;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public int getChildren() {
		return children;
	}

	public void setChildren(int children) {
		this.children = children;
	}

	public String getArrivaldate() {
		return arrivaldate;
	}

	public void setArrivaldate(String arrivaldate) {
		this.arrivaldate = arrivaldate;
	}

	public String getDeparturedate() {
		return departuredate;
	}

	public void setDeparturedate(String departuredate) {
		this.departuredate = departuredate;
	}

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public int getAdults() {
		return adults;
	}

	public void setAdults(int adults) {
		this.adults = adults;
	}

	public long getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(long noOfDays) {
		this.noOfDays = noOfDays;
	}

	@Override
	public String toString() {
		return "SearchRoom [arrivaldate=" + arrivaldate + ", departuredate=" + departuredate + ", rooms=" + rooms
				+ ", adults=" + adults + ", children=" + children + "]";
	}

}
