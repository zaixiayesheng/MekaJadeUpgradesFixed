package com.devdyna.mekajadeupgradefix;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 配置。
 *
 * 目前只有一项：与原版 MekaJadeUpgrades 同时安装时，停用【原版】，
 * 由本模组接管提示框里的升级行。两个模组显示的是同一行内容，
 * 一起生效会重复显示，所以默认开启停用。
 */
public final class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DISABLE_ORIGINAL = BUILDER
            .comment("Disable the original MekaJadeUpgrades when it is installed together with this mod.")
            .comment("与原版 MekaJadeUpgrades 同时安装时停用原版，由本模组接管（避免同一行升级显示两遍）。")
            .comment("默认开启；关掉则两个模组都生效。")
            .define("disableMekaJadeUpgrades", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {
    }

    public static void register(ModContainer container) {
        // COMMON：原版的提示框在客户端、数据收集在服务端，两边都要能读到这个开关
        container.registerConfig(ModConfig.Type.COMMON, SPEC);
    }

    /**
     * 是否停用原版。配置尚未加载时回退到默认值 true（宁可保守，
     * 也不要在冲突环境下重复显示）。
     */
    public static boolean shouldDisableOriginal() {
        try {
            return DISABLE_ORIGINAL.get();
        } catch (IllegalStateException e) {
            return true;
        }
    }
}
