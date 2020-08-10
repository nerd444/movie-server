package com.nerd.movieinfoapp.model;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private String attendance;
    private String year;
    private int cnt;

    public Movie(){

    }

    public Movie(int id, String title, String genre, String attendance, String year) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
    }

    public Movie(int id, String title, String genre, String attendance, String year, int cnt) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
        this.cnt = cnt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}

