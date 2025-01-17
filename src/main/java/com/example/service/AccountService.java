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

    public Optional<Account> registerAccount(Account acc) {
        if(acc.getUsername() == null || acc.getUsername().length() < 4)
            return null;
        
        // check if account with username exists
        Optional<Account> result = accountRepository.findAccountByUsername(acc.getUsername());

        // add account if it does not exist, and get the account again so that you have the updated id
        if(result.isEmpty()) {
            accountRepository.save(acc);
            result = accountRepository.findAccountByUsername(acc.getUsername());
        } else
            result = Optional.empty(); //empty the optional if it already existed, so that we return an empty optional

        return result;
    }
}
