package com.allblue.user.presentation.request;

import com.allblue.user.application.command.UserProfileUpdateCommand;
import com.allblue.user.domain.model.enums.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        @Size(min = 2, max = 10, message = "???? 2???댁 10???대?ъ??⑸??")
        String nickname,

        @Min(value = 100, message = "?ㅻ 100cm ?댁?댁???⑸??") @Max(value = 220, message = "?ㅻ 220cm ?댄?댁???⑸??")
        Integer height,

        @Min(value = 30, message = "紐몃Т寃? 30kg ?댁?댁???⑸??") @Max(value = 150, message = "紐몃Т寃? 150kg ?댄?댁???⑸??")
        Integer weight,

        Gender gender) {
    public UserProfileUpdateCommand toCommand() {
        return new UserProfileUpdateCommand(nickname, height, weight, gender);
    }
}
