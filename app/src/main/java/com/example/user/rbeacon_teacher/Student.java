package com.example.user.rbeacon_teacher;

/**
 * Created by user on 2016/11/7.
 */

public class Student {
    private String number; // 學號
    private String name; // 名字
    private String sex; // 性別
    private String phone; // 手機

    private Double scoreMid; // 分數 - 期中
    private Double scoreFin; // 分數 - 期末
    private Double scorePar; // 分數 - 平均
    //private String attend; // 出席率
    //private String birthday; // 生日
    //private String home; // 地址
    private int afl; // 請假數
    private int late; // 遲到數
    private int abs; // 曠課數

    public Student(String number, String name, String sex)
    {
        this.number = number;
        this.name = name;
        this.sex = sex;
        this.scoreMid = 0.0;
        this.scorePar = 0.0;
        this.scoreFin = 0.0;
        //this.attend = "100%";
        this.phone = "";
        this.afl = 0;
        this.late = 0;
        this.abs = 0;
    }

    public Student(String number, String name, String sex, String phone)
    {
        this.number = number;
        this.name = name;
        this.sex = sex;
        this.scoreMid = 0.0;
        this.scorePar = 0.0;
        this.scoreFin = 0.0;
        //this.attend = "100%";
        this.phone = phone;
        this.afl = 0;
        this.late = 0;
        this.abs = 0;
    }

    public Student(String number, String name, String sex, String phone, Double scoreMid, Double scoreFin, Double scorePar)
    {
        this.number = number;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.scoreMid = scoreMid;
        this.scorePar = scorePar;
        this.scoreFin = scoreFin;
        //this.attend = attend;
        this.afl = 0;
        this.late = 0;
        this.abs = 0;
    }

    public Student(String number, String name, String sex, String phone, Double scoreMid, Double scoreFin, Double scorePar, int afl, int late, int abs)
    {
        this.number = number;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.scoreMid = scoreMid;
        this.scorePar = scorePar;
        this.scoreFin = scoreFin;
        //this.attend = attend;
        this.afl = afl;
        this.late = late;
        this.abs = abs;
    }

    public Student(String number, String name, String sex, String phone, Double scoreMid, Double scoreFin, int afl, int late, int abs)
    {
        this.number = number;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.scoreMid = scoreMid;
        this.scorePar = 0.0;
        this.scoreFin = scoreFin;
        //this.attend = attend;
        this.afl = afl;
        this.late = late;
        this.abs = abs;
    }


    public String getNumber() {return number;}
    public String getName() {return name;}
    public String getSex() {return sex;}
    public String getPhone() {return phone;}
    public Double getScoreMid() {return scoreMid;}
    public Double getScoreFin() {return scoreFin;}
    public Double getScorePar() {return scorePar;}
    public int getAfl() {return afl;}
    public int getLate() {return late;}
    public int getAbs() {return abs;}
    public String[] getAll()
    {
        String[] string = new String[10];
        string[0] = number;
        string[1] = name;
        string[2] = sex;
        string[3] = phone;
        string[4] = scoreMid.toString();
        string[5] = scoreFin.toString();
        string[6] = scorePar.toString();
        string[7] = String.valueOf(afl);
        string[8] = String.valueOf(late);
        string[9] = String.valueOf(abs);
        return string;
    }
    public void setNumber(String input) {number = input;}
    public void setName(String input) {name = input;}
    public void setSex(String input) {sex = input;}
    public void setPhone(String input) {phone = input;}
    public void setScoreMid(Double input) {scoreMid = input;}
    public void setScoreFin(Double input) {scoreFin = input;}
    public void setScorePar(Double input) {scorePar = input;}
    public void setAfl(Integer input) {afl = input;}
    public void setLate(Integer input) {late = input;}
    public void setAbs(Integer input) {abs = input;}
}
