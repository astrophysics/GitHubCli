package com.vova.githubcli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Asset {
    String releaseName;
    String distribution;
    int downloadCount;
}