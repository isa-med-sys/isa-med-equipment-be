package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.model.EquipmentType;
import org.springframework.data.jpa.domain.Specification;

public class EquipmentSpecifications {
    private EquipmentSpecifications() {}

    public static Specification<Equipment> nameLike(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Equipment> typeEquals(String type) {

        return (root, query, builder) ->
                builder.equal(root.get("type"), EquipmentType.valueOf(type).getNumericValue());
    }

    public static Specification<Equipment> ratingGreaterThanOrEqual(float rating) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("rating"), rating);
    }
}
