package net.starfall.tcrm.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Commands {
    public static void register() {
        // Register the commands
		CommandRegistrationCallback.EVENT.register(SetRankCommand::register);
		CommandRegistrationCallback.EVENT.register(SetInviterCommand::register);
		CommandRegistrationCallback.EVENT.register(SetConfigCommand::register);

        CommandRegistrationCallback.EVENT.register(GetRankCommand::register);
		CommandRegistrationCallback.EVENT.register(GetInviterCommand::register);

        CommandRegistrationCallback.EVENT.register(ToggleCommand::register);

        CommandRegistrationCallback.EVENT.register(DumpConfigCommand::register);
    }
}
