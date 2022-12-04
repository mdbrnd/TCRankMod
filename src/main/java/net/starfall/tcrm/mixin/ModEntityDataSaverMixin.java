package net.starfall.tcrm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.starfall.tcrm.TCRankingMod;
import net.starfall.tcrm.util.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    // NBT Data (one nbt data in this case)
    private NbtCompound rank;
    private NbtCompound inviter;

    @Override
    public NbtCompound getRankNbt() {
        if (rank == null) {
            rank = new NbtCompound();
        }
        return rank;
    }

    @Override
    public NbtCompound getInviterNbt() {
        if (inviter == null) {
            inviter = new NbtCompound();
        }
        return inviter;
    }

    // Mixins used to modify player NBT data
    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if (rank != null) {
            nbt.put(TCRankingMod.MOD_ID + "rank", rank);
        }
        if (inviter != null) {
            nbt.put(TCRankingMod.MOD_ID + "inviter", inviter);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(TCRankingMod.MOD_ID + "rank", 10)) {
            rank = nbt.getCompound(TCRankingMod.MOD_ID + "rank");
        }

        if (nbt.contains(TCRankingMod.MOD_ID + "inviter", 10)) {
            inviter = nbt.getCompound(TCRankingMod.MOD_ID + "inviter");
        }
    }
}
