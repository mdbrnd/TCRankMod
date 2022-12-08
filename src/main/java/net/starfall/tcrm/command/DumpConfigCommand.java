package net.starfall.tcrm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.starfall.tcrm.TCRankingMod;
import net.starfall.tcrm.command.suggestion_providers.DefaultConfigSuggestionProvider;
import net.starfall.tcrm.util.Player;

import static net.minecraft.server.command.CommandManager.*;

public class DumpConfigCommand {
    public static final int REQUIRED_PERMISSION_LEVEL = 2; // == admin/op

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        
        // Dump TCRankingMod.config in a readable format using source.sendFeedback()
        // Make it colored
        source.sendFeedback(Text.literal( "Dumping config:").formatted(Formatting.AQUA).formatted(Formatting.ITALIC), false);

        source.sendFeedback(Text.literal("defaultRank: " + TCRankingMod.config.defaultRank).formatted(Formatting.YELLOW), false);
        source.sendFeedback(Text.literal("defaultInviterName: " + TCRankingMod.config.defaultInviterName).formatted(Formatting.YELLOW), false);

        source.sendFeedback(Text.literal( "Players: ").formatted(Formatting.DARK_AQUA).formatted(Formatting.ITALIC), false);
        
        for (Player player : TCRankingMod.config.players) {
            source.sendFeedback(Text.of("{"), false);
            source.sendFeedback(Text.of("Player: " + player.name), false);
            source.sendFeedback(Text.of("Rank: " + player.rank), false);
            source.sendFeedback(Text.of("Inviter: " + player.inviterName), false);
            source.sendFeedback(Text.of("}"), false);
            source.sendFeedback(Text.of(""), false);
        }

        return 1;
    }

    // Registers the command to the registry, so it can be recognized by minecraft
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register((literal("tcrmdumpconfig"))
                .requires((source) -> source.hasPermissionLevel(REQUIRED_PERMISSION_LEVEL))
                .executes(context -> run(context)));
    }
}
