package edu.java.bot.links;

public record Link(Long userId, String stringLink, LinkValidatorService linkValidatorService) {
    public Link(Long userId, String stringLink, LinkValidatorService linkValidatorService) {
        this.linkValidatorService = linkValidatorService;
        this.userId = userId;
        if (linkValidatorService.isCorrectUri(stringLink)) {
            this.stringLink = stringLink;
        } else {
            throw new InvalidLinkException();
        }
    }
}
