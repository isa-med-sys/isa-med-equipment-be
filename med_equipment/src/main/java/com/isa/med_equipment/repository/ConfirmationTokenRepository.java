package com.isa.med_equipment.repository;

import org.springframework.stereotype.Repository;
import com.isa.med_equipment.beans.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String token);
}