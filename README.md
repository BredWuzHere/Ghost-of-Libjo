Ghost-of-Libjo
 text-based roguelike RPG focused on tactical combat, status effects, & emergent multi-enemy encounters.

*Explore Batangas City , collect relics, craft builds, and survive procedurally tense encounters.*


[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=java&logoColor=white)](https://adoptopenjdk.net/) [![Turn-based RPG](https://img.shields.io/badge/Genre-Turn-based%20RPG-blue?logo=gamepad&logoColor=white)](https://en.wikipedia.org/wiki/Role-playing_video_game)




About
Ghost-of-Libjo is a compact, code-first RPG built in Java with emphasis on modular combat systems, readable text-driven immersion, and emergent difficulty scaling through multi-enemy encounters and adaptive AI.

This repository contains the core gameplay systems (Player, Enemy, Moves, Items/Relics, Status Effects), combat loop/HUD hooks, and region-based encounter spawners.



Features
•	Turn-based tactical combat with Action Points (AP) and multi-AP moves.
•	Rich **Status Effect** system (stacking DOTs, stuns, slows, jolt/electric effects, armor/crit modifiers).
•	**Moves** system for weapons and enemies: each move has AP cost, effects, and callbacks.
•	**Item / Inventory Containers**: containerized inventories (Weapons, Armor, Consumables, Relics, Misc) with boundary checks.
•	**Relics & Armor** that grant passive bonuses (crit chance, resistances, AP changes).
•	**HUD integration** showing live HP/AP/speed, equipped gear, and active status effects during combat.
•	**Multi-enemy encounters** with probabilistic spawn rules and caps.
•	Region-aware enemy AI that adapts move selection and difficulty across regions.
•	Text-driven immersion: dynamic lore lines, enemy speech, and formatted room lore with readable pacing.



Architecture & Containers
Ghost-of-Libjo code favors small, well-defined classes and container abstractions:

•	`ItemContainer` — generic container used by `Player` for categories: Weapons, Armor, Consumables, Relics, Misc.

•	`StatusEffect` — central class enumerating effect kinds and implementing tick/expire logic.
•	`Move` — encapsulates a named action with AP cost and an effect callback that applies damage/statuses.
•	`Enemy` / `SimpleEnemy` / `Boss` — enemy hierarchy with evasion, stats, status handling, and AI heuristics.
•	`EnemyDatabase` — authoritatively constructs enemy instances used by spawn rules and region definitions.

**Containers & safety**
•	Factory wrappers (e.g., `safeAddWeapon(...)`) centralize DB calls and enforce container boundaries.



Gameplay Systems
Status Effects
•	Multiple kinds: JOLT, POISON, STUN, SLOW, SLIPPERY (evasion-related), CRIT_DOWN, ARMOR_DOWN, etc.
•	Each effect has values (int/double), duration, tick behavior, and expiration callbacks.

Moves & Consumables
•	Moves consume AP and can apply damage and/or status effects.
•	Consumables call `use(player)` to apply immediate healing and/or status effects.

Combat Loop & HUD
•	Turn order supports multi-AP actions and per-actor status processing at start-of-turn and end-of-turn ticks.
•	Live HUD hooks render current player stats and status effects during the player's turn.

Enemy AI & Encounters
•	Per-region base chance to spawn extra enemies (Region 1 = 35%, Region 2 = 45%, Region 3 = 60%).
•	Chance decays by half after each extra spawn (base → base/2 → base/4 ...).
•	Max 5 enemies per encounter.
•	AI chooses moves by scoring; later regions bias toward stronger moves.



Running the Game (dev)
These are example steps — adjust to your build system (Gradle / Maven / plain `javac` + `java`).

2.	1. Build
./gradlew build
or
mvn package

2. Run (example)
java -jar build/libs/ghost-of-libjo.jar

3. Play
•	Starts in a text terminal; follow prompts to explore, fight, and manage inventory.



Testing & Verification
•	Manual smoke tests were used for inventory add/remove and container boundary checks.
•	Unit tests are recommended for: StatusEffect ticks, Move.perform mechanics, Enemy.isHit/evasion math, and spawn probability decay.







Development Updates

**11/11/2025 (Bred & vltairee-jpg)**
•	Kickoff, architecture & containers
•	Created the high-level plan for the combat refactor and wrote acceptance criteria for HUD, status effects, and multi-enemy encounters.
•	Implemented Item / Inventory Containers: `ItemContainer` and refactored `Player` inventory to use containers for Weapons, Armor, Consumables, Relics, and misc Items.
•	Added safe factory wrappers (`safeAddWeapon`, etc.) to centralize DB calls.
•	Introduced core scaffolding classes:
  - `StatusEffect` skeleton (kinds, tick, expire).
  - `Move` class skeleton (name, AP cost, effect callback).
  - `ActionContainer` concept to group move/item actions.
•	Cleaned up `Player` getters needed by HUD (armor, relic, status list).
•	Added basic hooks to `InstanceGame` where the HUD, combat loop, and spawn code will plug in.
•	Tests / verification: Manual smoke tests for inventory add/remove and container boundaries.

**11/12/2025 (Bred & Liuwat- jpg martinez)**
•	Make systems dynamic — status effects, moves, spawn rules
•	Turned skeletons into working systems:
  - Implemented `StatusEffect` logic (int/double values, tick, expiration) and `processStatusEffectsStartTurn()` on `Player`.
  - Implemented `Consumable.use(player)` to apply status effects and/or instant heals.
  - Implemented `Move.perform(...)` wiring so weapons and enemies can run moves that consume AP and apply damage/statuses.
•	Combat plumbing:
  - Refactored `combat()` and the per-actor turn order to support multi-AP actions, moves, and status ticks.
  - Added live HUD hooks to display player HP/AP/speed, equipped gear, and status effects in real time during the player turn.
•	Enemy AI & encounters:
  - Implemented per-region multi-enemy spawn logic: Base chance to spawn a second enemy — Region 1 = 35%, Region 2 = 45%, Region 3 = 60%.
  - After each extra spawn the chance is halved (decay: base → base/2 → base/4 …).
  - Enforce max 5 enemies per encounter.
  - Early AI heuristic: enemy chooses moves by scoring (basic) — later regions bias toward stronger moves.
•	Text & immersion:
  - Added `Text.java` entries: dynamic lore lines and placeholder enemy speech lines.
  - Ensure room lore prints with a pause so players can read it.
•	Playtest: Verified multiple enemy spawn distribution roughly matches probabilities; trimmed lists to cap at 5.

**11/13/2025 — (Bred & Aaronshikii)**
•	Major System Overhaul (Text System and Item Data Fix)
•	Combat & Core Systems
  - Completely refactored the entire combat system for smoother turn handling and clearer flow.
  - Added Status Effects that dynamically affect both player and enemies (e.g., burn, armor down, crit debuffs, etc.).
  - Introduced Player HUD during combat showing live stats, equipped gear, and all active status effects in real time.
  - Added Moves system for weapons and enemies — each move has unique AP costs, damage, and effects.
  - Implemented Action Containers for better organization of items, moves, and status handling.
•	Enemy AI Improvements
  - Added smarter AI logic per region — higher regions = tougher AI with reduced chance to perform weak or random moves.
  - Enemies now adapt their actions and use status-based or high-damage moves more often in later regions.
•	Encounter System
  - Added multi-enemy encounters:
    - Region 1: 35% chance to spawn 2 enemies
    - Region 2: 45% chance
    - Region 3: 60% chance
    - After each successful extra spawn, the next chance is halved (up to 5 enemies total).
•	Text & Immersion
  - Expanded `Text.java` with enemy dialogue, battle speech, and dynamic lore generation per region.
  - Added lore prompts when entering new rooms (now pauses for readability).
  - Improved pacing — added screen pauses and slight delays between major combat or exploration events.
•	Items & Effects
  - Items and monster moves can now apply status effects (e.g., poison from a consumable, stun from an attack).
  - Relics and armors grant passive bonuses like crit chance, resistances, or AP boosts.

**11/14/2025 — (Bred & Aaronshikii & Liuwat - jpg martinez)**
•	Refining Character Stats & Status Effect System
•	Expanded `StatusEffect.Kind`: Added new dynamic status effect types for robust combat:
  - `JOLT` — electric, chaining, or stunning effects.
  - `POISON` — stackable damage over time (DOT).
  - `STUN` — make a target skip their next turn.
  - `SLOW` — reduce a target's speed or Action Points (AP).
  - `SLIPPERY` — explicitly linked to providing Evasion bonuses/penalties.
•	Evasion Stat Implementation:
  - Introduced a new `evasion` stat (`double`) to the base `Enemy` class constructor and internal fields.
  - Implemented `isHit(accuracy)` check in the `Enemy` class to calculate miss chance based on base evasion plus any active `SLIPPERY` status effects.
  - Added logic to print "Enemy avoided the attack!" on a successful miss.
•	Enemy Core Logic:
  - Updated the base `Enemy` class to include methods for managing status effects (`addStatusEffect`, `hasStatusEffect`) and processing their effects at the start of a turn (`processStatusEffectsStartTurn()`).
  - Modified the `takeTurn()` method to check for death (from DOT) and `STUN` effects, skipping the turn if necessary.

**11/15/2025 — (Bred & vltairee-jpg)**
•	Database & Constructor Sync
•	Enemy Database Initialization:
  - Refactored the `EnemyDatabase.createEnemy(...)` function to include the new `evasion` stat (as the 8th parameter for `SimpleEnemy` and the 10th parameter for `Boss`) for all defined enemy and boss types.
  - Assigned unique evasion values to each enemy type (e.g., Rat has high evasion, Orc has low evasion).
•	Class Structure Synchronization:
  - Fixed compilation errors by updating the constructors for `SimpleEnemy.java` (8 parameters) and `Boss.java` (10 parameters) to correctly accept the new `evasion` stat and pass it to the `super()` call of the `Enemy` class.
•	Status Kind Cleanup:
  - Updated all enemy moves in `EnemyDatabase.createEnemy(...)` that used the generic `StatusEffect.Kind.GENERIC` to use the appropriate dedicated kinds (`STUN`, `JOLT`, `CRIT_DOWN`, `ARMOR_DOWN`) for clearer combat intent.


**11/30/25 - (Aaron & vltairee-jpg)**
- **`iron_sword`**: `Iron Sword` — A sturdy iron sword. (Weapon)
- **`fire_staff`**: `Fire Staff` — A staff imbued with flame; includes fire moves and a burn status effect. (Weapon)
- **`leather_armor`**: `Leather Armor` — Light protective leather. (Armor)
- **`chain_armor`**: `Chain Armor` — Better protection. (Armor)
- **`balat_ng_hipon`**: `Balat ng Hipon` — Matigas sa labas, lambot sa loob. (Armor)
- **`kalawang_armor`**: `Kalawang Armor` — Made from pure, authentic, Gulod rust. (Armor)
- **`construction_vest`**: `Construction Vest` — Safety first, kahit sa gulod battles. (Armor)
- **`ukayukay_jacket`**: `Ukay-Ukay Jacket` — Vintage jacket na amoy lumang aparador. (Armor)
- **`relic_plus_ap`**: `Relic of Swiftness` — +1 Max AP. (Relic)
- **`relic_crit`**: `Relic of Precision` — +10% Crit Chance. (Relic)
- **`extra_rice`**: `Extra Rice` — Restores 8 HP. (Consumable)
- **`hp_potion`**: `Health Potion` — Restores 10 HP. (Consumable)
- **`lumpia_roll`**: `Lumpia Roll` — Restores 5 HP. (Consumable)
- **`sinigang_soup`**: `Sinigang Soup` — Restores 15 HP. (Consumable)
- **`goto_bowl`**: `Goto Bowl` — Restores 12 HP. (Consumable)
- **`kape_3in1`**: `Kape 3-in-1` — Boosts speed temporarily. (Consumable / Buff)
- **`red_horse`**: `Red Horse` — Increase attack slightly. (Consumable / Buff)
- **`mentos_fresh`**: `Mentos Fresh` — Boosts evasion due to 'freshness'. (Consumable / Buff)
- **`pancit_canton`**: `Pancit Canton` — Increases energy/attack for a bit. (Consumable / Buff)
- **`elixir_of_life`**: `Elixir of Life` — Permanently increases Max HP by 50. (Relic)
- **`dev_sword`**: `Dev Sword` — A dev/test sword (very high stats). (Weapon)
- **`hipon_tentacle`**: `Hipon Tentacle` — A strange slippery tentacle. (Weapon)
- **`gulod_amulet`**: `Gulod Amulet` — Warm, throbbing with heat. Grants modest strength and resilience. (Relic)
- **`dechavez_claw`**: `DeChavez Claw` — Sharp and ledger-lined. (Weapon)
- **`mirage_shard`**: `Mirage Shard` — Shimmers with illusion. (Weapon)
- **`blade_of_hepatytis`**: `Blade of Hepatytis` — Penetration-focused blade with toxic moves. (Weapon)
- **`fishball_stick`**: `Fishball Stick Stabber` — Small but terrible. (Weapon)
- **`electricfan_blade`**: `Electric Fan Blade Toss` — Sharp fan blade. Hard to aim but deadly (has electric-themed move). (Weapon)

**11/15/2025 — (vltairee-jpg & Aaronshikii & Liuwat - jpg martinez)**
---------------------------
# CONCEPTUALIZED STORYLINE, ADDED AND IMPLEMENTED NEW FEATURES

- **Console UI colorization**: Added a central `Color.java` with ANSI codes and updated many game classes to print colorized messages (damage, healing, buffs, HUD, map legend) for improved readability in terminals.
- **Buff system (defense & attack)**: Implemented explicit buff kinds (`BUFF_DEFENSE`, `BUFF_ATTACK`) in `StatusEffect`, added `Player.addBuff(...)` and `Player.addAttackBuff(...)`, and applied/reverted buff effects inside `Entity` lifecycle so buffs take effect immediately and expire automatically.
- **Consumable overhaul**: Replaced `if` chains with an enhanced `switch` in `Consumable.use(...)`, enforced non-null `ConsumableType`, and made consumables apply defence/attack buffs for a random duration (2–3 turns) in addition to heals.
- **Per-turn HUD & cleared-turn flow**: Added `displayTurnStatus()` and integrated `clearScreen()` calls so the screen refreshes each turn and shows the player's current HUD cleanly between actions.
- **Map rendering polish**: `InstanceGame.renderMap()` now colorizes tokens: player (`[P]`) bright green, enemies (`[E]`) bright red, and unexplored tiles are light grey (bright black) for better contrast.
- **Status-effect lifecycle & processing**: `Entity.processStatusEffectsStartTurn()` was centralized to tick DOTs (POISON, BURN, JOLT), handle STUN/SLOW, and revert buffed stats when effects expire.
- **Combat & stacking behavior**: Buffs currently stack additively (multiple consumables increase the same stat); expiration correctly reverts only the amount each buff applied. Consider changing to a refresh-on-reapply model if desired.
- **Title experiments & UX choices**: Several ASCII title experiments were tried and then reverted to the simpler original at the user's request.

Notes:
- Many weapon items include predefined `Move` entries (e.g., `fire_staff` has `Fire Burst` and `Ember Slap` with a Burn effect).  
- Consumables and relics provide heals or passive bonuses; relics like `elixir_of_life` permanently add Max HP.  
- The in-game loot pool is stage-aware and pulls from these IDs via `ItemDatabase.createRandomLootForRegion(stage)`.

License
This project uses the MIT License — see `LICENSE` for details.



