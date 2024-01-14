package com.example.pokedex.controllers;

import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokemonSQL;
import com.example.pokedex.services.AccessData;
import com.example.pokedex.services.ApiData;
import com.example.pokedex.views.PokemonView;
import org.json.simple.JSONObject;

import java.sql.ResultSet;

public class PokemonController {
    /**
     * An object of this class is constructed with a provided database.
     * Subsequently, utilizing this database, the class initializes a PokemonDisplay instance within one of its attributes
     *  through the generateData() method.
     * The PokemonDisplay object can then be accessed using getPokemonDisplay().
     */

    private AccessData dataAccess;
    private PokemonDisplay pokemonDisplay;

    public PokemonDisplay getPokemonDisplay() {
        return pokemonDisplay;
    }

    public PokemonController(AccessData dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void generateData() {

        if (dataAccess.getData() instanceof ResultSet) {
            PokemonSQL pokemon = new PokemonSQL((ResultSet) dataAccess.getData());
            pokemonDisplay = new PokemonDisplay(pokemon);
        } else if (dataAccess.getData() instanceof JSONObject) {
            Pokemon pokemon = new Pokemon((JSONObject) dataAccess.getData());
            pokemonDisplay = new PokemonDisplay(pokemon);
        } else {
            System.err.println("Error : PokemonController");
        }
    }

    //Modified Versions

    //For part 2 after implementing the connection to the local database
    /*
    private AccessData dataAccess;

    public PokemonController(AccessData dataAccess){
        this.dataAccess = dataAccess;
    }

    public void generateData(){

        if (dataAccess.getData() instanceof ResultSet) {
            PokemonSQL pokemon = new PokemonSQL((ResultSet) dataAccess.getData());
            PokemonDisplay pokemonDisplay = new PokemonDisplay();
            pokemonDisplay.show(pokemon);
        }
        else if (dataAccess.getData() instanceof JSONObject){
            Pokemon pokemon = new Pokemon((JSONObject) dataAccess.getData());
            PokemonDisplay pokemonDisplay = new PokemonDisplay();
            pokemonDisplay.show(pokemon);
        }
        else {
            System.err.println("Error");
        }
    }
    */

    //For 1-B when the Api is called via an interface
    /*
    private AccessData dataAccess;
    private Pokemon pokemon;

    public PokemonController(AccessData dataAccess){
        this.dataAccess = dataAccess;
    }

    public void generateData(){
        if (dataAccess.getData() instanceof JSONObject){
            this.pokemon = new Pokemon((JSONObject) dataAccess.getData());
            PokemonDisplay pokemonDisplay = new PokemonDisplay();
            pokemonDisplay.setPokemon(this.pokemon);
            pokemonDisplay.show();
        }
        else {
            System.err.println("Error, we expected a JSON Object from the API");
        }
    }
    */

    //For 1-A when ApiData is called directly
    /*
    private ApiData apiData;
    private Pokemon pokemon = new Pokemon();

    public PokemonController(int id){
        apiData = new ApiData(id);
    }

    public void generateData(){
        if (apiData.getData() instanceof JSONObject){
            pokemon.setPokemon((JSONObject) apiData.getData());
            PokemonDisplay pokemonDisplay = new PokemonDisplay();
            pokemonDisplay.setPokemon(pokemon);
            pokemonDisplay.show();
        }
        else {
            System.err.println("Error, we expected a JSON Object from the API");
        }
    }
    */
}
