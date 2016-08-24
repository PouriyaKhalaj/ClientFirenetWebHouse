package com.ifirenet.clientfirenetwebhouse.Utils.Update;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pouriya.kh on 8/21/2016.
 */
public class VersionList {

    @SerializedName("apkLink")
    public String apkLink;

    @SerializedName("version")
    public ArrayList<Version> versionList;
}
