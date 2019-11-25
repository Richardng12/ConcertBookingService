package se325.assignment01.concert.service.util;


import javax.ws.rs.container.AsyncResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a data class to hold the AsyncResponses for each ConcertInfoSubscription
 * This class is held in a list in ConcertResource and is iterated through to check for the corresponding concert
 * and if its percentageBooked threshold is over the threshold set.
 */
public class ConcertInfoSubscription {


    private AsyncResponse concertAsyncResponse;
    private long concertId;
    private LocalDateTime date;
    private int percentageBooked;

    public ConcertInfoSubscription() {
    }

    public ConcertInfoSubscription(long concertId, LocalDateTime date, int percentageBooked) {
        this.concertId = concertId;
        this.date = date;
        this.percentageBooked = percentageBooked;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getPercentageBooked() {
        return percentageBooked;
    }

    public void setPercentageBooked(int percentageBooked) {
        this.percentageBooked = percentageBooked;

    }

    public AsyncResponse getConcertAsyncResponse() {
        return concertAsyncResponse;
    }

    public void setConcertAsyncResponse(AsyncResponse concertAsyncResponse) {
        this.concertAsyncResponse = concertAsyncResponse;
    }
}