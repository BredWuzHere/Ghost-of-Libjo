# Ghost-of-Libjo

11/11/2025 (Bred)

Kickoff, architecture & containers

Created the high-level plan for the combat refactor and wrote acceptance criteria for HUD, status effects, and multi-enemy encounters.

Implemented Item / Inventory Containers:

ItemContainer and refactored Player inventory to use containers for Weapons, Armor, Consumables, Relics, and misc Items.

Added safe factory wrappers (safeAddWeapon, etc.) to centralize DB calls.

Introduced core scaffolding classes:

StatusEffect skeleton (kinds, tick, expire).

Move class skeleton (name, AP cost, effect callback).

ActionContainer concept to group move/item actions.

Small driver & refactors:

Cleaned up Player getters needed by HUD (armor, relic, status list).

Added basic hooks to InstanceGame where the HUD, combat loop, and spawn code will plug in.

Tests / verification:

Manual smoke tests for inventory add/remove and container boundaries.



11/12/2025 (Bred)

Make systems dynamic — status effects, moves, spawn rules

Turned skeletons into working systems:

Implemented StatusEffect logic (int/double values, tick, expiration) and processStatusEffectsStartTurn() on Player.

Implemented Consumable.use(player) to apply status effects and/or instant heals.

Implemented Move.perform(...) wiring so weapons and enemies can run moves that consume AP and apply damage/statuses.

Combat plumbing:

Refactored combat() and the per-actor turn order to support multi-AP actions, moves, and status ticks.

Added live HUD hooks to display player HP/AP/speed, equipped gear, and status effects in real time during the player turn.

Enemy AI & encounters:

Implemented per-region multi-enemy spawn logic:

Base chance to spawn a second enemy — region mapping: Region 1 = 35%, Region 2 = 45%, Region 3 = 60%.

After each extra spawn the chance is halved (decay: base → base/2 → base/4 …).

Enforce max 5 enemies per encounter.

Early AI heuristic: enemy chooses moves by scoring (basic) — later regions bias toward stronger moves (lower chance to perform bad/random moves).

Text & immersion:

Added Text.java entries: dynamic lore lines and placeholder enemy speech lines.

Ensure room lore prints with a pause so players can read it.

Playtest:

Verified multiple enemy spawn distribution roughly matches probabilities; trimmed lists to cap at 5.






Update 11/13/2025 — (Bred)
to fix (text system and item data missing!!)
**Major System Overhaul**

** Combat & Core Systems**

* Completely **refactored the entire combat system** for smoother turn handling and clearer flow.
* Added **Status Effects** that dynamically affect both player and enemies (e.g., burn, armor down, crit debuffs, etc.).
* Introduced **Player HUD** during combat showing live stats, equipped gear, and all active status effects in real time.
* Added **Moves system** for weapons and enemies — each move has unique AP costs, damage, and effects.
* Implemented **Action Containers** for better organization of items, moves, and status handling.

 Enemy AI Improvements

* Added smarter **AI logic per region** — higher regions = tougher AI with reduced chance to perform weak or random moves.
* Enemies now adapt their actions and use status-based or high-damage moves more often in later regions.

 Encounter System

* Added **multi-enemy encounters**:

  * Region 1: 35% chance to spawn 2 enemies
  * Region 2: 45% chance
  * Region 3: 60% chance
  * After each successful extra spawn, the next chance is **halved** (up to **5 enemies total**).

 Text & Immersion

* Expanded **Text.java** with enemy dialogue, battle speech, and dynamic lore generation per region.
* Added lore prompts when entering new rooms (now pauses for readability).
* Improved pacing — added screen pauses and slight delays between major combat or exploration events.

 Items & Effects

 Items and monster moves can now apply status effects (e.g., poison from a consumable, stun from an attack).
 Relics and armors grant passive bonuses like crit chance, resistances, or AP boosts.



