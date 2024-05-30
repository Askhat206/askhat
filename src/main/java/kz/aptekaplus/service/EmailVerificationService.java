package kz.aptekaplus.service;

import kz.aptekaplus.dto.TokenResponseDTO;
import kz.aptekaplus.enumuration.SMSRequestType;
import kz.aptekaplus.model.User;
import kz.aptekaplus.model.Verification;
import kz.aptekaplus.repository.UserRepository;
import kz.aptekaplus.repository.VerificationRepository;
import kz.aptekaplus.util.GmailSMSSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    public static final int SMS_REQUEST_LIMIT = 100;
    public static final int LIMIT_RELIEVE_TIME = 24; //unit: hour
    public static final int EXPIRED_TIME = 30; //unit: minute
    private final JWTService jwtService;

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final GmailSMSSender gmailSMSSender;
    private final Logger log = LogManager.getLogger(EmailVerificationService.class);

    @SneakyThrows
    @Transactional
    public void requestSMS(String email, SMSRequestType smsType) {
//        if (userRepository.findByEmail(email).isPresent()) {
//
//        }

        Verification verification = verificationRepository.findById(email)
                .orElseGet(() -> new Verification(email, null));
        System.out.println("===========");
        System.out.println(verification);
        System.out.println("===========");
        if (verification.getCount() >= SMS_REQUEST_LIMIT) {
            var now = getTime();
            var nextRequestTime = verification.getCreationDate().plusHours(LIMIT_RELIEVE_TIME);
            if (now.isBefore(nextRequestTime)) {
                throw new IllegalAccessException("sms request max");
            } else {
                verification.setCount(0);
            }
        }

        verification.setCode(generateVerificationCode());
        verification.setCount(verification.getCount() + 1);
        verification.setCreationDate(getTime());
        verification.setValid(true);
        gmailSMSSender.smsSender(verification.getEmail(), verification.getCode());
        verificationRepository.saveAndFlush(verification);
    }

    @Transactional
    public TokenResponseDTO isVerificationCodeValid(String email, String verificationCode) {
        Optional<Verification> optionalVerification = verificationRepository.findById(email);
        if (optionalVerification.isEmpty()) {
            System.out.println("isEmpty");
            return null;
        }

        Verification verification = optionalVerification.get();

        if (verification.getCode().equals(verificationCode)) {
            verification.setValid(false);
            User user2 = null;
            Optional<User> byEmail = userRepository.findByEmail(email);
            user2 = byEmail.orElseGet(() -> new User(email));
            userRepository.saveAndFlush(user2);
            User user = userRepository.findByEmail(email).get();
            var access = jwtService.generateToken(user);
            var refresh = jwtService.generateRefreshToken(new HashMap<>(), user);
            TokenResponseDTO tokens = new TokenResponseDTO();
            tokens.setAccessToken(access);
            tokens.setRefreshToken(refresh);
            return tokens;
        }

        return null;

    }

    @Transactional
    public void invalidateVerificationCode(String email) {
        Optional<Verification> optionalVerification = verificationRepository.findById(email);

        if (optionalVerification.isEmpty()) {
            return;
        }

        Verification verification = optionalVerification.get();
        verification.setValid(false);
        verificationRepository.save(verification);
    }

    public LocalDateTime getTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    private String generateVerificationCode() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Verification.VERIFICATION_CODE_LENGTH; i++) {
            builder.append((int) (Math.random() * 10));
        }
        return builder.toString();
    }
}
