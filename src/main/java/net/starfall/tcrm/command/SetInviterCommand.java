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
import net.starfall.tcrm.util.DataManager;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetInviterCommand {
    public static final int REQUIRED_PERMISSION_LEVEL = 2; // == admin/op

    public static int run(CommandContext<ServerCommandSource> context, String playerName, String inviterName) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity caller = source.getPlayer();
        Text callerDisplayName;

        // Caller == null -> called from server
        if (caller == null) callerDisplayName = Text.literal("Server");
        else callerDisplayName = caller.getName();

        // People below members can't be inviters (disabled for now)
        /*String inviterRank = DataManager.getRankJson(inviterName);
        if (!Objects.equals(inviterRank, "admin") && !Objects.equals(inviterRank, "member")) {
            source.sendError(Text.literal("Inviters must at least be of rank member"));
            return -1;
        }*/

        DataManager.setInviter(playerName, inviterName);

        source.sendFeedback(Text.literal(callerDisplayName.getString() + " set inviter of " + playerName + " to " + inviterName), true);

        return 1;
    }

    // Registers the command to the registry, so it can be recognized by minecraft
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register((literal("setinviterof").requires((source) -> source.hasPermissionLevel(REQUIRED_PERMISSION_LEVEL))) // Only allow if user has op
                .then(argument("player_of", StringArgumentType.string())
                        .then(argument("player_to", StringArgumentType.string())
                                .executes(context -> run(context, StringArgumentType.getString(context, "player_of"), StringArgumentType.getString(context, "player_to")))
                        )));
    }
}
