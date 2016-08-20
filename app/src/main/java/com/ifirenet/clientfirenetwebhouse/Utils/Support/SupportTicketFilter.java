package com.ifirenet.clientfirenetwebhouse.Utils.Support;

/**
 * Created by Pouriya.kh on 8/16/2016.
 */
public class SupportTicketFilter {
    private int trackingCode;
    public String result;
    public String status;
    public String priority;

    public void setTrackingCode(int trackingCode) { this.trackingCode = trackingCode; }
    public int getTrackingCode() {return this.trackingCode; }

    public void setResult(String result){this.result = result; }
    public String getResult(){return this.result; }

    public void setPriority(String priority){ this.priority = priority;}
    public String getPriority(){return this.priority;}

    public void setStatus(String status){ this.status = status;}
    public String getStatus(){ return this.status;}
}
