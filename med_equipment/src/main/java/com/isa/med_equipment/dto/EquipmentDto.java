package com.isa.med_equipment.dto;

import com.isa.med_equipment.model.EquipmentType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentDto {

    private Long id;
    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 32)
    private String name;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotEmpty(message = "Price is required")
    private Float price;

    private Float rating;

    private EquipmentType type;

    private Integer quantity;

    private Boolean remove;

    public EquipmentDto(Long id, @NotEmpty(message = "Name is required") @Size(min = 2, max = 32) String name,
                   @NotEmpty(message = "Description is required") String description,
                   @NotEmpty(message = "Price is required") Float price, Float rating, EquipmentType type, Integer quantity, Boolean remove) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.type = type;
        this.quantity = quantity;
        this.remove = remove;
    }
}
