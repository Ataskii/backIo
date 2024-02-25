package io.backQL.BackIo.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingsForm {
    @NotNull(message = "Id cannot be empty")
    public Boolean enabled;
    @NotNull(message = "Not locked cannot be empty")
    public Boolean notLocked;
}
