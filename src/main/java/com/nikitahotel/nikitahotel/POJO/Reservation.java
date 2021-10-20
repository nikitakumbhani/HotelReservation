package com.nikitahotel.nikitahotel.POJO;

import java.util.Date;

public class Reservation {
	private int reservationId;
	private Date checkin_date;
	private Date checkout_date;
	private long noOfDays;
	private int no_of_room_reserved;
	private int no_of_adults;
	private int no_of_child;
	private double roomRate;
	private double totalCost;
	private int roomId;
	private String cardNumber;
	private String paymentMethod;

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public int getNo_of_room_reserved() {
		return no_of_room_reserved;
	}

	public void setNo_of_room_reserved(int no_of_room_reserved) {
		this.no_of_room_reserved = no_of_room_reserved;
	}

	public double getRoomRate() {
		return roomRate;
	}

	public void setRoomRate(double roomRate) {
		this.roomRate = roomRate;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public int getNo_of_adults() {
		return no_of_adults;
	}

	public void setNo_of_adults(int no_of_adults) {
		this.no_of_adults = no_of_adults;
	}

	public int getNo_of_child() {
		return no_of_child;
	}

	public void setNo_of_child(int no_of_child) {
		this.no_of_child = no_of_child;
	}

	public Date getCheckin_date() {
		return checkin_date;
	}

	public void setCheckin_date(Date checkin_date) {
		this.checkin_date = checkin_date;
	}

	public Date getCheckout_date() {
		return checkout_date;
	}

	public void setCheckout_date(Date checkout_date) {
		this.checkout_date = checkout_date;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public long getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(long noOfDays) {
		this.noOfDays = noOfDays;
	}

	@Override
	public String toString() {
		return "Reservation [reservationId=" + reservationId + ", checkin_date=" + checkin_date + ", checkout_date="
				+ checkout_date + ", no_of_room_reserved=" + no_of_room_reserved + ", roomRate=" + roomRate
				+ ", totalCost=" + totalCost + "]";
	}

}
