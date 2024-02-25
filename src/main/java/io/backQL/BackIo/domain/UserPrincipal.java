package io.backQL.BackIo.domain;

import io.backQL.BackIo.dto.userDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static io.backQL.BackIo.dto.userDtoMapper.fromUser;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    public Userr user;
    private Role role;

//    public  UserPrincipal(Userr user, String permission) {
//        this.user = user;
//        this.permission = permission;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return stream(this.role.getPermission().split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getIsNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { return user.getEnabled(); }

    public userDto getUser() {return fromUser(this.user, this.role);
    }

}
