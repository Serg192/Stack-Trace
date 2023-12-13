package com.sstohnij.stacktraceqabackendv0.dto.request;

import com.sstohnij.stacktraceqabackendv0.utils.Regex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    @Size(min = 4, message = "Username should be at least 4 chars")
    @Size(max = 30, message = "Username can't be more than 31 chars")
    private String username;

    @Size(min = 6, message = "Password should be at least 6 chars")
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email is not valid", regexp = Regex.EMAIL_REGEX)
    private String email;
}
