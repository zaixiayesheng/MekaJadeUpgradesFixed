# MekaJade Upgrades Fixed

> Shows the upgrades installed in a Mekanism machine in the Jade tooltip.
>
> A fixed fork of **MekaJadeUpgrades** by DevDyna.
>
> 中文说明：[README.md](./README.md)

---

## What was fixed

- **Tooltip overflow**: the original positioned every icon with a fixed horizontal offset, but Jade only counts element sizes when measuring a line — the offset is not part of the width, so icons were drawn past the panel edge. With enough upgrade types installed (Mekanism Extras, Empowered Unleashed) the row ran out of the box. The fork wraps rows **by measured width** and applies no horizontal offset, so nothing overflows at any resolution or GUI scale.
- **Icon/count alignment**: the small vertical offsets are kept so icons and their counts sit on the same line.
- **Version support**: detects the merged **Mekanism: Empowered Unleashed** (new id `mekanism_empowered_unleashed`) directly, while still accepting the legacy `mekanism_empowered` id — the compatibility shim inside the merged jar is no longer needed.

## Requirements

| File | Notes |
|---|---|
| `mekajadeupgrades_fixed-1.0.0.jar` | This mod |
| Jade | required |
| Mekanism 1.21.1 (10.7.19+) | required |
| Mekanism Extras | optional, shows its upgrade icons too |
| Mekanism: Empowered Unleashed | optional, shows the empowered upgrade icons |

## Changelog

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
   - Jade (`Jade-1.21.1-NeoForge-*.jar`)
   - Mekanism (`Mekanism-1.21.1-*.jar`)
   - Mekanism Extras (`mekanism_extras-1.21.1-*.jar`)
   - Mekanism: Empowered Unleashed (`MekanismEmpoweredUnleashed-*.jar`)
2. Run:

```
gradlew build
```

The jar is written to `build/libs/`.

## License and credits

- Based on [MekaJadeUpgrades](https://github.com/DevDyna/MekaJadeUpgrades) 1.3 (MIT, Copyright © DevDyna)
- Fork changes: Copyright © 2026 zaixiayesheng, MIT License
