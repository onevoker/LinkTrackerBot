package edu.java.stackOverflow.dto;

import java.util.List;

public record QuestionResponse(
    List<Item> items) {
}
