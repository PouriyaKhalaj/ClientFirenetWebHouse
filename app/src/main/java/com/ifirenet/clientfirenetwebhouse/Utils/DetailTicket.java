package com.ifirenet.clientfirenetwebhouse.Utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pouriya.kh on 8/20/2016.
 */
public class DetailTicket {

    @SerializedName("Attachment")
    public String attachment;

    @SerializedName("DocumentCreatedWhen")
    public String documentCreatedWhen;

    @SerializedName("ExternalStatus")
    public String externalStatus;

    @SerializedName("ExternalStatusColor")
    public String externalStatusColor;

    @SerializedName("FirstName")
    public String firstName;

    @SerializedName("LastName")
    public String lastName;

    @SerializedName("NodeID")
    public String nodeID;

    @SerializedName("PriorityStatus")
    public String priorityStatus;

    @SerializedName("PriorityStatusColor")
    public String priorityStatusColor;

    @SerializedName("PublicStatus")
    public String publicStatus;

    @SerializedName("PublicStatusColor")
    public String publicStatusColor;

    @SerializedName("Text")
    public String text;

    @SerializedName("Title")
    public String title;

    @SerializedName("TrackingNumber")
    public String trackingNumber;

}
