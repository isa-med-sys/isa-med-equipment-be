package com.isa.med_equipment.model;

import lombok.Getter;

@Getter
public enum EquipmentType {
    DIAGNOSTIC(0),
    INSTRUMENT(1),
    THERAPEUTIC(2);

    private final int numericValue;

    EquipmentType(int numericValue) {
        this.numericValue = numericValue;
    }
}
