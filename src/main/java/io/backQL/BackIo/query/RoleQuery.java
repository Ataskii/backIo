package io.backQL.BackIo.query;

public class RoleQuery {
    public static final String INSERT_ROLE_TO_USER_ROLES_QUERY = "INSERT INTO UserRoles (user_id, role_id) VALUES (:userId, :roleId)";
    public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM Roles WHERE name = :roleName";
    public static final String SELECT_ROLE_BY_ID_QUERY = "SELECT r.id, r.name, r.permission FROM Roles r JOIN UserRoles ur ON ur.id = r.id JOIN Users u ON u.id = ur.id WHERE u.id = :id";
    public static final String FETCH_ALL_ROLES_QUERY = "SELECT * FROM roles ORDER BY id";
    public static final String GET_USER_ROLE_BY_USER_ID_QUERY = "SELECT role_id FROM UserRoles WHERE user_id = :userid";
}
