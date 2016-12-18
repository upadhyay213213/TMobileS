package com.moodmedia.tmobiles.webclient;

/**
 * Created by pkatya on 12/18/16.
 */
public class DataConnector {

    private ServerCall mServerCall;
     public DataConnector(ServerCall serverCall){
         mServerCall=serverCall;
     }

    public void getSongsList() {
        ServerCall mServerCall = ServiceGenerator.getRestService("http://hearqa.moodmedia.com");
        mServerCall.getSongsList();
    }
}
