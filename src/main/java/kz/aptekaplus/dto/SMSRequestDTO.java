package kz.aptekaplus.dto;

import kz.aptekaplus.enumuration.SMSRequestType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SMSRequestDTO {
    private String email;
    private SMSRequestType smsRequestType;
}
