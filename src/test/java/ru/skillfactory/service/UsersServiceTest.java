package ru.skillfactory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skillfactory.entity.Operations;
import ru.skillfactory.entity.Users;
import ru.skillfactory.repository.OperationsRepository;
import ru.skillfactory.repository.UsersRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private OperationsRepository operationsRepository;

    @Test
    void getBalanceWhenUserExistTest() {

        Users user = new Users(BigDecimal.valueOf(500.00));
        user.setUser_id(1L);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        String expect = "==================\nClient (ID:" + user.getUser_id() + ") balance is " + user.getBalance()
                + " credits\n==================";

        String message = usersService.getBalance(user.getUser_id());

        assertEquals(expect, message);
    }

    @Test
    void getBalanceWhenUserNotExistTest() {

        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

        long id = 1L;

        String expect = "==================\nError: -1    :   There is not such client with ID= " + id +
                "\n==================";

        String error = usersService.getBalance(1L);

        assertEquals(expect, error);
    }

    @Test
    void putMoneyWhenUserExistTest() {
        Users user = new Users(BigDecimal.valueOf(500.00));
        user.setUser_id(1L);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        BigDecimal sum = BigDecimal.valueOf(600.00);

        String expect = "===================== \nCredited to account: " + sum + " \nYour amount = "
                + user.getBalance().add(sum) + "\n=====================";

        String result = usersService.putMoney(1L, BigDecimal.valueOf(600.00));

        assertEquals(expect, result);
    }

    @Test
    void putMoneyTryNegativeAmountTest() {
        Users user = new Users(BigDecimal.valueOf(500.00));
        user.setUser_id(1L);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        String expect = "==================\nError: 0    :   You cannot enter a negative amount\n==================";

        String result = usersService.putMoney(1L, BigDecimal.valueOf(-600.00));

        assertEquals(expect, result);
    }

    @Test
    void putMoneyWhenUserNotExistTest() {
        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

        long id = 1L;

        String expect = "==================\nError: 0    :   There is not such client with ID= " + id +
                "\n==================";

        String error = usersService.putMoney(1L, BigDecimal.valueOf(20.00));

        assertEquals(expect, error);
    }

    @Test
    void takeMoneyWhenUserExistTest() {
        Users user = new Users(BigDecimal.valueOf(500.00));
        user.setUser_id(1L);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        BigDecimal sum = BigDecimal.valueOf(200.00);

        String expect = "===================== \nDebited from account: " + sum + " \nYour amount = "
                + user.getBalance().subtract(sum) + "\n=====================";

        String result = usersService.takeMoney(1L, BigDecimal.valueOf(200.00));

        assertEquals(expect, result);
    }

    @Test
    void takeMoneyTryNegativeAmountTest() {
        Users user = new Users(BigDecimal.valueOf(500.00));
        user.setUser_id(1L);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        String expect = "==================\nError: 0    :   Can't withdraw negative amount\n==================";

        String result = usersService.takeMoney(1L, BigDecimal.valueOf(-200.00));

        assertEquals(expect, result);
    }

    @Test
    void takeMoneyWhenUserNotExistTest() {
        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

        long id = 1L;

        String expect = "==================\nError: 0    :   There is not such client with ID= " + id +
                "\n==================";

        String error = usersService.takeMoney(1L, BigDecimal.valueOf(20.00));

        assertEquals(expect, error);
    }

    @Test
    void takeMoneyWhenUserHaveNotGotEnoughMoneyTest() {
        Users user = new Users(BigDecimal.valueOf(500.00));
        user.setUser_id(1L);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        String expect = "==================\nError: 0    :   Insufficient funds on the personal account ID= "
                + user.getUser_id() + "\n==================";

        String result = usersService.takeMoney(1L, BigDecimal.valueOf(700.00));

        assertEquals(expect, result);
    }

    @Test
    void getOperationListWithoutDateFilterTest() {

        Operations operation_1 = new Operations(1L, 1, 500, LocalDate.of(2023, 3, 1));
        Operations operation_2 = new Operations(2L, 2, 500, LocalDate.of(2023, 3, 5));
        Operations operation_3 = new Operations(1L, 3, 500, LocalDate.of(2023, 5, 1), 2L);
        Operations operation_4 = new Operations(4L, 3, 500, LocalDate.of(2023, 5, 5), 1L);

        List<String> expect = new ArrayList<>();
        expect.add("---------------------------------------");
        expect.add("Operation date: " + operation_1.getDate());
        expect.add("Operation type is: Put to the balance. (" + operation_1.getOperation_type() + ")");
        expect.add("Transaction amount: " + operation_1.getAmount());
        expect.add("---------------------------------------");
        expect.add("---------------------------------------");
        expect.add("Operation date: " + operation_3.getDate());
        expect.add("Operation type is: Transfer money to client with ID=" + operation_3.getTo_user_id()
                + " (" + operation_3.getOperation_type() + ")");
        expect.add("Transaction amount: " + operation_3.getAmount());
        expect.add("---------------------------------------");
        expect.add("---------------------------------------");
        expect.add("Operation date: " + operation_4.getDate());
        expect.add("Operation type is: Receive money from client with ID=" + operation_4.getUser_id()
                + " (" + operation_4.getOperation_type() + ")");
        expect.add("Transaction amount: " + operation_4.getAmount());
        expect.add("---------------------------------------");


        List<Operations> operations = List.of(operation_1, operation_2, operation_3, operation_4);
        when(operationsRepository.findAll()).thenReturn(operations);

        List<String> result = usersService.getOperationList(1L, null, null);

        assertEquals(expect, result);
    }

    @Test
    void getOperationListWithDatesFilterTest() {

        Operations operation_1 = new Operations(1L, 1, 500, LocalDate.of(2023, 3, 1));
        Operations operation_2 = new Operations(2L, 2, 500, LocalDate.of(2023, 3, 5));
        Operations operation_3 = new Operations(1L, 3, 500, LocalDate.of(2023, 5, 1), 2L);
        Operations operation_4 = new Operations(4L, 3, 500, LocalDate.of(2023, 5, 5), 1L);

        List<String> expect = new ArrayList<>();

        expect.add("---------------------------------------");
        expect.add("Operation date: " + operation_3.getDate());
        expect.add("Operation type is: Transfer money to client with ID=" + operation_3.getTo_user_id()
                + " (" + operation_3.getOperation_type() + ")");
        expect.add("Transaction amount: " + operation_3.getAmount());
        expect.add("---------------------------------------");

        expect.add("---------------------------------------");
        expect.add("Operation date: " + operation_4.getDate());
        expect.add("Operation type is: Receive money from client with ID=" + operation_4.getUser_id()
                + " (" + operation_4.getOperation_type() + ")");
        expect.add("Transaction amount: " + operation_4.getAmount());
        expect.add("---------------------------------------");


        List<Operations> operations = List.of(operation_1, operation_2, operation_3, operation_4);
        when(operationsRepository.findAll()).thenReturn(operations);

        List<String> result = usersService.getOperationList(1L, LocalDate.of(2023, 3, 4)
                , LocalDate.of(2023, 6, 1));

        assertEquals(expect, result);
    }

    @Test
    void getOperationListWithStartDateFilterTest() {

        Operations operation_1 = new Operations(1L, 1, 500, LocalDate.of(2023, 3, 1));
        Operations operation_2 = new Operations(2L, 2, 500, LocalDate.of(2023, 3, 5));
        Operations operation_3 = new Operations(1L, 3, 500, LocalDate.of(2023, 5, 1), 2L);
        Operations operation_4 = new Operations(4L, 3, 500, LocalDate.of(2023, 5, 5), 1L);

        List<String> expect = new ArrayList<>();

        expect.add("---------------------------------------");
        expect.add("Operation date: " + operation_3.getDate());
        expect.add("Operation type is: Transfer money to client with ID=" + operation_3.getTo_user_id()
                + " (" + operation_3.getOperation_type() + ")");
        expect.add("Transaction amount: " + operation_3.getAmount());
        expect.add("---------------------------------------");

        expect.add("---------------------------------------");
        expect.add("Operation date: " + operation_4.getDate());
        expect.add("Operation type is: Receive money from client with ID=" + operation_4.getUser_id()
                + " (" + operation_4.getOperation_type() + ")");
        expect.add("Transaction amount: " + operation_4.getAmount());
        expect.add("---------------------------------------");


        List<Operations> operations = List.of(operation_1, operation_2, operation_3, operation_4);
        when(operationsRepository.findAll()).thenReturn(operations);

        List<String> result = usersService.getOperationList(1L, LocalDate.of(2023, 3, 4)
                , null);

        assertEquals(expect, result);
    }

    @Test
    void getOperationListWithEndDateFilterTest() {

        Operations operation_1 = new Operations(1L, 1, 500, LocalDate.of(2023, 3, 1));
        Operations operation_2 = new Operations(2L, 2, 500, LocalDate.of(2023, 3, 5));
        Operations operation_3 = new Operations(1L, 3, 500, LocalDate.of(2023, 5, 1), 2L);
        Operations operation_4 = new Operations(4L, 3, 500, LocalDate.of(2023, 5, 5), 1L);

        List<String> expect = new ArrayList<>();

        expect.add("---------------------------------------");
        expect.add("Operation date: " + operation_1.getDate());
        expect.add("Operation type is: Put to the balance. (" + operation_1.getOperation_type() + ")");
        expect.add("Transaction amount: " + operation_1.getAmount());
        expect.add("---------------------------------------");


        List<Operations> operations = List.of(operation_1, operation_2, operation_3, operation_4);
        when(operationsRepository.findAll()).thenReturn(operations);

        List<String> result = usersService.getOperationList(1L, null
                , LocalDate.of(2023, 3, 4));

        assertEquals(expect, result);
    }

    @Test
    void transferMoneyOkTest() {

        Users fromUser = new Users(BigDecimal.valueOf(500.00));
        fromUser.setUser_id(1L);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(fromUser));

        Users toUser = new Users(BigDecimal.valueOf(150.00));
        toUser.setUser_id(2L);
        when(usersRepository.findById(2L)).thenReturn(Optional.of(toUser));

        String expect = "=====================\nTransfer from account: ID=1\nto account with ID=2" +
                "\ntransaction amount: 250\n=====================";

        String result = usersService.transferMoney(1L, 2L, 250);

        assertEquals(expect, result);
    }

    @Test
    void transferMoneyWhenFromUserNotExistTest() {

        Users toUser = new Users(BigDecimal.valueOf(150.00));
        toUser.setUser_id(2L);
        when(usersRepository.findById(2L)).thenReturn(Optional.of(toUser));

        String expect = "==================\nError: 0    :   There is not such client with ID= 1\n==================";

        String result = usersService.transferMoney(1L, 2L, 250);

        assertEquals(expect, result);
    }

    @Test
    void transferMoneyWhenToUserNotExistTest() {

        Users fromUser = new Users(BigDecimal.valueOf(500.00));
        fromUser.setUser_id(1L);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(fromUser));

        String expect = "==================\nError: 0    :   There is not such client with ID= 2\n==================";

        String result = usersService.transferMoney(1L, 2L, 250);

        assertEquals(expect, result);
    }

    @Test
    void transferMoneyWhenNotEnoughMoneyTest() {

        Users fromUser = new Users(BigDecimal.valueOf(500.00));
        fromUser.setUser_id(1L);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(fromUser));

        Users toUser = new Users(BigDecimal.valueOf(150.00));
        toUser.setUser_id(2L);
        when(usersRepository.findById(2L)).thenReturn(Optional.of(toUser));

        String expect = "==================\nError: 0    :   Insufficient funds on the personal account ID= 1\n==================";

        String result = usersService.transferMoney(1L, 2L, 750);

        assertEquals(expect, result);
    }

    @Test
    void transferMoneyTryNegativeAmountTest() {

        Users fromUser = new Users(BigDecimal.valueOf(500.00));
        fromUser.setUser_id(1L);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(fromUser));

        Users toUser = new Users(BigDecimal.valueOf(150.00));
        toUser.setUser_id(2L);
        when(usersRepository.findById(2L)).thenReturn(Optional.of(toUser));

        String expect = "==================\nError: 0    :   You can't transfer a negative amount\n==================";

        String result = usersService.transferMoney(1L, 2L, -250);

        assertEquals(expect, result);
    }
}