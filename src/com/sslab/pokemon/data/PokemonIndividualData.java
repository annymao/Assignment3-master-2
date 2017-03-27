package com.sslab.pokemon.data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jerry on 2017/3/21.
 */
public class PokemonIndividualData implements Comparable<PokemonIndividualData>{
    private String speciesName;
    private int id;
    private String nickName;
    private PokemonValueData speciesValue;

    public PokemonIndividualData(int id, String speciesName,String name,PokemonValueData valueData)
    {
        this.id = id;
        this.speciesName = speciesName;
        this.nickName=name;
        this.speciesValue = valueData;
    }

    public int getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }
    public String getSpeciesName(){
        return speciesName;
    }
    public PokemonValueData getSpeciesValue() {
        return speciesValue;
    }
    @Override
    public int compareTo(PokemonIndividualData o)
    {
        return this.id-o.getId();
    }
}
