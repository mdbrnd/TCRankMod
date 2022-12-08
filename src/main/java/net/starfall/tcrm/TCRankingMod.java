package net.starfall.tcrm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import net.starfall.tcrm.command.Commands;
import net.starfall.tcrm.util.Config;
import net.starfall.tcrm.util.ConfigManager;
import net.starfall.tcrm.util.IEntityDataSaver;
import net.starfall.tcrm.util.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class TCRankingMod implements ModInitializer {
	public static final String MOD_ID = "tcrm";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Create config
	public static Config config;

	@Override
	public void onInitialize() {
		LOGGER.info("Taeko & Co Ranking Mod initialized");

		// Register commands
		Commands.register();

		// Read config (only needs to be read once on startup, the other times you don't have to because the reference to the config object is always the same)
		try {
			ConfigManager.readConfig(true);
		} catch (Exception e) {
			LOGGER.error("Failed to read config file: " + e.getMessage());
		}

		// Check if visitor is online without inviter etc. every server tick
		ServerTickEvents.END_SERVER_TICK.register((server -> {
			if (config.enabled) {
				List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
				// -> go through all players, look at their nbt, if they are visitor or helper
				// without inviter on then kick them, if it is visitor with inviter then set to gm2
				for (ServerPlayerEntity player : players) {
					NbtCompound nbt = ((IEntityDataSaver) player).getPersistentData();
					String rankName = nbt.getString("rank");
					String inviterUUID = nbt.getString("inviter");
					String inviterName = nbt.getString("inviter_name");
					String playerName = player.getDisplayName().getString();
					boolean isAdventureMode = player.interactionManager.getGameMode().equals(GameMode.ADVENTURE);
	
					// If it hasn't been initialized yet
					if (rankName == null || rankName.equals("")) {
						LOGGER.info("User " + player.getDisplayName().getString()
								+ " doesn't have a rank yet, setting to default (" + config.defaultRank + ")");
						DataManager.setRank((IEntityDataSaver) player, config.defaultRank);
					}
	
					if (inviterUUID == null || inviterUUID.equals("")) {
						LOGGER.info("User " + player.getDisplayName().getString()
								+ " doesn't have an inviter yet, setting to default (" + config.defaultInviterName + ")");
						DataManager.setInviter((IEntityDataSaver) player, config.defaultInviterUUID,
								config.defaultInviterName);
					}
	
					if (inviterUUID != null && !inviterUUID.equals("")) {
						switch (rankName) {
							case "visitor" -> {
								// Check if inviter is online; if not, then kick, if yes, then set to gm2
								if (!isPlayerOnline(inviterUUID, server)) {
									// Kick player
									player.networkHandler.disconnect(Text.literal(
											"You are currently a visitor, which means you cannot be online without your inviter ("
													+ inviterName + ")"));
									LOGGER.info("User " + playerName + " was kicked because their inviter " + inviterName
											+ " is currently offline.");
								} else {
									// Set to gm2
									if (!isAdventureMode) {
										LOGGER.info("Inviter " + inviterName + "is online, setting " + playerName
												+ " to adventure mode (gm2)");
										player.sendMessage(Text.literal(
												"You are currently a visitor, which is why your gamemode has been changed to adventure mode")
												.formatted(Formatting.YELLOW));
										player.changeGameMode(GameMode.ADVENTURE);
									}
								}
							}
							case "helper" -> {
								// Check if inviter is online, if not, then kick
								if (!isPlayerOnline(inviterUUID, server)) {
									// Kick player
									player.networkHandler.disconnect(Text.literal(
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
			}
		));

		// Spawn fake player if player is not online
	}

	public static boolean isPlayerOnline(String UUID, MinecraftServer server) {
		List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
		for (ServerPlayerEntity player : players) {
			if (Objects.equals(player.getUuidAsString(), UUID))
				return true;
		}
		return false;
	}
}
