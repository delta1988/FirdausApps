package com.masterweb.firdausapps.hadits.model;

public class DetailHaditsModel {
    String number,arab,id;
    public DetailHaditsModel(String number,String arab, String id) {
        this.number =number;
        this.arab = arab;
        this.id = id;
    }
    public String getNumber(){
        return number;
    }
    public void setNumber(String number){
        this.number = number;
    }
    public String getArab(){
        return arab;
    }
    public void setArab(String arab){
        this.arab = arab;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
}
