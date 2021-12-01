package com.example.twitchclipsfinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class clipGenerator {
    public Tuple[] getCategory(String query) throws ExecutionException, InterruptedException, JSONException {
        twitchRequest request = new twitchRequest("https://api.twitch.tv/helix/search/categories?query=" + query + "&first=5");

        request.execute().get();
        JSONObject jsonOBJ_arr[] = getArrayOfJsonObjectsFromJsonString(request._jsonString);

        Tuple tuples[] = new Tuple[jsonOBJ_arr.length];
        for (int i = 0; i < jsonOBJ_arr.length; i++) {
            tuples[i] = new Tuple(jsonOBJ_arr[i].getString("name"), jsonOBJ_arr[i].getString("id"));
        }
        return tuples;
    }

    public Tuple[] getStreamer(String query) throws ExecutionException, InterruptedException, JSONException {
        twitchRequest request = new twitchRequest("https://api.twitch.tv/helix/search/channels?query=" + query + "&first=5");

        request.execute().get();

        JSONObject jsonObBJ_arr[] = getArrayOfJsonObjectsFromJsonString(request._jsonString);
        // printJsonObjectArray(jsonObBJ_arr);

        Tuple tuples[] = new Tuple[jsonObBJ_arr.length];
        for (int i = 0; i < jsonObBJ_arr.length; i++) {
            tuples[i] = new Tuple(jsonObBJ_arr[i].getString("display_name"), jsonObBJ_arr[i].getString("id"));
        }
        return tuples;
    }



    public Tuple getClipsFromCategory(String game_id, String pagination, String start_date, String end_date) throws IOException, JSONException, ExecutionException, InterruptedException {
        twitchRequest request;

        if ((pagination == "") && (start_date == "")) {
            request = new twitchRequest("https://api.twitch.tv/helix/clips?game_id=" + game_id);
        } else if ((pagination != "") && (start_date == "")) {
            request = new twitchRequest("https://api.twitch.tv/helix/clips?game_id=" + game_id + "&after=" + pagination);
        } else if ((pagination == "") && (start_date != "")) {
            request = new twitchRequest("https://api.twitch.tv/helix/clips?game_id=" + game_id + "&started_at=" + start_date + "&ended_at=" + end_date);
        } else {
            request = new twitchRequest("https://api.twitch.tv/helix/clips?game_id=" + game_id + "&after=" + pagination + "&started_at=" + start_date + "&ended_at=" + end_date);
        }

        // Execute the Twitch Request
        request.execute().get();
        JSONObject jsonOBJ_arr[] = getArrayOfJsonObjectsFromJsonString(request._jsonString);
        String next_pagination = getPaginationFromJsonString(request._jsonString);
        //printJsonObjectArray(jsonOBJ_arr);

        Clip clips[] = new Clip[jsonOBJ_arr.length];
        for (int i = 0; i < jsonOBJ_arr.length; i++) {
            clips[i] = new Clip(jsonOBJ_arr[i]);
        }
        return new Tuple(next_pagination, clips);
    }

    public Tuple getClipsFromStreamer(String streamer, String pagination, String start_date, String end_date) throws IOException, JSONException, ExecutionException, InterruptedException {
        twitchRequest request;
        if ((pagination == "") && (start_date == "")) {
            request = new twitchRequest("https://api.twitch.tv/helix/clips?broadcaster_id=" + start_date);
        } else if ((pagination != "") && (start_date == "")) {
            request = new twitchRequest("https://api.twitch.tv/helix/clips?broadcaster_id=" + start_date + "&after=" + pagination);
        } else if ((pagination == "") && (start_date != "")) {
            request = new twitchRequest("https://api.twitch.tv/helix/clips?broadcaster_id=" + start_date + "&started_at=" + start_date + "&ended_at=" + end_date);
        } else {
            request = new twitchRequest("https://api.twitch.tv/helix/clips?broadcaster_id=" + start_date + "&after=" + pagination + "&started_at=" + start_date + "&ended_at=" + end_date);
        }

        // Execute the Twitch Request
        request.execute().get();
        JSONObject jsonObBJ_arr[] = getArrayOfJsonObjectsFromJsonString(request._jsonString);
        String next_pagination = getPaginationFromJsonString(request._jsonString);
        // printJsonObject(jsonObBJ_arr[0]);

        Clip clips[] = new Clip[jsonObBJ_arr.length];
        for (int i = 0; i < jsonObBJ_arr.length; i++) {
            clips[i] = new Clip(jsonObBJ_arr[i]);
        }
        return new Tuple(next_pagination, clips);
    }



    public String getStreamStartDate(String video_id) throws IOException, JSONException, ExecutionException, InterruptedException {
        twitchRequest request = new twitchRequest("https://api.twitch.tv/helix/videos?id=" + video_id);

        request.execute().get();
        JSONObject jsonOBJ_arr[] = getArrayOfJsonObjectsFromJsonString(request._jsonString);

        String created_at = jsonOBJ_arr[0].getString("created_at");
        return created_at;
    }



    // ========== Json processing functions ==========
    private void parseJsonString(String jsonString, String key) throws JSONException {
        JSONObject jsonOBJ = new JSONObject(jsonString);
        // System.out.println(jsonOBJ.toString(4));

        JSONArray jsonARR = jsonOBJ.getJSONArray("data");
        for (int i = 0; i < jsonARR.length(); i++) {
            System.out.print(jsonARR.getJSONObject(i).getString(key) + "\n");
        }
    }

    private JSONObject getJsonObjFromJsonSrting(String jsonString, int i) throws JSONException {
        JSONObject jsonOBJ = new JSONObject(jsonString);

        JSONArray jsonARR = jsonOBJ.getJSONArray("data");
        if (i < jsonARR.length()) {
            return jsonARR.getJSONObject(i);
        } else {
            return new JSONObject();
        }
    }

    private JSONObject[] getArrayOfJsonObjectsFromJsonString(String jsonString) throws JSONException {
        JSONObject jsonOBJ = new JSONObject(jsonString);

        JSONArray jsonARR = jsonOBJ.getJSONArray("data");
        JSONObject jsonObBJ_arr[] = new JSONObject[jsonARR.length()];
//        System.out.println(jsonARR.getJSONObject(19).toString(4));
        for (int i = 0 ; i < jsonARR.length(); i++) {
            jsonObBJ_arr[i] = jsonARR.getJSONObject(i);
        }
        return jsonObBJ_arr;
    }

    private String getPaginationFromJsonString(String jsonString) throws JSONException {
        JSONObject jsonOBJ = new JSONObject(jsonString);

        JSONObject jsonOBJ_pagination = jsonOBJ.getJSONObject("pagination");
        String ret = jsonOBJ_pagination.getString("cursor");

        return ret;
    }


    // ========== Json debug functions ==========
    public void printJsonString(String jsonString) throws JSONException {
        JSONObject jsonOBJ = new JSONObject(jsonString);
        System.out.println(jsonOBJ.toString(4));
    }

    public void printJsonObject(JSONObject jsonObject) throws JSONException {
        System.out.println(jsonObject.toString(4));
    }

    public void printJsonObjectArray(JSONObject[] jsonObjects) throws JSONException {
        for (int i = 0; i < jsonObjects.length; i++) {
            System.out.println(jsonObjects[i].toString(4));
        }
    }
}
