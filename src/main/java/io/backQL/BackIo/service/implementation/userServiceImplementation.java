package io.backQL.BackIo.service.implementation;

import io.backQL.BackIo.domain.Userr;
import io.backQL.BackIo.dto.userDto;
import io.backQL.BackIo.dto.userDtoMapper;
import io.backQL.BackIo.form.UpdateForm;
import io.backQL.BackIo.repository.UserRepository;
import io.backQL.BackIo.repository.UserRoleRepository;
import io.backQL.BackIo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class userServiceImplementation implements UserService {
    private final UserRepository<Userr> userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public userDto createUser(Userr user) {
        return mapToUserDto(userRepository.create(user));
    }
    @Override
    public userDto getUserByEmail(String email) {
        return mapToUserDto(userRepository.getUserByEmail(email));
    }
    @Override
    public void sendVarificationCode(userDto user) {
        userRepository.sendVarificationCode(user);
    }
    @Override
    public userDto verifyCode(String email, String code){ return mapToUserDto(userRepository.verifyCode(email, code)); }
    @Override
    public userDto verifyPasswordKey(String key) {return mapToUserDto(userRepository.verifyPasswordKey(key));}
    @Override
    public void resetPassword(String email) {userRepository.resetPassword(email);}
    @Override
    public void renewPassword(String key, String newPassword, String confirmPassword) {userRepository.renewPassword(key, newPassword, confirmPassword);}
    @Override
    public userDto verifyAccountKey(String key) {
        return mapToUserDto(userRepository.verifyAccountKey(key));
    }
    @Override
    public userDto updateUserDetails(UpdateForm user) {return mapToUserDto(userRepository.updateUserDetails(user)); }
    @Override
    public userDto getUserById(Long userId) { return mapToUserDto(userRepository.get(userId)); }
    @Override
    public void updateUserRole(Long userId, String roleName) { userRoleRepository.updateUserRole(userId, roleName); }
    @Override
    public void updateAccountSettings(Long userId, Boolean enabled, Boolean isLocked) { userRepository.updateAccountSettings(userId, enabled, isLocked); }

    @Override
    public void updateUserImage(userDto userDto, MultipartFile image) { userRepository.updateUserImage(userDto, image); }

    @Override
    public userDto toggleMfa(String email) {return mapToUserDto(userRepository.toggleMfa(email)); }

    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        userRepository.updatePassword(id, currentPassword, newPassword, confirmPassword); }

    private userDto mapToUserDto(Userr user) { return userDtoMapper.fromUser(user, userRoleRepository.getRoleByUserId(user.getId())); }
}
