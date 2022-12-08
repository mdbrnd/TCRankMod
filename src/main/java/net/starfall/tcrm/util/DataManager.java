package net.starfall.tcrm.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.starfall.tcrm.TCRankingMod;
import net.starfall.tcrm.command.suggestion_providers.RankSuggestionProvider;

import java.util.List;
import java.util.Objects;

public class DataManager {
    public static Player getPlayer(String playerName) {
        // Reload config in case something was changed
        ConfigManager.readConfigIntoMemory();

        // Find the player in TCRankingMod.config and get their rank
        for (Player player : TCRankingMod.config.players) {
            if (player.name.equalsIgnoreCase(playerName)) {
                return player;
            }
        }

        return null;
    }

    public static void setInviter(String playerName, String inviterName) {
        ConfigManager.readConfigIntoMemory();

        // If the player doesn't exist, add them
        if (!DataManager.playerExists(playerName)) {
            TCRankingMod.config.players.add(new Player(playerName));
        }

        // Find the player in TCRankingMod.config
        for (Player player : TCRankingMod.config.players) {
            if (player.name.equalsIgnoreCase(playerName)) {
                player.inviterName = inviterName;
                break;
            }
        }

        // Save config
        ConfigManager.saveConfig();
    }

    public static void setRank(String playerName, String rankName) {
        if (!RankSuggestionProvider.isValidRank(rankName)) return;

        ConfigManager.readConfigIntoMemory();

        // If the player doesn't exist, add them
        if (!DataManager.playerExists(playerName)) {
            TCRankingMod.config.players.add(new Player(playerName));
        }

        // Find the player in TCRankingMod.config
        for (Player player : TCRankingMod.config.players) {
            if (player.name.equalsIgnoreCase(playerName)) {
                player.rank = rankName;
                break;
            }
        }

        // Save config
        ConfigManager.saveConfig();
    }

    public static boolean playerExists(String playerName) {
        for (Player player : TCRankingMod.config.players) {
            if (player.name.equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }
}
