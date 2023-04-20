package ru.skillfactory.entity;

import javax.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "USER_BALANCE")
@Data
public class Users {

    public Users() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long user_id;
    @Column(name = "BALANCE")
    private BigDecimal balance;

    public Users(BigDecimal balance) {
        this.balance = balance;
    }
}
