package com.ifirenet.clientfirenetwebhouse.Links;

import android.os.Parcel;
import android.os.Parcelable;

import com.ifirenet.clientfirenetwebhouse.Utils.Urls;

/**
 * Created by Pouriya.kh on 8/13/2016.
 */
public class Login {
    private String username, password;

    public Login(String username, String password){
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) { this.username = username;}
    public String getUsername() {return this.username;}

    public void setPassword(String password) {this.password = password;}
    public String getPassword() {return this.password;}

    public String getLoginUrl(){
        String url = Urls.baseURL +  "ClientPortalService.svc/Authentication/" + username + "/" + password;
        return url;
    }
}
