package com.ifirenet.clientfirenetwebhouse.Utils.Client;

/**
 * Created by Pouriya.kh on 8/17/2016.
 */
public class CreateTicket {
    String title, text;
    int priority, userId;
    public CreateTicket(String title, String text, int priority, int userId){
        this.title = title;
        this.text = text;
        this.priority = priority;
        this.userId = userId;
    }

    public String getTitle() {return this.title;}

    public String getText() {return this.text;}

    public int getPriority() {return this.priority;}

    public int getUserId() {return this.userId;}
}
