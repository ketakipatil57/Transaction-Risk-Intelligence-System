package com.pict.Repository;

import com.pict.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction , Long> {

    List<Transaction> findByUserId(Long userId);

    long countByUserId(Long userId);
    // To get to know how many transactions the respective user has made

    boolean existsByUserIdAndReceiver(Long UserId, String Receiver);

    // to check whether the respective user exists ani ya adhi kadhi ys receiver sobat transaction kele ahe ka?


}
