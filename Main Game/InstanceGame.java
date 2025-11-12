import java.util.*;

public class InstanceGame {
    private final Scanner in;
    private Player player;
    private final int WIDTH = 5;
    private final int HEIGHT = 4;
    private boolean[][] explored;
    private int exploredCount = 0;
    private final int totalRooms = WIDTH * HEIGHT;
    private Random rnd = new Random();

    private int campaignStage = 1;
    private final int MAX_CAMPAIGN_STAGE = 3;

    public InstanceGame(Scanner in) { this(in, false); }

    public InstanceGame(Scanner in, boolean devMode) {
        this.in = in;
        this.explored = new boolean[HEIGHT][WIDTH];
        ItemDatabase.setStage(campaignStage);
        EnemyDatabase.setStage(campaignStage);

        if (!devMode) {
            this.player = new Player("Hero", 30, 5, 4);
            safeAddWeapon("iron_sword"); safeAddArmor("leather_armor"); safeAddConsumable("hp_potion"); safeAddRelic("relic_plus_ap");
            safeEquipFirstWeapon(); safeEquipFirstArmor();
        } else {
            this.player = new Player("DevHero", 999, 999, 20);
            safeAddWeapon("dev_sword"); safeAddWeapon("iron_sword"); safeAddArmor("chain_armor"); safeAddConsumable("hp_potion"); safeAddRelic("relic_crit");
            safeEquipFirstWeapon(); safeEquipFirstArmor(); safeEquipFirstRelic();
        }
        int sx = WIDTH/2, sy = HEIGHT/2;
        player.setPosition(sx, sy);
        explored[sy][sx] = true; exploredCount++;
    }

    private void safeAddWeapon(String id) { Weapon w = ItemDatabase.createWeapon(id); if (w!=null) player.getInventory().add(w); }
    private void safeAddArmor(String id) { Armor a = ItemDatabase.createArmor(id); if (a!=null) player.getInventory().add(a); }
    private void safeAddConsumable(String id) { Consumable c = ItemDatabase.createConsumable(id); if (c!=null) player.getInventory().add(c); }
    private void safeAddRelic(String id) { Relic r = ItemDatabase.createRelic(id); if (r!=null) player.getInventory().add(r); }
    private void safeEquipFirstWeapon() { for (Item it : player.getInventory()) if (it instanceof Weapon) { player.equipWeapon((Weapon)it); break; } }
    private void safeEquipFirstArmor() { for (Item it : player.getInventory()) if (it instanceof Armor) { player.equipArmor((Armor)it); break; } }
    private void safeEquipFirstRelic() { for (Item it : player.getInventory()) if (it instanceof Relic) { player.setEquippedRelic((Relic)it); break; } }

    public void start() { gameLoop(); }

