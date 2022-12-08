package net.starfall.tcrm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import net.starfall.tcrm.command.Commands;
import net.starfall.tcrm.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TCRankingMod implements ModInitializer {
	public static final String MOD_ID = "tcrm";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Create global config
	public static Config config;

	@Override
	public void onInitialize() {
		LOGGER.info("Taeko & Co Ranking Mod initialized");

		// Register commands
		Commands.register();

		// Read config
		try {
			ConfigManager.readConfigIntoMemory();
		} catch (Exception e) {
			LOGGER.error("Failed to read config file: " + e.getMessage());
		}

		// Check if visitor is online without inviter etc. every server tick
		ServerTickEvents.END_SERVER_TICK.register((server -> {
			if (config.enabled) {
				List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
				// -> go through all players, look at their data, if they are visitor or helper
				// without inviter on then kick them, if it is visitor with inviter then set to gm2
				for (ServerPlayerEntity svpe : players) {
					Player player = DataManager.getPlayer(svpe.getName().getString());
					if (player == null) {
						LOGGER.info("User " + svpe.getName().getString()
								+ " doesn't have a rank yet, setting to default (" + config.defaultRank + ")");

						DataManager.setRank(svpe.getName().getString(), config.defaultRank);

						LOGGER.info("User " + svpe.getName().getString()
								+ " doesn't have an inviter yet, setting to default (" + config.defaultInviterName + ")");

						DataManager.setInviter(svpe.getName().getString(), config.defaultInviterName);
					}

					player = DataManager.getPlayer(svpe.getName().getString());
					String rankName = player.rank;
					String inviterName = player.inviterName;
					String playerName = svpe.getName().getString();
					boolean isAdventureMode = svpe.interactionManager.getGameMode().equals(GameMode.ADVENTURE);

					switch (rankName) {
						case "visitor" -> {
							// Check if inviter is online; if not, then kick, if yes, then set to gm2
							if (!isPlayerOnline(inviterName, server)) {
								// Kick player
								svpe.networkHandler.disconnect(Text.literal(
										"You are currently a visitor, which means you cannot be online without your inviter ("
												+ inviterName + ")"));
								LOGGER.info("User " + playerName + " was kicked because their inviter " + inviterName
										+ " is currently offline.");
							} else {
								// Set to gm2
								if (!isAdventureMode) {
									LOGGER.info("Inviter " + inviterName + "is online, setting " + playerName
											+ " to adventure mode (gm2)");
									svpe.sendMessage(Text.literal(
													"You are currently a visitor, which is why your gamemode has been changed to adventure mode")
											.formatted(Formatting.YELLOW));
									svpe.changeGameMode(GameMode.ADVENTURE);
								}
							}
						}
						case "helper" -> {
							// Check if inviter is online, if not, then kick
							if (!isPlayerOnline(inviterName, server)) {
								// Kick player
								svpe.networkHandler.disconnect(Text.literal(
										"You are currently a helper, which means you cannot be online without your inviter ("
												+ inviterName + ")"));
								LOGGER.info("User " + playerName + " was kicked because their inviter " + inviterName
										+ " is currently offline.");
							}
						}
					}
				}
			}
			}
		));
	}

	public static boolean isPlayerOnline(String name, MinecraftServer server) {
		List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
		for (ServerPlayerEntity player : players) {
			if (name.equalsIgnoreCase(player.getName().getString()))
				return true;
		}
		return false;
	}
}
