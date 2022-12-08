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
import net.starfall.tcrm.TCRankingMod;
import net.starfall.tcrm.util.DataManager;
import net.starfall.tcrm.util.Player;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.*;

public class GetInviterCommand {
    public static final int REQUIRED_PERMISSION_LEVEL = 2; // == admin/op

    public static int run(CommandContext<ServerCommandSource> context,  String playerName) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        Player player = DataManager.getPlayer(playerName);

        if (player != null) source.sendFeedback(Text.literal(player.name + "'s inviter is: " + player.inviterName), false);
        else source.sendFeedback(Text.literal("That player currently does not have an inviter. (default: " + TCRankingMod.config.defaultInviterName + ")"), false);

        return 1;
    }

    public static int run_without_args(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity caller = source.getPlayer();

        // Only if player not server called it
        if (caller != null) {
            Player player = DataManager.getPlayer(caller.getName().getString());
            if (player != null) {
                boolean isInviterOnline = TCRankingMod.isPlayerOnline(player.inviterName, Objects.requireNonNull(caller.getServer()));

                source.sendFeedback(Text.literal("Your inviter is: " + player.inviterName + " (currently " + (isInviterOnline ? "online" : "offline") + ")"), false);
            } else {
                source.sendFeedback(Text.literal("You currently don't have an inviter."), false);
            }
            return 1;
        } else {
            source.sendFeedback(Text.literal("The Server doesn't have an inviter"), false);
        }
        return -1;
    }

    // Registers the command to the registry, so it can be recognized by minecraft
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register((literal("getinviter").executes(context -> run_without_args(context)))
                .then(argument("player_name", StringArgumentType.string())
                        .requires((source) -> source.hasPermissionLevel(REQUIRED_PERMISSION_LEVEL)) // Ranks of other players can only be seen by operators
                        .executes(context -> run(context, StringArgumentType.getString(context, "player_name")))));
    }
}
