package com.vova.githubcli.model;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Stats {
    int stars;
    int forks;
    int contributors;
    String language;
}
