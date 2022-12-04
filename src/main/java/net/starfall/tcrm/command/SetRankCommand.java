package net.starfall.tcrm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.starfall.tcrm.command.suggestion_providers.RankSuggestionProvider;
import net.starfall.tcrm.util.IEntityDataSaver;
import net.starfall.tcrm.util.RankData;

import static net.minecraft.server.command.CommandManager.*;

import java.util.Collection;

public class SetRankCommand {
    public static final int REQUIRED_PERMISSION_LEVEL = 2; // == admin/op

    public static int run(CommandContext<ServerCommandSource> context,  Collection<ServerPlayerEntity> players, String rankName) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        Text playerDisplayName = player.getDisplayName();

        if (!RankSuggestionProvider.isValidRank(rankName)) {
            source.sendError(Text.literal("Invalid rank " + "\"" + rankName +"\""));
            source.sendFeedback(Text.literal("Available ranks are:"), false);
            for (String rank : RankSuggestionProvider.ValidRanks) {
                source.sendFeedback(Text.literal(rank), false);
            }
            return -1;
        }

        // Make every requested player the wanted rank
        for (ServerPlayerEntity target : players) {
            RankData.setRank((IEntityDataSaver) target, rankName);
            source.sendFeedback(Text.literal(playerDisplayName.getString() + " made " + target.getDisplayName().getString() + " a " + rankName), true);
        }
        return 1;
    }

    // Registers the command to the registry, so it can be recognized by minecraft
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register((literal("setrank").requires((source) -> source.hasPermissionLevel(REQUIRED_PERMISSION_LEVEL))) // Only allow if user has op
                .then(argument("targets", EntityArgumentType.players())
                    .then(argument("rank_name", StringArgumentType.string()).suggests(new RankSuggestionProvider())
                        .executes(context -> run(context, EntityArgumentType.getPlayers(context, "targets"), StringArgumentType.getString(context, "rank_name")))
        )));
    }
}
