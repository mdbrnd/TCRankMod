package net.starfall.tcrm.command.suggestion_providers;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class RankSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

    public static final Collection<String> ValidRanks = new ArrayList<>() {
        {
            add("visitor");
            add("helper");
            add("member");
            add("admin");
        }
    };

    public static boolean isValidRank(String rankName) {
        return ValidRanks.contains(rankName.toLowerCase());
    }

    // Shows autocomplete suggestions when typing the rank, like how you get a list of players when you do /tp or /give for example
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        for (String suggestion : ValidRanks) {
            builder.suggest(suggestion);
        }

        return builder.buildFuture();
    }
}
