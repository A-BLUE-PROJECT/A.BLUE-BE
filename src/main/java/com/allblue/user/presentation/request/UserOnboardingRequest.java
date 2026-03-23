package com.allblue.user.presentation.request;

import com.allblue.user.application.command.UserOnboardingCommand;
import com.allblue.user.domain.model.enums.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserOnboardingRequest(
        @NotBlank(message = "???? ??????")
        @Size(min = 2, max = 10, message = "???? 2???댁 10???대?ъ??⑸??")
        @Pattern(regexp = "^[a-zA-Z0-9媛-??]+$", message = "???? ??, ?臾? ?レ, ?몃諛?_)留????⑸??")
        String nickname,

        @NotNull(message = "?ㅻ ?? ??κ?????")
        @Min(value = 100, message = "?ㅻ 100cm ?댁?댁???⑸??")
        @Max(value = 220, message = "?ㅻ 220cm ?댄?댁???⑸??")
        Integer height,

        @NotNull(message = "紐몃Т寃? ?? ??κ?????")
        @Min(value = 30, message = "紐몃Т寃? 30kg ?댁?댁???⑸??")
        @Max(value = 150, message = "紐몃Т寃? 150kg ?댄?댁???⑸??")
        Integer weight,

        @NotNull(message = "?깅?? ?? ??κ?????") Gender gender) {
    public UserOnboardingCommand toCommand() {
        return new UserOnboardingCommand(nickname, height, weight, gender);
    }
}
