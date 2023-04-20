package ru.skillfactory.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.skillfactory.service.UsersService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class BalanceOperationsController {

    private final UsersService usersService;

    @GetMapping("/getBalance/{id}")
    @ApiOperation(value = "Получение данных о балансе")
    public String getBalance(@PathVariable(value = "id") Long id) {
        return usersService.getBalance(id);
    }

    @GetMapping("/putMoney/{id}/{sum}")
    @ApiOperation(value = "Операция пополнения счета")
    public String putMoney(@PathVariable(value = "id") Long id,@PathVariable(value = "sum") BigDecimal sum) {
        return usersService.putMoney(id, sum);
    }

    @GetMapping("/takeMoney/{id}/{sum}")
    @ApiOperation(value = "Операция снятия со счета")
    public String takeMoney(@PathVariable(value = "id") Long id,@PathVariable(value = "sum") BigDecimal sum) {
        return usersService.takeMoney(id, sum);
    }
}
