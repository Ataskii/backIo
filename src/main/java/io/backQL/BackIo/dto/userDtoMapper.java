package io.backQL.BackIo.dto;

import io.backQL.BackIo.domain.Role;
import io.backQL.BackIo.domain.Userr;
import org.springframework.beans.BeanUtils;


public class userDtoMapper {
    public static userDto fromUser(Userr user){
        userDto userDTO = new userDto();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
        }
    public static userDto fromUser(Userr user, Role role){
        userDto userDTO = new userDto();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoleName((role.getName()));
        userDTO.setPermissions(role.getPermission());
        return userDTO;
        }
    public static Userr toUser(userDto userDTO){
        Userr user = new Userr();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
