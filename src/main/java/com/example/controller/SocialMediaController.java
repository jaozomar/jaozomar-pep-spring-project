package com.example.controller;

import com.example.entity.*;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController // accounts for @ResponseBody annotation
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity registerAccount(@RequestBody Account account) {
        Optional<Account> result = accountService.registerAccount(account);

        //failed because of empty or short username
        if(result == null)
            return ResponseEntity.status(400).body(account);
        else if(result.isEmpty()) // failed because of duplicate
            return ResponseEntity.status(409).body(account);
        else // return account with updated id if succesfully persisted
            return ResponseEntity.status(200).body(result);
    }
}
