package edu.java.scrapper.dto.stackOverflowDto;

import java.util.List;

public record QuestionResponse(
    List<Item> items) {
}
