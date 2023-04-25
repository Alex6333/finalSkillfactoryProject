package ru.skillfactory.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillfactory.entity.Users;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {


}
