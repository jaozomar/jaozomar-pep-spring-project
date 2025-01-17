package com.example.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Create a new Account in the account table as long as it has a valid, non-duplicate username
     * @param Account acc
     * @return null if username was of invalid length, an empty Optional if a duplicate exists, or a 
     *         non-empty Optional if creation was succesful
     */
    public Optional<Account> registerAccount(Account acc) {
        if(acc.getUsername() == null || acc.getUsername().length() < 4)
            return null;
        
        // check if account with username exists
        Optional<Account> result = accountRepository.findAccountByUsername(acc.getUsername());

        // add account if it does not exist, and get the account again so that you have the updated id
        if(result.isEmpty())
            result = Optional.ofNullable(accountRepository.save(acc)); // get account just saved
        else
            result = Optional.empty(); //empty the optional if it already existed, so that we return an empty optional

        return result;
    }

    /**
     * Attempt to login to an account with the username and password
     * @param Account acc
     * @return an Optional that is the result of searching for an account with matching username and password.
     *         The Optional will be empty if no match found.
     */
    public Optional<Account> login(Account acc) {
        return accountRepository.findAccountByUsernameAndPassword(acc.getUsername(), acc.getPassword());
    }

    /**
     * Check if an Account with a specified AccountId exists. Used to check if postedBy in Message table is valid
     * @param int id
     * @return true if matching account exists, or false otherwise
     */
    public boolean existsById(int id) {
        return accountRepository.existsByAccountId(id);
    }
}
