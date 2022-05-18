package com.vova;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.vova.githubcli.model.Asset;
import com.vova.githubcli.model.Stats;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.List;

public class GitHubCliApp {
    private final com.vova.githubcli.service.interfaces.GitHubCli cli;

    public GitHubCliApp(com.vova.githubcli.service.interfaces.GitHubCli cli) {
        this.cli = cli;
    }

    public String getOutputDownloads(String repo) {
        String outputStr;
        List<Asset> assets = cli.getDownloads(repo);
        if(assets.size()>1) { // including total
            outputStr = AsciiTable.getTable(assets, Arrays.asList(
                    new Column().header("RELEASE NAME").with(Asset::getReleaseName),
                    new Column().header("DISTRIBUTION").with(Asset::getDistribution),
                    new Column().header("DOWNLOAD COUNT").with(asset -> Integer.toString(asset.getDownloadCount()))));
        } else {
            outputStr = "no assets for this repository";
        }
        return outputStr;
    }

    public String getOutputStats(String repo) {
        String outputStr;
        Stats stats = cli.getStats(repo);
        String[] headers = {"Stat", "Value"};
        String[][] data = {
                {"Stars", Integer.toString(stats.getStars())},
                {"Forks", Integer.toString(stats.getForks())},
                {"Contributors", Integer.toString(stats.getContributors())},
                {"Language", stats.getLanguage()}};
        outputStr = AsciiTable.getTable(headers, data);
        return outputStr;
    }

    public void printHelpAndExit(Options options, HelpFormatter formatter) {
        formatter.printHelp("githubCli", options);
        System.exit(1);
    }
}
