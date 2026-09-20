package com.devdyna.mekajadeupgradefix.client;

import com.devdyna.mekajadeupgradefix.client.provider.UpgradeProvider;

import mekanism.common.block.prefab.BlockTile;
import snownee.jade.api.*;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    // 与原版 MekaJadeUpgrades 同时安装时，由 mixin（MixinOriginalUpgradeProvider）
    // 停用原版，本模组照常注册并接管显示；开关是配置项 disableMekaJadeUpgrades。

    @Override
    public void registerClient(IWailaClientRegistration r) {
        r.registerBlockComponent(UpgradeProvider.INSTANCE,
                BlockTile.class);
    }

    @Override
    public void register(IWailaCommonRegistration r) {
        r.registerBlockDataProvider(UpgradeProvider.INSTANCE,
                BlockTile.class);
    }
}
