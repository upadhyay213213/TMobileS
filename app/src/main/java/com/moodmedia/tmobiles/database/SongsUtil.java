package com.moodmedia.tmobiles.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moodmedia.tmobiles.model.SongsListResponse;

import java.util.ArrayList;

/**
 * Created by pkatya on 11/6/16.
 */
public class SongsUtil {

    public static void addSongsToDb(ArrayList<SongsListResponse> songsList) {

        for (int i = 0; i < songsList.size(); i++) {
       /* ""id" VARCHAR, "mood_id" VARCHAR, "name" VARCHAR, "singers" VARCHAR,
        "album" VARCHAR, "music_compony" VARCHAR, "track_id" VARCHAR, "sd_card_path" VARCHAR, "url" VARCHAR)*/
            SongsListResponse songs = songsList.get(i);
            if (isSongPresent(songs.getId())) {
                //update
                updateSongs(songs, songs.getId());


            } else {
                SQLiteDatabase sqLiteDatabase = SqliteHelper.getSqliteDatabase();
                String query = "Insert into song_details (id,mood_id,name,singers,album,music_compony,track_id) VALUES (?,?,?,?,?,?,?)";
                SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(query);
                sqLiteStatement.bindString(1, songs.getId());
                sqLiteStatement.bindString(2, songs.getModId());
                sqLiteStatement.bindString(3, songs.getName());
                sqLiteStatement.bindString(4, songs.getSingers());
                sqLiteStatement.bindString(5, songs.getAlbum());
                sqLiteStatement.bindString(6, songs.getMusicCompany());
                sqLiteStatement.bindString(7, songs.getTrackId());
                sqLiteStatement.execute();
                sqLiteStatement.close();

            }
        }

    }

    public static void addRawSongsToDb(ArrayList<SongsListResponse> songsList) {

        for (int i = 0; i < songsList.size(); i++) {
       /* ""id" VARCHAR, "mood_id" VARCHAR, "name" VARCHAR, "singers" VARCHAR,
        "album" VARCHAR, "music_compony" VARCHAR, "track_id" VARCHAR, "sd_card_path" VARCHAR, "url" VARCHAR)*/
            SongsListResponse songs = songsList.get(i);

            SQLiteDatabase sqLiteDatabase = SqliteHelper.getSqliteDatabase();
            String query = "Insert into raw_song_details (id,mood_id,name,singers,album,music_compony,track_id,sd_card_path) VALUES (?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(query);
            sqLiteStatement.bindString(1, songs.getId());
            sqLiteStatement.bindString(2, songs.getModId());
            sqLiteStatement.bindString(3, songs.getName());
            sqLiteStatement.bindString(4, songs.getSingers());
            sqLiteStatement.bindString(5, songs.getAlbum());
            sqLiteStatement.bindString(6, songs.getMusicCompany());
            sqLiteStatement.bindString(7, songs.getTrackId());
            sqLiteStatement.bindString(8, songs.getSdCardPath());
            sqLiteStatement.execute();
            sqLiteStatement.close();


        }

    }

    public static boolean isUserValid(String userName, String password) {
        String query = "Select * from song_details Where user_name=? and password=?";
        String[] projection = new String[]{userName, password};
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        Cursor cursor = SqliteHelper.executeSelectQuery(db, query, projection);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public static boolean isSongPresent(String id) {
        String query = "Select * from song_details Where id=?";
        String[] projection = new String[]{id};
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        Cursor cursor = SqliteHelper.executeSelectQuery(db, query, projection);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public static boolean isRawSongPresent(String id) {
        String query = "Select * from raw_song_details Where id=?";
        String[] projection = new String[]{id};
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        Cursor cursor = SqliteHelper.executeSelectQuery(db, query, projection);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public static void updateDownloadUrlPath(String url, String id) {
        String query = "UPDATE song_details set url=? WHERE id=?";
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindString(1, url);
        statement.bindString(2, id);
        statement.execute();
        statement.close();
    }

    public static void updateSdCardPathAndDownloadStatus(String filePath, String id) {
        String query = "UPDATE song_details set sd_card_path=?,is_downloaded=? WHERE id=?";
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindString(1, filePath);
        statement.bindString(2, "Y");
        statement.bindString(3, id);
        statement.execute();
        statement.close();
    }

    public static void updateSongs(SongsListResponse song, String id) {
        String query = "UPDATE song_details set mood_id=?,name=?,singers=?,album=?,music_compony=?,track_id=? WHERE id=?";
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindString(1, song.getModId());
        statement.bindString(2, song.getName());
        statement.bindString(3, song.getSingers());
        statement.bindString(4, song.getAlbum());
        statement.bindString(5, song.getMusicCompany());
        statement.bindString(6, song.getTrackId());
        statement.bindString(7, id);
        statement.execute();
        statement.close();
    }

    public static Cursor getSongsFromDb() {
        String query = "Select * from song_details";
        String[] projection = new String[]{};
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        return SqliteHelper.executeSelectQuery(db, query, projection);
    }

    public static Cursor getRawSongsFromDb() {
        String query = "Select * from raw_song_details";
        String[] projection = new String[]{};
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        return SqliteHelper.executeSelectQuery(db, query, projection);
    }

    public static ArrayList<SongsListResponse> getSongsListFromDb() {
        Cursor cursor = getSongsFromDb();
        return parseSongs(cursor);
    }

    public static ArrayList<SongsListResponse> parseSongs(Cursor cursor) {
        ArrayList<SongsListResponse> songsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            SongsListResponse songs = new SongsListResponse();
            songs.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
            songs.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow("album")));
            songs.setSingers(cursor.getString(cursor.getColumnIndexOrThrow("singers")));
            songs.setMusicCompany(cursor.getString(cursor.getColumnIndexOrThrow("music_compony")));
            songs.setSdCardPath(cursor.getString(cursor.getColumnIndexOrThrow("sd_card_path")));
            songs.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            songsList.add(songs);
        }
        cursor.close();
        return songsList;
    }

    public static ArrayList<SongsListResponse> getrawSongsListFromDb() {
        Cursor cursor = getRawSongsFromDb();
        return parseSongs(cursor);
    }

    private static ArrayList<SongsListResponse> getTrackIdsOfUnDownloadedSongs() {
        ArrayList<SongsListResponse> songsList = new ArrayList<>();
        String query = "Select DISTINCT id from song_details Where is_downloaded = 'N'";
        String[] projection = new String[]{};
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        Cursor cursor = SqliteHelper.executeSelectQuery(db, query, projection);
        while (cursor.moveToNext()) {
            SongsListResponse songs = new SongsListResponse();
            songs.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
            songsList.add(songs);
        }
        return songsList;
    }

    public static ArrayList<SongsListResponse> getUnDownloadedSongsToDownLoad() {
        if (isUnDownLoadedSongSongPresent()) {
            return getTrackIdsOfUnDownloadedSongs();
        }
        return null;
    }

    public static boolean isUnDownLoadedSongSongPresent() {
        String query = "Select is_downloaded from song_details Where is_downloaded = 'N'";
        String[] projection = new String[]{};
        SQLiteDatabase db = SqliteHelper.getSqliteDatabase();
        Cursor cursor = SqliteHelper.executeSelectQuery(db, query, projection);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
