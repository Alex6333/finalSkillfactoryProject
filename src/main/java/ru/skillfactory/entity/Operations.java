package ru.skillfactory.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "OPERATION_LIST")
@Data
public class Operations implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OPERATION_ID", nullable = false)
    private Long id;

    @Column(name = "USER_ID")
    private Long user_id;

    @Column(name = "OPERATION_TYPE")
    @JsonProperty("Operation type")
    private int operation_type;

    @Column(name = "AMOUNT")
    @JsonProperty("Transaction amount")
    private int amount;

    @Column(name = "OPERATION_DATE")
    @JsonProperty("Operation date")
    private LocalDate date;

    public Operations(Long user_id, int operation_type, int amount, LocalDate date) {
        this.user_id = user_id;
        this.operation_type = operation_type;
        this.amount = amount;
        this.date = date;
    }

    public Operations() {
    }
}
