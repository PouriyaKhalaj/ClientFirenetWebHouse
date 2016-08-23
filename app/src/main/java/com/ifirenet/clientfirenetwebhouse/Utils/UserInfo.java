package com.ifirenet.clientfirenetwebhouse.Utils;

import com.google.gson.annotations.SerializedName;
import com.ifirenet.clientfirenetwebhouse.Links.Login;

/**
 * Created by Pouriya.kh on 8/23/2016.
 */
public class UserInfo {
    @SerializedName("user")
    public User user;

    @SerializedName("login")
    public Login login;
}
