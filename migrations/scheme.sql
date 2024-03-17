CREATE TABLE chat
(
    id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE link
(
    id             BIGSERIAL,
    url            VARCHAR(255)             NOT NULL,
    last_update    TIMESTAMP WITH TIME ZONE NOT NULL,
    last_api_check TIMESTAMP WITH TIME ZONE NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (url)
);

CREATE TABLE chat_link
(
    id      BIGSERIAL,
    chat_id BIGINT REFERENCES CHAT (id),
    link_id BIGINT REFERENCES LINK (id),

    PRIMARY KEY (id),
    CONSTRAINT unique_chat_link_pair UNIQUE (chat_id, link_id)
);

CREATE TABLE repository_response
(
    id        BIGINT,
    link_id   BIGINT REFERENCES LINK (id) ON DELETE CASCADE,
    pushed_at TIMESTAMP WITH TIME ZONE NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (link_id)
);

CREATE TABLE question_response
(
    question_id        BIGINT,
    link_id            BIGINT REFERENCES LINK (id) ON DELETE CASCADE,
    answered           BOOLEAN,
    answer_count       BIGINT,
    last_activity_date TIMESTAMP WITH TIME ZONE NOT NULL,

    PRIMARY KEY (question_id),
    UNIQUE (link_id)
)
