package com.pritha.www.otpverification.Model;

public class ModelUsers {

    String username, email, id,image,onlinestatus,typingto;
    private String imgName,imgUrl;

    public ModelUsers() {

    }

    public ModelUsers(String username, String email, String id, String image, String onlinestatus, String typingto, String imgName,String imgUrl) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.image = image;
        this.onlinestatus = onlinestatus;
        this.typingto = typingto;

        if(imgName.trim().equals(""))
        {
            imgName="No name";
        }
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOnlinestatus() {
        return onlinestatus;
    }

    public void setOnlinestatus(String onlinestatus) {
        this.onlinestatus = onlinestatus;
    }

    public String getTypingto() {
        return typingto;
    }

    public void setTypingto(String typingto) {
        this.typingto = typingto;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}