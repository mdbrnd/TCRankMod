package net.starfall.tcrm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.starfall.tcrm.TCRankingMod;
import net.starfall.tcrm.command.suggestion_providers.DefaultConfigSuggestionProvider;
import net.starfall.tcrm.command.suggestion_providers.RankSuggestionProvider;
import net.starfall.tcrm.util.ConfigManager;

import static net.minecraft.server.command.CommandManager.*;

public class SetConfigCommand {
    public static final int REQUIRED_PERMISSION_LEVEL = 2; // == admin/op

    public static int run(CommandContext<ServerCommandSource> context, String option, String value) throws CommandSyntaxException {
        // Check if the option is valid
        if (DefaultConfigSuggestionProvider.isValidDefaultOption(option)) {
            ServerCommandSource source = context.getSource();

            // Set the option to the value
            switch (option) {
                case "defaultRank" -> {
                    if (RankSuggestionProvider.isValidRank(value)) TCRankingMod.config.defaultRank = value;
                    else {
                        source.sendError(Text.literal("Invalid rank " + "\"" + value + "\""));
                        source.sendFeedback(Text.literal("Available ranks are:"), false);
                        for (String rank : RankSuggestionProvider.ValidRanks) {
                            source.sendFeedback(Text.literal(rank), false);
                        }
                    }
                }
                case "defaultInviterName" -> TCRankingMod.config.defaultInviterName = value;
            }

            // Save the config
            ConfigManager.saveConfig();

            // Send feedback to the user
            source.sendFeedback(Text.of("Taeko & Co Ranking Mod: Set default " + option + " to " + value), true);
            return 1;
        } else {
            // Send feedback to the user
            context.getSource().sendFeedback(Text.of("Invalid option"), true);
            return 0;
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register((literal("tcrmsetconfig").requires((source) -> source.hasPermissionLevel(REQUIRED_PERMISSION_LEVEL))) // Only allow if user has op
                .then(argument("option", StringArgumentType.string()).suggests(new DefaultConfigSuggestionProvider())
                        .then(argument("value", StringArgumentType.string())
                                .executes(context -> run(context, StringArgumentType.getString(context, "option"), StringArgumentType.getString(context, "value")))
                        )
                )
        );
    }
}
