package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.ConcertInfoSubscriptionDTO;
import se325.assignment01.concert.service.util.ConcertInfoSubscription;

public class ConcertInfoSubscriptionMapper {

    /**
     * Takes a ConcertSubscriptionInfoDto and converts it into its relevant data class
     * @param concertInfoSubscriptionDTO
     * @return
     */
    public static ConcertInfoSubscription convertToDomain(ConcertInfoSubscriptionDTO concertInfoSubscriptionDTO){
        return new ConcertInfoSubscription(concertInfoSubscriptionDTO.getConcertId(),concertInfoSubscriptionDTO.getDate(),concertInfoSubscriptionDTO.getPercentageBooked());
    }
}
