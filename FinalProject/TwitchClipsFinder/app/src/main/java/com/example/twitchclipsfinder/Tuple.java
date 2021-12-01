package com.example.twitchclipsfinder;

import org.json.JSONObject;

public class Tuple {
    public String _key = "";
    public String _value = "";
    public Clip[] _clips;

    Tuple() { }

    Tuple(String key, String value) {
        _key = key;
        _value = value;
    }

    Tuple(String key, Clip[] clips) {
        _key = key;
        _clips = clips;
    }
}
