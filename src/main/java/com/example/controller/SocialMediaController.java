package com.example.controller;

import com.example.entity.*;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.http.ResponseEntity;
import java.util.List;
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

    /**
     * Use Post request to register a new account
     * @param Account account
     * @return ResponseEntity with status 400 in case of invalid username length, 409 in
     *         case of duplicate username, and 200 in case of successful registration
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        Optional<Account> result = accountService.registerAccount(account);

        //failed because of empty or short username
        if(result == null)
            return ResponseEntity.status(400).body(account);
        else if(result.isEmpty()) // failed because of duplicate
            return ResponseEntity.status(409).body(account);
        else // return account with updated id if succesfully persisted
            return ResponseEntity.status(200).body(result.get());
    }

    /**
     * Use Post request to attempt a login for a specified account
     * @param Account account
     * @return ResponseEntity with status 401 if login fail, or 200 if login success
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Optional<Account> result = accountService.login(account);

        if(result.isEmpty()) // fail to login if no matching account
            return ResponseEntity.status(401).body(account);
        else // return account with account id if log in succesful
            return ResponseEntity.status(200).body(result.get());
    }

    /**
     * Use Post request to create a new message
     * @param Message message
     * @return ResponseEntity with status 400 if message creation fails, or 200 if
     *         creation successful
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message result = messageService.createMessage(message);

        if(result == null) // failed to create message for any reason
            return ResponseEntity.status(400).body(message);
        else // created message
            return ResponseEntity.status(200).body(result);
    }

    /**
     * Use Get request to retrieve a list of all messages
     * @return ResponseEntity with status 200. will always succeed
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> retrieveMessages() {
        return ResponseEntity.status(200).body(messageService.retrieveMessages());
    }

    /**
     * Use Get request to retrieve a message by a specified messageId
     * @param int messageId
     * @return ResponseEntity with resulting message in response body if successful retrieval.
     *         Otherwise the response body will be empty. Status is always 200
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> retrieveMessageById(@PathVariable int messageId) {
        Optional<Message> result = messageService.retrieveById(messageId);
        if(!result.isEmpty())
            return ResponseEntity.status(200).body(result.get()); // retrieved message in body
        else
            return ResponseEntity.status(200).build(); // empty body if message not found
    }

    /**
     * Use Delete request to delete a message by a specified messageId
     * @param int messageId
     * @return ResponseEntity with number of affected rows (1) in response body if
     *         deletion was successful. Otherwise the body will be empty. Status is
     *         always 200
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteById(@PathVariable int messageId) {
        if(messageService.deleteById(messageId))
            return ResponseEntity.status(200).body(1); // 1 row updated
        else
            return ResponseEntity.status(200).build(); // empty body
    }

    /**
     * Use Patch request to update a specified message's messageText, given its messageId
     * @param int messageId
     * @param Message message
     * @return ResponseEntity with status 200 and body 1 if a row was successfully updated.
     *         Otherwise the status will be 400 and the body will be empty.
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int messageId, @RequestBody Message message) {
        if(messageService.updateMessage(messageId, message.getMessageText()))
            return ResponseEntity.status(200).body(1); // 1 row updated
        else
            return ResponseEntity.status(400).build(); // client error status and empty body
    }

    /**
     * Use Get request to retrieve a list of messages given a specified postedBy account id
     * @param int accountId
     * @return ResponseEntity with status 200 and a list of messages in the response body. List can
     *         be empty.
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> retrieveMessagesByUser(@PathVariable int accountId) {
        return ResponseEntity.status(200).body(messageService.retrieveMessagesByUser(accountId));
    }

    /**
     * Handle unexpected exceptions
     * @param Exception e
     * @return ResponseEntity with status 500 and the exception message in the response body
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedExceptions(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
