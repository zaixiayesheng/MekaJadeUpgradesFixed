package com.devdyna.mekajadeupgradefix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "mekajadeupgrades_fixed";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public Main(IEventBus bus, ModContainer mc) {
        Config.register(mc);
    }
}
