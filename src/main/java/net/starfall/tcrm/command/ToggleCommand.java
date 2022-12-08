package net.starfall.tcrm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.starfall.tcrm.TCRankingMod;
import net.starfall.tcrm.util.ConfigManager;

import static net.minecraft.server.command.CommandManager.*;

public class ToggleCommand {
    public static final int REQUIRED_PERMISSION_LEVEL = 2; // == admin/op

    public static int run(CommandContext<ServerCommandSource> context ) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        // Toggle the mod off by changing the config
        TCRankingMod.config.enabled = !TCRankingMod.config.enabled;

        // Save the config
        ConfigManager.saveConfig();

        // Send feedback to the user, green if it enabled, red if it disabled
        source.sendFeedback(Text.of("Taeko & Co Ranking Mod " + (TCRankingMod.config.enabled ? "§aenabled" : "§cdisabled")), true);
        return 1;
    }

    // Registers the command to the registry, so it can be recognized by minecraft
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register((literal("tcrmtoggle").requires((source) -> source.hasPermissionLevel(REQUIRED_PERMISSION_LEVEL))).executes(context -> run(context)));
    }
}
