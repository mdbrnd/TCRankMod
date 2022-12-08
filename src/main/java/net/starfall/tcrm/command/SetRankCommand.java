package net.starfall.tcrm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.starfall.tcrm.command.suggestion_providers.RankSuggestionProvider;
import net.starfall.tcrm.util.DataManager;

import static net.minecraft.server.command.CommandManager.*;

public class SetRankCommand {
    public static final int REQUIRED_PERMISSION_LEVEL = 2; // == admin/op

    public static int run(CommandContext<ServerCommandSource> context,  String playerName, String rankName) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity caller = source.getPlayer();

        Text callerDisplayName;

        // Caller == null -> called from server
        if (caller == null) callerDisplayName = Text.literal("Server");
        else callerDisplayName = caller.getDisplayName();

        if (!RankSuggestionProvider.isValidRank(rankName)) {
            source.sendError(Text.literal("Invalid rank " + "\"" + rankName +"\""));
            source.sendFeedback(Text.literal("Available ranks are:"), false);
            for (String rank : RankSuggestionProvider.ValidRanks) {
                source.sendFeedback(Text.literal(rank), false);
            }
            return -1;
        }

        // Make the requested player the wanted rank
        DataManager.setRank(playerName, rankName);

        source.sendFeedback(Text.literal(callerDisplayName.getString() + " made " + playerName + " a " + rankName), true);

        return 1;
    }

    // Registers the command to the registry, so it can be recognized by minecraft
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register((literal("setrank").requires((source) -> source.hasPermissionLevel(REQUIRED_PERMISSION_LEVEL))) // Only allow if user has op
                .then(argument("player_name", StringArgumentType.string())
                    .then(argument("rank_name", StringArgumentType.string()).suggests(new RankSuggestionProvider())
                        .executes(context -> run(context, StringArgumentType.getString(context, "player_name"), StringArgumentType.getString(context, "rank_name")))
        )));
    }
}
