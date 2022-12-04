package net.starfall.tcrm.util;

import net.minecraft.nbt.NbtCompound;

// An interface which the mixin uses; it serves to be able to edit the nbt data
public interface IEntityDataSaver {
    NbtCompound getRankNbt();
    NbtCompound getInviterNbt();
}
