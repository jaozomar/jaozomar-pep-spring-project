package com.example.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
    /**
     * Find account by specified username
     * @param String username
     * @return Optional<Account>. Either empty, or the found account
     */
    Optional<Account> findAccountByUsername(String username);

    /**
     * Find account by specified username and password
     * @param String username
     * @param String password
     * @return Optional<Account>. Either empty, or the found account
     */
    Optional<Account> findAccountByUsernameAndPassword(String username, String password);

    /**
     * Check if an account with a specified accountId exists
     * @param int accountId
     * @return true if found, false if not found
     */
    boolean existsByAccountId(int accountId);
}
