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
            this.player = new Player("SOOAYAN", 30, 5, 4);
            safeAddWeapon("iron_sword"); safeAddArmor("leather_armor"); safeAddConsumable("hp_potion"); safeAddRelic("relic_plus_ap");
            safeEquipFirstWeapon(); safeEquipFirstArmor();
        } else {
            this.player = new Player("TESTBETLOG", 999, 10, 10);
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
            if (campaignStage == 1) System.out.println("Region: " + campaignStage + "/" + MAX_CAMPAIGN_STAGE + " (Libjo Central)");
            else if (campaignStage == 2) System.out.println("Region: " + campaignStage + "/" + MAX_CAMPAIGN_STAGE + " (Gulod Itaas)");
            else if (campaignStage == 3) System.out.println("Region: " + campaignStage + "/" + MAX_CAMPAIGN_STAGE + " (Batangas City Hall)");
            else System.out.println("No Region Game is broken lol");


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
                    if (Text.roll(0.35)) {
                        System.out.println("\nLore: " + Text.roomLoreForStage(campaignStage));
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
                    if (!bossTriggered && rnd.nextDouble() < 0.12) {
                        clearScreen();
                        NPC npc = NPCDatabase.createRandomNPCForRegion(region);
                        System.out.println("You encounter " + npc.getDisplayName() + " in this room.");
                        npc.interact(player, in);
                        pause();
                        continue;
                    }
                    if (!bossTriggered && rnd.nextDouble() < 0.10) {
                        clearScreen();
                        Item chest = ItemDatabase.createRandomLootForRegion(region);
                        if (chest != null) {
                            System.out.println("You found a chest! Inside: " + chest.getName() + " - " + chest.getDescription());
                            player.getInventory().add(chest);
                        } else {
                            System.out.println("You found a chest, but it was empty.");
                        }
                        pause();
                        continue;
                    }
                } else {
                    if (Text.roll(0.15)) {
                        System.out.println("\nYou notice: " + Text.roomLoreForStage(campaignStage));
                    }
                }
                if (bossTriggered) continue;
                if (rnd.nextDouble() < 0.5) {
                    clearScreen();
                    List<Enemy> enemies = EnemyDatabase.createEnemiesForRegion(region);
                    System.out.println("Encounter! Enemies appear:");
                    for (Enemy en : enemies) {
                        System.out.println("- " + en.getName() + " (HP:" + en.getHp() + " AP:" + en.getAp() + ")");
                    }
                    boolean survived = combat(enemies, false, region);
                    if (!survived) { System.out.println("You died. Run ends."); pause(); break; }
                } else if (rnd.nextDouble() < 0.3) {
                    clearScreen();
                    Item it = ItemDatabase.createRandomLootForRegion(region);
                    if (it != null) {
                        System.out.println("You found: " + it.getName() + " - " + it.getDescription());
                        player.getInventory().add(it);
                    } else {
                        System.out.println("You search the area, but find nothing of interest.");
                    }
                    pause();
                }
            } else { System.out.println("Unknown command."); pause(); }
        }
    }

    private boolean triggerBossSequence(int region, String introLine) {
        if (introLine != null) System.out.println(introLine);

        if (campaignStage < MAX_CAMPAIGN_STAGE) {
            Enemy boss = EnemyDatabase.createStageBoss(campaignStage);
            System.out.println("BOSS ENCOUNTER! " + boss.getName() + " appears!");
            boolean survived = combat(Arrays.asList(boss), true, region);
            return survived;
        } else {
            Enemy dechavez = EnemyDatabase.createEnemy("dechavez");
            System.out.println("BOSS ENCOUNTER! " + dechavez.getName() + " appears!");
            boolean beatDechavez = combat(Arrays.asList(dechavez), true, region);
            if (!beatDechavez) {
                return false;
            }
            System.out.println("\nAs DeChavez collapses, reality shimmers... something even worse approaches.");
            System.out.println(Text.regionTransitionForStage(campaignStage));
            pause();
            Enemy sales = EnemyDatabase.createEnemy("sales");
            System.out.println("TRUE BOSS APPEARS! " + sales.getName() + " emerges!");
            boolean beatSales = combat(Arrays.asList(sales), true, region);
            if (!beatSales) { return false; }
            System.out.println("\nYou have defeated the True Boss! The Elixir of Life is yours.");
            pause();
            return true;
        }
    }

    // --- NEW ANIMATED TRANSITIONS START ---

    private void advanceToNextStage() {
        clearScreen();
        
        if (campaignStage == 2) {
            playFirstTransition();
        } else if (campaignStage == 3) {
            playSecondTransition();
        } else {
            System.out.println("The world shifts around you...");
            pause();
        }

        // Reset map logic
        for (int r=0;r<HEIGHT;r++) for (int c=0;c<WIDTH;c++) explored[r][c]=false;
        exploredCount=0; 
        int sx=WIDTH/2, sy=HEIGHT/2; 
        player.setPosition(sx, sy); 
        explored[sy][sx]=true; 
        exploredCount++;
        
        ItemDatabase.setStage(campaignStage); 
        EnemyDatabase.setStage(campaignStage);
    }

    private void playFirstTransition() {
        String[] paragraphs = {
            "Between Libjo and Gulod, travelers pass through the quiet road of Dumantay — a place watched over by a spirit named Collmer.",
            "Long ago, Collmer was a rider who lost control of his motorcycle on a rainy night. He slipped on the sharp curve of Dumantay, and his life ended there. But his spirit never left.",
            "Now, on foggy evenings, drivers say a figure in a leather jacket appears by the roadside, guiding lost travelers or warning reckless ones to slow down. Engines flicker, headlights dim — and some swear they feel a gentle push when their vehicle stalls.",
            "Collmer protects Dumantay not out of anger, but redemption — forever guarding the road that once took his life."
        };

        System.out.println("\n=== ENROUTE FROM LIBJO TO GULOD ===\n");
        for (String p : paragraphs) {
            typeText(p, 25);
            System.out.println("\n"); // Spacing
            pause(); // Wait for user
        }
    }

    private void playSecondTransition() {
        String[] paragraphs = {
            "In the bustling roads of Lawas, where smoke hangs heavy and engines never rest, travelers speak of a figure who guides the lost — Kenny, the Guardian of Lawas.",
            "They say he was once a rider who knew every turn, every shortcut, and every danger hidden in the city’s haze. But one fateful day, the smog grew too thick, and Kenny disappeared into it — his body never found, only the echo of his roaring engine fading in the distance.",
            "Since then, those who pass through Lawas feel a strange calm when the air grows dense. Some see headlights appear beside them, guiding their way through the fog. Others say a voice whispers warnings before accidents happen.",
            "Ken’s soul never left Lawas. Surrounded by smoke and chaos, he now leads travelers safely through with his niche motorcycle — a silent guardian in a city that never sleeps."
        };

        System.out.println("\n=== ENROUTE FROM GULOD TO CITY HALL ===\n");
        for (String p : paragraphs) {
            typeText(p, 25);
            System.out.println("\n"); // Spacing
            pause(); // Wait for user
        }
    }

    private void typeText(String text, int delayMillis) {
        try {
            for (char c : text.toCharArray()) {
                System.out.print(c);
                Thread.sleep(delayMillis);
            }
        } catch (InterruptedException e) {
            System.out.print(text); // If interrupted, print remaining instantly
        }
    }

    // --- NEW ANIMATED TRANSITIONS END ---

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
                    if (sel instanceof Consumable) { ((Consumable)sel).use(player); player.getInventory().remove(sel); System.out.println("Used " + sel.getName()); }
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
        player.resetAp(); for (Enemy e: enemies) e.resetAp();

        while (player.isAlive() && enemies.stream().anyMatch(Enemy::isAlive)) {
            List<TurnActor> actors = new ArrayList<>();
            actors.add(new TurnActor(player));
            for (Enemy e: enemies) if (e.isAlive()) actors.add(new TurnActor(e));
            actors.sort(Comparator.comparingInt(TurnActor::getSpeed).reversed());

            for (TurnActor ta: actors) {
                if (!player.isAlive() || enemies.stream().noneMatch(Enemy::isAlive)) break;
                if (ta.isPlayer()) {
                    playerTurn(enemies);
                } else {
                    Enemy e = ta.getEnemy();
                    if (e.isAlive()) {
                        System.out.println("\n--- Enemy Turn: " + e.getName() + " ---");
                        e.takeTurn(player);
                        pause();
                        showPlayerHudDuringCombat();
                    }
                }
            }

            player.resetAp();
            for (Enemy e : enemies) e.resetAp();
        }

        boolean survived = player.isAlive();
        if (survived) {
            System.out.println("You won the fight!");
            for (Enemy e : enemies) if (!e.isAlive()) {
                if (rnd.nextDouble() < 0.5) {
                    Item it = ItemDatabase.createRandomLootForRegion(region);
                    if (it != null) {
                        System.out.println("Enemy dropped: " + it.getName());
                        player.getInventory().add(it);
                    }
                }
                String ud = EnemyDatabase.getUniqueDropFor(e.getId());
                if (ud != null) {
                    Item uitem = ItemDatabase.createItem(ud);
                    if (uitem!=null) {
                        System.out.println("Unique drop: " + uitem.getName());
                        player.getInventory().add(uitem);
                    }
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

    private void showPlayerHudDuringCombat() {
        System.out.println("\n--- Player HUD ---");
        System.out.println("HP: " + player.getHp() + "/" + player.getMaxHp() + "  AP: " + player.getAp());
        System.out.print("Statuses: ");
        List<StatusEffect> s = player.getActiveStatuses();
        if (s.isEmpty()) System.out.println("None");
        else {
            for (StatusEffect se : s) {
                System.out.print(se.getName() + "(" + se.getRemainingTurns() + ") ");
            }
            System.out.println();
        }
    }

    private void playerTurn(List<Enemy> enemies) {
        player.processStatusEffectsStartTurn();

        while (player.getAp()>0 && player.isAlive() && enemies.stream().anyMatch(Enemy::isAlive)) {
            System.out.println("\n--- Player Turn --- AP: " + player.getAp());
            System.out.println("Enemies:");
            List<Enemy> alive = new ArrayList<>();
            for (Enemy e : enemies) if (e.isAlive()) alive.add(e);
            for (int i = 0; i < alive.size(); i++) {
                Enemy e = alive.get(i);
                System.out.println((i+1) + ") " + e.getName() + "  HP: " + e.getHp() + "  AP: " + e.getAp() + "  DMG: " + e.getAttackValue() + "  ARM: " + e.getArmor());
            }

            System.out.println("\n1) Attack (choose weapon move)  2) Use Consumable (1 AP)  3) Inspect enemies  4) End Turn");
            System.out.print("Choice: ");
            String c = in.nextLine().trim();
            if (c.equals("1")) {
                Weapon w = player.getWeapon();
                if (w==null) { System.out.println("No weapon equipped."); pause(); continue; }
                List<Move> moves = w.getMoves();
                for (int i=0;i<moves.size();i++) System.out.println((i+1)+") " + moves.get(i).getName() + " (AP:" + moves.get(i).getApCost() + ") - " + moves.get(i).getDescription());
                System.out.print("Pick move number: ");
                try {
                    int mi = Integer.parseInt(in.nextLine().trim())-1;
                    if (mi<0||mi>=moves.size()) { System.out.println("Invalid move."); pause(); continue; }
                    if (player.getAp() < moves.get(mi).getApCost()) { System.out.println("Not enough AP."); pause(); continue; }
                    if (alive.isEmpty()) break;
                    for (int i=0;i<alive.size();i++) System.out.println((i+1)+") " + alive.get(i).getName() + " HP:" + alive.get(i).getHp());
                    System.out.print("Target: ");
                    int ti = Integer.parseInt(in.nextLine().trim())-1;
                    if (ti<0||ti>=alive.size()) { System.out.println("Invalid target."); pause(); continue; }
                    Enemy target = alive.get(ti);
                    w.performMove(mi, player, target);
                    player.spendAp(moves.get(mi).getApCost());
                } catch (NumberFormatException ex) { System.out.println("Invalid"); pause(); continue; }
            } else if (c.equals("2")) {
                if (player.getAp()<1) { System.out.println("Not enough AP."); pause(); continue; }
                List<Consumable> cons = player.getConsumables();
                if (cons.isEmpty()) { System.out.println("No consumables."); pause(); continue; }
                for (int i=0;i<cons.size();i++) System.out.println((i+1)+") " + cons.get(i).getName() + " - " + cons.get(i).getDescription());
                System.out.print("Use which: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim())-1;
                    if (idx<0||idx>=cons.size()) { System.out.println("Invalid"); pause(); continue; }
                    Consumable citem = cons.get(idx); citem.use(player); player.getInventory().remove(citem); player.spendAp(1);
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
            } else if (c.equals("3")) {
                System.out.println("\n--- Inspect Enemies ---");
                for (Enemy e : alive) {
                    System.out.println(e.getName() + "  HP: " + e.getHp() + "  AP: " + e.getAp() );
                    System.out.println("  Moves: " + e.getMovesDescription());
                }
                System.out.println("\nPress Enter to continue...");
                in.nextLine();
            } else if (c.equals("4")) {
                player.spendAp(player.getAp());
                break;
            } else {
                System.out.println("Unknown");
                pause();
            }
        }
    }

    private static class TurnActor {
        private Player player;
        private Enemy enemy;
        TurnActor(Player p) { this.player = p; }
        TurnActor(Enemy e) { this.enemy = e; }
        boolean isPlayer() { return player != null; }
        int getSpeed() { return isPlayer() ? player.getSpeed() : enemy.getSpeed(); }
        Enemy getEnemy() { return enemy; }
    }

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

    private void pause() {
        System.out.println("\nPress Enter to continue...");
        in.nextLine();
    }
}