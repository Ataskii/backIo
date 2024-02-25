package io.backQL.BackIo.repository.implementation;

import io.backQL.BackIo.domain.Role;
import io.backQL.BackIo.domain.UserPrincipal;
import io.backQL.BackIo.domain.Userr;
import io.backQL.BackIo.dto.userDto;
import io.backQL.BackIo.enumeration.VarificationType;
import io.backQL.BackIo.exception.ApiException;
import io.backQL.BackIo.form.UpdateForm;
import io.backQL.BackIo.repository.UserRepository;
import io.backQL.BackIo.repository.UserRoleRepository;
import io.backQL.BackIo.rowmapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static io.backQL.BackIo.enumeration.RoleType.ROLE_USER;
import static io.backQL.BackIo.enumeration.VarificationType.ACCOUNT;
import static io.backQL.BackIo.enumeration.VarificationType.PASSWORD;
import static io.backQL.BackIo.query.RoleQuery.GET_USER_ROLE_BY_USER_ID_QUERY;
import static io.backQL.BackIo.query.UserQuery.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImplementation implements UserRepository<Userr>, UserDetailsService {
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private final NamedParameterJdbcTemplate jdbc;
    private final UserRoleRepository<Role> userRoleRepository;
    private final BCryptPasswordEncoder encoder;
    private final Role role;

    @Override
    public Userr create(Userr user) {
        if(getEmailCount(user.getEmail().trim().toLowerCase())> 0)
            throw new ApiException("Email already in use,choose different one");
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(requireNonNull(holder.getKey()).longValue());
            userRoleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            String varificationUrl = getVarificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            jdbc.update(INSERT_ACCOUNT_VARIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", varificationUrl));
            user.setEnabled(false);
            user.setIsNotLocked(true);
            log.info("user enabled status: {}", user.getEnabled());
            return user;
        } catch (EmptyResultDataAccessException exception) {
                throw new ApiException("No role find by this name" + ROLE_USER.name());
        } catch (Exception exception ) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred, Please try again.");
        }
    }

    @Override
    public Collection<Userr> list(int page, int pageSize) {
        return null;
    }

    @Override
    public Userr get(Long id) {
        try {
            return jdbc.queryForObject(GET_USER_BY_ID_QUERY, Map.of("id", id), new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            throw new ApiException("No user found by this id");
        }
    }
    @Override
    public Userr update(Userr data) {
        return null;
    }
    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Userr user = getUserByEmail(email);
        if(user == null){
            log.error("user not found in the database");
            throw new UsernameNotFoundException("user not found in the database");
        } else {
            log.info("login successfull");
            log.info("user found in the database {}", email);
            return new UserPrincipal(user, userRoleRepository.getRoleByUserId(user.getId()));
        }
    }

    @Override
    public Userr getUserByEmail(String email) {
        try {
            // query part
            Userr user = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());
            return user;
        }catch (EmptyResultDataAccessException exception){
            throw new ApiException("No user found by this email: " + email);
        }
    }

    @Override
    public void sendVarificationCode(userDto user) {
        try{
        String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
        String varificationCode = randomAlphanumeric(8).toUpperCase();
        jdbc.update(DELETE_VARIFICATION_CODE_BY_ID_QUERY, Map.of("id", user.getId()));
        jdbc.update(INSERT_VARIFICATION_CODE_QUERY, Map.of("id", user.getId(), "varificationCode", varificationCode, "expirationDate", expirationDate));
//        sendSms(user.getPhone(), "From: SecureCapita\nYour varification code is: \n" + varificationCode);
        log.info("varification code is: {}", varificationCode);
    } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred, Please try again1.");
        }
    }

    @Override               /* I NEED TO READ ABOUT THIS METHOD */
    public Userr verifyCode(String email, String code) {
        if(code.length() != 8)
            throw new ApiException("Code must be 8 characters long");
        if (isVerificationCodeExpired(code))
            throw new ApiException("Code is expired. Please try again.");
        try{
            Userr userByCode = jdbc.queryForObject(SELECT_USER_BY_USER_CODE_QUERY, Map.of("code", code), new UserRowMapper());
            Userr userByEmail = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());
            if (userByCode.getEmail().equalsIgnoreCase(userByEmail.getEmail())){
                jdbc.update(DELETE_VARIFICATION_CODE_BY_ID_QUERY, Map.of("id", userByEmail.getId() ));
                return userByCode;
            } else {
                throw new ApiException("Code is invalid. Please try again.");
            }
        }catch (EmptyResultDataAccessException exception) {
            throw new ApiException("Invalid code");
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error accurred, Please try again.");
        }
    }
    @Override
    public void resetPassword(String email){
        if (getEmailCount(email.trim().toLowerCase()) <= 0) { throw new ApiException("There is no account for this email address."); }
            try {
                String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
                Userr user = getUserByEmail(email);
                String varificationUrl = getVarificationUrl(UUID.randomUUID().toString(), PASSWORD.getType());
                jdbc.update(DELETE_PASSWORD_VARIFICATION_BY_USER_ID, Map.of("userId", user.getId()));
                jdbc.update(INSERT_INTO_PASSWORD_VARIFICATION, Map.of("userId", user.getId(), "url", varificationUrl, "expirationDate", expirationDate));
                //* TODO send email to user *//
                log.info("varification url: {}", varificationUrl);
            } catch (DataAccessException exception) {
                log.error(exception.getMessage(), exception);
                throw new ApiException("An error occurred while resetting the password. Please try again.");
        }
    }

    @Override
    public Userr verifyPasswordKey(String key) {
        if (isLinkExpired(key, PASSWORD)) throw new ApiException("this link has expired, please reset your password again.");
        try {
            Userr user = jdbc.queryForObject(SELECT_USER_BY_PASSWORD_URL_QUERY, Map.of("url", getVarificationUrl(key, PASSWORD.getType())), new UserRowMapper());
// It may cause ERROR, Will see. \\ // before password renewing we're deleting verification url, and password are not changing successfully.\\
//            jdbc.update(DELETE_USER_FROM_PASSWORD_VERIFICATION_QUERY, Map.of("id", user.getId()));
            return user;
        }catch (EmptyResultDataAccessException exception){
            log.error(exception.getMessage());
            throw new ApiException("This link is not valid, please reset your password again.2");
        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error accurred, Please try again.");
        }
    }
    @Override
    public void renewPassword(String key, String newPassword, String confirmPassword) {
//         TODO Check if they newPassword = confirmPassword //
        try {
            if (newPassword.equals(confirmPassword)) {
                jdbc.update(REPLACE_PASSWORD_BY_ID_QUERY, Map.of(
                        "newPassword", encoder.encode(newPassword),
                        "url", getVarificationUrl(key, PASSWORD.getType())));
                jdbc.update(DELETE_USER_FROM_PASSWORD_VARIFICATION_QUERY, Map.of(
                        "url", getVarificationUrl(key, PASSWORD.getType())));
            } else throw new ApiException("Passwords are not same!");
        }catch (SQLWarningException e) {
            log.error(e.getMessage());
            throw new ApiException("An error occured on sql statement, please try again.");
        }
    }


    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
