package com.devdyna.mekajadeupgradefix;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

/**
 * 1.20.1 版配置：把 1.21.1 的 ModConfigSpec 换成 Forge 的 ForgeConfigSpec，
 * 注册入口从 {@code ModContainer#registerConfig} 换成 {@code ModLoadingContext}（见 Main）。
 *
 * 只有一个开关：和原版 MekaJadeUpgrades 同时装时，停用原版，避免同一行升级显示两遍。
 */
public final class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue DISABLE_ORIGINAL = BUILDER
            .comment("Disable the original MekaJadeUpgrades when it is installed together with this mod.")
            .comment("与原版 MekaJadeUpgrades 同时安装时停用原版，由本模组接管（避免同一行升级显示两遍）。")
            .comment("默认开启；关掉则两个模组都生效。")
            .define("disableMekaJadeUpgrades", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private Config() {}

    /** 配置还没加载完时（比如 mixin 在配置载入前就被调用）当作开启，保持与原版行为一致。 */
    public static boolean shouldDisableOriginal() {
        try {
            return DISABLE_ORIGINAL.get();
        } catch (IllegalStateException e) {
            return true;
        }
    }
}
