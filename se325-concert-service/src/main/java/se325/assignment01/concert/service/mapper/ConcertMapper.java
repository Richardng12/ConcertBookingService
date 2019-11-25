package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.ConcertDTO;
import se325.assignment01.concert.common.dto.ConcertSummaryDTO;
import se325.assignment01.concert.common.dto.PerformerDTO;
import se325.assignment01.concert.service.domain.Concert;
import se325.assignment01.concert.service.domain.Performer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConcertMapper {

    /**
     * Takes a Concert and converts it into a concertDTO for transmission
     * @param c
     * @return
     */
    public static ConcertDTO convertToDTO(Concert c){
        ConcertDTO concertDTO = new ConcertDTO(c.getconcertID(),c.getTitle(),c.getImageName(),c.getBlurb());
        List<PerformerDTO> performerDTOList = new ArrayList<>();
        concertDTO.setDates(new ArrayList<>(c.getDates()));
        for(Performer p: c.getPerformers()){
            performerDTOList.add(PerformerMapper.convertToDTO(p));
        }
        concertDTO.setPerformers(performerDTOList);
        return concertDTO;
    }


    /**
     * Takes a Concert and converts it to a concertSummaryDTO
     * @param c
     * @return
     */
    public static ConcertSummaryDTO convertToSummaryDTO(Concert c){
        ConcertSummaryDTO concertSummaryDTO = new ConcertSummaryDTO(c.getconcertID(),c.getTitle(),c.getImageName());
        return concertSummaryDTO;
    }
}
