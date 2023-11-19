package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Equipment;
import org.springframework.data.jpa.domain.Specification;

public class EquipmentSpecifications {
    private EquipmentSpecifications() {}

    public static Specification<Equipment> nameLike(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Equipment> typeLike(String type) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("type")), "%" + type.toLowerCase() + "%"); //ss
    }

    public static Specification<Equipment> ratingGreaterThanOrEqual(float rating) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("rating"), rating);
    }
}
