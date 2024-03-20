package edu.java.scrapper.linkParser.dto;

public record GitHubLinkData(String owner, String repo) implements LinkData {
}
