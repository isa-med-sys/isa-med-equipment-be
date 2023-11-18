package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Company;
import org.springframework.data.jpa.domain.Specification;

public class CompanySpecifications {

    private CompanySpecifications() {}

    public static Specification<Company> nameLike(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Company> cityLike(String city) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("address").get("city")), "%" + city.toLowerCase() + "%");
    }

    public static Specification<Company> ratingGreaterThanOrEqual(float rating) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("rating"), rating);
    }
}
