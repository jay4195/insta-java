package com.jay.instagram.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Data;

import java.util.Date;

@Data
public class Message {
    @JsonProperty("isSender")
    private boolean senderIsMe;
    private User sender;
    private User receiver;
    private String text;
    private Date createdAt;
}
