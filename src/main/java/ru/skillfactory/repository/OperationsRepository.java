package ru.skillfactory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillfactory.entity.Operations;

@Repository
public interface OperationsRepository extends JpaRepository<Operations, Long> {

}
