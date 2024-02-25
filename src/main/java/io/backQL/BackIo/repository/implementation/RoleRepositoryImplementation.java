package io.backQL.BackIo.repository.implementation;

import io.backQL.BackIo.domain.Role;
import io.backQL.BackIo.exception.ApiException;
import io.backQL.BackIo.repository.UserRoleRepository;
import io.backQL.BackIo.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

import static io.backQL.BackIo.query.RoleQuery.*;
import static io.backQL.BackIo.enumeration.RoleType.ROLE_USER;
import static io.backQL.BackIo.query.UserQuery.UPDATE_USER_ROLE_BY_ID;
import static java.util.Map.of;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImplementation implements UserRoleRepository<Role> {

    private final NamedParameterJdbcTemplate jdbc;


    @Override
    public Role create(Role role) {
        return null;
    }

    @Override
    public Collection<Role> list() {
        log.info("Fetching all roles.");
        try {
            return jdbc.query(FETCH_ALL_ROLES_QUERY, new RoleRowMapper());
        } catch (Exception e) {log.error(e.getMessage());
        throw new ApiException("Some error occurred. Please try again.");
        }
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }


    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role {} to user id {}", roleName, userId);
        try {
            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, of("roleName", roleName), new RoleRowMapper());
            jdbc.update(INSERT_ROLE_TO_USER_ROLES_QUERY, of("userId", userId, "roleId", role.getId()));
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No role find by this0: " + ROLE_USER.name());
        } catch (Exception exception ) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred, Please try again.");
        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        log.info("Adding role for user id: {}", userId);
        try {
            return jdbc.queryForObject(SELECT_ROLE_BY_ID_QUERY, of("id", userId), new RoleRowMapper());
        } catch (EmptyResultDataAccessException exception){
            throw new ApiException("No role find by this name1: " + ROLE_USER.name());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occured, Please try again.");
        }
    }
    @Override
    public Role getRoleByUserEmail(String Email) {
        return null;
    }
    @Override
    public void updateUserRole(Long userid, String roleName) {
        try {
            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("roleName", roleName), new RoleRowMapper());
            jdbc.update(UPDATE_USER_ROLE_BY_ID, Map.of("roleId", role.getId(), "userId", userid));
        } catch (EmptyResultDataAccessException e) { throw new ApiException("No user role find by this name" + roleName );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }
}