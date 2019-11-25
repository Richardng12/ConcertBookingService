package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.SeatDTO;
import se325.assignment01.concert.service.domain.Seat;

public class SeatMapper {

    /**
     * Takes a seat and converts it into a seatDTO
     * @param seat
     * @return
     */
    public static SeatDTO convertToDTO(Seat seat){
        return new SeatDTO(seat.getLabel(),seat.getPrice());
    }

}
