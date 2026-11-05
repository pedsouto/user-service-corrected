package com.pedsouto.userservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PhoneDto(
        Long id,
        @Size(max = 5) String areaCode,
        @Size(max = 20) String number
) {
}
