package com.example.pokedex;


import com.example.pokedex.controllers.ControllerPokemon;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokemonSQL;
import com.example.pokedex.services.ApiData;
import com.example.pokedex.services.LocalData;
import com.example.pokedex.utilities.ConsoleOutputUtility;
import com.example.pokedex.utilities.OutputFormat;
import com.example.pokedex.views.PokemonView;

import java.text.ParseException;

import org.apache.commons.cli.*;
import org.json.simple.JSONObject;

public class Pokedex {
/**
 * This section initializes its properties based on the arguments provided through the command line.
 * Note that depending on the command line format, the main() method behaves differently to display
 * information about a selected Pokémon.
 * The complete command line format is as follows:
 *      run --args="3 -d jdbc:sqlite:../sujet_TP/data/pokemon_database.sqlite -f csv"
 *  The '3' represents the chosen Pokémon's ID.
 *  The "-d jdbc:sqlite:../sujet_TP/data/pokemon_database.sqlite" is the optional path to a
 *   local database.
 *  The "-f csv" segment is the optional selection of the output format (among text, html, and csv).
 **/

    private enum DataSource {WEB_API, LOCAL_DATABASE}
    private static DataSource dataSource = DataSource.WEB_API;
    private static String databasePath;
    private static OutputFormat outputFormat = OutputFormat.TEXT;
    private static int pokemonId;

    public static void main(String[] args) throws ParseException {

        try {
            parseCommandArgs(args);
        } catch (PokemonException e) {
            System.err.println(e.getMessage());
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("./PokemonApp <ID> [-d|--db <dbFile>] [-f|--fmt <fmt>]", e.getOptions());
            System.exit(0);
        }
    
        if (dataSource == DataSource.LOCAL_DATA_STORAGE) {
            LocalStorage localData = new LocalStorage(databasePath, pokemonId);
            PokemonController controller = new PokemonController(localData);
            controller.generateData();
            ConsoleOutputFormatter outputFormatter = new ConsoleOutputFormatter(outputFormat, controller.getPokemonDisplay());
            outputFormatter.displayOutput();
        } else {
            ApiDataSource apiData = new ApiDataSource(pokemonId);
            PokemonController controller = new PokemonController(apiData);
            controller.generateData();
            ConsoleOutputFormatter outputFormatter = new ConsoleOutputFormatter(outputFormat, controller.getPokemonDisplay());
            outputFormatter.displayOutput();
        }

        // Uncomment this when you are at part 3 of the assignment
        //ConsoleOutputFormatter outputFormatter = new ConsoleOutputFormatter(outputFormat, /* PokemonDisplay instance */);

        //Modified Versions

        //Version 3, part 2 after implementing the connection to the local database
        /*
        if (dataSource == DataSource.LOCAL_DATA_STORAGE) {
            LocalStorage localData = new LocalStorage(databasePath, pokemonId);
            PokemonController controller = new PokemonController(localData);
            controller.generateData();
        }
        else {
            ApiDataSource apiData = new ApiDataSource(pokemonId);
            PokemonController controller = new PokemonController(apiData);
            controller.generateData();
        }
        */

        //v2
        /*
        ApiDataSource apiData = new ApiDataSource(pokemonId);
        PokemonController controller = new PokemonController(apiData);
        controller.generateData();
        */

        //v1
        /*
        PokemonController controller = new PokemonController(pokemonId);
        controller.generateData();
        */

    }

    public static void parseCommandLineArguments(String[] args) throws PokemonCommandLineParsingException, ParseException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("d", "database", true, "Path to a SQLite database containing pokemons");
        options.addOption("f", "format", true, "Specify the output format, between 'text', 'html' and 'csv'. By default 'text'.");

        // parse the command line arguments
        CommandLine line = parser.parse(options, args);
        if (line.hasOption("d")) {
            dataSource = DataSource.LOCAL_DATABASE;
            databasePath = line.getOptionValue("d");
        }

        if (line.hasOption("f")) {
            String formatArgValue = line.getOptionValue("f");
            if (formatArgValue.equals("html")) {
                outputFormat = OutputFormat.HTML;
            } else if (formatArgValue.equals("csv")) {
                outputFormat = OutputFormat.CSV;
            } else if (formatArgValue.equals("text")) {
                outputFormat = OutputFormat.TEXT;
            } else {
                throw new PokemonCommandLineParsingException("Invalid value for the option -f/--format", options);
            }
        }

        // Get pokemon ID from remaining arguments
        String[] remainingArgs = line.getArgs();
        if (remainingArgs.length < 1) {
            throw new PokemonCommandLineParsingException("You must provide a pokemon ID", options);
        }
        try {
            pokemonId = Integer.parseInt(remainingArgs[0]);
        } catch (NumberFormatException e) {
            throw new PokemonCommandLineParsingException("'" + remainingArgs[0] + "' is not a valid pokemon ID", options);
        }
    }


    static class PokemonCommandLineParsingException extends Exception {

        private Options options;

        public PokemonCommandLineParsingException(String msg, Options options) {
            super(msg);
            this.options = options;
        }

        public Options getOptions() {
            return options;
        }

    };
}
