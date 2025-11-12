// InstanceGame.java
import java.util.*;

public class InstanceGame {
    private Scanner in;
    private Player player;
    private final int WIDTH = 5; // ~20 rooms (5x4)
    private final int HEIGHT = 4;
    private boolean[][] explored;
    private int exploredCount = 0;
    private final int totalRooms = WIDTH * HEIGHT;
    private Random rnd = new Random();

    // Campaign progression: start at stage 1, final stage = 3 (final boss -> elixir)
    private int campaignStage = 1;
    private final int MAX_CAMPAIGN_STAGE = 3;

    // track cleared regions by boss defeat (not strictly required but kept)
    private boolean[] clearedRegions = new boolean[5];

    // Default constructor (normal play)
    public InstanceGame(Scanner in) {
        this(in, false);
    }

    // Overloaded constructor: if devMode==true, create a high-powered player for testing
    public InstanceGame(Scanner in, boolean devMode) {
        this.in = in;
        this.explored = new boolean[HEIGHT][WIDTH];

        // ensure databases know current stage
        ItemDatabase.setStage(campaignStage);
        EnemyDatabase.setStage(campaignStage);

        if (!devMode) {
            this.player = new Player("Hero", 30, 5, 4);
            // give starter items
            safeAddWeaponToInventory("iron_sword");
            safeAddArmorToInventory("leather_armor");
            safeAddConsumableToInventory("hp_potion");
            safeAddRelicToInventory("relic_plus_ap");
            // equip first if present
            safeEquipFirstWeapon();
            safeEquipFirstArmor();
        } else {
            // Developer mode: maxed stats and lots of gear/items, but avoid unknown IDs causing NPE
            this.player = new Player("DevHero", 999, 999, 10);
            safeAddWeaponToInventory("fire_staff");
            safeAddWeaponToInventory("iron_sword");
            safeAddWeaponToInventory("iron_sword"); // duplicate so there's a high-index weapon too
            safeAddArmorToInventory("chain_armor");
            safeAddArmorToInventory("leather_armor");
            safeAddConsumableToInventory("hp_potion");
            safeAddConsumableToInventory("hp_potion");
            safeAddRelicToInventory("relic_plus_ap");
            safeAddRelicToInventory("relic_crit");
            safeEquipFirstWeapon();
            safeEquipFirstArmor();
            safeEquipFirstRelic();
            // ensure HP is full
            player.heal(0); // no-op but consistent
        }

        // start in center-like position and mark explored
        int sx = WIDTH / 2;
        int sy = HEIGHT / 2;
        player.setPosition(sx, sy);
        if (!explored[sy][sx]) { explored[sy][sx] = true; exploredCount++; }
    }

    /* -------------------------
       Helpers for safe adding (avoid NPE if ID not found)
       ------------------------- */
    private void safeAddWeaponToInventory(String id) {
        try {
            Weapon w = ItemDatabase.createWeapon(id);
            if (w != null) player.getInventory().add(w);
        } catch (Exception ignored) {}
    }
    private void safeAddArmorToInventory(String id) {
        try {
            Armor a = ItemDatabase.createArmor(id);
            if (a != null) player.getInventory().add(a);
        } catch (Exception ignored) {}
    }
    private void safeAddRelicToInventory(String id) {
        try {
            Relic r = ItemDatabase.createRelic(id);
            if (r != null) player.getInventory().add(r);
        } catch (Exception ignored) {}
    }
    private void safeAddConsumableToInventory(String id) {
        try {
            Consumable c = ItemDatabase.createConsumable(id);
            if (c != null) player.getInventory().add(c);
        } catch (Exception ignored) {}
    }
    private void safeEquipFirstWeapon() {
        for (Item it : player.getInventory()) if (it instanceof Weapon) { player.equipWeapon((Weapon)it); break; }
    }
    private void safeEquipFirstArmor() {
        for (Item it : player.getInventory()) if (it instanceof Armor) { player.equipArmor((Armor)it); break; }
    }
    private void safeEquipFirstRelic() {
        for (Item it : player.getInventory()) if (it instanceof Relic) { player.setEquippedRelic((Relic)it); break; }
    }

    public void start() {
        gameLoop();
    }

