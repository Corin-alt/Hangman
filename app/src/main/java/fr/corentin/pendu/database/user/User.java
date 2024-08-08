package fr.corentin.pendu.database.user;

import java.io.Serializable;

/**
 * Class {@link User}
 * @author Corentin Dupont
 * @version For project Info0306
 */
public class User implements Serializable {

    private long id;
    private String email;
    private String pseudo;
    private String password;
    private int win;
    private int lose;

    public User(long id, String email, String pseudo, String password, int win, int lose) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
        this.win = win;
        this.lose = lose;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

}
