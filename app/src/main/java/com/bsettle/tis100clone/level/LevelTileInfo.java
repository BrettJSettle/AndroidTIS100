package com.bsettle.tis100clone.level;

import java.io.Serializable;

public class LevelTileInfo implements Serializable{
    private int number;
    private String name;
    private String description;

    public LevelTileInfo(int num, String name, String description){
        this.number = num;
        this.name = name;
        this.description = description;
    }

    public int getNumber(){
        return number;
    }
    public String getDescription(){
        return description;
    }
    public String getName(){
        return name;
    }

}
