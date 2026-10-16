package com.devdyna.mekajadeupgradefix.client.provider;

import java.util.ArrayList;
import java.util.List;

import com.devdyna.mekajadeupgradefix.Main;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import com.jerry.mekanism_extras.common.registries.ExtraItems;

import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade;
import dev.lapis256.mekanism_empowered.common.init.MekEmpItems;
import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.fml.ModList;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

/**
 * 在 Jade 的提示框里追加一行「已装升级 ×数量」。
 *
 * 服务端把每台机器装了什么升级塞进 server data（{@link #appendServerData}），
 * 客户端读出来排版成图标 + 数量（{@link #appendTooltip}）。
 *
 * 与 1.21.1 分支的 API 差异都用「1.20.1:」注释标了出来：
 * getRawName() 取代 getSerializedName()、Upgrade.GAS 取代 CHEMICAL、
 * IElement.Align.values() 取代 VALUES、Extras 的包名是 com.jerry.mekanism_extras。
 */
@SuppressWarnings("null")
public enum UpgradeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    private static final boolean DEBUG = System.getProperty("mekajadeupgrades.debug") != null;

    private static boolean renderWarned = false;

    private static void warnOnce(Throwable e) {
        if (renderWarned) {
            return;
        }
        renderWarned = true;
        Main.LOGGER.warn("升级图标渲染失败，已退回占位图标（只提示这一次）", e);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag tag = accessor.getServerData();

        // 量出原版提示框里最长一行的宽度，让升级行按同样的宽度换行；
        // 量不到（比如别的模组换了排版）就退回默认值。
        final float FALLBACK_WIDTH = 100F;
        final float ROW_WIDTH_CAP = 100F;

        float maxWidth = 0F;
        for (int i = 0; i < tooltip.size(); i++) {
            float lineWidth = 0F;
            // 1.20.1: Jade 11 的 Align 是普通枚举，没有 1.21.1 的 VALUES 常量数组
            for (IElement.Align align : IElement.Align.values()) {
                for (IElement element : tooltip.get(i, align)) {
                    lineWidth += element.getCachedSize().x;
                }
            }
            maxWidth = Math.max(maxWidth, lineWidth);
        }
        if (maxWidth <= 0F) {
            maxWidth = FALLBACK_WIDTH;
        }
        maxWidth = Math.min(maxWidth, ROW_WIDTH_CAP);

        List<IElement> row = new ArrayList<>();
        float rowWidth = 0;
        List<Float> rowWidths = new ArrayList<>();

        for (Upgrade upgrade : Upgrade.values()) {
            // 1.20.1: Upgrade#getRawName()（10.4 没有 getSerializedName()）
            if (tag.contains(upgrade.getRawName())) {
                int count = tag.getInt(upgrade.getRawName());
                IElement icon = item(upgrade);
                IElement text = countText(count);
                float pairWidth = icon.getCachedSize().x + text.getCachedSize().x;
                if (!row.isEmpty() && rowWidth + pairWidth > maxWidth) {
                    rowWidths.add(rowWidth);
                    tooltip.add(row);
                    row = new ArrayList<>();
                    rowWidth = 0;
                }
                row.add(icon);
                row.add(text);
                rowWidth += pairWidth;
            }
        }
        if (!row.isEmpty()) {
            rowWidths.add(rowWidth);
            tooltip.add(row);
        }

        if (DEBUG) {
            Main.LOGGER.info("[debug] tooltip lines={} measuredMax={} cap={} rows={}",
                    tooltip.size(), maxWidth, maxWidth, rowWidths);
        }
    }

    /** 一个升级的图标。取不到物品时退回屏障方块，避免整行渲染崩掉。 */
    public IElement item(Upgrade upgrade) {
        try {
            return IElementHelper.get().item(new ItemStack(getUpgrade(upgrade).get()), 0.55f)
                    .size(new Vec2(10, 10))
                    .translate(new Vec2(0, -2))
                    .message(null);
        } catch (Throwable e) {
            warnOnce(e);
            return IElementHelper.get().item(new ItemStack(Items.BARRIER), 0.55f)
                    .size(new Vec2(10, 10))
                    .translate(new Vec2(0, -2))
                    .message(null);
        }
    }

    /** 「x数量」。不强制尺寸，让 Jade 自己量。 */
    public IElement countText(int count) {
        return IElementHelper.get().text(Component.literal("x" + count))
                .translate(new Vec2(0, -1))
                .message(null);
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof TileEntityMekanism be)) {
            return;
        }
        var component = be.getComponent();
        if (component == null) {
            return;
        }
        for (Upgrade upgrade : Upgrade.values()) {
            if (component.isUpgradeInstalled(upgrade)) {
                data.putInt(upgrade.getRawName(), component.getUpgrades(upgrade));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        // 1.20.1: 1.20.1 没有 ResourceLocation.fromNamespaceAndPath
        return new ResourceLocation(Main.MODID, "upgrades");
    }

    /** 升级常量 → 对应的物品。可选的联动模组没装时那一段直接跳过。 */
    private ItemRegistryObject<ItemUpgrade> getUpgrade(Upgrade upgrade) {
        if (upgrade == Upgrade.SPEED) {
            return mekanism.common.registries.MekanismItems.SPEED_UPGRADE;
        }
        if (upgrade == Upgrade.ENERGY) {
            return mekanism.common.registries.MekanismItems.ENERGY_UPGRADE;
        }
        // 1.20.1: 10.4 的「气体升级」叫 GAS，1.21.1 才叫 CHEMICAL
        if (upgrade == Upgrade.GAS) {
            return mekanism.common.registries.MekanismItems.GAS_UPGRADE;
        }
        if (upgrade == Upgrade.ANCHOR) {
            return mekanism.common.registries.MekanismItems.ANCHOR_UPGRADE;
        }
        if (upgrade == Upgrade.FILTER) {
            return mekanism.common.registries.MekanismItems.FILTER_UPGRADE;
        }
        if (upgrade == Upgrade.MUFFLING) {
            return mekanism.common.registries.MekanismItems.MUFFLING_UPGRADE;
        }
        if (upgrade == Upgrade.STONE_GENERATOR) {
            return mekanism.common.registries.MekanismItems.STONE_GENERATOR_UPGRADE;
        }

        if (ModList.get().isLoaded("mekanism_extras")) {
            if (upgrade == ExtraUpgrade.STACK) {
                return ExtraItems.STACK;
            }
            if (upgrade == ExtraUpgrade.CREATIVE) {
                return ExtraItems.CREATIVE;
            }
            if (upgrade == ExtraUpgrade.IONIC_MEMBRANE) {
                return ExtraItems.IONIC_MEMBRANE;
            }
        }

        if (ModList.get().isLoaded("mekanism_empowered_unleashed") || ModList.get().isLoaded("mekanism_empowered")) {
            if (upgrade == MekEmpUpgrade.getAUTO_INSERTER()) {
                return MekEmpItems.INSTANCE.getAUTO_INSERTER();
            }
            if (upgrade == MekEmpUpgrade.getEMPOWERED_ENERGY()) {
                return MekEmpItems.INSTANCE.getEMPOWERED_ENERGY();
            }
            if (upgrade == MekEmpUpgrade.getEMPOWERED_SPEED()) {
                return MekEmpItems.INSTANCE.getEMPOWERED_SPEED();
            }
            if (upgrade == MekEmpUpgrade.getFAST_ITEM_EJECT()) {
                return MekEmpItems.INSTANCE.getFAST_ITEM_EJECT();
            }
            if (upgrade == MekEmpUpgrade.getFAST_ITEM_INSERT()) {
                return MekEmpItems.INSTANCE.getFAST_ITEM_INSERT();
            }
            if (upgrade == MekEmpUpgrade.getIO_CAPACITY()) {
                return MekEmpItems.INSTANCE.getIO_CAPACITY();
            }
        }

        return null;
    }
}
