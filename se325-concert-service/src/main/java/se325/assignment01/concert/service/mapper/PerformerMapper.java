package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.PerformerDTO;
import se325.assignment01.concert.service.domain.Performer;

public class PerformerMapper {

    /**
     * Takes a Performer and converts it into a performerDTO
     * @param p
     * @return
     */
    public static PerformerDTO convertToDTO(Performer p){
        return new PerformerDTO(p.getId(),p.getName(),p.getImageName(),p.getGenre(),p.getBlurb());
    }
}
