<p align="center"><img src="logo.png" width="160" alt="MekaJade Upgrades Fixed"></p>

<h1 align="center">MekaJade Upgrades Fixed</h1>

<p align="center">Shows the upgrades installed in a Mekanism machine in the Jade tooltip.<br>
An <strong>unofficial</strong> fixed fork of <strong>MekaJadeUpgrades</strong> by DevDyna. Not affiliated with or endorsed by the original author.</p>

<p align="center"><a href="./README.md">中文</a> | English</p>

<p align="center">
  <a href="https://github.com/zaixiayesheng/MekaJadeUpgradesFixed/releases"><img src="https://img.shields.io/github/v/release/zaixiayesheng/MekaJadeUpgradesFixed?style=flat-square&label=Release&color=blue" alt="Release"></a>
  <img src="https://img.shields.io/badge/Minecraft-1.20.1-green?style=flat-square" alt="Minecraft 1.20.1">
  <img src="https://img.shields.io/badge/Forge-47.4-orange?style=flat-square" alt="Forge 47.4">
  <img src="https://img.shields.io/badge/Jade-required-purple?style=flat-square" alt="Jade">
  <img src="https://img.shields.io/badge/Mekanism-10.4.16%2B-yellow?style=flat-square" alt="Mekanism">
  <img src="https://img.shields.io/badge/License-MIT-blue?style=flat-square" alt="MIT License">
  <a href="https://github.com/zaixiayesheng/MekaJadeUpgradesFixed/issues"><img src="https://img.shields.io/badge/Issues-GitHub-black?style=flat-square" alt="Issues"></a>
</p>

<p align="center"><a href="#-what-is-this">📖 What is this</a> · <a href="#-differences-from-the-original">🔧 Differences from the original</a> · <a href="#-installation">📦 Installation</a> · <a href="#-config">⚙️ Config</a> · <a href="#-building">🛠️ Building</a> · <a href="#-changelog">📜 Changelog</a> · <a href="#-license-and-credits">⚖️ License and credits</a> · <a href="#-feedback">🐛 Feedback</a></p>

## 📖 What is this

Shows the upgrades installed in a Mekanism machine in the Jade tooltip. This fork is a fixed fork of **MekaJadeUpgrades** by DevDyna. It is **unofficial**, not affiliated with or endorsed by the original author.

This README covers the **1.20.1 / Forge 47** branch; the 1.21.1 / NeoForge implementation lives in the `1.21.1` branch of the repository.

| | |
|---|---|
| Original mod | **MekaJadeUpgrades (Mekanism addon)** by **DevDyna** |
| Original CurseForge | https://www.curseforge.com/minecraft/mc-mods/mekajadeupgrades |
| Original GitHub | https://github.com/DevDyna/MekaJadeUpgrades |
| Original license | **MIT License, Copyright (c) 2024-2030 DevDyna** |
| This fork's license | MIT; the original copyright notice is kept in [LICENSE](./LICENSE) next to the fork author's line |
| This fork's source | https://github.com/zaixiayesheng/MekaJadeUpgradesFixed |
| This fork's platform | **Minecraft 1.20.1 / Forge 47.4** (JDK 17, Java 17 bytecode) |

## 🔧 Differences from the original

Only the points below were changed — icons, upgrade support and project layout all come from the original mod:

- **Tooltip overflow**: the original positioned every icon with a fixed horizontal offset, but Jade only counts element sizes when measuring a line — the offset is not part of the width, so the draw position landed past the panel edge. With enough upgrade types installed (Mekanism Extras, Empowered Unleashed) the row ran out of the box. The fork wraps rows **by measured width** and applies no horizontal offset, so nothing overflows at any resolution or GUI scale.
- **Icon/count alignment**: the small vertical offsets are kept, so icons and their count labels sit on the same line.
- **Version support**: detects the merged **Mekanism: Empowered Unleashed** (new id `mekanism_empowered_unleashed`) directly, while still accepting the legacy `mekanism_empowered` id. This 1.20.1 branch is also adapted to the Mekanism 10.4 and Jade 11 APIs: upgrade names are read via `Upgrade#getRawName()`, the gas upgrade is `Upgrade.GAS`, `IElement.Align` uses `values()`, and the addon's package name is `com.jerry.mekanism_extras`.
- **Disables the original automatically** when both mods are installed (config `disableMekaJadeUpgrades`, default true), so the upgrade row is never drawn twice.
- **Slimmed down**: this fork only contains the Jade tooltip part of the original mod; the rest of the original's code is not included.

