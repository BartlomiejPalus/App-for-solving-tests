package com.example.testy;

public final class Account {
    private static Account account;
    private String login;

    public synchronized static Account getInstance(){
        if(account == null){
            account = new Account();
        }
        return account;
    }

    public void logIn(String login){
        this.login = login;
    }

    public void logOut()
    {
        login = null;
    }

    public String getLogin(){
        return login;
    }
}
