<p align="center"><img src="logo.png" width="160" alt="MekaJade Upgrades Fixed"></p>

<h1 align="center">MekaJade Upgrades Fixed</h1>

<p align="center">用 Jade 看向通用机械的机器时，在提示框里列出这台机器已装上的升级图标与数量。这是 1.20.1 / Forge 47.4 的构建。</p>

<p align="center">中文 | <a href="./README_EN.md">English</a></p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.20.1-green.svg?style=flat-square" alt="Minecraft 1.20.1">
  <img src="https://img.shields.io/badge/Forge-47.4+-orange.svg?style=flat-square" alt="Forge 47.4">
  <img src="https://img.shields.io/badge/Jade-required-purple.svg?style=flat-square" alt="Jade">
  <img src="https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square" alt="MIT">
</p>

---

## 这是什么

通用机械的机器装了什么升级，平时只能打开升级界面才看得到。本模组接进 Jade 的提示框：把鼠标指到机器上，已装的升级会以图标加数字的形式排在提示框里，种类再多也按宽度自动换行。

本模组是 **MekaJadeUpgrades**（作者 DevDyna）的修复分支，**非官方**，与原版作者无关，也未获其背书。1.20.1 分支以 1.21.1 分支为蓝本移植，行为与显示保持一致。

| | |
|---|---|
| 原版模组 | MekaJadeUpgrades，作者 DevDyna |
| 原版协议 | MIT，Copyright (c) 2024-2030 DevDyna |
| 本分支协议 | MIT，Copyright (c) 2026 zaixiayesheng |

## 安装

| 文件 | 必需 | 说明 |
|---|---|---|
| `mekajadeupgrades_fixed-20.1-1.0.0.jar` | 是 | 本模组 |
| Jade | 是 | 提示框来自它 |
| 通用机械 1.20.1（10.4.16.x） | 是 | 升级图标的来源 |
| 通用机械：扩展 | 否 | 装了才会显示它的升级 |
| 通用机械：增强·解放 | 否 | 装了才会显示强化升级 |

- 平台：Minecraft 1.20.1 / Forge 47.4+，需要 Java 17。
- 客户端必需，服务端可选（提示框只在客户端绘制）。
- **不要再装原版 MekaJadeUpgrades**：两个都会往提示框里加同一条升级。实在要一起装，本模组会自动掐掉原版那一条（`disableMekaJadeUpgrades`，默认开启），并在日志里写明。

## 提示框显示什么

- 机器的**升级图标**，每个图标右下角是已装数量。
- 只显示这台机器真正支持、并且确实装了的升级。
- 长列表按提示框宽度换行，不再横向溢出面板。
- 额外认识通用机械：扩展的升级，以及增强·解放的强化速度 / 强化能量升级。

## 与原版的区别

| 改动 | 说明 |
|---|---|
| 溢出改为换行 | 原版给每个图标加固定横向偏移定位，但 Jade 的行宽只按元素自身尺寸计算、偏移不计入宽度，升级种类一多就画到面板外。本分支改为按宽度自动换行、横向不加偏移 |
| 图标与数字对齐 | 保留竖直方向 1~2 像素的微调，图标和数字在同一水平线上 |
| 识别合并版 | 直接认 `mekanism_empowered_unleashed`，同时保留旧 id `mekanism_empowered` |
| 自动停用原版 | 检测到原版 MekaJadeUpgrades 时停用它的提示框，避免同一行显示两遍 |
| 健壮性 | mixin 用 `require = 0`，原版改方法名或签名时不再让游戏启动崩溃；认不出的升级退回占位图标并只记一次日志 |
| 只保留提示框部分 | 本分支只包含原版里负责 Jade 提示框的代码 |

## 配置

配置文件 `config/mekajadeupgrades_fixed-common.toml`，改完重启生效。

| 配置项 | 默认 | 说明 |
|---|---|---|
| `disableMekaJadeUpgrades` | true | 与原版同时安装时停用原版，由本模组接管；关掉则两者都显示 |

## 常见问题

- **提示框里没有升级那一行**：先确认机器真的装了升级；再确认 Jade 已安装、模组版本与 Mekanism 版本对得上。
- **升级显示成占位图标**：说明这个升级来自本模组不认识的附属模组，日志里会记一次，不影响游戏。
- **同时装了原版出现两行**：把 `disableMekaJadeUpgrades` 打开（默认已开），或者移除原版。
- **只在客户端装**：可以，提示框在客户端绘制。

## 兼容性

- 已在 Jade、通用机械、通用机械：扩展、增强·解放上测试。
- 认出原版 MekaJadeUpgrades 后会停用它的提示框；不装原版时不受影响。
- 本模组只读机器状态，不写存档，不改变任何机器行为。

## 开发者

### 实现方式

| 位置 | 做法 |
|---|---|
| Jade 插件（`mekajadeupgrades_fixed`） | 向 Jade 注册提示框元素，内容来自机器的升级组件 |
| 渲染 | 按提示框可用宽度逐行排图标，横向不加偏移，超宽换行 |
| 图标解析 | 按升级 id 取图标；认不出的走占位图标，异常不出渲染线程 |
| 原版停用 | mixin 掐掉原版插件的提示框注册与数据入口（`require = 0`） |

### 构建

需要 JDK 17。把编译依赖放进项目根目录的 `libs/`（不随仓库分发）：

- Jade（`jade-1.20.1-forge-*.jar`）
- 通用机械（`mekanism-1.20.1-*.jar`）
- 通用机械：扩展（`mekanism_extras-1.20.1-*.jar`）
- 通用机械：增强·解放（`MekanismEmpoweredUnleashed-*.jar`）

然后 `gradlew build`，产物在 `build/libs/`。

## 更新日志

### 1.0.0

- 首个 Forge 1.20.1 版本，以 1.21.1 分支为蓝本移植：提示框溢出改为按宽度换行、图标与数字对齐。
- 适配通用机械：增强·解放（Mekanism: Empowered Unleashed），同时保留旧 id `mekanism_empowered`。
- 与原版 MekaJadeUpgrades 同装时自动停用原版，避免同一行显示两遍。
- 两处 mixin 使用 `require = 0`，原版改签名时不会让游戏启动崩溃；认不出的升级退回占位图标。

## 许可证与署名

- 原版 [MekaJadeUpgrades](https://github.com/DevDyna/MekaJadeUpgrades)：MIT，Copyright (c) 2024-2030 DevDyna
- 本分支：MIT，Copyright (c) 2026 zaixiayesheng
- 完整协议见 [LICENSE](./LICENSE)，其中保留原作者的版权声明
- 除「与原版的区别」里列出的改动外，其余代码、图标与资源均来自原版

## 反馈

- 本分支的问题提到 [GitHub Issues](https://github.com/zaixiayesheng/MekaJadeUpgradesFixed/issues)，附上 Minecraft 与 Forge 版本、Jade 版本和 `logs/latest.log`
- 原版本身的问题请找原作者 DevDyna
