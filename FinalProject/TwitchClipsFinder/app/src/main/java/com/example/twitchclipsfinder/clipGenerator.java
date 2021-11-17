package com.example.twitchclipsfinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class clipGenerator {
    public Tuple[] getCategory(String query) throws ExecutionException, InterruptedException, JSONException {
        twitchRequest request = new twitchRequest("https://api.twitch.tv/helix/search/categories?query=" + query + "&first=5");

        request.execute().get();

        JSONObject jsonObBJ_arr[] = getArrayOfJsonObjectsFromJsonString(request._jsonString);
        // printJsonObjectArray(jsonObBJ_arr);

        Tuple tuples[] = new Tuple[jsonObBJ_arr.length];
        for (int i = 0; i < jsonObBJ_arr.length; i++) {
            tuples[i] = new Tuple(jsonObBJ_arr[i].getString("name"), jsonObBJ_arr[i].getString("id"));
        }
        return tuples;
    }


    public Clip[] getClips(String game_id) throws IOException, JSONException, ExecutionException, InterruptedException {
        twitchRequest request = new twitchRequest("https://api.twitch.tv/helix/clips?game_id=" + game_id);

        // Execute the Twitch Request
        request.execute().get();

        JSONObject jsonObBJ_arr[] = getArrayOfJsonObjectsFromJsonString(request._jsonString);
        // printJsonObject(jsonObBJ_arr[0]);

        Clip clips[] = new Clip[jsonObBJ_arr.length];
        for (int i = 0; i < jsonObBJ_arr.length; i++) {
            clips[i] = new Clip(jsonObBJ_arr[i]);
        }
        return clips;
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
