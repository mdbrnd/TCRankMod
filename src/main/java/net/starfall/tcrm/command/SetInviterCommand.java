package net.starfall.tcrm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.starfall.tcrm.util.IEntityDataSaver;
import net.starfall.tcrm.util.DataManager;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetInviterCommand {
    public static final int REQUIRED_PERMISSION_LEVEL = 2; // == admin/op

    public static int run(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, ServerPlayerEntity inviter) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity caller = source.getPlayer();

        // Only if player not server called it
        if (caller != null){
            Text callerDisplayName = caller.getDisplayName();

            // People below members can't be inviters
            String inviterRank = ((IEntityDataSaver)inviter).getPersistentData().getString("rank");
            if (!Objects.equals(inviterRank, "admin") && !Objects.equals(inviterRank, "member")) {
                source.sendError(Text.literal("Inviters must at least be of rank member"));
                return -1;
            }

            DataManager.setInviter((IEntityDataSaver)player, inviter);
            source.sendFeedback(Text.literal(callerDisplayName.getString() + " set inviter of " + player.getDisplayName().getString() + " to " + inviter.getDisplayName().getString()), true);

            return 1;
        }
        return -1;
    }

    // Registers the command to the registry, so it can be recognized by minecraft
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register((literal("setinviterof").requires((source) -> source.hasPermissionLevel(REQUIRED_PERMISSION_LEVEL))) // Only allow if user has op
                .then(argument("of", EntityArgumentType.players())
                        .then(argument("to", EntityArgumentType.players())
                                .executes(context -> run(context, EntityArgumentType.getPlayer(context, "of"), EntityArgumentType.getPlayer(context, "to")))
                        )));
    }
}
