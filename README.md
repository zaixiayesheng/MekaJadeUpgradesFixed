# MekaJade Upgrades Fixed

> 用 Jade 看向通用机械的机器时，在提示框里显示这台机器已装上的升级图标和数量。
>
> 这是 **MekaJadeUpgrades**（作者 DevDyna）的修复分支。
>
> English: [README_EN.md](./README_EN.md)

---

## 修了什么

- **提示框溢出**：原版给每个图标加固定横向偏移来定位，但 Jade 的行宽只按元素自身尺寸计算、偏移不计入宽度，于是绘制位置跑到面板外面——升级种类一多（装了通用机械扩展、增强解放之后）就出框。现在改成**按宽度自动换行**、横向不再加偏移，任何分辨率、任何 GUI 缩放下都不会溢出。
- **图标与数字对齐**：保留竖直方向的 1~2 像素微调，图标和数量文字在同一水平线上。
- **版本适配**：直接识别合并版 **Mekanism: Empowered Unleashed**（新 id `mekanism_empowered_unleashed`），旧 id `mekanism_empowered` 也一并保留，所以不再需要合并版 jar 里的兼容桥接。

## 更新日志

### 1.0.0

- 修复了 Jade 提示框溢出
- 适配了通用机械：增强·解放（Mekanism: Empowered Unleashed）
- 不要与 MekaJadeUpgrades 一同安装

## 安装

| 文件 | 说明 |
|---|---|
| `mekajadeupgrades_fixed-1.0.0.jar` | 本模组 |
| Jade | 必需 |
| Mekanism 1.21.1（10.7.19 及以上） | 必需 |
| 通用机械：扩展 | 可选，装了就会显示它的升级图标 |
| Mekanism: Empowered Unleashed | 可选，装了就会显示强化升级图标 |

- 平台：Minecraft 1.21.1 / NeoForge 21.1.x
- **不要与 MekaJadeUpgrades 一同安装**：两个模组都会在提示框里加同一行升级。万一两个都装了，本模组会**自动停用原版 MekaJadeUpgrades**（用 mixin 掐掉它的提示框和数据入口，只取消、不改别的，不会崩），由本模组接管显示，并在日志里说明。该行为由配置 `disableMekaJadeUpgrades` 控制，默认开启。

## 配置

配置文件：`config/mekajadeupgrades_fixed-common.toml`

| 配置项 | 默认 | 说明 |
|---|---|---|
| `disableMekaJadeUpgrades` | true | 与原版 MekaJadeUpgrades 同时安装时停用原版，由本模组接管；关掉则两者都生效（会显示两行升级） |

## 构建

环境：JDK 21。

1. 把编译用的 jar 放进项目根目录的 `libs/`（这些是本模组的编译依赖，不随仓库分发）：
   - Jade（`jade-1.21.1-neoforge-*.jar`）
   - Mekanism（`mekanism-1.21.1-*.jar`）
   - 通用机械：扩展（`mekanism_extras-1.21.1-*.jar`）
   - 通用机械：增强·解放（`MekanismEmpoweredUnleashed-*.jar`）
2. 执行：

```
gradlew build
```

产物在 `build/libs/`。

## 许可与署名

- 基于 [MekaJadeUpgrades](https://github.com/DevDyna/MekaJadeUpgrades) 1.3（MIT，Copyright © DevDyna）修改
- 本分支修改：Copyright © 2026 zaixiayesheng，MIT License
