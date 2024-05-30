package kz.aptekaplus.controller;

import kz.aptekaplus.dto.SMSRequestDTO;
import kz.aptekaplus.dto.SMSVerificationDTO;
import kz.aptekaplus.dto.TokenResponseDTO;
import kz.aptekaplus.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailVerificationService verificationService;

    @PostMapping
    public ResponseEntity<String> requestSMS(@RequestBody SMSRequestDTO smsRequestDTO) {
        verificationService.requestSMS(smsRequestDTO.getEmail(), smsRequestDTO.getSmsRequestType());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<TokenResponseDTO> verifySMSCode(@RequestBody SMSVerificationDTO smsVerificationDTO) {
        System.out.println("===============");
        System.out.println(smsVerificationDTO.getVerificationCode());
        System.out.println(smsVerificationDTO.getEmail());
        System.out.println("===============");
        return ResponseEntity.ok(verificationService.isVerificationCodeValid(
                smsVerificationDTO.getEmail(),
                smsVerificationDTO.getVerificationCode()));
    }
}
