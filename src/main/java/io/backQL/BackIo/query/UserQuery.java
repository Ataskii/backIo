package io.backQL.BackIo.query;

public class UserQuery {
    public static final String CHECK_IF_VARIFICATION_CODE_EXPIRED_QUERY =       "SELECT date < NOW() FROM TwoFactorVerifications WHERE code = :code";
    public static final String SELECT_USER_BY_PASSWORD_URL_QUERY =              "SELECT * FROM Users WHERE id = (SELECT user_id FROM ResetPasswordaccountVarification WHERE url = :url)";
    public static final String SELECT_USER_BY_ACCOUNT_URL_QUERY =               "SELECT * FROM Users WHERE id = (SELECT user_id FROM AccountVerifications WHERE url = :url)";
    public static final String SELECT_USER_BY_USER_CODE_QUERY =                 "SELECT * FROM Users WHERE id = (SELECT user_id FROM TwoFactorVerifications WHERE code = :code)";
    public static final String SELECT_USER_BY_EMAIL_QUERY =                     "SELECT * FROM Users WHERE email = :email";
    public static final String COUNT_EMAIL_USER_QUERY =                         "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String IS_LINK_EXPIRED_QUERY =                          "SELECT expiration_date < NOW() FROM ResetPasswordaccountVarification WHERE url = :url";
    public static final String GET_USER_BY_ID_QUERY =                           "SELECT * FROM Users WHERE id = :id";
    public static final String INSERT_USER_QUERY =                              "INSERT INTO Users (first_name, last_name, email, password) VALUES(:firstName, :lastName, :email, :password)";
    public static final String INSERT_ACCOUNT_VARIFICATION_URL_QUERY =          "INSERT INTO AccountVerifications (user_id, url) VALUES(:userId, :url)";
    public static final String INSERT_INTO_PASSWORD_VARIFICATION =              "INSERT INTO ResetPasswordaccountVarification (user_id, url, expiration_date) VALUES (:userId, :url, :expirationDate)";
    public static final String INSERT_VARIFICATION_CODE_QUERY =                 "INSERT INTO TwoFactorVerifications (user_id, code, expiration_date) VALUES(:id, :varificationCode, :expirationDate)";
    public static final String REPLACE_PASSWORD_BY_ID_QUERY =                   "UPDATE Users SET password = :newPassword WHERE id = (SELECT user_id FROM ResetPasswordaccountVarification WHERE url = :url)";
    public static final String UPDATE_USER_ENABLED_QUERY =                      "UPDATE Users SET enabled = :enabled WHERE id = :id";
    public static final String UPDATE_USER_ROLE_BY_ID =                         "UPDATE UserRoles SET role_id = :roleId WHERE user_id = :userId";
    public static final String UPDATE_PASSWORD_QUERY =                          "UPDATE Users SET password = :newPassword WHERE id = :id";
    public static final String UPDATE_ACCOUNT_MFA_QUERY =                       "UPDATE Users SET using_mfa = :mfa WHERE email = :email";
    public static final String UPDATE_USER_DETAILS_QUERY =                      "UPDATE Users SET first_name = :firstName, last_name = :lastName, email = :email, password = :password, address = :address, phone = :phone, title = :title, bio = :bio";
    public static final String UPDATE_PROFILE_PICTURE_QUERY =                   "UPDATE users SET image_url = :image WHERE id = :id";
    public static final String UPDATE_ACCOUNT_SETTINGS_QUERY =                  "UPDATE Users SET enabled = :enabled, non_locked = :isLocked WHERE id = :id";
    public static final String DELETE_VARIFICATION_CODE_BY_ID_QUERY =           "DELETE FROM TwoFactorVerifications WHERE user_Id = :id";
    public static final String DELETE_PASSWORD_VARIFICATION_BY_USER_ID =        "DELETE FROM ResetPasswordaccountVarification WHERE user_id = :userId";
    public static final String DELETE_USER_FROM_PASSWORD_VARIFICATION_QUERY =   "DELETE FROM ResetPasswordaccountVarification WHERE url = :url";
}