    private void gameLoop() {
        boolean running = true;
        while (running) {
            clearScreen();
            renderMap();
            System.out.println("Campaign Stage: " + campaignStage + "/" + MAX_CAMPAIGN_STAGE);
            System.out.println("HP: " + player.getHp() + "/" + player.getMaxHp() + "  AP: " + player.getAp() + "  Weapon: " + (player.getWeapon()!=null?player.getWeapon().getName():"None"));
            System.out.println("Inventory: " + player.getInventory().size() + " items. (type 'i' to inspect)");
            System.out.print("Move (w/a/s/d) or (i)nventory or (q)uit run: ");
            String cmd = in.nextLine().trim().toLowerCase();
            if (cmd.equals("q")) {
                System.out.println("You abandoned your run.");
                pause();
                break;
            } else if (cmd.equals("i")) {
                inspectInventory();
                continue;
            } else if (cmd.equals("w")||cmd.equals("a")||cmd.equals("s")||cmd.equals("d")) {
                boolean newlyExplored = move(cmd);
                // after move: check boss chance first
                boolean bossTriggered = false;
                int currentRegion = getRegionAt(player.getX(), player.getY());
                if (newlyExplored) {
                    if (exploredCount >= totalRooms) {
                        // last room explored -> guaranteed boss for this campaign stage
                        clearScreen();
                        System.out.println("As this is the last unexplored room in this area, a powerful presence fills the air...");
                        Enemy boss = EnemyDatabase.createEnemyForStage("stage_boss", campaignStage);
                        System.out.println("BOSS ENCOUNTER! " + boss.getName() + " appears!");
                        bossTriggered = true;
                        boolean survived = combat(Arrays.asList(boss), true, currentRegion);
                        if (!survived) { System.out.println("You were slain by the boss."); pause(); break; }
                    } else if (rnd.nextDouble() < 0.05) {
                        // 5% chance on any newly explored room
                        clearScreen();
                        Enemy boss = EnemyDatabase.createEnemyForStage("stage_boss", campaignStage);
                        System.out.println("A chilling wind... Boss appears: " + boss.getName());
                        bossTriggered = true;
                        boolean survived = combat(Arrays.asList(boss), true, currentRegion);
                        if (!survived) { System.out.println("You were slain by the boss."); pause(); break; }
                    }
                }
                if (bossTriggered) continue; // boss handled this room

                // regular encounters / loot
                if (rnd.nextDouble() < 0.5) {
                    clearScreen();
                    Enemy e = EnemyDatabase.createRandomEnemyForRegion(currentRegion);
                    System.out.println("Encounter! A " + e.getName() + " appears!");
                    boolean survived = combat(Arrays.asList(e), false, currentRegion);
                    if (!survived) {
                        System.out.println("You died. Run ends.");
                        pause();
                        break; // exit game loop to return to main menu
                    }
                } else if (rnd.nextDouble() < 0.3) {
                    clearScreen();
                    Item it = ItemDatabase.createRandomLootForRegion(currentRegion);
                    System.out.println("You found: " + it.getName() + " - " + it.getDescription());
                    player.getInventory().add(it);
                    pause();
                }
            } else {
                System.out.println("Unknown command.");
                pause();
            }
        }
    }

    private int getRegionAt(int x, int y) {
        int halfW = WIDTH/2;
        int halfH = HEIGHT/2;
        if (x < halfW && y < halfH) return 1;
        if (x >= halfW && y < halfH) return 2;
        if (x < halfW && y >= halfH) return 3;
        return 4;
    }

