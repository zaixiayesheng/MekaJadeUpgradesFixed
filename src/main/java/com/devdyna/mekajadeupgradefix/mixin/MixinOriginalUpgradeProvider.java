package com.devdyna.mekajadeupgradefix.mixin;

import java.util.concurrent.atomic.AtomicBoolean;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.devdyna.mekajadeupgradefix.Config;
import com.devdyna.mekajadeupgradefix.Main;

import net.minecraft.nbt.CompoundTag;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * 原版 MekaJadeUpgrades 装着的时候，把它的提示框和数据入口停掉。
 *
 * 两个模组显示的是同一件事（已装升级），一起装就会在一行里显示两遍，
 * 所以默认由本模组接管；配置 disableMekaJadeUpgrades 设为 false 可以恢复原版。
 *
 * @Pseudo：原版没安装时整段跳过；require = 0：原版哪天改了方法签名，这里只是不生效，
 * 不会把游戏带崩。
 */
@Pseudo
@Mixin(targets = "com.devdyna.mekajadeupgrade.client.provider.UpgradeProvider", remap = false)
public class MixinOriginalUpgradeProvider {

    private static final AtomicBoolean LOGGED = new AtomicBoolean();

    private static void logOnce() {
        if (LOGGED.compareAndSet(false, true)) {
            Main.LOGGER.info("检测到原版 MekaJadeUpgrades，已停用它的升级提示框与数据入口（把配置 disableMekaJadeUpgrades 设为 false 可恢复原版）。");
        }
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true, require = 0)
    private void mekajadeupgradefix$skipTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config, CallbackInfo ci) {
        if (Config.shouldDisableOriginal()) {
            logOnce();
            ci.cancel();
        }
    }

    @Inject(method = "appendServerData", at = @At("HEAD"), cancellable = true, require = 0)
    private void mekajadeupgradefix$skipServerData(CompoundTag data, BlockAccessor accessor, CallbackInfo ci) {
        if (Config.shouldDisableOriginal()) {
            logOnce();
            ci.cancel();
        }
    }
}
