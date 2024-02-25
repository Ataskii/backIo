package io.getarrays.BackIo.repository;

import io.getarrays.BackIo.domain.Userr;
import io.getarrays.BackIo.dto.userDto;
import io.getarrays.BackIo.form.UpdateForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
public interface UserRepository <T extends Userr> {
//    Basic CRUD operations   //
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);
    // complex operations //
    T getUserByEmail(String email);
    void sendVarificationCode(userDto user);
    T verifyCode(String email, String code);
    void resetPassword(String email);
    T verifyPasswordKey(String key);
    T verifyAccountKey(String key);
    T updateUserDetails(UpdateForm user);
    void renewPassword(String key, String newPassword, String confirmPassword);
    void updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword);
    void updateAccountSettings(Long userId, Boolean enabled, Boolean isLocked);
    T toggleMfa(String email);
    void updateUserImage(userDto userDto, MultipartFile image);

}
