package net.starfall.tcrm.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.starfall.tcrm.TCRankingMod;
import net.starfall.tcrm.command.suggestion_providers.RankSuggestionProvider;

public class RankData {
    public static void setRank(IEntityDataSaver player, String rankName) {
        NbtCompound nbt = player.getRankNbt();

        if (RankSuggestionProvider.isValidRank(rankName)) nbt.putString("rank", rankName);
        else TCRankingMod.LOGGER.debug("Tried setting invalid rank");
    }

    public static void setInviter(IEntityDataSaver player, ServerPlayerEntity inviter) {
        NbtCompound nbt = player.getRankNbt();

        // Save the UUID of the inviter
        nbt.putString("inviter", inviter.getUuidAsString());
    }
}
