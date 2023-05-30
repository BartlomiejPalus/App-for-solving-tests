package com.example.testy;

public final class Account {
    private static Account account;
    private String login;
    private int id = 1;

    public synchronized static Account getInstance(){
        if(account == null){
            account = new Account();
        }
        return account;
    }

    public void logIn(String login, int id){
        this.login = login;
        this.id = id;
    }

    public void logOut()
    {
        login = null;
        id = 0;
    }

    public String getLogin(){
        return login;
    }

    public int getId() {
        return id;
    }
}
