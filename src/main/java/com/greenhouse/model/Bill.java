package com.greenhouse.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Bills")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @ToString.Exclude
    private Account accountId;

    @Column(name = "bill_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date billDate;

    @Column(name = "total")
    private BigDecimal total;

}