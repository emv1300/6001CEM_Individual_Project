package com.example.a6001cem_artapp.adapters_and_models;

public class User {
    public String email, userName, Image;
    public User(){}

    public User(String email, String userName) {
        this.email = email;
        this.userName = userName;
        this.Image = "https://firebasestorage.googleapis.com/v0/b/artapp6001cem.appspot.com/o/Users%2Fdefault_profile.jpg?alt=media&token=cde8d310-ca25-4b9b-baad-937c15868ca6";
    }
}
