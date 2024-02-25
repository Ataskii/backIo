package io.backQL.BackIo.repository;

import io.backQL.BackIo.domain.Role;

import java.util.Collection;

public interface UserRoleRepository <T extends Role> {
    //    Basic CRUD operations   //
    T create(T data);
    Collection<T> list();
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);
    //    More Complex Operations    //
    void addRoleToUser(Long userId, String roleName);
    T getRoleByUserId(Long userId);
    T getRoleByUserEmail(String Email);
    void updateUserRole(Long userid, String roleName);
}






























