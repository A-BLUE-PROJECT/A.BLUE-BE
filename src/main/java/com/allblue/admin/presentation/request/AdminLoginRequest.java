package com.allblue.admin.presentation.request;

import com.allblue.admin.application.dto.command.AdminLoginCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminLoginRequest(
        @NotBlank(message = "관리자 이메일은 필수입니다") @Email(message = "유효한 이메일 형식이어야합니다")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, max = 20, message = "비밀번호는 8글자 이상, 20글자 이하여야합니다")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함해야합니다")
        String password) {
    public AdminLoginCommand toCommand() {
        return new AdminLoginCommand(email, password);
    }
}
