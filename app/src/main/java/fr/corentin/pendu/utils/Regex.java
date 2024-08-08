package fr.corentin.pendu.utils;

/**
 * Enum {@link Regex}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public enum Regex {

    EMAIL("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");

    private final String reg;

    Regex(String reg) { this.reg = reg; }

    public String getReg() { return reg; }
}
