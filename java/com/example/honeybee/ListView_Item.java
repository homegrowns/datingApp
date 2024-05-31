package com.example.honeybee;

public class ListView_Item {
    private String nation;
    private String PhoneNum;

    public ListView_Item(String nation, String phoneNum) {
        this.nation = nation;
        PhoneNum = phoneNum;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }
}
