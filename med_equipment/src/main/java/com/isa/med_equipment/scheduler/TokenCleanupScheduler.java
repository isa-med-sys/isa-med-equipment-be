package com.isa.med_equipment.scheduler;

import com.isa.med_equipment.beans.ConfirmationToken;
import com.isa.med_equipment.repository.ConfirmationTokenRepository;
import com.isa.med_equipment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.isa.med_equipment.beans.User;
import java.time.*;
import java.time.Duration;

@Component
public class TokenCleanupScheduler {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public TokenCleanupScheduler(ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens() {
        confirmationTokenRepository.findAll().stream()
                .filter(this::isTokenExpired)
                .forEach(this::processExpiredToken);
    }

    private void processExpiredToken(ConfirmationToken token) {
        User user = token.getUser();
        if (user != null) {
            userRepository.delete(user);
        }
        confirmationTokenRepository.delete(token);
    }

    private boolean isTokenExpired(ConfirmationToken token) {
        LocalDateTime expirationDateTime = token.getCreatedDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime currentDateTime = LocalDateTime.now();
        long daysDifference = Duration.between(currentDateTime, expirationDateTime).toDays();
        long expirationThresholdInDays = 1;

        return daysDifference <= expirationThresholdInDays;
    }
}