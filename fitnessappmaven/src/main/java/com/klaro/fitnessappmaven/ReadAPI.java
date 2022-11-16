package com.klaro.fitnessappmaven;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.FileReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;

public class ReadAPI {

    public ReadAPI() throws IOException, ParseException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://fitness-calculator.p.rapidapi.com/burnedcalorie?activityid=bi_2&activitymin=50&weight=77"))
                .header("X-RapidAPI-Key", "b4b40d284amshacf7b676b928e88p1a5c77jsned0db45910bf")
                .header("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(String.valueOf(response.body()));
            JSONArray jArray = new JSONArray();
            
            for (Object x : json.entrySet()) {
                jArray.add(x);
            }


            System.out.println(jArray.get(1));
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
