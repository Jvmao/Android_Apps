package com.josevicente.ap2app.Model;

/**
 * Created by JoseVicente on 19/02/2018.
 */

public class User {
    private String id;
    private String name;
    private String lastName;
    private int age;
    private String mail;
    private String pass;

    public User(){}

    public User(String id, String name, String lastName, int age, String mail) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
