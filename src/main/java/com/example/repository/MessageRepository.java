package com.example.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.entity.Message;;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    /**
     * Find message by specified messageId
     * @param int messageId
     * @return Optional<Message>. Either empty if not found, or the matching message.
     */
    Optional<Message> findMessageByMessageId(int messageId);

    /**
     * Custom query for an update of messageText by messageId
     * @param String messageText
     * @param int messageId
     */
    @Modifying
    @Query("UPDATE Message m SET m.messageText =:messageText WHERE m.messageId =:messageId")
    void updateMessageText(@Param("messageText") String messageText, @Param("messageId") int messageId);

    /**
     * Retrieve a list of all messages given a specified postedBy account id
     * @param int postedBy
     * @return List<Message> collection
     */
    List<Message> findMessageByPostedBy(int postedBy);
}
