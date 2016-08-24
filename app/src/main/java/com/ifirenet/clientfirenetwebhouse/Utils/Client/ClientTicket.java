package com.ifirenet.clientfirenetwebhouse.Utils.Client;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Pouriya.kh on 8/8/2016.
 */
public class ClientTicket {
    @SerializedName("trackingCode")
    public String trackingCode;

    @SerializedName("title")
    public String title;

    @SerializedName("status")
    public String status;

    @SerializedName("nodeID")
    public String nodeID;

    @SerializedName("company")
    public String company;

    @SerializedName("createDate")
    public String createDate;

    @SerializedName("customerFirstName")
    public String customerFirstName;

    @SerializedName("customerID")
    public String customerID;

    @SerializedName("customerLastName")
    public String customerLastName;

    @SerializedName("modifyDate")
    public String modifyDate;

    @SerializedName("priority")
    public String priority;

    @SerializedName("result")
    public String result;

    @SerializedName("url")
    public String url;

    @SerializedName("priorityColor")
    public String priorityColor;

    @SerializedName("resultColor")
    public String resultColor;

    @SerializedName("statusColor")
    public String statusColor;

    @SerializedName("text")
    public String text;

    @SerializedName("ticketThreads")
    public String ticketThreads;

}