    private void gameLoop() {
        boolean running = true;
        while (running) {
            clearScreen();
            renderMap();
            System.out.println("Campaign Stage: " + campaignStage + "/" + MAX_CAMPAIGN_STAGE);
            System.out.println("HP: " + player.getHp() + "/" + player.getMaxHp() +
                    "  AP: " + player.getAp() + "  Weapon: " + (player.getWeapon()!=null?player.getWeapon().getName():"None"));
            System.out.println("Inventory: " + player.getInventory().size() + " items. (type 'i' to inspect)");
            System.out.print("Move (w/a/s/d) or (i)nventory or (q)uit run: ");
            String cmd = in.nextLine().trim().toLowerCase();
            if (cmd.equals("q")) { System.out.println("You abandoned your run."); pause(); break; }
            else if (cmd.equals("i")) { inspectInventory(); continue; }
            else if (cmd.matches("[wasd]")) {
                boolean newly = move(cmd);
                boolean bossTriggered = false;
                int region = getRegionAt(player.getX(), player.getY());
                if (newly) {
                    // show a lore line sometimes when discovering new room
                    if (Text.roll(0.35)) {
                        System.out.println("\nLore: " + Text.randomRoomLoreForRegion(region));
                        // make sure the player can read lore before continuing
                        pause();
                    }
                    if (exploredCount >= totalRooms) {
                        clearScreen();
                        String intro = "As this is the last unexplored room in this area, a powerful presence fills the air...";
                        boolean survived = triggerBossSequence(region, intro);
                        if (!survived) { System.out.println("You were slain by the boss."); pause(); break; }
                        bossTriggered = true;
                    } else if (rnd.nextDouble() < 0.05) {
                        clearScreen();
                        String intro = "A chilling wind... something powerful stirs in the shadows.";
                        boolean survived = triggerBossSequence(region, intro);
                        if (!survived) { System.out.println("You were slain by the boss."); pause(); break; }
                        bossTriggered = true;
                    }
                } else {
                    // sometimes show small lore when entering already-explored room
                    if (Text.roll(0.15)) {
                        System.out.println("\nYou notice: " + Text.randomRoomLoreForRegion(region));
                        // ensure player sees the notice
                        pause();
                    }
                }
                if (bossTriggered) continue;

                // ENCOUNTER CHANCE: when encountering, decide how many enemies spawn (1..5) per your rules
                if (rnd.nextDouble() < 0.5) {
                    clearScreen();
                    clearScreen();

                    // --- Spawn logic start ---
                    // base chance for an additional (second) enemy depending on region:
                    double baseSecondChance;
                    switch (region) {
                        case 1: baseSecondChance = 0.35; break;
                        case 2: baseSecondChance = 0.50; break;
                        case 4: baseSecondChance = 0.60; break; // treat region 4 as "last region"
                        default: baseSecondChance = 0.35; break; // region 3 or others default to 35%
                    }

                    List<Enemy> enemies = new ArrayList<>();
                    // always spawn the initial enemy(s) from the DB
                    enemies.addAll(EnemyDatabase.createEnemiesForRegion(region));

                    // Try to spawn more enemies up to max 5.
                    // First chance (to go from whatever we have to adding a second group) uses baseSecondChance.
                    // After each successful extra spawn we halve the chance for the next spawn.
                    double chance = baseSecondChance;
                    // Keep trying to add groups while we haven't hit cap and roll succeeds.
                    while (enemies.size() < 5 && rnd.nextDouble() < chance) {
                        // add another group (the DB call returns one or more enemies); then enforce cap
                        enemies.addAll(EnemyDatabase.createEnemiesForRegion(region));
                        if (enemies.size() > 5) {
                            // trim extras so final size is at most 5
                            enemies = enemies.subList(0, 5);
                        }
                        // halve chance for next extra spawn
                        chance = chance / 2.0;
                    }
                    // --- Spawn logic end ---

                    System.out.println("Encounter! Enemies appear:");
                    for (Enemy en : enemies) {
                        System.out.println("- " + en.getName() + " (HP:" + en.getHp() + " AP:" + en.getAp() + ")");
                    }

                    // show a tiny animation + wait so the player can read the encounter before combat begins
                    animateDots("Preparing for combat", 3, 220);
                    pause();

                    boolean survived = combat(enemies, false, region);
                    if (!survived) { System.out.println("You died. Run ends."); pause(); break; }
                } else if (rnd.nextDouble() < 0.3) {
                    clearScreen();
                    Item it = ItemDatabase.createRandomLootForRegion(region);
                    System.out.println("You found: " + it.getName() + " - " + it.getDescription());
                    player.getInventory().add(it);
                    pause();
                }
            } else { System.out.println("Unknown command."); pause(); }
        }
    }

