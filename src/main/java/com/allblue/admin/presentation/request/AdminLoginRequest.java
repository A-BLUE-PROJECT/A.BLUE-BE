package com.allblue.admin.presentation.request;

import com.allblue.admin.application.dto.command.AdminLoginCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminLoginRequest(
        @NotBlank(message = "?대??쇱? ??????") @Email(message = "?щ?瑜??대????????????")
        String email,

        @NotBlank(message = "鍮?踰?????????")
        @Size(min = 8, max = 20, message = "鍮?踰???8???댁, 20???댄濡????댁??⑸??")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",
                message = "鍮?踰????臾? ?レ, ?뱀臾몄瑜??ы?댁??⑸??")
        String password) {
    public AdminLoginCommand toCommand() {
        return new AdminLoginCommand(email, password);
    }
}
