package com.masterweb.firdausapps.quran.model;

public class SuratModel {
    public SuratModel(String number,String arab, String nama, String keterangan) {
        this.number =number;
        this.arab = arab;
        this.nama = nama;
        this.keterangan = keterangan;
    }
    private String arab;
    private String nama;
    private String keterangan;
    private String number;
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

    public String getNama(){
        return nama;
    }
    public void setNama(String nama){
        this.nama = nama;
    }

    public String getKeterangan(){
        return keterangan;
    }
    public void setKeterangan(String keterangan){
        this.keterangan = keterangan;
    }
}
