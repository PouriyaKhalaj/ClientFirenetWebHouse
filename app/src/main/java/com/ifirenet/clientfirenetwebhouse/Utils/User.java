package com.ifirenet.clientfirenetwebhouse.Utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pouriya.kh on 8/13/2016.
 */
public class User {
    @SerializedName("ID")
    public int id;

    @SerializedName("username")
    public String username;

    @SerializedName("name")
    public String name;

    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("email")
    public String email;

    @SerializedName("tel")
    public String phone;

    @SerializedName("cell")
    public String cellPhone;

    @SerializedName("avatar")
    public String avatar;

    @SerializedName("company")
    public String company;

    @SerializedName("service")
    public String service;

    @SerializedName("url")
    public String url;

    @SerializedName("role")
    public String role;

    @SerializedName("message")
    public String message;
}
