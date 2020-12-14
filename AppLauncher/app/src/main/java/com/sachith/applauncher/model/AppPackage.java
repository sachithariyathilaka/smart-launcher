package com.sachith.applauncher.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class AppPackage {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "name")
    private String name;

    public AppPackage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
