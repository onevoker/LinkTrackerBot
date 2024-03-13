CREATE TABLE CHAT
(
    id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE LINK
(
    id          BIGSERIAL,
    url         VARCHAR(255)             NOT NULL,
    last_update TIMESTAMP WITH TIME ZONE NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (url)
);

CREATE TABLE CHAT_LINK
(
    id      BIGSERIAL,
    chat_id BIGINT REFERENCES CHAT(id),
    link_id BIGINT REFERENCES LINK(id),

    PRIMARY KEY (id)
);
