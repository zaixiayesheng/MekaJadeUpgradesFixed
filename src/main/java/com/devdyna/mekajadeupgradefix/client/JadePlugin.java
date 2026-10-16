package com.devdyna.mekajadeupgradefix.client;

import com.devdyna.mekajadeupgradefix.client.provider.UpgradeProvider;

import mekanism.common.block.prefab.BlockTile;
import mekanism.common.tile.base.TileEntityMekanism;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/**
 * 把这个提供器挂到 Mekanism 的所有机器上。
 *
 * 与 1.21.1 的差别：1.20.1 的 Jade（11.x）在 {@code registerBlockDataProvider} 里收的是
 * **方块实体类**，不是方块类，所以这里传 {@link TileEntityMekanism}。
 */
@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(UpgradeProvider.INSTANCE, BlockTile.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(UpgradeProvider.INSTANCE, TileEntityMekanism.class);
    }
}