//            if (Objects.equals(newPassword, confirmPassword)) {
/* its also works but if one of those parameters are null or both
its not gonna return any null exception so second option its more safe i guess */
        if (newPassword.equals(confirmPassword)) {
            Userr user = jdbc.queryForObject(GET_USER_BY_ID_QUERY, Map.of("id", id), new UserRowMapper());
            assert user != null;
            if (encoder.matches(currentPassword, user.getPassword())) { // attention on encoder match here
                try {
                jdbc.update(UPDATE_PASSWORD_QUERY, Map.of("id", id, "newPassword", encoder.encode(newPassword)));
                } catch (Exception e) { throw new ApiException("Something went wrong please try again."); }
        } else throw new ApiException("Current password is incorrect. Please try again.");
        } else throw new ApiException("Two password should match");
    }

    @Override
    public void updateAccountSettings(Long userId, Boolean enabled, Boolean isLocked) {
        try {
//            Userr user = jdbc.queryForObject(GET_USER_BY_ID_QUERY, Map.of("id", userId), new UserRowMapper());
            jdbc.update(UPDATE_ACCOUNT_SETTINGS_QUERY, Map.of("id", userId, "enabled", enabled, "isLocked", isLocked ));
        }catch (Exception e) {log.error(e.getMessage()); throw new ApiException("An error occurred. Please try again.");}
    }

    @Override
    public Userr toggleMfa(String email) {
        Userr user = getUserByEmail(email);
        Long roleid = jdbc.queryForObject(GET_USER_ROLE_BY_USER_ID_QUERY, Map.of("userid", getUserByEmail(email).getId()), Long.class);
        log.info(roleid.toString());
        if (roleid.toString().equals("1")) {throw new ApiException("You are not authorized to modify mfa."); } else {
        if (isBlank(user.getPhone())) { throw new ApiException("You don't have phone number in your account. Please update your phone number and try again."); }
        else {
            user.setIsUsingMfa(!user.getIsUsingMfa());  // its like opening the light when flip the button its closing or opening the light
            jdbc.update(UPDATE_ACCOUNT_MFA_QUERY, Map.of("email", user.getEmail(), "mfa", user.getIsUsingMfa()));
            return user;
            }
        }
    }
