package com.isa.med_equipment.model;

import com.isa.med_equipment.security.authorization.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.isa.med_equipment.security.authorization.Permission.USER_READ;
import static com.isa.med_equipment.security.authorization.Permission.USER_UPDATE;

@Getter
@RequiredArgsConstructor
public enum Role {
    REGISTERED_USER(
            Set.of(
                    USER_READ,
                    USER_UPDATE
            )
    ),
    COMPANY_ADMIN(Collections.emptySet()),
    SYSTEM_ADMIN(Collections.emptySet())
    ;

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}