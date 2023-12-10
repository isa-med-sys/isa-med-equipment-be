package com.isa.med_equipment.model;

import lombok.Getter;

@Getter
public enum EquipmentType {
    TYPE1(0),
    TYPE2(1),
    TYPE3(2);

    private final int numericValue;

    EquipmentType(int numericValue) {
        this.numericValue = numericValue;
    }
}
