package com.vova;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.vova.githubcli.model.Asset;
import com.vova.githubcli.model.Stats;
import com.vova.githubcli.service.GitHubCliImpl;
import org.apache.commons.cli.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class Main {
    static GitHubCliImpl cli = new GitHubCliImpl();

    public static void main(String[] args) {
        Options options = new Options();

        Option input = new Option("r", "repo", true, "The repository to analyze");
        input.setRequired(false);
        options.addOption(input);

        Option output = new Option("o", "output", true, "The output path of the txt file");
        output.setRequired(false);
        options.addOption(output);

        Option help = new Option("h", "help", false, "Print information about each command");
        help.setRequired(false);
        options.addOption(help);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelpAndExit(options, formatter);
        }

        assert cmd != null;

        if(cmd.hasOption("help")) {
            printHelpAndExit(options, formatter);
        }
        String arg = args[0];

        String repo = cmd.getOptionValue("repo");
        if(repo == null) {
            printHelpAndExit(options, formatter);
        }
        String outputFilePath = cmd.getOptionValue("output");

        String outputStr;

        if("stats".equals(arg)) {
            Stats stats = cli.getStats(repo);
            String[] headers = {"Stat", "Value"};
            String[][] data = {
                    {"Stars", Integer.toString(stats.getStars())},
                    {"Forks", Integer.toString(stats.getForks())},
                    {"Contributors", Integer.toString(stats.getContributors())},
                    {"Language", stats.getLanguage()}};
            outputStr = AsciiTable.getTable(headers, data);
        } else if("downloads".equals(arg)) {
            List<Asset> assets = cli.getDownloads(repo);
            outputStr = AsciiTable.getTable(assets, Arrays.asList(
                    new Column().header("RELEASE NAME").with(Asset::getReleaseName),
                    new Column().header("DISTRIBUTION").with(Asset::getDistribution),
                    new Column().header("DOWNLOAD COUNT").with(asset -> Integer.toString(asset.getDownloadCount()))));
        } else {
            printHelpAndExit(options, formatter);
            outputStr = "";
        }

        if(outputFilePath == null) {
            System.out.println(outputStr);
        } else {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                writer.write(outputStr);
            } catch (Exception e) {
                System.out.println("Couldn't write to " + outputFilePath);
                System.out.println(outputStr);
            }
        }

    }

    private static void printHelpAndExit(Options options, HelpFormatter formatter) {
        formatter.printHelp("githubCli", options);
        System.exit(1);
    }
}
