package com.ifirenet.clientfirenetwebhouse.Utils.Support;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Pouriya.kh on 8/14/2016.
 */
public class SupportTicket {
    @SerializedName("trackingCode")
    public int trackingCode;

    @SerializedName("title")
    public String title;

    @SerializedName("status")
    public String status;

    @SerializedName("nodeID")
    public int nodeID;

    @SerializedName("company")
    public String company;

    @SerializedName("createDate")
    public String createDate;

    @SerializedName("customerFirstName")
    public String customerFirstName;

    @SerializedName("customerID")
    public int customerID;

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
