package com.vova;

import com.vova.githubcli.service.GitHubCliImpl;
import com.vova.githubcli.service.interfaces.GitHubCli;
import org.apache.commons.cli.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static final GitHubCli gitHubCli = new GitHubCliImpl();
    private static final GitHubCliApp gitHubCliApp = new GitHubCliApp(gitHubCli);

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
            gitHubCliApp.printHelpAndExit(options, formatter);
        }

        assert cmd != null;

        if(args.length == 0 || cmd.hasOption("help")) {
            gitHubCliApp.printHelpAndExit(options, formatter);
        }
        String arg = args[0];

        String repo = cmd.getOptionValue("repo");
        if(repo == null) {
            System.out.println("Missing required option: repo");
            gitHubCliApp.printHelpAndExit(options, formatter);
        }
        String outputFilePath = cmd.getOptionValue("output");

        String outputStr;

        if("stats".equals(arg)) {
            outputStr = gitHubCliApp.getOutputStats(repo);
        } else if("downloads".equals(arg)) {
            outputStr = gitHubCliApp.getOutputDownloads(repo);
        } else {
            gitHubCliApp.printHelpAndExit(options, formatter);
            outputStr = "";
        }

        if(outputFilePath == null) {
            System.out.println(outputStr);
        } else {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                writer.write(outputStr);
            } catch (IOException e) {
                System.out.println("Couldn't write to " + outputFilePath);
                e.printStackTrace();
                System.out.println(outputStr);
            }
        }
    }
}
