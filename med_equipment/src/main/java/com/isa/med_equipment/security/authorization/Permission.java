package com.isa.med_equipment.security.authorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    COMPANY_ADMIN_READ("company_admin:read"),
    COMPANY_ADMIN_UPDATE("company_admin:update"),
    SYSTEM_ADMIN_READ("system_admin:read"),
    SYSTEM_ADMIN_UPDATE("system_admin:update"),
    SYSTEM_ADMIN_DELETE("system_admin:delete"),
    SYSTEM_ADMIN_CREATE("system_admin:create")
    ;

    private final String permission;
}
