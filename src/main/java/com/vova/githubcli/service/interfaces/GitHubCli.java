package com.vova.githubcli.service.interfaces;

import com.vova.githubcli.model.Asset;
import com.vova.githubcli.model.Stats;

import java.io.IOException;
import java.util.List;

public interface GitHubCli {
    List<Asset> getDownloads(String repo);
    Stats getStats(String repo);
}