// Update image START
    @Override
    public void updateUserImage(userDto userDto, MultipartFile image) {
        String url = setImageUrl(userDto.getEmail());
        userDto.setImageUrl(url);
        saveImage(userDto.getEmail(), image);
        jdbc.update(UPDATE_PROFILE_PICTURE_QUERY, Map.of("id", userDto.getId(), "image", url));
    }


    private String setImageUrl(String email) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/" + email + ".png").toUriString();
    }
    private void saveImage(String email, MultipartFile image) {
        /* this method converts a pathname to path object getProperty can take different keys to get various things in this case returning
        home directory and "absolutepath" appending both each other and "normalize" removes the unwanted parts from directory path */
        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + "/Download/image/").toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(fileStorageLocation);
            }catch (Exception e) {
                log.error(e.getMessage());
                throw new ApiException("Unable to create directories to save image");
            }
            log.info("Created directories: ", fileStorageLocation);
        }
        try {
            /* This is works for copying image data as binary(which is mean image it self) to the path we give (fileStorageLocation)
            and pointing this path by "resolve" and giving the file name in this case "userEmail.png, and REPLACE_EXISTING is
            build in copying option its works for Replacing the file with new one if its already exist */
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(email + ".png"), REPLACE_EXISTING);
        } catch (IOException e) { /* this exception works for if any error happens while receiving file like network error or file
                                     does not exist and kind of things */
            throw new ApiException(e.getMessage());
        }
        log.info("File saved in {} folder", fileStorageLocation);
    }
// UPDATE IMAGE FINISH

    @Override
    public Userr verifyAccountKey(String key) {
        String vurl =  getVarificationUrl(key, ACCOUNT.getType());
        try {
            Userr user = jdbc.queryForObject(SELECT_USER_BY_ACCOUNT_URL_QUERY, Map.of("url", vurl), new UserRowMapper());
            jdbc.update(UPDATE_USER_ENABLED_QUERY, Map.of("enabled", true, "id", user.getId()));
            return user;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException(" Invalid code");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error accurred, Please try again.");
        }
    }
    public Userr updateUserDetails(UpdateForm user) {
        SqlParameterSource parameterMap = getUserDetailsSqlParameterSource(user);
        try {
            jdbc.update(UPDATE_USER_DETAILS_QUERY, parameterMap);
            return get(user.getId());
        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occured, please try again");
        }
    }

    private boolean isLinkExpired(String key, VarificationType password) {
        try {
            return jdbc.queryForObject(IS_LINK_EXPIRED_QUERY, Map.of("url", getVarificationUrl(key, password.getType())), boolean.class);
        }catch (EmptyResultDataAccessException exception){
            log.error(exception.getMessage());
            throw new ApiException("This link is not valid, please reset your password again.") ;
        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error accurred, Please try again.");
        }
    }
//    private isLinkExpired(String key, String Password)

    private Boolean isVerificationCodeExpired(String code) {
        try {
            return jdbc.queryForObject(CHECK_IF_VARIFICATION_CODE_EXPIRED_QUERY, Map.of("code", code), Boolean.class);
        }catch (EmptyResultDataAccessException exception){
            throw new ApiException(" Invalid code");
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error accurred, Please try again.");
        }
    }
    private SqlParameterSource getSqlParameterSource(Userr user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }
    private SqlParameterSource getUserDetailsSqlParameterSource(UpdateForm user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()))
                .addValue("phone", user.getPhone())
                .addValue("address", user.getAddress())
                .addValue("title", user.getTitle())
                .addValue("bio", user.getBio());
    }
    private Integer getEmailCount(String email){
        return jdbc.queryForObject(COUNT_EMAIL_USER_QUERY, Map.of("email", email), Integer.class);
    }
    private String getVarificationUrl(String key, String type){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
    }
    // Create function to check if user authorized.
}