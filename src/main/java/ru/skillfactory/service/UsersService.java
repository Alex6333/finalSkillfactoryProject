package ru.skillfactory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                /*при создании таблицы "user_balance" для колонки "balance" прописано условие "not null",
                т.е. такой вариант вообще не должен появиться*/
                error = "There is not any data about client with ID= " + id;
            }
            message = "==================\nError: -1    :   " + error + "\n==================";
        }
        return message;
    }

    @Transactional
    public String putMoney(Long id, BigDecimal sum) {

        String result = "";

        if (usersRepository.findById(id).isPresent() && sum.compareTo(BigDecimal.valueOf(0L)) != -1) {

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
            } else if (sum.compareTo(BigDecimal.valueOf(0L)) == -1) {

                error = "You cannot enter a negative amount";
            } else {

                error = "There were technical problems. If the error occurs again, please contact technical support";
            }
            result = "==================\nError: 0    :   " + error + "\n==================";
        }

        return result;
    }

    @Transactional
    public String takeMoney(Long id, BigDecimal sum) {

        String result = "";

        if (usersRepository.findById(id).isPresent() && usersRepository.findById(id).get().getBalance().subtract(sum)
                .compareTo(BigDecimal.valueOf(0L)) != -1 && sum.compareTo(BigDecimal.valueOf(0L)) != -1) {

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
            } else if (sum.compareTo(BigDecimal.valueOf(0L)) == -1) {

                error = "Can't withdraw negative amount";
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
            } else if (operation.getOperation_type() == 3 && operation.getTo_user_id() != null) {
                if (operation.getUser_id().equals(id)) {
                    oper_type = "Transfer money to client with ID=" + operation.getTo_user_id();
                } else {
                    oper_type = "Receive money from client with ID=" + operation.getUser_id();
                }
            } else {
                oper_type = "Sorry. Don't know this operation type.";
            }
            if (start == null && end == null) {

                if (operation.getUser_id().equals(id) || (operation.getTo_user_id() != null && operation.getTo_user_id().equals(id))) {

                    result.add("---------------------------------------");
                    result.add("Operation date: " + operation.getDate());
                    result.add("Operation type is: " + oper_type + " (" + operation.getOperation_type() + ")");
                    result.add("Transaction amount: " + operation.getAmount());
                    result.add("---------------------------------------");
                }
            } else if (start == null) {

                if (operation.getUser_id().equals(id) && (operation.getDate().isBefore(end))
                        || ((operation.getTo_user_id() != null && operation.getTo_user_id().equals(id)) && (operation.getDate().isBefore(end)))) {

                    result.add("---------------------------------------");
                    result.add("Operation date: " + operation.getDate());
                    result.add("Operation type is: " + oper_type + " (" + operation.getOperation_type() + ")");
                    result.add("Transaction amount: " + operation.getAmount());
                    result.add("---------------------------------------");
                }
            } else if (end == null) {
                if ((operation.getUser_id().equals(id) && (operation.getDate().isAfter(start) || operation.getDate().equals(start)))
                        || (((operation.getTo_user_id() != null && operation.getTo_user_id().equals(id))
                        && (operation.getDate().isAfter(start) || operation.getDate().equals(start))))) {

                    result.add("---------------------------------------");
                    result.add("Operation date: " + operation.getDate());
                    result.add("Operation type is: " + oper_type + " (" + operation.getOperation_type() + ")");
                    result.add("Transaction amount: " + operation.getAmount());
                    result.add("---------------------------------------");
                }
            } else if ((operation.getDate().isAfter(start) || operation.getDate().equals(start)) &&
                    (operation.getDate().isBefore(end) || operation.getDate().equals(end))) {
                if (operation.getUser_id().equals(id) || (operation.getTo_user_id() != null && operation.getTo_user_id().equals(id))) {

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

    @Transactional
    public String transferMoney(Long fromId, Long toId, int amount) {

        String result = "";
        if ((usersRepository.findById(fromId).isPresent()
                && usersRepository.findById(fromId).get().getBalance().subtract(BigDecimal.valueOf(amount))
                .compareTo(BigDecimal.valueOf(0L)) != -1) && usersRepository.findById(toId).isPresent()
                && amount >= 0) {

            Optional<Users> userFrom = usersRepository.findById(fromId);
            Optional<Users> userTo = usersRepository.findById(toId);

            result = "=====================\nTransfer from account: ID=" + fromId + "\nto account with ID=" + toId
                    + "\ntransaction amount: " + amount + "\n=====================";

            userFrom.get().setBalance(usersRepository.findById(fromId).get().getBalance().subtract(BigDecimal.valueOf(amount)));
            usersRepository.save(userFrom.get());

            userTo.get().setBalance(usersRepository.findById(toId).get().getBalance().add(BigDecimal.valueOf(amount)));
            usersRepository.save(userTo.get());

            Operations operationFrom = new Operations(fromId, 3, amount, LocalDate.now(), toId);
            operationsRepository.save(operationFrom);

        } else {
            String error;
            if (usersRepository.findById(fromId).isEmpty()) {
                error = "There is not such client with ID= " + fromId;
            } else if (usersRepository.findById(toId).isEmpty()) {
                error = "There is not such client with ID= " + toId;
            } else if (usersRepository.findById(fromId).get().getBalance().subtract(BigDecimal.valueOf(amount))
                    .compareTo(BigDecimal.valueOf(0L)) == -1) {

                error = "Insufficient funds on the personal account ID= " + fromId;
            } else if (amount < 0) {
                error = "You can't transfer a negative amount";
            } else {

                error = "There were technical problems. If the error occurs again, please contact technical support";
            }
            result = "==================\nError: 0    :   " + error + "\n==================";
        }

        return result;
    }
}
