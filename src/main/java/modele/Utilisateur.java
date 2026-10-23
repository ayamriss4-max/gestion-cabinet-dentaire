package modele;

import util.MotDePasse;

public abstract class Utilisateur {

    private String login;
    private String pwd;

    public Utilisateur(String login, String pwdHache) {
        this.login = login;
        this.pwd = pwdHache;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPwd() { return pwd; }
    public void setPwd(String pwdHache) { this.pwd = pwdHache; }

    public void changerMotDePasse(String motDePasseClair) {
        this.pwd = MotDePasse.hacher(motDePasseClair);
    }

    
    public boolean authentifier(String login, String motDePasseClair) {
        return this.login.equals(login) && this.pwd.equals(MotDePasse.hacher(motDePasseClair));
    }

    
    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + " " + login;
    }
}