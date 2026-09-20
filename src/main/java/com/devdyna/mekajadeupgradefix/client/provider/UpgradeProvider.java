package com.devdyna.mekajadeupgradefix.client.provider;

import java.util.ArrayList;
import java.util.List;
import com.devdyna.mekajadeupgradefix.Main;

import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.neoforged.fml.ModList;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.*;
import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.registries.MekanismItems;
import com.jerry.mekextras.api.ExtraUpgrade;
import com.jerry.mekextras.common.registries.ExtraItems;

import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade;
import dev.lapis256.mekanism_empowered.common.init.MekEmpItems;

@SuppressWarnings("null")
public enum UpgradeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag tag = accessor.getServerData();

        // 换行宽度取"别的行的最大测量宽度"，但再压到 ROW_WIDTH_CAP 以内。
        //
        // 为什么要压：Jade 的面板宽度 = 各行测量宽度的最大值；而能量条那种元素的
        // 测量宽度往往比它实际画出来的条更宽（它常是行尾元素、会被拉伸填充），
        // 按它对齐仍会让图标行显得比条宽、右边突出来。压到一个保守值（约 4 组
        // 图标）后，图标行永远不是最宽的行，面板尺寸和观感都稳定。
        //
        // 尺寸都是 GUI 像素：分辨率 / GUI 缩放只改变 1 个 GUI 像素画多大，
        // 不影响这些数值，所以上限在任何分辨率下表现一致。
        final float FALLBACK_WIDTH = 100F;
        final float ROW_WIDTH_CAP = 100F;

        float maxWidth = 0F;
        for (int i = 0; i < tooltip.size(); i++) {
            float lineWidth = 0F;
            for (IElement.Align align : IElement.Align.VALUES) {
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

        // 注意：这里【不能】用 .translate() 定位。Jade 的行宽 = 各元素自身尺寸之和，
        // 偏移量不计入宽度；而绘制位置 = 顺序排版位置 + 偏移量。原版既让 Jade 顺序
        // 排一遍、又用 translate 往右推一遍，两个位置叠加，图标就被画到提示框外面去了。
        // 让 Jade 自己按顺序排版，绘制位置与测量宽度一致，面板就不会被撑破。
        List<IElement> row = new ArrayList<>();
        float rowWidth = 0;
        List<Float> rowWidths = new ArrayList<>();

        for (Upgrade upgrade : Upgrade.values()) {
            if (tag.contains(upgrade.getSerializedName())) {
                int count = tag.getInt(upgrade.getSerializedName());

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

        if (System.getProperty("mekajadeupgrades.debug") != null) {
            Main.LOGGER.info("[debug] tooltip lines={} measuredMax={} cap={} rows={}",
                    tooltip.size(), maxWidth, maxWidth, rowWidths);
        }
    }

    public IElement item(Upgrade upgrade) {
        try {

            // 只保留竖直微调（对齐图标与数量文字）；横向绝不能加偏移，
            // 否则绘制位置会超出 Jade 按元素尺寸算出来的行宽，导致溢出。
            return IElementHelper.get()
                    .item(new ItemStack(getUpgrade(upgrade).get()), 0.55f)
                    .size(new Vec2(10, 10))
                    .translate(new Vec2(0, -2))
                    .message(null);

        } catch (NullPointerException e) {
            // catch eventual crashes with a dummy item render
            return IElementHelper.get()
                    .item(new ItemStack(Items.BARRIER), 0.55f)
                    .size(new Vec2(10, 10))
                    .translate(new Vec2(0, -2))
                    .message(null);
        }
    }

    public IElement countText(int count) {
        // 不强制尺寸：让 Jade 按文字实际宽度排版，数量文字紧跟在图标右边。
        return IElementHelper.get()
                .text(Component.literal("x" + count))
                .translate(new Vec2(0, -1))
                .message(null);
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {

        TileEntityMekanism be = (TileEntityMekanism) accessor.getBlockEntity();

        for (Upgrade upgrade : Upgrade.values())
            if (be.getComponent() != null)
                if (be.getComponent().isUpgradeInstalled(upgrade))
                    data.putInt(upgrade.getSerializedName(), be.getComponent().getUpgrades(upgrade));

    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(Main.MODID, "upgrades");
    }

    public ItemRegistryObject<ItemUpgrade> getUpgrade(Upgrade upgrade) {

        if (Upgrade.SPEED.equals(upgrade))
            return MekanismItems.SPEED_UPGRADE;
        if (Upgrade.ENERGY.equals(upgrade))
            return MekanismItems.ENERGY_UPGRADE;
        if (Upgrade.CHEMICAL.equals(upgrade))
            return MekanismItems.CHEMICAL_UPGRADE;
        if (Upgrade.ANCHOR.equals(upgrade))
            return MekanismItems.ANCHOR_UPGRADE;
        if (Upgrade.FILTER.equals(upgrade))
            return MekanismItems.FILTER_UPGRADE;

        if (Upgrade.MUFFLING.equals(upgrade))
            return MekanismItems.MUFFLING_UPGRADE;

        if (Upgrade.STONE_GENERATOR.equals(upgrade))
            return MekanismItems.STONE_GENERATOR_UPGRADE;

        if (ModList.get().isLoaded("mekanism_extras")) {

            if (ExtraUpgrade.STACK.equals(upgrade))
                return ExtraItems.STACK;

            if (ExtraUpgrade.CREATIVE.equals(upgrade))
                return ExtraItems.CREATIVE;

            if (ExtraUpgrade.IONIC_MEMBRANE.equals(upgrade))
                return ExtraItems.IONIC_MEMBRANE;

        }

        // 适配合并版的新 id；旧 id 一并保留，兼容原版 Mekanism: Empowered
        if (ModList.get().isLoaded("mekanism_empowered_unleashed") || ModList.get().isLoaded("mekanism_empowered")) {

            if (MekEmpUpgrade.getAUTO_INSERTER().equals(upgrade))
                return MekEmpItems.INSTANCE.getAUTO_INSERTER();

            if (MekEmpUpgrade.getEMPOWERED_ENERGY().equals(upgrade))
                return MekEmpItems.INSTANCE.getEMPOWERED_ENERGY();

            if (MekEmpUpgrade.getEMPOWERED_SPEED().equals(upgrade))
                return MekEmpItems.INSTANCE.getEMPOWERED_SPEED();

            if (MekEmpUpgrade.getFAST_ITEM_EJECT().equals(upgrade))
                return MekEmpItems.INSTANCE.getFAST_ITEM_EJECT();

            if (MekEmpUpgrade.getFAST_ITEM_INSERT().equals(upgrade))
                return MekEmpItems.INSTANCE.getFAST_ITEM_INSERT();

            if (MekEmpUpgrade.getIO_CAPACITY().equals(upgrade))
                return MekEmpItems.INSTANCE.getIO_CAPACITY();

        }

        return null;

    }

}