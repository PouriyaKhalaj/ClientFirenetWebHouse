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

    public String getClientTicketUrl(){
        String url = Urls.baseURL + "ClientPortalService.svc/" ;
        if (offset == -1 && topNumber == -1)
            return url + "GetTicketsByUserID/" + userId ;
        else if(offset != -1 && topNumber == -1)
            return url + "GetTicketsByUserIDFromRow/" + userId  + "?offset=" + offset;
        else if(offset != -1 && topNumber != -1)
            return url + "GetTicketsByUserIDFromRowTopN/"+ userId +"?offset=" + offset + "&topN=" + topNumber;
        return url;
    }

    public String getSupportTicketUrl(){
        String url = Urls.baseURL + "ClientPortalService.svc/" ;
        if (offset == -1 && topNumber == -1)
            return url + "GetTicketsToUser/" + userId ;
        else if(offset != -1 && topNumber == -1)
            return url + "GetTicketsToUserFromRow/" + userId  + "?offset=" + offset;
        else if(offset != -1 && topNumber != -1)
            return url + "GetTicketsToUserFromRowTopN/"+ userId +"?offset=" + offset + "&topN=" + topNumber;
        return url;
    }
}
