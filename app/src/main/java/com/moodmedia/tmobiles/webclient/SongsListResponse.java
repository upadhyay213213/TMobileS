package com.moodmedia.tmobiles.webclient;

import com.google.gson.annotations.SerializedName;

public class SongsListResponse implements Model {
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
    public String trackId;
    @SerializedName("app_group_id")
    String appGroup;

    public void setUrl(String url) {
        this.url = url;
    }

    String url;
    String sdCardPath;

    public String getSdCardPath() {
        return sdCardPath;
    }

    public void setSdCardPath(String sdCardPath) {
        this.sdCardPath = sdCardPath;
    }

    public String getId() {
        return id;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    @Override
    public String toString() {
        return "SongsListResponse{" +
                "id='" + id + '\'' +
                ", modId='" + modId + '\'' +
                ", name='" + name + '\'' +
                ", singers='" + singers + '\'' +
                ", album='" + album + '\'' +
                ", musicCompany='" + musicCompany + '\'' +
                ", trackId='" + trackId + '\'' +
                ", appGroup='" + appGroup + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getSingers() {
        return singers;
    }

    public String getAlbum() {
        return album;
    }

    public String getMusicCompany() {
        return musicCompany;
    }

    public String getModId() {
        return modId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSingers(String singers) {
        this.singers = singers;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setMusicCompany(String musicCompany) {
        this.musicCompany = musicCompany;
    }

    public void setAppGroup(String appGroup) {
        this.appGroup = appGroup;
    }
}
