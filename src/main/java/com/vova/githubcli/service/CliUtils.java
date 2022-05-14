package com.vova.githubcli.service;

public class CliUtils {
    public static void printMessageAndExit(String message) {
        System.out.println(message);
        System.exit(1);
    }
}
