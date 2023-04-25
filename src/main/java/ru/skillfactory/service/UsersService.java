package ru.skillfactory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillfactory.entity.Operations;
import ru.skillfactory.entity.Users;
import ru.skillfactory.repository.OperationsRepository;
import ru.skillfactory.repository.UsersRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    private final OperationsRepository operationsRepository;

    public String getBalance(Long id) {

        String message = "";
        if (usersRepository.findById(id).isPresent()) {

            message = "==================\nClient (ID:" + id + ") balance is " + usersRepository.findById(id).get().getBalance()
                    + " credits\n==================";
        } else {
            String error;
            if (usersRepository.findById(id).isEmpty()) {
                error = "There is not such client with ID= " + id;
            } else {
                //при создании таблицы "user_balance" для колонки "balance" прописано условие "not null",
                // т.е. такой вариант вообще не должен появиться
                error = "There is not any data about client with ID= " + id;
            }
            message = "==================\nError: -1    :   " + error + "\n==================";
        }
        return message;
    }

    public String putMoney(Long id, BigDecimal sum) {

        //ошибка = 0 - ошибка при выполнении операции
        //код = 1 - успех

        String result = "";

        if (usersRepository.findById(id).isPresent()) {

            Optional<Users> users = usersRepository.findById(id);

            result = "===================== \nCredited to account: " + sum + " \nYour amount = "
                    + usersRepository.findById(id).get().getBalance().add(sum) + "\n=====================";

            users.get().setBalance(usersRepository.findById(id).get().getBalance().add(sum));
            usersRepository.save(users.get());

            Operations operations = new Operations(id, 1, sum.intValue()
                    , LocalDate.now());
            operationsRepository.save(operations);
        } else {
            String error;
            if (usersRepository.findById(id).isEmpty()) {
                error = "There is not such client with ID= " + id;
            } else {

                error = "There were technical problems. If the error occurs again, please contact technical support";
            }
            result = "==================\nError: 0    :   " + error + "\n==================";
        }

        return result;
    }

    public String takeMoney(Long id, BigDecimal sum) {
        //ошибка = 0 - недостаточно средств
        //код = 1 - успех

        String result = "";

        if (usersRepository.findById(id).isPresent() && usersRepository.findById(id).get().getBalance().subtract(sum)
                .compareTo(BigDecimal.valueOf(0L)) != -1) {

            Optional<Users> users = usersRepository.findById(id);

            result = "===================== \nDebited from account: " + sum + " \nYour amount = "
                    + usersRepository.findById(id).get().getBalance().subtract(sum) + "\n=====================";

            users.get().setBalance(usersRepository.findById(id).get().getBalance().subtract(sum));
            usersRepository.save(users.get());

            Operations operations = new Operations(id, 2, sum.intValue()
                    , LocalDate.now());
            operationsRepository.save(operations);
        } else {
            String error;
            if (usersRepository.findById(id).isEmpty()) {
                error = "There is not such client with ID= " + id;
            } else if (usersRepository.findById(id).get().getBalance().subtract(sum).compareTo(BigDecimal.valueOf(0L)) == -1) {

                error = "Insufficient funds on the personal account ID= " + id;
            } else {

                error = "There were technical problems. If the error occurs again, please contact technical support";
            }
            result = "==================\nError: 0    :   " + error + "\n==================";
        }

        return result;
    }


    public List<String> getOperationList(Long id, LocalDate start, LocalDate end) {

        Iterable<Operations> operations = operationsRepository.findAll();

        List<String> result = new ArrayList<>();
        for (Operations operation : operations) {
            String oper_type = "";
            if (operation.getOperation_type() == 1) {
                oper_type = "Put to the balance.";
            } else if (operation.getOperation_type() == 2) {
                oper_type = "Take from the balance.";
            } else if (operation.getOperation_type() == 3) {
                oper_type = "Transfer the amount.";
            } else {
                oper_type = "Sorry. Don't know this operation type.";
            }
            if (start == null && end == null) {

                if (operation.getUser_id().equals(id)) {

                    result.add("---------------------------------------");
                    result.add("Operation date: " + operation.getDate());
                    result.add("Operation type is: " + oper_type + " (" + operation.getOperation_type() + ")");
                    result.add("Transaction amount: " + operation.getAmount());
                    result.add("---------------------------------------");
                }
            } else if (start == null) {

                if (operation.getUser_id().equals(id) && (operation.getDate().equals(end) || operation.getDate().isBefore(end))) {

                    result.add("---------------------------------------");
                    result.add("Operation date: " + operation.getDate());
                    result.add("Operation type is: " + oper_type + " (" + operation.getOperation_type() + ")");
                    result.add("Transaction amount: " + operation.getAmount());
                    result.add("---------------------------------------");
                }
            } else if (end == null) {
                if (operation.getUser_id().equals(id) && (operation.getDate().isAfter(start) || operation.getDate().equals(start))) {

                    result.add("---------------------------------------");
                    result.add("Operation date: " + operation.getDate());
                    result.add("Operation type is: " + oper_type + " (" + operation.getOperation_type() + ")");
                    result.add("Transaction amount: " + operation.getAmount());
                    result.add("---------------------------------------");
                }
            } else if ((operation.getDate().isAfter(start) || operation.getDate().equals(start)) &&
                    (operation.getDate().isBefore(end) || operation.getDate().equals(end))) {
                if (operation.getUser_id().equals(id)) {

                    result.add("---------------------------------------");
                    result.add("Operation date: " + operation.getDate());
                    result.add("Operation type is: " + oper_type + " (" + operation.getOperation_type() + ")");
                    result.add("Transaction amount: " + operation.getAmount());
                    result.add("---------------------------------------");
                }
            }
        }
        return result;
    }
}