    private void renderMap() {
        System.out.println("Map Legend: [ ] = unexplored  , [E] = explored  , [P] = you\n");
        for (int r = 0; r < HEIGHT; r++) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < WIDTH; c++) {
                // Player marker has priority
                if (player.getX() == c && player.getY() == r) {
                    line.append("[P]");
                } else if (explored[r][c]) {
                    line.append("[E]");
                } else {
                    line.append("[ ]");
                }
                if (c < WIDTH-1) line.append("-");
            }
            System.out.println(line.toString());
        }
        System.out.println("Explored rooms: " + exploredCount + "/" + totalRooms + "\n");
    }

    private boolean move(String dir) {
        int nx = player.getX();
        int ny = player.getY();
        switch (dir) {
            case "w": ny -= 1; break;
            case "s": ny += 1; break;
            case "a": nx -= 1; break;
            case "d": nx += 1; break;
        }
        if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT) {
            System.out.println("You can't move further in that direction.");
            pause();
            return false;
        }
        boolean newlyExplored = false;
        if (!explored[ny][nx]) {
            explored[ny][nx] = true;
            exploredCount++;
            newlyExplored = true;
            clearScreen();
            System.out.println("You discovered a new room! (" + exploredCount + "/" + totalRooms + ")");
        } else {
            clearScreen();
            System.out.println("You moved to (" + nx + "," + ny + ") - already visited.");
        }
        player.setPosition(nx, ny);
        pause();
        return newlyExplored;
    }

    private void inspectInventory() {
        while (true) {
            clearScreen();
            System.out.println("--- Inventory ---");
            for (int i = 0; i < player.getInventory().size(); i++) {
                Item it = player.getInventory().get(i);
                System.out.println(i+1 + ") " + it.getName() + " - " + it.getDescription());
            }
            System.out.println("e) Equip weapon/armor, u) Use consumable, d) Delete item, b) back");
            System.out.print("Choice: ");
            String c = in.nextLine().trim().toLowerCase();
            if (c.equals("b")) break;
            if (c.equals("e")) {
                System.out.print("Enter item number to equip: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= player.getInventory().size()) { System.out.println("Invalid"); pause(); continue; }
                    Item sel = player.getInventory().get(idx);
                    if (sel instanceof Weapon) {
                        player.equipWeapon((Weapon)sel);
                        System.out.println("Equipped " + sel.getName());
                    } else if (sel instanceof Armor) {
                        player.equipArmor((Armor)sel);
                        System.out.println("Equipped " + sel.getName());
                    } else if (sel instanceof Relic) {
                        player.setEquippedRelic((Relic)sel);
                        System.out.println("Equipped relic " + sel.getName());
                    } else {
                        System.out.println("Cannot equip that item.");
                    }
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
                pause();
            } else if (c.equals("u")) {
                System.out.print("Enter item number to use: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= player.getInventory().size()) { System.out.println("Invalid"); pause(); continue; }
                    Item sel = player.getInventory().get(idx);
                    if (sel instanceof Consumable) {
                        ((Consumable)sel).use(player);
                        player.getInventory().remove(idx);
                        System.out.println("Used " + sel.getName());
                    } else {
                        System.out.println("That item is not usable.");
                    }
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
                pause();
            } else if (c.equals("d")) {
                System.out.print("Enter item number to DELETE (permanent): ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= player.getInventory().size()) { System.out.println("Invalid"); pause(); continue; }
                    Item sel = player.getInventory().get(idx);
                    System.out.print("Confirm delete " + sel.getName() + " ? (y/n): ");
                    String conf = in.nextLine().trim().toLowerCase();
                    if (conf.equals("y")) {
                        player.getInventory().remove(idx);
                        System.out.println("Item deleted.");
                    } else {
                        System.out.println("Cancelled.");
                    }
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
                pause();
            } else {
                System.out.println("Unknown command.");
                pause();
            }
        }
    }

    /**
     * Main combat routine.
     * @param enemies list of enemies in this encounter
     * @param isBoss whether this is a boss encounter
     * @param region the region index where this combat takes place (1..4)
     * @return true if player survived, false if player died
     */
    private boolean combat(List<Enemy> enemies, boolean isBoss, int region) {
        System.out.println("Combat start!");
        // reset AP
        player.resetAp();
        for (Enemy e : enemies) e.resetAp();

        while (player.isAlive() && enemies.stream().anyMatch(Enemy::isAlive)) {
            // build turn order by speed including all alive entities
            List<TurnActor> actors = new ArrayList<>();
            actors.add(new TurnActor(player));
            for (Enemy e : enemies) if (e.isAlive()) actors.add(new TurnActor(e));
            actors.sort(Comparator.comparingInt(TurnActor::getSpeed).reversed());
            for (TurnActor ta : actors) {
                if (!player.isAlive() || enemies.stream().noneMatch(Enemy::isAlive)) break;
                if (ta.isPlayer()) {
                    playerTurn(enemies);
                } else {
                    Enemy e = ta.getEnemy();
                    if (e.isAlive()) e.takeTurn(player);
                }
            }
            // at the end of round reset AP
            player.resetAp();
            for (Enemy e : enemies) e.resetAp();
        }

        boolean survived = player.isAlive();
        if (survived) {
            System.out.println("You won the fight!");
            // loot
            for (Enemy e : enemies) if (!e.isAlive()) {
                if (rnd.nextDouble() < 0.5) {
                    Item it = ItemDatabase.createRandomLootForRegion(region);
                    System.out.println("Enemy dropped: " + it.getName());
                    player.getInventory().add(it);
                }
            }

            // If this was a boss encounter and player survived -> progress campaign stage
            if (isBoss) {
                System.out.println("\n--- BOSS DEFEATED ---");
                if (campaignStage < MAX_CAMPAIGN_STAGE) {
                    campaignStage++;
                    // prepare next stage: reset map and inform DBs
                    advanceToNextStage();
                } else {
                    // final stage boss defeated -> grant elixir (if defined)
                    Relic elixir = ItemDatabase.createRelic("elixir_of_life");
                    if (elixir != null) {
                        player.getInventory().add(elixir);
                        int addHp = elixir.getAddMaxHp();
                        if (addHp > 0) {
                            player.addMaxHp(addHp);
                            System.out.println("Your maximum HP increases by " + addHp + "!");
                        }
                        System.out.println("You obtained: " + elixir.getName() + " - " + elixir.getDescription());
                    } else {
                        System.out.println("You found a mysterious elixir (database entry missing).");
                    }
                    System.out.println("\nCongratulations — you have completed the campaign!");
                }
            }

            pause();
        } else {
            pause();
        }
        return survived;
    }

    // move player to next stage: reset map, set DBs to new stage, small lore message
    private void advanceToNextStage() {
        clearScreen();
        System.out.println("The land shifts as your victory echoes. You feel the world rearrange...");
        // reset map exploration for new stage
        for (int r = 0; r < HEIGHT; r++) for (int c = 0; c < WIDTH; c++) explored[r][c] = false;
        exploredCount = 0;
        // reposition player to center and mark explored
        int sx = WIDTH / 2;
        int sy = HEIGHT / 2;
        player.setPosition(sx, sy);
        explored[sy][sx] = true;
        exploredCount++;

        // inform databases of stage change so that new items/enemies appear
        ItemDatabase.setStage(campaignStage);
        EnemyDatabase.setStage(campaignStage);

        // stage lore
        switch (campaignStage) {
            case 2:
                System.out.println("You are drawn to Region 2 — new dangers and treasures await.");
                break;
            case 3:
                System.out.println("The path to the final stronghold opens. This is the last challenge.");
                break;
            default:
                System.out.println("You feel the world change.");
        }
        pause();
    }

    private void playerTurn(List<Enemy> enemies) {
        while (player.getAp() > 0 && player.isAlive() && enemies.stream().anyMatch(Enemy::isAlive)) {
            clearScreen();
            System.out.println("--- Player Turn --- AP: " + player.getAp());
            System.out.println("1) Attack (2 AP)  2) Use Consumable (1 AP)  3) Inspect enemies  4) End Turn");
            System.out.print("Choice: ");
            String c = in.nextLine().trim();
            if (c.equals("1")) {
                if (player.getAp() < 2) { System.out.println("Not enough AP."); pause(); continue; }
                // pick enemy
                List<Enemy> alive = new ArrayList<>();
                for (Enemy e : enemies) if (e.isAlive()) alive.add(e);
                if (alive.isEmpty()) break;
                for (int i = 0; i < alive.size(); i++) System.out.println((i+1)+") " + alive.get(i).getName() + " HP:" + alive.get(i).getHp());
                System.out.print("Target: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= alive.size()) { System.out.println("Invalid"); pause(); continue; }
                    Enemy target = alive.get(idx);
                    int dmg = player.attack();
                    System.out.println("You hit " + target.getName() + " for " + dmg + " damage.");
                    target.takeDamage(dmg);
                    player.spendAp(2);
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
                pause();
            } else if (c.equals("2")) {
                if (player.getAp() < 1) { System.out.println("Not enough AP."); pause(); continue; }
                // use consumable
                List<Consumable> cons = player.getConsumables();
                if (cons.isEmpty()) { System.out.println("No consumables."); pause(); continue; }
                for (int i = 0; i < cons.size(); i++) System.out.println((i+1)+") " + cons.get(i).getName() + " - " + cons.get(i).getDescription());
                System.out.print("Use which: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= cons.size()) { System.out.println("Invalid"); pause(); continue; }
                    Consumable citem = cons.get(idx);
                    citem.use(player);
                    player.getInventory().remove(citem);
                    player.spendAp(1);
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
                pause();
            } else if (c.equals("3")) {
                for (Enemy e : enemies) System.out.println(e.getName() + " HP:" + e.getHp());
                System.out.println("Press Enter to continue...");
                in.nextLine();
            } else if (c.equals("4")) {
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
}
