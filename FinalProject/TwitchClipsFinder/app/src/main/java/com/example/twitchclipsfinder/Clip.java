package com.example.twitchclipsfinder;

import org.json.JSONException;
import org.json.JSONObject;

public class Clip {
    public String _thumbnail;
    public String _embed_url;
    public String _title;
    public int _views;
    public String _broadcaster_name;

    Clip(String thumbnail, String embed_url, String title, int views, String broadcaster_name) {
        _thumbnail = thumbnail;
        _embed_url = embed_url;
        _title = title;
        _views = views;
        _broadcaster_name = broadcaster_name;
    }

    Clip(JSONObject jsonObject) throws JSONException {
        _thumbnail = jsonObject.getString("thumbnail_url");
        _embed_url = jsonObject.getString("embed_url");
        _title = jsonObject.getString("title");
        _views = Integer.parseInt(jsonObject.getString("view_count"));
        _broadcaster_name = jsonObject.getString("broadcaster_name");
    }

    public void print() {
        System.out.print("Title = " + _title + "\n"
                + "\tBroadcaster Name = " + _broadcaster_name + "\n"
                + "\tView Count = " + _views + "\n"
                + "\tThumbnail URL = " + _thumbnail + "\n"
                + "\tEmbed URL = " + _embed_url + "\n"
        );
    }
}
