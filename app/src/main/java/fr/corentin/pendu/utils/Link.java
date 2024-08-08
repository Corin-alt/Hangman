package fr.corentin.pendu.utils;

public enum Link {

    RULES_FR("https://fr.wikihow.com/jouer-au-pendu"),
    RULES_EN("https://www.wikihow.com/Play-Hangman");

    private final String link;

    Link(String link) { this.link = link; }

    public String getLink() { return link; }
}