    /**
     * Handles boss sequence for the current campaignStage.
     * If this is the final stage, spawn DeChavez first, then Sales if DeChavez is defeated.
     * Returns true if the player survived the entire boss sequence, false if player died.
     */
    private boolean triggerBossSequence(int region, String introLine) {
        if (introLine != null) {
            System.out.println(introLine);
            // make sure player reads the intro
            pause();
        }

        if (campaignStage < MAX_CAMPAIGN_STAGE) {
            // regular single boss for non-final stages
            Enemy boss = EnemyDatabase.createStageBoss(campaignStage);
            System.out.println("BOSS ENCOUNTER! " + boss.getName() + " appears!");
            animateDots("The boss looms", 3, 200);
            pause();
            boolean survived = combat(Arrays.asList(boss), true, region);
            return survived;
        } else {
            // Final stage sequence: first DeChavez, then Sales (true boss)
            Enemy dechavez = EnemyDatabase.createEnemy("dechavez");
            System.out.println("BOSS ENCOUNTER! " + dechavez.getName() + " appears!");
            animateDots("Something huge is approaching", 3, 200);
            pause();
            boolean beatDechavez = combat(Arrays.asList(dechavez), true, region);
            if (!beatDechavez) {
                // player died to DeChavez
                return false;
            }

            // short cutscene / taunt between bosses
            System.out.println("\nAs DeChavez collapses, reality shimmers... something even worse approaches.");
            System.out.println(Text.randomCutsceneLine());
            pause();

            // spawn the true final boss: Sales (or swap if you want other order)
            Enemy sales = EnemyDatabase.createEnemy("sales");
            System.out.println("TRUE BOSS APPEARS! " + sales.getName() + " emerges!");
            animateDots("The true menace arrives", 3, 200);
            pause();
            boolean beatSales = combat(Arrays.asList(sales), true, region);
            if (!beatSales) {
                return false;
            }

            // player beat both; reward and end-run
            System.out.println("\nYou have defeated the True Boss! The Elixir of Life is yours.");
            player.getInventory().add(ItemDatabase.createRelic("elixir_of_life"));
            pause();
            return true;
        }
    }

    private int getRegionAt(int x, int y) {
        int halfW = WIDTH/2, halfH = HEIGHT/2;
        if (x < halfW && y < halfH) return 1;
        if (x >= halfW && y < halfH) return 2;
        if (x < halfW && y >= halfH) return 3;
        return 4;
    }

    private void renderMap() {
        System.out.println("Map Legend: [ ] = unexplored  , [E] = explored  , [P] = you\n");
        for (int r=0;r<HEIGHT;r++) {
            StringBuilder line = new StringBuilder();
            for (int c=0;c<WIDTH;c++) {
                if (player.getX()==c && player.getY()==r) line.append("[P]");
                else if (explored[r][c]) line.append("[E]");
                else line.append("[ ]");
                if (c<WIDTH-1) line.append("-");
            }
            System.out.println(line.toString());
        }
        System.out.println("Explored rooms: " + exploredCount + "/" + totalRooms + "\n");
    }

    private boolean move(String dir) {
        int nx = player.getX(), ny = player.getY();
        switch (dir) { case "w": ny--; break; case "s": ny++; break; case "a": nx--; break; case "d": nx++; break; }
        if (nx<0||nx>=WIDTH||ny<0||ny>=HEIGHT) { System.out.println("You can't move further in that direction."); pause(); return false; }
        boolean newly = false;
        if (!explored[ny][nx]) { explored[ny][nx]=true; exploredCount++; newly=true; clearScreen(); System.out.println("You discovered a new room! ("+exploredCount+"/"+totalRooms+")"); }
        else { clearScreen(); System.out.println("You moved to ("+nx+","+ny+") - already visited."); }
        player.setPosition(nx, ny); pause(); return newly;
    }

