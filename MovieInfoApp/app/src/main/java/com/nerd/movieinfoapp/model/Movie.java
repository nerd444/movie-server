package com.nerd.movieinfoapp.model;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private String attendance;
    private String year;
    private int reply_cnt;
    private Double avg_rating;
    private int is_favorite;

    public Movie(){

    }

    public Movie(int id, String title, String genre, String attendance, String year, int is_favorite) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
        this.is_favorite = is_favorite;
    }

    public Movie(int id, String title, String genre, String attendance, String year, int reply_cnt, Double avg_rating, int is_favorite) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
        this.reply_cnt = reply_cnt;
        this.avg_rating = avg_rating;
        this.is_favorite = is_favorite;
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

    public int getReply_cnt() {
        return reply_cnt;
    }

    public void setReply_cnt(int reply_cnt) {
        this.reply_cnt = reply_cnt;
    }

    public Double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(Double avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }
}

