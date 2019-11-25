package se325.assignment01.concert.service.domain;

import se325.assignment01.concert.common.types.BookingStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * This class represents a Seat in the Concert database, it contains an id, seat label, price, time (of concert)
 * and also holds a boolean to check if the seat is currently booked
 */
@Entity
@Table(name="SEATS")
public class Seat{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private long id;

	@Column(name = "LABEL", nullable = false)
	private String label;

	@Column(name="PRICE", nullable = false)
	private BigDecimal price;

	@Column(name = "DATE_TIME")
	private LocalDateTime dateTime;

	@Version
	private int version;

	private boolean isBooked = false;

	private BookingStatus bookingStatus = BookingStatus.Unbooked;


	public Seat() {}

	public Seat(String label, boolean isBooked, LocalDateTime dateTime, BigDecimal price) {
		this.label = label;
		this.dateTime = dateTime;
		this.price = price;
		this.isBooked = isBooked;
	}

	public Seat(String label, BigDecimal price) {
		this.label = label;
		this.price = price;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public long getSeatID() {
		return id;
	}

	public void setSeatID(long seatID) {
		this.id = id;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean booked) {
		isBooked = booked;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
}
