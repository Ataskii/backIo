package io.backQL.BackIo.utils;

import io.backQL.BackIo.domain.UserPrincipal;
import io.backQL.BackIo.dto.userDto;
import org.springframework.security.core.Authentication;

public class UserUtils {
    public static userDto getAuthenticatedUserReturnUserPrincipal(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }
    public static userDto getAuthenticatedUserReturnUserDto(Authentication authentication) {
        return ((userDto) authentication.getPrincipal());
    }
    public static userDto getLoggedInUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }
}
