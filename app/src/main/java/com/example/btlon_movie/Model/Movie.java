package com.example.btlon_movie.Model;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private int ID;
    private String Name;
    private String Image;
    private String Description;
    private String thumbnail;
    private String language;
    private List<Category> category=new ArrayList<>();
    private List<Country> country=new ArrayList<>();
    private String rating;
    private String Link;
    private int Year;


    public Movie() {
    }

    public Movie(int ID, String name, String image, String description, String thumbnail,
                 String language, List<Category> category, List<Country> country, String rating, String link, int year) {
        this.ID = ID;
        Name = name;
        Image = image;
        Description = description;
        this.thumbnail = thumbnail;
        this.language = language;
        this.category=category;
        this.country=country;
        this.rating = rating;
        Link = link;
        Year = year;
    }



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Category> getCategory() {
        return category;
    }
    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }


}
