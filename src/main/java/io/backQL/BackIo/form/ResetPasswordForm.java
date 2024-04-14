package io.backQL.BackIo.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordForm {
    public String oldPassword;
    public String newPassword;
    public String confirmNewPassword;
}
