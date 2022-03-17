package com.example.liftyourlife.Class;

public class Tag {
    private String TagMame;
    private int Photo;
    public Tag() { }
    public Tag(String TagMame, int photo) {
        this.TagMame = TagMame;
        this.Photo = photo;
    }
    public int getPhoto() { return Photo; }
    public String getTagName() { return TagMame; }
    public void setTagMame(String TagMame) {  this.TagMame = TagMame; }
    public void setPhoto(int photo) {  this.Photo = photo; }
}