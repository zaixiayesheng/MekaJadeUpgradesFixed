<p align="center"><img src="logo.png" width="160" alt="MekaJade Upgrades Fixed"></p>

<h1 align="center">MekaJade Upgrades Fixed</h1>

<p align="center">Lists the upgrades installed in a Mekanism machine in the Jade tooltip, with an icon and a count for each.</p>

<p align="center"><a href="./README.md">中文</a> | English</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21.1-green.svg?style=flat-square" alt="Minecraft 1.21.1">
  <img src="https://img.shields.io/badge/NeoForge-21.1+-orange.svg?style=flat-square" alt="NeoForge 21.1">
  <img src="https://img.shields.io/badge/Jade-required-purple.svg?style=flat-square" alt="Jade">
  <img src="https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square" alt="MIT">
</p>

---

## What it is

There is no way to tell what a Mekanism machine has installed without opening its upgrade screen. This mod hooks into Jade: point at a machine and the installed upgrades appear in the tooltip as icons with counts, wrapping to the tooltip width instead of running off the panel.

It is an **unofficial** fixed fork of **MekaJadeUpgrades** by DevDyna, not affiliated with or endorsed by the original author. The changes are one rendering path plus a few robustness fixes; everything else comes from the original.

| | |
|---|---|
| Original mod | MekaJadeUpgrades by DevDyna |
| Original licence | MIT, Copyright (c) 2024-2030 DevDyna |
| This fork | MIT, Copyright (c) 2026 zaixiayesheng |

## Install

| File | Required | Notes |
|---|---|---|
| `mekajadeupgrades_fixed-21.1-1.0.1.jar` | yes | The mod |
| Jade | yes | Provides the tooltip |
| Mekanism 1.21.1 (10.7.15 or newer) | yes | Source of the upgrade icons |
| Mekanism Extras | no | Shows its upgrades when present |
| Mekanism: Empowered Unleashed | no | Shows the empowered upgrades when present |

- Platform: Minecraft 1.21.1 / NeoForge 21.1.x, Java 21.
- Client side required, server side optional (the tooltip is drawn client-side).
- **Do not install the original MekaJadeUpgrades alongside it**: both add the same tooltip line. If you do, this mod disables the original's line automatically (`disableMekaJadeUpgrades`, on by default) and says so in the log.

## What the tooltip shows

- The machine's **upgrade icons**, each with its installed count in the corner.
- Only upgrades the machine actually supports and actually has; absent upgrades take no space.
- Long lists wrap to the tooltip width instead of overflowing the panel.
- Beyond vanilla, it knows Mekanism Extras upgrades and the Empowered Speed / Empowered Energy upgrades from Mekanism: Empowered Unleashed.

## Differences from the original

| Change | Detail |
|---|---|
| Overflow becomes wrapping | The original positions each icon with a fixed horizontal offset, but Jade only measures an element's own size and ignores the offset, so many upgrades draw outside the panel. This fork wraps by available width and drops the horizontal offset |
| Icon and count alignment | Keeps the 1-2 pixel vertical nudge so icons and numbers sit on the same line |
| Merged mod id | Recognises `mekanism_empowered_unleashed` and still accepts the old `mekanism_empowered` |
| Original disabled | When the original MekaJadeUpgrades is installed, its tooltip is disabled so the upgrade line does not appear twice |
| Robustness | Both mixins use `require = 0`, so a renamed method in the original no longer crashes startup; unknown upgrades fall back to a placeholder icon and log once |
| Tooltip only | This fork contains only the code responsible for the Jade tooltip |

## Config

`config/mekajadeupgrades_fixed-common.toml`, restart required.

| Option | Default | Purpose |
|---|---|---|
| `disableMekaJadeUpgrades` | true | Disables the original MekaJadeUpgrades when both are installed; turn it off to show both lines |

## FAQ

- **No upgrade line in the tooltip**: check the machine really has upgrades installed, then that Jade is present and the Mekanism version matches.
- **Placeholder icon instead of an upgrade**: that upgrade comes from an add-on this build does not know; it is logged once and does not affect the game.
- **Two lines when the original is installed**: keep `disableMekaJadeUpgrades` on (default), or remove the original.
- **Client only works?**: Yes, the tooltip is drawn client-side; on a server you see what your own client computed.

## Compatibility

- Tested with Jade, Mekanism, Mekanism Extras and Mekanism: Empowered Unleashed.
- Detection of the original MekaJadeUpgrades disables its tooltip; nothing changes when it is absent.
- The mod only reads machine state: it does not write saves or change machine behaviour.

## For developers

### Implementation

| Site | Behaviour |
|---|---|
| Jade plugin (`mekajadeupgrades_fixed`) | Registers a tooltip element whose content comes from the machine's upgrade component |
| Rendering | Lays icons out row by row within the available width, no horizontal offset, wraps on overflow |
| Icon lookup | Resolves the icon by upgrade id; unknown ids get a placeholder, exceptions never reach the render thread |
| Disabling the original | Mixins remove the original plugin's tooltip registration and data entry (`require = 0`, missing targets are ignored) |

### Build

JDK 21. Put the compile dependencies into `libs/` in the project root (not shipped with the repo):

- Jade (`jade-1.21.1-neoforge-*.jar`)
- Mekanism (`mekanism-1.21.1-*.jar`)
- Mekanism Extras (`mekanism_extras-1.21.1-*.jar`)
- Mekanism: Empowered Unleashed (`MekanismEmpoweredUnleashed-*.jar`)

Then `gradlew build`; output in `build/libs/`.

## Changelog

### 1.0.1

- Both mixins now use `require = 0`, so a renamed method or signature in the original no longer crashes startup.
- Unknown upgrades, or add-on classes that no longer match, no longer throw into the render thread: the icon falls back to a placeholder and the problem is logged once.
- Disabling the original MekaJadeUpgrades is reported in the log.
- Added Chinese names to the Jade plugin entry.
- The original MekaJadeUpgrades is declared as an optional dependency.

<details>
<summary><b>1.0.0</b></summary>

- Fixed the Jade tooltip overflow: the icon list now wraps by width.
- Added support for Mekanism: Empowered Unleashed.
- The original MekaJadeUpgrades is disabled automatically when both are installed.

</details>

## Licence and credits

- Original [MekaJadeUpgrades](https://github.com/DevDyna/MekaJadeUpgrades): MIT, Copyright (c) 2024-2030 DevDyna
- This fork: MIT, Copyright (c) 2026 zaixiayesheng
- Full text in [LICENSE](./LICENSE), which keeps the original copyright notice
- Apart from the changes listed above, all code, icons and assets come from the original

## Feedback

- Report problems with this fork at [GitHub Issues](https://github.com/zaixiayesheng/MekaJadeUpgradesFixed/issues), including your Minecraft and NeoForge version, the Jade version and `logs/latest.log`
- Problems with the original mod go to DevDyna
