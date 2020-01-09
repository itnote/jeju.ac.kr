package com.infomind.axboot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class JsonUtils {


    public static <T> T jsonToAny(String data, Type type) {
        JsonParser parser = new JsonParser();
        JsonElement jsonData = parser.parse(data);

       return new Gson().fromJson(jsonData, type);
    }
}
