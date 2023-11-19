package com.isa.med_equipment.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "qr_codes")
public class QRCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", referencedColumnName="id")
    private Reservation reservation;

    @Column(name = "content")
    private String content;

    @Column(name = "size")
    private Integer size;
}
