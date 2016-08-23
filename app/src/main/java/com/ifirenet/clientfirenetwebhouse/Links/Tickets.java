package com.ifirenet.clientfirenetwebhouse.Links;

import com.ifirenet.clientfirenetwebhouse.Utils.Urls;

/**
 * Created by Pouriya.kh on 8/13/2016.
 */
public class Tickets {
    int userId, offset = -1, topNumber = -1;
    public Tickets(int userId, int offset, int topNumber){
        this.userId = userId;
        this.offset = offset;
        this.topNumber = topNumber;
    }

    public String getClientTicketUrl(Login login){
        String url = Urls.baseURL + "ClientPortalService.svc/"  ;
        if (offset == -1 && topNumber == -1)
            return url + "GetTicketsByUserID/"+ login.getUsername() + "/" + login.getPassword() + "/" + userId ;
        else if(offset != -1 && topNumber == -1)
            return url + "GetTicketsByUserIDFromRow/" + login.getUsername() + "/" + login.getPassword() + "/" + userId  + "?offset=" + offset;
        else if(offset != -1 && topNumber != -1)
            return url + "GetTicketsByUserIDFromRowTopN/"+ login.getUsername() + "/" + login.getPassword() + "/" + userId +"?offset=" + offset + "&topN=" + topNumber;
        return url;
    }

    public String getSupportTicketUrl(Login login){
        String url = Urls.baseURL + "ClientPortalService.svc/";
        if (offset == -1 && topNumber == -1)
            return url + "GetTicketsToUser/" + login.getUsername() + "/" + login.getPassword() + "/" + userId ;
        else if(offset != -1 && topNumber == -1)
            return url + "GetTicketsToUserFromRow/" + login.getUsername() + "/" + login.getPassword() + "/" + userId  + "?offset=" + offset;
        else if(offset != -1 && topNumber != -1)
            return url + "GetTicketsToUserFromRowTopN/" + login.getUsername() + "/" + login.getPassword() + "/" + userId +"?offset=" + offset + "&topN=" + topNumber;
        return url;
    }
}
