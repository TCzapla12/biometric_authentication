package com.example.biometricauthentication;


public class FileData {
    public String id;
    public String name;
    public String type;
    public String size;
    public String date;
    FileData(String id, double size, String date)
    {
        this.id = id;
        this.name = id.split(".")[0];
        this.type = id.split(".")[1];
        this.size = Math.round(size/10000)/100.0 + " MB";
        this.date = date;

    }
    public void Rebase()
    {
        this.size =  Math.round(Double.parseDouble(this.size)/10000)/100.0 + " MB";
    }
}
