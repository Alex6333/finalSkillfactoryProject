package ru.skillfactory.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.skillfactory.service.UsersService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    public String putMoney(@PathVariable(value = "id") Long id, @PathVariable(value = "sum") BigDecimal sum) {
        return usersService.putMoney(id, sum);
    }

    @GetMapping("/takeMoney/{id}/{sum}")
    @ApiOperation(value = "Операция снятия со счета")
    public String takeMoney(@PathVariable(value = "id") Long id, @PathVariable(value = "sum") BigDecimal sum) {
        return usersService.takeMoney(id, sum);
    }

    @PostMapping("/getOperationList")
    @ApiOperation(value = "Отобразить список операций за выбранный период")
    public List<String> getOperationList(
            @RequestParam Long user_id,
            @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        return usersService.getOperationList(user_id, start, end);
    }
}
