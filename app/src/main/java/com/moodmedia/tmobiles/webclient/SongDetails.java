package com.moodmedia.tmobiles.webclient;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pkatya on 12/18/16.
 */
public class SongDetails implements Serializable{
    @SerializedName("id")
    String id;
    @SerializedName("mood_id")
    String modId;
    @SerializedName("name")
    String name;
    @SerializedName("singers")
    String singers;
    @SerializedName("album")
    String album;
    @SerializedName("music_company")
    String musicCompany;
    @SerializedName("track_id")
    String trackId;
    @SerializedName("app_group_id")
    String appGroup;
     @SerializedName("created_at")
    String createdAt;
     @SerializedName("updated_at")
    String upDatedAt;

    @SerializedName("download_url")
    String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "SongDetails{" +
                "id='" + id + '\'' +
                ", modId='" + modId + '\'' +
                ", name='" + name + '\'' +
                ", singers='" + singers + '\'' +
                ", album='" + album + '\'' +
                ", musicCompany='" + musicCompany + '\'' +
                ", trackId='" + trackId + '\'' +
                ", appGroup='" + appGroup + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", upDatedAt='" + upDatedAt + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
