package io.backQL.BackIo.service;

import io.backQL.BackIo.domain.Userr;
import io.backQL.BackIo.dto.userDto;
import io.backQL.BackIo.form.UpdateForm;
import org.springframework.web.multipart.MultipartFile;

public interface UserService{
    userDto createUser(Userr user);
    userDto getUserByEmail(String email);
    void sendVarificationCode(userDto user);
    userDto verifyCode(String email, String code);
    void resetPassword(String email);
    userDto verifyPasswordKey(String key);
    void renewPassword(String key, String newPassword, String ConfirmPassword);
    userDto verifyAccountKey(String key);
    userDto updateUserDetails(UpdateForm user);
    userDto getUserById(Long userId);
    void updateUserRole(Long userId, String roleName);
    void updateAccountSettings(Long userId, Boolean enabled, Boolean isLocked);
    void updatePassword(Long userId, String currentPassword, String newPassword, String confirmPassword);
    void updateUserImage(userDto userDto, MultipartFile image);
    void resetPassword(userDto userDto, String oldPassword, String newPassword, String confirmNewPassword);

    userDto toggleMfa(String email);

}
