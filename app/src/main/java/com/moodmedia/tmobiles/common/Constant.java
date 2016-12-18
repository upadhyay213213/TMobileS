package com.moodmedia.tmobiles.common;

/**
 * Created by pkatya on 12/18/16.
 */
public class Constant {

    public static class ApiHeader {
        public static final String API_KEY = "X-Api-Key: cFjxjHHhJ16JwxcKQRa3ogvv";
        public static final String CONTENT_TYPE_JSON = "Content-Type: application/json";
        public static final String AUTH_TOKEN = "X-Auth-Token: zRsgnWycuHoLHrAsHRAyaAEE";
    }

    public class ApiForConfigurations {
      /*  public static final String GET_SONGS_LIST = "/api/track";//dummy url
        public static final String BASE_URL = "http://psilin13:7000";//dummy url*/
        public static final String GET_SONGS_URL ="/api/v1/tracks/{track_id}" ;//

        //TODO :remove comments for original API call
        public static final String GET_SONGS_LIST = "/api/v1/tracks";
        public static final String BASE_URL = "http://hearqa.moodmedia.com";
    }
}
