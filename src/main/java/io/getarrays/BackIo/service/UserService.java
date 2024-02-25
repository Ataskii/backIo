package io.getarrays.BackIo.service;

import io.getarrays.BackIo.domain.Userr;
import io.getarrays.BackIo.dto.userDto;
import io.getarrays.BackIo.form.UpdateForm;
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
    void updatePassword(Long userId, String currentPassword, String newPassword, String confirmPassword);
    void updateAccountSettings(Long userId, Boolean enabled, Boolean isLocked);
    void updateUserImage(userDto userDto, MultipartFile image);
    userDto toggleMfa(String email);

}
