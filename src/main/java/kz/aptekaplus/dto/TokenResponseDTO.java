package kz.aptekaplus.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