    private void inspectInventory() {
        while (true) {
            clearScreen();
            System.out.println("--- Inventory ---");
            for (int i=0;i<player.getInventory().size();i++) {
                Item it = player.getInventory().get(i);
                System.out.println((i+1) + ") " + it.getName() + " - " + it.getDescription());
            }
            System.out.println("e) Equip weapon/armor, u) Use consumable, d) Delete item, b) back");
            System.out.print("Choice: ");
            String c = in.nextLine().trim().toLowerCase();
            if (c.equals("b")) break;
            if (c.equals("e")) {
                System.out.print("Enter item number to equip: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim())-1;
                    if (idx<0||idx>=player.getInventory().size()) { System.out.println("Invalid"); pause(); continue; }
                    Item sel = player.getInventory().get(idx);
                    if (sel instanceof Weapon) { player.equipWeapon((Weapon)sel); System.out.println("Equipped " + sel.getName()); }
                    else if (sel instanceof Armor) { player.equipArmor((Armor)sel); System.out.println("Equipped " + sel.getName()); }
                    else if (sel instanceof Relic) { player.setEquippedRelic((Relic)sel); System.out.println("Equipped relic " + sel.getName()); }
                    else System.out.println("Cannot equip that item.");
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
                pause();
            } else if (c.equals("u")) {
                System.out.print("Enter item number to use: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim())-1;
                    if (idx<0||idx>=player.getInventory().size()) { System.out.println("Invalid"); pause(); continue; }
                    Item sel = player.getInventory().get(idx);
                    if (sel instanceof Consumable) { ((Consumable)sel).use(player); player.getInventory().remove(idx); System.out.println("Used " + sel.getName()); }
                    else System.out.println("That item is not usable.");
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
                pause();
            } else if (c.equals("d")) {
                System.out.print("Enter item number to DELETE (permanent): ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim())-1;
                    if (idx<0||idx>=player.getInventory().size()) { System.out.println("Invalid"); pause(); continue; }
                    Item sel = player.getInventory().get(idx);
                    System.out.print("Confirm delete " + sel.getName() + " ? (y/n): ");
                    String conf = in.nextLine().trim().toLowerCase();
                    if (conf.equals("y")) { player.getInventory().remove(idx); System.out.println("Item deleted."); } else System.out.println("Cancelled.");
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
                pause();
            } else System.out.println("Unknown command.");
        }
    }
    
    private boolean combat(List<Enemy> enemies, boolean isBoss, int region) {
        System.out.println("Combat start!");
        // give full AP at combat start
        player.resetAp(); for (Enemy e: enemies) e.resetAp();

        while (player.isAlive() && enemies.stream().anyMatch(Enemy::isAlive)) {
            // build turn order by speed including all alive entities
            List<TurnActor> actors = new ArrayList<>();
            actors.add(new TurnActor(player));
            for (Enemy e: enemies) if (e.isAlive()) actors.add(new TurnActor(e));
            actors.sort(Comparator.comparingInt(TurnActor::getSpeed).reversed());

            for (TurnActor ta: actors) {
                if (!player.isAlive() || enemies.stream().noneMatch(Enemy::isAlive)) break;
                if (ta.isPlayer()) {
                    // player's turn: playerTurn handles AP-consuming loop for player
                    playerTurn(enemies);
                } else {
                    Enemy e = ta.getEnemy();
                    if (e.isAlive()) {
                        System.out.println("\n--- Enemy Turn: " + e.getName() + " ---");
                        // Enemy will act using its AP inside takeTurn(...)
                        e.takeTurn(player);
                        // pause so the player can read enemy actions before the next actor
                        pause();
                    }
                }
            }

            // at the end of round reset AP for everyone
            player.resetAp();
            for (Enemy e : enemies) e.resetAp();
        }

        boolean survived = player.isAlive();
        if (survived) {
            System.out.println("You won the fight!");
            for (Enemy e : enemies) if (!e.isAlive()) {
                if (rnd.nextDouble() < 0.5) {
                    Item it = ItemDatabase.createRandomLootForRegion(region);
                    System.out.println("Enemy dropped: " + it.getName());
                    player.getInventory().add(it);
                }
            }
            if (isBoss) {
                System.out.println("\n--- BOSS DEFEATED ---");
                if (campaignStage < MAX_CAMPAIGN_STAGE) { campaignStage++; advanceToNextStage(); }
                else { System.out.println("You have cleared the final stage. Check inventory for the Elixir!"); }
            }
            pause();
        } else pause();
        return survived;
    }

    private void advanceToNextStage() {
        clearScreen();
        System.out.println("The land shifts as your victory echoes. You feel the world rearrange...");
        System.out.println("\n" + Text.randomCutsceneLine());
        for (int r=0;r<HEIGHT;r++) for (int c=0;c<WIDTH;c++) explored[r][c]=false;
        exploredCount=0; int sx=WIDTH/2, sy=HEIGHT/2; player.setPosition(sx, sy); explored[sy][sx]=true; exploredCount++;
        ItemDatabase.setStage(campaignStage); EnemyDatabase.setStage(campaignStage);
        switch (campaignStage) { case 2: System.out.println("You are drawn to Region 2 — new dangers and treasures await."); break; case 3: System.out.println("The path to the final stronghold opens. This is the last challenge."); break; default: System.out.println("You feel the world change."); break; }
        pause();
    }

    private void displayPlayerCombatHUD(List<Enemy> alive) {
        System.out.println("\n--- Player ---");
        System.out.println(player.getName() + "  HP: " + player.getHp() + "/" + player.getMaxHp() +
                "  AP: " + player.getAp() + "  SPD: " + player.getSpeed());
        System.out.println("Weapon: " + (player.getWeapon() != null ? player.getWeapon().getName() : "None")
                + "  Armor: " + player.getArmorRating()
                + "  Relic: " + (player.getEquippedRelic() != null ? player.getEquippedRelic().getName() : "None"));

        List<StatusEffect> se = player.getStatusEffects();
        if (se.isEmpty()) {
            System.out.println("Status Effects: (none)");
        } else {
            System.out.println("Status Effects:");
            for (StatusEffect s : se) {
                String vals = "";
                try {
                    if (s.getIntValue() != 0) vals += " int=" + s.getIntValue();
                } catch (Exception ex) { /* ignore if method not available */ }
                try {
                    if (Math.abs(s.getDblValue()) > 1e-9) vals += " dbl=" + s.getDblValue();
                } catch (Exception ex) { /* ignore if method not available */ }
                System.out.println("- " + s.getKind() + vals + (s.isExpired() ? " (expiring)" : ""));
            }
        }

        System.out.println("\nEnemies:");
        for (int i = 0; i < alive.size(); i++) {
            Enemy e = alive.get(i);
            System.out.println((i + 1) + ") " + e.getName() + "  HP: " + e.getHp() + "  AP: " + e.getAp());
        }
    }

    private void playerTurn(List<Enemy> enemies) {
        player.processStatusEffectsStartTurn();

        // Player may perform multiple actions while AP remains
        while (player.getAp() > 0 && player.isAlive() && enemies.stream().anyMatch(Enemy::isAlive)) {
            // Combat HUD: show player stats and enemies (refreshed at start of each loop iteration)
            List<Enemy> alive = new ArrayList<>();
            for (Enemy e : enemies) if (e.isAlive()) alive.add(e);

            displayPlayerCombatHUD(alive);

            System.out.println("\n--- Player Turn --- AP: " + player.getAp());
            System.out.println("\n1) Attack (choose weapon move)  2) Use Consumable (1 AP)  3) Inspect enemies  4) End Turn");
            System.out.print("Choice: ");
            String c = in.nextLine().trim();
            if (c.equals("1")) {
                Weapon w = player.getWeapon();
                if (w == null) { System.out.println("No weapon equipped."); pause(); continue; }
                List<Move> moves = w.getMoves();
                for (int i = 0; i < moves.size(); i++) System.out.println((i + 1) + ") " + moves.get(i).getName() + " (AP:" + moves.get(i).getApCost() + ") - " + moves.get(i).getDescription());
                System.out.print("Pick move number: ");
                try {
                    int mi = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (mi < 0 || mi >= moves.size()) { System.out.println("Invalid move."); pause(); continue; }
                    if (player.getAp() < moves.get(mi).getApCost()) { System.out.println("Not enough AP."); pause(); continue; }
                    // select target
                    if (alive.isEmpty()) break;
                    for (int i = 0; i < alive.size(); i++) System.out.println((i + 1) + ") " + alive.get(i).getName() + " HP:" + alive.get(i).getHp());
                    System.out.print("Target: ");
                    int ti = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (ti < 0 || ti >= alive.size()) { System.out.println("Invalid target."); pause(); continue; }
                    Enemy target = alive.get(ti);
                    w.performMove(mi, player, target);

                    // refresh HUD immediately after the move so player sees resulting status changes/damage
                    displayPlayerCombatHUD(alive);
                    // let the player read the result of their action
                    pause();

                } catch (NumberFormatException ex) { System.out.println("Invalid"); pause(); continue; }
                // after action, continue player loop (no full clear so enemy outputs remain)
            } else if (c.equals("2")) {
                if (player.getAp() < 1) { System.out.println("Not enough AP."); pause(); continue; }
                List<Consumable> cons = player.getConsumables();
                if (cons.isEmpty()) { System.out.println("No consumables."); pause(); continue; }
                for (int i = 0; i < cons.size(); i++) System.out.println((i + 1) + ") " + cons.get(i).getName() + " - " + cons.get(i).getDescription());
                System.out.print("Use which: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= cons.size()) { System.out.println("Invalid"); pause(); continue; }
                    Consumable citem = cons.get(idx);
                    citem.use(player);
                    player.getInventory().remove(citem);
                    player.spendAp(1);

                    // refresh HUD immediately after using consumable
                    List<Enemy> aliveAfter = new ArrayList<>();
                    for (Enemy e : enemies) if (e.isAlive()) aliveAfter.add(e);
                    displayPlayerCombatHUD(aliveAfter);
                    // give player time to read the effect
                    pause();

                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
            } else if (c.equals("3")) {
                // Detailed inspect: show moves description for each enemy
                System.out.println("\n--- Inspect Enemies ---");
                for (Enemy e : alive) {
                    System.out.println(e.getName() + "  HP: " + e.getHp() + "  AP: " + e.getAp());
                    System.out.println("  Moves: " + e.getMovesDescription());
                }
                System.out.println("\nPress Enter to continue...");
                in.nextLine();

                // after inspecting, refresh HUD so the player returns to an up-to-date view
                displayPlayerCombatHUD(alive);
                pause();

            } else if (c.equals("4")) {
                // End turn immediately: consume the remaining AP so enemy actors act now
                player.spendAp(player.getAp());
                break;
            } else {
                System.out.println("Unknown");
                pause();
            }
        }
    }

    // simple helper actor wrapper
    private static class TurnActor {
        private Player player;
        private Enemy enemy;
        TurnActor(Player p) { this.player = p; }
        TurnActor(Enemy e) { this.enemy = e; }
        boolean isPlayer() { return player != null; }
        int getSpeed() { return isPlayer() ? player.getSpeed() : enemy.getSpeed(); }
        Enemy getEnemy() { return enemy; }
    }

    // Clear screen helper; tries platform clear, falls back to newlines
    private void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    // small pause helper used to allow user to read messages before clearing
    private void pause() {
        System.out.println("\nPress Enter to continue...");
        in.nextLine();
    }

    /**
     * Small dot animation helper. Prints `msg` then prints `dots` dots spaced by `delayMs` milliseconds.
     * Non-fatal if sleep is interrupted.
     */
    private void animateDots(String msg, int dots, int delayMs) {
        System.out.print(msg);
        for (int i = 0; i < dots; i++) {
            System.out.print(".");
            try { Thread.sleep(delayMs); } catch (InterruptedException e) { /* ignore */ }
        }
        System.out.println();
    }
}
