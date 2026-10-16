<p align="center"><img src="logo.png" width="160" alt="MekaJade Upgrades Fixed"></p>

<h1 align="center">MekaJade Upgrades Fixed</h1>

<p align="center">用 Jade 看向通用机械的机器时，在提示框里显示这台机器已装上的升级图标和数量。<br>
<strong>MekaJadeUpgrades</strong>（作者 DevDyna）的非官方修复分支，与原版作者无关、也未获其背书。</p>

<p align="center">中文 | <a href="./README_EN.md">English</a></p>

<p align="center">
  <a href="https://github.com/zaixiayesheng/MekaJadeUpgradesFixed/releases"><img src="https://img.shields.io/github/v/release/zaixiayesheng/MekaJadeUpgradesFixed?style=flat-square&label=Release&color=blue" alt="Release"></a>
  <img src="https://img.shields.io/badge/Minecraft-1.21.1-green?style=flat-square" alt="Minecraft 1.21.1">
  <img src="https://img.shields.io/badge/NeoForge-21.1-orange?style=flat-square" alt="NeoForge 21.1">
  <img src="https://img.shields.io/badge/Jade-required-purple?style=flat-square" alt="Jade">
  <img src="https://img.shields.io/badge/Mekanism-10.7.15%2B-yellow?style=flat-square" alt="Mekanism">
  <img src="https://img.shields.io/badge/License-MIT-blue?style=flat-square" alt="MIT License">
  <a href="https://github.com/zaixiayesheng/MekaJadeUpgradesFixed/issues"><img src="https://img.shields.io/badge/Issues-GitHub-black?style=flat-square" alt="Issues"></a>
</p>

<p align="center"><a href="#-这是什么">📖 这是什么</a> · <a href="#-与原版的区别">🔧 与原版的区别</a> · <a href="#-安装">📦 安装</a> · <a href="#-配置">⚙️ 配置</a> · <a href="#-构建">🛠️ 构建</a> · <a href="#-更新日志">📜 更新日志</a> · <a href="#-许可证与署名">⚖️ 许可证与署名</a> · <a href="#-反馈">🐛 反馈</a></p>

## 📖 这是什么

用 Jade 看向通用机械的机器时，在提示框里显示这台机器已装上的升级图标和数量。本分支是 **MekaJadeUpgrades**（作者 DevDyna）的修复分支，**非官方**，与原版作者无关、也未获其背书。

| | |
|---|---|
| 原版模组 | **MekaJadeUpgrades (Mekanism addon)**，作者 **DevDyna** |
| CurseForge | https://www.curseforge.com/minecraft/mc-mods/mekajadeupgrades |
| GitHub | https://github.com/DevDyna/MekaJadeUpgrades |
| 原版协议 | **MIT License，Copyright (c) 2024-2030 DevDyna** |
| 本分支协议 | MIT；原作者的版权声明保留在 [LICENSE](./LICENSE) 里，并附上分支作者的版权行 |
| 本分支源码 | https://github.com/zaixiayesheng/MekaJadeUpgradesFixed |

## 🔧 与原版的区别

改动的只有下面这几处，图标、升级支持、工程结构等其余部分都来自原版：

- **提示框溢出**：原版给每个图标加固定横向偏移来定位，但 Jade 的行宽只按元素自身尺寸计算、偏移不计入宽度，于是绘制位置跑到面板外面——升级种类一多（装了通用机械扩展、增强解放之后）就出框。现在改成**按宽度自动换行**、横向不再加偏移，任何分辨率、任何 GUI 缩放下都不会溢出。
- **图标与数字对齐**：保留竖直方向的 1~2 像素微调，图标和数量文字在同一水平线上。
- **版本适配**：直接识别合并版 **Mekanism: Empowered Unleashed**（新 id `mekanism_empowered_unleashed`），旧 id `mekanism_empowered` 也一并保留。
- **同时装了原版时自动停用原版**：配置项 `disableMekaJadeUpgrades`（默认开启），避免升级那一行显示两遍。
- **只保留 Jade 提示框部分**：本分支只包含原版里负责 Jade 提示框的代码，其余原版代码未包含。

## 📦 安装

| 文件 | 说明 |
|---|---|
| `mekajadeupgrades_fixed-*.jar` | 本模组 |
| Jade | 必需 |
| Mekanism 1.21.1（10.7.15 及以上） | 必需 |
| 通用机械：扩展 | 可选，装了就会显示它的升级图标 |
| Mekanism: Empowered Unleashed | 可选，装了就会显示强化升级图标 |

- 平台：Minecraft 1.21.1 / NeoForge 21.1.x
- 一般不需要再装原版 MekaJadeUpgrades：两个模组都会在提示框里加同一行升级。万一两个都装了，本模组会自动停用原版（用 mixin 掐掉它的提示框和数据入口，只取消、不改别的，不会崩），由本模组接管显示，并在日志里说明。该行为由配置 `disableMekaJadeUpgrades` 控制，默认开启。

## ⚙️ 配置

配置文件：`config/mekajadeupgrades_fixed-common.toml`

| 配置项 | 默认 | 说明 |
|---|---|---|
| `disableMekaJadeUpgrades` | true | 与原版 MekaJadeUpgrades 同时安装时停用原版，由本模组接管；关掉则两者都生效（会显示两行升级） |

## 🛠️ 构建

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

## 📜 更新日志

### 1.0.1

- 原版以后改了方法名或签名时不再让游戏启动崩溃（两处 mixin 加 `require = 0`）
- 认不出的升级、或附属模组的类对不上时，不再把异常抛到渲染，退回占位图标并在日志里提示一次
- 停用原版 MekaJadeUpgrades 时会在日志里说明
- Jade 配置界面的插件名补了中文
- 把原版 MekaJadeUpgrades 声明为可选依赖

### 1.0.0

- 修复了 Jade 提示框溢出
- 适配了通用机械：增强·解放（Mekanism: Empowered Unleashed）
- 不要与 MekaJadeUpgrades 一同安装

## ⚖️ 许可证与署名

- 原版 [MekaJadeUpgrades](https://github.com/DevDyna/MekaJadeUpgrades)：MIT License，Copyright (c) 2024-2030 DevDyna
- 本分支：MIT License，Copyright (c) 2026 zaixiayesheng
- 完整协议文本见 [LICENSE](./LICENSE)，其中同时保留了原作者的版权声明
- 除上面「与原版的区别」里列出的改动之外，其余代码、图标与资源均来自原版

## 🐛 反馈

- 本分支的问题请提到 [GitHub Issues](https://github.com/zaixiayesheng/MekaJadeUpgradesFixed/issues)，附上 Minecraft 与 NeoForge 版本、Jade 版本，以及 `logs/latest.log`
- 原版模组本身的问题请找原作者 DevDyna
