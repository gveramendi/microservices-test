package net.veramendi.account_microservice.repository;

import net.veramendi.account_microservice.domain.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> getAllByAccountId(Long accountId);

    @Query(value = "select t from Transaction t where t.id = :id AND t.createdDate >= :startDate AND t.createdDate <= :endDate")
    List<Transaction> getAllByAccountIdAndCreatedDateBetween(
            @Param("id") Long id, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
