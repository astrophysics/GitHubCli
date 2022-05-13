package com.vova.githubcli.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vova.githubcli.model.Asset;
import com.vova.githubcli.model.Stats;
import com.vova.githubcli.service.interfaces.GitHubCli;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GitHubCliImpl implements GitHubCli {

    private static final String root = "https://api.github.com/repos/";

    private String get(String path) throws IOException { // "https://api.github.com/repos/"+repo (leios/simuleios)
        URL url = new URL(path);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        httpConn.setRequestProperty("Accept", "application/vnd.github.v3+json");

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    public List<Asset> getDownloads(String repo) {
        List<Asset> result = new ArrayList<>();
        String releasesStr;
        try {
            releasesStr = get(root+repo+"/releases");
        } catch (IOException e) {
            throw new RuntimeException("sth went wrong!"); // FIXME to handle?
        }
        JsonArray releases = JsonParser.parseString(releasesStr).getAsJsonArray();
        for (JsonElement release : releases) {
            String releaseName = release.getAsJsonObject().get("name").getAsString();
            JsonArray assets = release.getAsJsonObject().get("assets").getAsJsonArray();
            for (JsonElement asset : assets) {
                String assetName = asset.getAsJsonObject().get("name").getAsString();
                int downloadCount = asset.getAsJsonObject().get("download_count").getAsInt();
                result.add(new Asset(releaseName, assetName, downloadCount));
            }
        }
        return result;
    }

    @Override
    public Stats getStats(String repo) {
        String statsStr;
        String contributorsStr;
        try {
            statsStr = get(root+repo);
            contributorsStr = get(root+repo+"/contributors");
        } catch(IOException e) {
            throw new RuntimeException("sth went wrong!"); // FIXME to handle?
        }
        JsonObject statsJson = JsonParser.parseString(statsStr).getAsJsonObject();
        int contributors = JsonParser.parseString(contributorsStr).getAsJsonArray().size();
        int stars = statsJson.get("stargazers_count").getAsInt();
        int forks = statsJson.get("forks_count").getAsInt();
        String language = statsJson.get("language").getAsString();
        return new Stats(stars, forks, contributors, language);
    }
}
