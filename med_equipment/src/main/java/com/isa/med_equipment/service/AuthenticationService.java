package com.isa.med_equipment.service;

import com.isa.med_equipment.security.authentication.AuthenticationRequest;
import com.isa.med_equipment.security.authentication.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
