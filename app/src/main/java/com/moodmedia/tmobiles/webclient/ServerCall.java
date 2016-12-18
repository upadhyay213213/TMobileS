package com.moodmedia.tmobiles.webclient;

import com.moodmedia.tmobiles.common.Constant;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface ServerCall {

    @Headers({
            Constant.ApiHeader.CONTENT_TYPE_JSON,
            Constant.ApiHeader.API_KEY,
            Constant.ApiHeader.AUTH_TOKEN
    })
    @GET(Constant.ApiForConfigurations.GET_SONGS_LIST)
    Call<List<SongsListResponse>> getSongsList();

    @Headers({
            Constant.ApiHeader.CONTENT_TYPE_JSON,
            Constant.ApiHeader.API_KEY,
            Constant.ApiHeader.AUTH_TOKEN
    })
    @GET(Constant.ApiForConfigurations.GET_SONGS_URL)
    Call <SongDetails> getSongsDetailsWithURL(@Path("track_id") String tracIid);


    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String url);

}
