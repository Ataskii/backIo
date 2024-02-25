package io.getarrays.BackIo.service;

import io.getarrays.BackIo.domain.Role;

import java.util.Collection;

public interface RoleService {
    Collection<Role> getRoles();
    Role getRoleByUserId(Long id);
}
