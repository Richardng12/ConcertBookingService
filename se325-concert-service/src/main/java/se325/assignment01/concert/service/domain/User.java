package se325.assignment01.concert.service.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a user in the database, a user can make bookings and thus this class holds a list of bookings
 * It also contains the username, password, id and token that the user uses to authenticate themselves.
 */
@Entity
@Table(name="USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long id;


    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;


    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "TOKEN")
    private String token;

    @Version
    @Column(name = "VERSION")
    private int version;

    @OneToMany
    @Fetch(FetchMode.SUBSELECT)
    @Column(name = "BOOKINGS")
    private List<Booking> bookings = new ArrayList<>();


    public User(){

    }

    public User(long userID, String username, String password, int version){
        this.id = userID;
        this.username = username;
        this.password = password;
        this.version = version;
    }

    public long getUserID() {
        return id;
    }

    public void setUserID(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
