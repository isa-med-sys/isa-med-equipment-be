package com.isa.med_equipment.repository;

import com.isa.med_equipment.beans.User;
import org.springframework.stereotype.Repository;
import com.isa.med_equipment.beans.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String token);
}