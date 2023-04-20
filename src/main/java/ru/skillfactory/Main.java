package ru.skillfactory;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.skillfactory.entity.Users;
import ru.skillfactory.repository.UsersRepository;
import ru.skillfactory.service.UsersService;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootApplication
@RequiredArgsConstructor
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        /*ConfigurableApplicationContext context = SpringApplication.run(Main.class);
        UsersService service = context.getBean(UsersService.class);

        service.getBalance(21L);
        service.getBalance(23L);
        service.getBalance(27L);

        service.putMoney(21L,BigDecimal.valueOf(150.00));
        service.putMoney(23L,BigDecimal.valueOf(250.00));
        service.putMoney(27L,BigDecimal.valueOf(50.00));

        service.takeMoney(21L,BigDecimal.valueOf(950.00));
        service.takeMoney(23L,BigDecimal.valueOf(250.00));
        service.takeMoney(27L,BigDecimal.valueOf(50.00));*/
    }
}










/*try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {

                System.out.println("Connection to Store DB successful!");


                *//*Statement statement = conn.createStatement();


                ResultSet resultSet = statement.executeQuery("SELECT * FROM user_balance");
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    BigDecimal balance = resultSet.getBigDecimal(2);
                    System.out.printf("id: %d. Баланс = " + balance, id);
                }*//*
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

    public static Connection getConnection() throws SQLException, IOException {

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("src\\main\\resources\\application.properties"))) {
            props.load(in);
        }
        String url = props.getProperty("spring.datasource.url");
        String username = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");

        return DriverManager.getConnection(url, username, password);*/