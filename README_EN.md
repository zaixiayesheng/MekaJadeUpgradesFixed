# MekaJade Upgrades Fixed

> Shows the upgrades installed in a Mekanism machine in the Jade tooltip.
>
> An **unofficial** fixed fork of **MekaJadeUpgrades** by DevDyna. Not affiliated with or endorsed by the original author.
>
> 中文说明：[README.md](./README.md)

---

## Credit / based on

| | |
|---|---|
| Original mod | **MekaJadeUpgrades (Mekanism addon)** by **DevDyna** |
| CurseForge | https://www.curseforge.com/minecraft/mc-mods/mekajadeupgrades |
| GitHub | https://github.com/DevDyna/MekaJadeUpgrades |
| Original license | **MIT License, Copyright (c) 2024-2030 DevDyna** |
| This fork | MIT; the original copyright notice is kept in [LICENSE](./LICENSE) next to the fork author's line |
| Fork source | https://github.com/zaixiayesheng/MekaJadeUpgradesFixed |

## Differences from the original

Only the points below were changed — icons, upgrade support and project layout all come from the original mod:

- **Tooltip overflow**: the original positioned every icon with a fixed horizontal offset, but Jade only counts element sizes when measuring a line — the offset is not part of the width, so icons were drawn past the panel edge. With enough upgrade types installed (Mekanism Extras, Empowered Unleashed) the row ran out of the box. The fork wraps rows **by measured width** and applies no horizontal offset, so nothing overflows at any resolution or GUI scale.
- **Icon/count alignment**: the small vertical offsets are kept so icons and their counts sit on the same line.
- **Version support**: detects the merged **Mekanism: Empowered Unleashed** (new id `mekanism_empowered_unleashed`) directly, while still accepting the legacy `mekanism_empowered` id.
- **Disables the original automatically** when both mods are installed (config `disableMekaJadeUpgrades`, default true), so the upgrade row is never drawn twice.
- **Slimmed down**: this fork only contains the Jade tooltip part of the original mod plus the fixes above; the rest of the original's code is not included.

## Requirements

| File | Notes |
|---|---|
| `mekajadeupgrades_fixed-*.jar` | This mod |
| Jade | required |
| Mekanism 1.21.1 (10.7.19+) | required |
| Mekanism Extras | optional, shows its upgrade icons too |
| Mekanism: Empowered Unleashed | optional, shows the empowered upgrade icons |

## Changelog

### 1.0.1

- No longer crashes at startup when the original changes a method name or signature (both mixins now use `require = 0`)
- Upgrades it cannot resolve, or addon APIs that no longer line up, fall back to the placeholder icon and log once instead of throwing into the render path
- Disabling the original MekaJadeUpgrades is now stated in the log
- Added a Chinese name for the entry shown in the Jade config screen
- The original MekaJadeUpgrades is now declared as an optional dependency

### 1.0.0

- Fixed the Jade tooltip overflow
- Added support for Mekanism: Empowered Unleashed
- Do not install together with MekaJadeUpgrades

- Minecraft 1.21.1 / NeoForge 21.1.x
- Do not install together with the original MekaJadeUpgrades: both mods add the same upgrade row to the tooltip. If both are present, this mod **disables the original MekaJadeUpgrades** (a mixin cancels its tooltip and server-data entry points — cancel only, nothing else, so it cannot crash) and takes over the row, saying so in the log. Controlled by the `disableMekaJadeUpgrades` config option, on by default.

## Config

File: `config/mekajadeupgrades_fixed-common.toml`

| Option | Default | Description |
|---|---|---|
| `disableMekaJadeUpgrades` | true | Disable the original MekaJadeUpgrades when both are installed, letting this mod take over; turn it off to let both run (two upgrade rows will show) |

## Building

JDK 21 required.

1. Put the compile-time jars into `libs/` in the project root (they are not redistributed with this repository):
   - Jade (`jade-1.21.1-neoforge-*.jar`)
   - Mekanism (`mekanism-1.21.1-*.jar`)
   - Mekanism Extras (`mekanism_extras-1.21.1-*.jar`)
   - Mekanism: Empowered Unleashed (`MekanismEmpoweredUnleashed-*.jar`)
2. Run:

```
gradlew build
```

The jar is written to `build/libs/`.

## License and credits

- Original [MekaJadeUpgrades](https://github.com/DevDyna/MekaJadeUpgrades): MIT License, Copyright (c) 2024-2030 DevDyna
- This fork: MIT License, Copyright (c) 2026 zaixiayesheng
- The full license text is in [LICENSE](./LICENSE); it keeps the original copyright notice alongside the fork author's line
- Apart from the changes listed under "Differences from the original", all code, icons and assets come from the original mod
