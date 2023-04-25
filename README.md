# finalSkillfactoryProject
API для интернет-банка.

В данный момент реализовано два этапа проекта:

Шаг 1. Спроектировать базу данных (Создайте одну таблицу, которая будет хранить ID пользователя и текущий баланс пользователя.)

Шаг 2. Создать класс по работе с базой данных(С помощью этого класса ваш код будет взаимодействовать с базой данных. 
Создайте в этом классе три функции: getBalance, putMoneу, takeMoney.)

Шаг 3. Создайте Rest API(Реализуйте Rest API для операции getBalance. Когда она заработает, добавьте операции putMoney и takeMoney.)

Шаг 4. Создание таблицы с операциями(Создайте таблицу для хранения списка операций.)

Шаг 5. Доработайте класс по работе с базой данных(Доработайте функции putMoney и takeMoney, при каждой операции вы должны добавлять новое значение в вашу новую таблицу со списком операций. Добавьте в ваш класс по работе с базой данных функцию getOperationList. Предусмотрите возможность, чтобы одно или оба значения диапазона дат были пустыми.)

Шаг 6.  Добавьте операцию getOperationList в Rest API(Добавьте новую операцию в ваш Rest API.)

Выполнять операции со счётом по соответсвующим эндпоинтам
узнать баланс
http://localhost:8080/getBalance/{id}
http://localhost:8080/getBalance/21
снять со счета
http://localhost:8080/takeMoney/{id}/{sum}
http://localhost:8080/takeMoney/21/550
пополнить счёт
http://localhost:8080/putMoney/{id}/{sum}
http://localhost:8080/putMoney/21/550
получить операции пользователя по id и дате
http://localhost:8080/getOperationList?user_id=21&start=2023-04-25&end=2023-04-25
http://localhost:8080/getOperationList?user_id=21&start=2023-04-10&end=
http://localhost:8080/getOperationList?user_id=21&start=&end=2023-04-10
http://localhost:8080/getOperationList?user_id=21&start=&end=

Скриншот структуры базы данных:

![image](https://user-images.githubusercontent.com/96256651/234287456-a14f9ded-eca8-4b4c-b207-1ecdbe9e267c.png)

