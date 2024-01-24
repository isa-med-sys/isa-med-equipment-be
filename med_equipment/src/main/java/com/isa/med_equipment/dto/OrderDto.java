package com.isa.med_equipment.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String customer;
    private LocalDateTime timeslotStart;
    private LocalDateTime timeslotEnd;
    private List<String> equipment;
    private Boolean isValid;
    private Boolean isTaken;
    private Boolean isCanceled;
    private Boolean isRightAdmin;
}