## 📦 Installation

| File | Notes |
|---|---|
| `mekajadeupgrades_fixed-1.20.1-*.jar` | This mod |
| Jade (1.20.1 build, 11.x) | required |
| Mekanism 1.20.1 (10.4.16+) | required |
| Mekanism Extras (`mekanism_extras` 1.5.0+) | optional, shows its upgrade icons too |
| Mekanism: Empowered Unleashed (20.1-1.0.0+) | optional, shows the empowered upgrade icons |

- Platform: Minecraft 1.20.1 / Forge 47.4 (47.x)
- You do not normally need the original MekaJadeUpgrades alongside this fork: both mods add the same upgrade row to the tooltip. If both are present, this mod disables the original (a mixin cancels its tooltip and server-data entry points — cancel only, nothing else, so it cannot crash) and takes over the row, saying so in the log. Controlled by the `disableMekaJadeUpgrades` config option, on by default.

## ⚙️ Config

File: `config/mekajadeupgrades_fixed-common.toml`

| Option | Default | Description |
|---|---|---|
| `disableMekaJadeUpgrades` | true | Disable the original MekaJadeUpgrades when both are installed, letting this mod take over; turn it off to let both run (two upgrade rows will show) |

## 🛠️ Building

JDK 17 required.

1. Put the compile-time jars into `libs/` in the project root (they are the compile dependencies of this mod and are not redistributed with this repository):
   - Jade (`jade-1.20.1-forge-*.jar`)
   - Mekanism (`mekanism-10.4.*.jar` — 1.20.1 Mekanism jars carry no game version in the file name)
   - Mekanism Generators (`mekanism-generators-10.4.*.jar`)
   - Mekanism Extras (`mekanism_extras-1.20.1-*.jar`)
   - Kotlin for Forge (`kotlinforforge-4.12.0-all.jar`, a hard dependency of Empowered Unleashed)
   - Mekanism: Empowered Unleashed (`MekanismEmpoweredUnleashed-1.20.1-*.jar`)

    The mapping from file names to Maven coordinates lives in the `localDeps` table of `settings.gradle`; edit that table when you change versions.
2. Run:

```
gradlew build
```

`settings.gradle` first copies `libs/` into `libs-maven/` (a local Maven repository): the release jars for 1.20.1 use SRG names while dev and runtime use Mojmap names, so they must be declared as module coordinates (`modImplementation 'group:name:version'`) for ModDevGradle to remap them. A plain `implementation fileTree('libs')` throws `NoSuchMethodError` at runtime.

The jar is written to `build/libs/`. To run a server smoke test: `gradlew runServer`.

## 📜 Changelog

### 1.20.1-1.0.0

- First release for 1.20.1 / Forge 47: ports every feature and fix from the 1.21.1 branch
- The tooltip wraps by measured width, so upgrade icons no longer overflow the panel
- Icons and count labels are aligned
- Detects Mekanism: Empowered Unleashed (`mekanism_empowered_unleashed`) as well as the legacy `mekanism_empowered` id
- Disables the original MekaJadeUpgrades automatically when both are installed (config `disableMekaJadeUpgrades`)
- Both mixins use `require = 0`: if the original changes a method name or signature later it simply stops applying instead of crashing the game at startup
- Upgrades it cannot resolve, or addon classes that no longer line up, fall back to the placeholder icon and log once
- The plugin name in the Jade config screen supports Chinese

## ⚖️ License and credits

- Original [MekaJadeUpgrades](https://github.com/DevDyna/MekaJadeUpgrades): MIT License, Copyright (c) 2024-2030 DevDyna
- This fork: MIT License, Copyright (c) 2026 zaixiayesheng
- The full license text is in [LICENSE](./LICENSE); it keeps the original copyright notice alongside the fork author's line
- Apart from the changes listed under "Differences from the original", all code, icons and assets come from the original mod

## 🐛 Feedback

- Report problems with this fork in [GitHub Issues](https://github.com/zaixiayesheng/MekaJadeUpgradesFixed/issues), with your Minecraft, Forge, Jade and Mekanism versions
- For problems with the original mod itself, ask its author DevDyna
