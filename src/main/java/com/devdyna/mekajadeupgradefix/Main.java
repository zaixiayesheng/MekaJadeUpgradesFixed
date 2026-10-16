package com.devdyna.mekajadeupgradefix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * MekaJade Upgrades Fixed —— 1.20.1 / Forge 47 版。
 *
 * 与 1.21.1 分支的差别只有 API：NeoForge 的 {@code @Mod} 构造器可以注入
 * ModContainer，Forge 1.20.1 这里走 ModLoadingContext 注册配置。
 */
@Mod(Main.MODID)
public class Main {

    public static final String MODID = "mekajadeupgrades_fixed";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public Main() {
        // COMMON：原版的提示框在客户端、数据收集在服务端，两边都要能读到这个开关
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
