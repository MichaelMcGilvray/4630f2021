package com.example.twitchclipsfinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Clip implements Serializable {
    public String _thumbnail;
    public String _embed_url;
    public String _url;
    public String _title;
    public int _views;
    public String _broadcaster_name;
    public String _video_id;
    public String _created_at;
    public String _game_id;

    Clip(String thumbnail, String embed_url, String url, String title, int views, String broadcaster_name, String video_id, String created_at, String game_id) {
        _thumbnail = thumbnail;
        _embed_url = embed_url;
        _url = url;
        _title = title;
        _views = views;
        _broadcaster_name = broadcaster_name;
        _video_id = video_id;
        _created_at = created_at;
        _game_id = game_id;
    }

    Clip(JSONObject jsonObject) throws JSONException {
        _thumbnail = jsonObject.getString("thumbnail_url");
        _embed_url = jsonObject.getString("embed_url");
        _url = jsonObject.getString("url");
        _title = jsonObject.getString("title");
        _views = Integer.parseInt(jsonObject.getString("view_count"));
        _broadcaster_name = jsonObject.getString("broadcaster_name");
        _video_id = jsonObject.getString("video_id");
        _created_at = jsonObject.getString("created_at");
        _game_id = jsonObject.getString("game_id");
    }

    public void print() {
        System.out.print("Title = " + _title + "\n"
                + "\tBroadcaster Name = " + _broadcaster_name + "\n"
                + "\tView Count = " + _views + "\n"
                + "\tThumbnail URL = " + _thumbnail + "\n"
                + "\tEmbed URL = " + _embed_url + "\n"
                + "\tURL = " + _url + "\n"
                + "\tVideo ID = " + _video_id + "\n"
                + "\tCreated at = " + _created_at + "\n"
        );
    }
}
