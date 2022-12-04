package net.starfall.tcrm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.starfall.tcrm.util.IEntityDataSaver;

import static net.minecraft.server.command.CommandManager.literal;

public class MyRankCommand {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        // Only if player not server called it
        if (player != null) {
            String rank = ((IEntityDataSaver)player).getPersistentData().getString("rank");
            if (rank != null && !rank.equals("")) {
                source.sendFeedback(Text.literal("Your rank is: " + rank), false);
            } else {
                source.sendFeedback(Text.literal("You currently don't have a rank."), false);
            }
            return 1;
        }
        return -1;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(literal("myrank").executes(MyRankCommand::run));
    }
}
