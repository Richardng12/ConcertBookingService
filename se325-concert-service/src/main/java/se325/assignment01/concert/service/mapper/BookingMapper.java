package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.BookingDTO;
import se325.assignment01.concert.common.dto.SeatDTO;
import se325.assignment01.concert.service.domain.Booking;
import se325.assignment01.concert.service.domain.Seat;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    /**
     * Takes a booking and converts it into a bookingDTO for transmission
     * @param booking
     * @return
     */
    public static BookingDTO convertToDTO(Booking booking){

    List<Seat> listOfSeats = new ArrayList<>();
    for(Seat s : booking.getSeats()){
        listOfSeats.add(s);
    }
    List<SeatDTO> listOfSeatDTOS = new ArrayList<>();

    for(Seat s : listOfSeats){
        listOfSeatDTOS.add(SeatMapper.convertToDTO(s));
    }
    return new BookingDTO(booking.getConcert().getconcertID(),booking.getDateTime(),listOfSeatDTOS);
}
}
