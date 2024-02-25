package io.backQL.BackIo.service;

import io.backQL.BackIo.domain.Role;

import java.util.Collection;

public interface RoleService {
    Collection<Role> getRoles();
    Role getRoleByUserId(Long id);
}
