package se325.assignment01.concert.service.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * This class represents a booking in the database and contains a bookingID, the user that made the booking, the dateTime
 * of the booking, the concert the booking was for, and all the seats the booking was made for.
 */
@Entity
@Table(name = "BOOKINGS")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long bookingID;

    @ManyToOne
    @JoinColumn(name = "USER", nullable = false)
    private User user;

    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;

    @ManyToOne
    private Concert concert;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @Column(name = "SEAT",nullable = false)
    private Set<Seat> seats;


    public Booking(){

    }

    public Booking(User user, Concert concert, LocalDateTime dateTime, Set<Seat> seats ){
        this.user = user;
        this.concert = concert;
        this.dateTime = dateTime;
        this.seats = seats;
    }


    public long getBookingID() {
        return bookingID;
    }

    public void setBookingID(long bookingID) {
        this.bookingID = bookingID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }
}
