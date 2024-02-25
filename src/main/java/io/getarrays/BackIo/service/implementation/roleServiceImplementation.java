package io.getarrays.BackIo.service.implementation;

import io.getarrays.BackIo.domain.Role;
import io.getarrays.BackIo.repository.UserRoleRepository;
import io.getarrays.BackIo.service.RoleService;
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
