package ru.skillfactory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillfactory.entity.Users;
import ru.skillfactory.repository.UsersRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    public String getBalance(Long id) {

        String message = "";
        if (usersRepository.findById(id).isPresent()) {

            message = "==================\nClient (ID:" + id + ") balance is " + usersRepository.findById(id).get().getBalance() + " credits\n==================";
        } else {
            String error;
            if (usersRepository.findById(id).isEmpty()) {
                error = "There is not such client with ID= " + id;
            } else {
                //при создании таблицы "user_balance" для колонки "balance" прописано условие "not null", т.е. такой вариант вообще не должен появиться
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

        if (usersRepository.findById(id).isPresent() && usersRepository.findById(id).get().getBalance().subtract(sum).compareTo(BigDecimal.valueOf(0L)) != -1) {

            Optional<Users> users = usersRepository.findById(id);

            result = "===================== \nDebited from account: " + sum + " \nYour amount = "
                    + usersRepository.findById(id).get().getBalance().subtract(sum) + "\n=====================";

            users.get().setBalance(usersRepository.findById(id).get().getBalance().subtract(sum));
            usersRepository.save(users.get());
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
}
