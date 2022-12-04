package net.starfall.tcrm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.starfall.tcrm.util.IEntityDataSaver;

import static net.minecraft.server.command.CommandManager.literal;

public class MyInviterCommand {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        // Only if player not server called it
        if (player != null) {
            String inviterUUID = ((IEntityDataSaver)player).getInviterNbt().getString("inviter");
            if (inviterUUID != null && inviterUUID != "") {
                source.sendFeedback(Text.literal("Your inviters UUID is: " + inviterUUID), false);
            } else {
                source.sendFeedback(Text.literal("You currently don't have an inviter."), false);
            }
            return 1;
        }
        return -1;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(literal("myinviter").executes(context -> run(context)));
    }
}
