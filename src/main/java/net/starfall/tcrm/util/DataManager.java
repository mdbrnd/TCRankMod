package net.starfall.tcrm.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.starfall.tcrm.command.suggestion_providers.RankSuggestionProvider;

public class DataManager {
    public static void setRank(IEntityDataSaver player, String rankName) {
        NbtCompound nbt = player.getPersistentData();

        if (RankSuggestionProvider.isValidRank(rankName)) nbt.putString("rank", rankName);
    }

    public static void setInviter(IEntityDataSaver player, ServerPlayerEntity inviter) {
        NbtCompound nbt = player.getPersistentData();

        // Save the UUID of the inviter
        nbt.putString("inviter", inviter.getUuidAsString());
        nbt.putString("inviter_name", inviter.getDisplayName().getString());
    }

    public static void setEmptyInviter(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putString("inviter", "");
        nbt.putString("inviter_name", "");
    }
}