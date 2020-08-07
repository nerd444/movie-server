package com.nerd.movieinfoapp.model;

public class Movie {
    private String title;
    private String genre;
    private String attendance;
    private String year;

    public Movie(){

    }

    public Movie(String title, String genre, String attendance, String year) {
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
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
}
