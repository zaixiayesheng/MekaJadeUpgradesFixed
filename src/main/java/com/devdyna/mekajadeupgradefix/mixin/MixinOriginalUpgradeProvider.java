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
 * 停用原版 MekaJadeUpgrades（mod id: mekajadeupgrade）。
 *
 * 两个模组都会往 Jade 提示框里加同一行升级，一起装就会显示两遍。
 * 这里直接掐掉原版的提示框与数据入口：只取消，不做别的改动，
 * 所以不会崩、也不会影响它的其它内容。
 *
 * 原版没安装时（@Pseudo）本 mixin 整段跳过。
 * 由配置 `disableMekaJadeUpgrades` 控制，默认开启。
 *
 * 两处注入都写了 require = 0：原版以后改了方法名或签名，这里只是不再生效，
 * 不会在启动阶段抛 InjectionError 把游戏带崩。
 */
@Pseudo
@Mixin(targets = "com.devdyna.mekajadeupgrade.client.provider.UpgradeProvider", remap = false)
public class MixinOriginalUpgradeProvider {

    private static final AtomicBoolean LOGGED = new AtomicBoolean();

    /** 真正掐掉原版时在日志里说一次，免得玩家不知道是谁把原版关掉了。 */
    private static void logOnce() {
        if (LOGGED.compareAndSet(false, true)) {
            Main.LOGGER.info("检测到原版 MekaJadeUpgrades，已停用它的升级提示框与数据入口"
                    + "（把配置 disableMekaJadeUpgrades 设为 false 可恢复原版）。");
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
