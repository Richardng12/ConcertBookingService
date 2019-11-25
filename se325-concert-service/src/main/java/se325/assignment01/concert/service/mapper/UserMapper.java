package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.UserDTO;
import se325.assignment01.concert.service.domain.User;

public class UserMapper {

    /**
     * Takes a user and converts it into a userDTO
     * @param u
     * @return
     */
    public static UserDTO convertToDTO(User u){
        return new UserDTO(u.getUsername(),u.getPassword());
    }
}
