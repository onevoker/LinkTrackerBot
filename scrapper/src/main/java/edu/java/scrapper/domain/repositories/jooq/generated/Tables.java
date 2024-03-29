/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.domain.repositories.jooq.generated;

import edu.java.scrapper.domain.repositories.jooq.generated.tables.Chat;
import edu.java.scrapper.domain.repositories.jooq.generated.tables.ChatLink;
import edu.java.scrapper.domain.repositories.jooq.generated.tables.Link;
import edu.java.scrapper.domain.repositories.jooq.generated.tables.QuestionResponse;
import edu.java.scrapper.domain.repositories.jooq.generated.tables.RepositoryResponse;
import javax.annotation.processing.Generated;

/**
 * Convenience access to all tables in the default schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Tables {

    /**
     * The table <code>CHAT</code>.
     */
    public static final Chat CHAT = Chat.CHAT;

    /**
     * The table <code>CHAT_LINK</code>.
     */
    public static final ChatLink CHAT_LINK = ChatLink.CHAT_LINK;

    /**
     * The table <code>LINK</code>.
     */
    public static final Link LINK = Link.LINK;

    /**
     * The table <code>QUESTION_RESPONSE</code>.
     */
    public static final QuestionResponse QUESTION_RESPONSE = QuestionResponse.QUESTION_RESPONSE;

    /**
     * The table <code>REPOSITORY_RESPONSE</code>.
     */
    public static final RepositoryResponse REPOSITORY_RESPONSE = RepositoryResponse.REPOSITORY_RESPONSE;
}
