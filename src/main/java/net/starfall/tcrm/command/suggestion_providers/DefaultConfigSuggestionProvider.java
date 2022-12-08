package net.starfall.tcrm.command.suggestion_providers;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.starfall.tcrm.TCRankingMod;
import net.starfall.tcrm.util.Config;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

public class DefaultConfigSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    // Shows autocomplete suggestions when typing the rank, like how you get a list of players when you do /tp or /give for example
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        // Add all fields of Config class that have "default" to the suggestions
        for (Field field : Config.class.getDeclaredFields()) {
            if (field.getName().startsWith("default")) {
                builder.suggest(field.getName());
            }
        }

        return builder.buildFuture();
    }

    // Get a string array of default options
    public static String[] getDefaultConfigOptions() {
        String[] options = new String[Config.class.getDeclaredFields().length];
        
        int i = 0;
        for (Field field : Config.class.getDeclaredFields()) {
            if (field.getName().startsWith("default")) {
                // Add the field name to the array
                options[i] = field.getName();
            }
            i += 1;
        }

        return options;
    }

    public static boolean isValidDefaultOption(String optionName) {
        for (String option : getDefaultConfigOptions()) {
            if (option == optionName) return true;
        }

        return false;
    }
}
