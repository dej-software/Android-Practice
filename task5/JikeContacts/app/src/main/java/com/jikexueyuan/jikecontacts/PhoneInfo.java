package com.jikexueyuan.jikecontacts;

/**
 * Created by dej on 2016/9/21.
 */
public class PhoneInfo {

    private String name;
    private String number;

    public PhoneInfo() {
    }

    public PhoneInfo(String name, String number) {
        setName(name);
        setNumber(number);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
