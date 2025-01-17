package com.example.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    AccountService accountService;
    MessageRepository messageRepository;

    @Autowired
    public MessageService(AccountService accountService, MessageRepository messageRepository) {
        this.accountService = accountService;
        this.messageRepository = messageRepository;
    }

    /**
     * Create a new message as long as the postedBy user exists and the message is of valid length
     * @param Message msg (message to create)
     * @return null if failed to create, or Message created if we are able to create one
     */
    public Message createMessage(Message msg) {
        if(msg.getMessageText() == null || msg.getMessageText().length() <= 0) // can't be empty
            return null;
        else if(msg.getMessageText().length() > 255) // can't be over 255 characters
            return null;
        else if(!accountService.existsById(msg.getPostedBy())) // a corresponding account must exist
            return null;
        else {
            return messageRepository.save(msg); // return saved message
        }
    }

    /**
     * Retrieve all messages from the Message table
     * @return List<Message> collection
     */
    public List<Message> retrieveMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieve a message by its messageId
     * @param int msgId
     * @return An Optional<Message> that can be the found message, or it can be null
     */
    public Optional<Message> retrieveById(int msgId) {
        return messageRepository.findMessageByMessageId(msgId);
    }

    /**
     * Delete a message by its messageId
     * @param int msgId
     * @return true if the message was deleted, or false if the message did not exist
     */
    public boolean deleteById(int msgId) {
        Optional<Message> msgFound = retrieveById(msgId);

        if(msgFound.isEmpty()) // message doesn't exist
            return false;
        else {
            messageRepository.delete(msgFound.get()); // delete found message
            return true;
        }
    }

    /**
     * Update a specified message's messageText
     * @param int msgId
     * @param String msgText
     * @return true if the message was updated succesfully, false if it could not be updated
     */
    @Transactional // will be executing a custom query to update messageText
    public boolean updateMessage(int msgId, String msgText) {
        if(msgText == null || msgText.length() <= 0) // can't be empty
            return false;
        else if(msgText.length() > 255) // can't be over 255 characters
            return false;

        Optional<Message> msgFound = retrieveById(msgId);

        if(msgFound.isEmpty()) // message doesn't exist
            return false;
        else {
            messageRepository.updateMessageText(msgText, msgId); // update message if it exists
            return true;
        }
    }

    /**
     * Retrieve a list of all messages posted by a specified user
     * @param int postedBy
     * @return List<Message> collection
     */
    public List<Message> retrieveMessagesByUser(int postedBy) {
        return messageRepository.findMessageByPostedBy(postedBy);
    }
}
