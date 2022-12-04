package net.starfall.tcrm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.starfall.tcrm.command.MyInviterCommand;
import net.starfall.tcrm.command.SetInviterCommand;
import net.starfall.tcrm.command.SetRankCommand;
import net.starfall.tcrm.command.MyRankCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCRankingMod implements ModInitializer {
	public static final String MOD_ID = "tcrm";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		// Register the 2 commands
		CommandRegistrationCallback.EVENT.register(SetRankCommand::register);
		CommandRegistrationCallback.EVENT.register(SetInviterCommand::register);
		CommandRegistrationCallback.EVENT.register(MyRankCommand::register);
		CommandRegistrationCallback.EVENT.register(MyInviterCommand::register);
	}
}
