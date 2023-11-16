package com.isa.med_equipment.security.authorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    USER_READ("user:read"),
    USER_UPDATE("user:update")
    ;

    private final String permission;
}
