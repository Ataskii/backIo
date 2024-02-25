package io.backQL.BackIo.service.implementation;

import io.backQL.BackIo.domain.Role;
import io.backQL.BackIo.repository.UserRoleRepository;
import io.backQL.BackIo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class roleServiceImplementation implements RoleService {
    private final UserRoleRepository<Role> roleRepository;

    @Override
    public Role getRoleByUserId(Long id) {
        return roleRepository.getRoleByUserId(id);
    }

    @Override
    public Collection<Role> getRoles() {
        return roleRepository.list();
    }
}
