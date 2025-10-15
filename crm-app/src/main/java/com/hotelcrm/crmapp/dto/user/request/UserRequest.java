package com.hotelcrm.crmapp.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank
    @Size(min = 3, max = 64)
    private String userName;
    @NotBlank
    @Size(min = 6, max = 128)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,128}$",
            message = "Password must contain lower, upper, and digit.")
    private String password;
    @NotNull
    private Long hotelId;
    @NotNull
    private Long roleId;
}
