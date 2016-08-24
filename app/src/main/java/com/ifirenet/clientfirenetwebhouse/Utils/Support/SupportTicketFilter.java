package com.ifirenet.clientfirenetwebhouse.Utils.Support;

/**
 * Created by Pouriya.kh on 8/16/2016.
 */
public class SupportTicketFilter {
    private String trackingCode;
    private int result;
    private int status;
    private int priority;

    public void setTrackingCode(String trackingCode) { this.trackingCode = trackingCode; }
    public String getTrackingCode() {return this.trackingCode; }

    public void setResult(int result){this.result = result; }
    public int getResult(){return this.result; }

    public void setPriority(int priority){ this.priority = priority;}
    public int getPriority(){return this.priority;}

    public void setStatus(int status){ this.status = status;}
    public int getStatus(){ return this.status;}
}
