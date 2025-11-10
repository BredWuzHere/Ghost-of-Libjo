import java.util.*;

public class InstanceGame {
    private Scanner in;
    private Player player;
    private final int WIDTH = 6;
    private final int HEIGHT = 6;
    private boolean[][] explored;
    private Random rnd = new Random();

    public InstanceGame(Scanner in) {
        this.in = in;
        this.explored = new boolean[HEIGHT][WIDTH];
        this.player = new Player("Hero", 30, 5, 4);
        // give starter items
        player.getInventory().add(ItemDatabase.createWeapon("iron_sword"));
        player.equipWeapon((Weapon)player.getInventory().get(0));
        player.getInventory().add(ItemDatabase.createArmor("leather_armor"));
        player.equipArmor((Armor)player.getInventory().get(1));
        player.getInventory().add(ItemDatabase.createConsumable("hp_potion"));
        player.getInventory().add(ItemDatabase.createRelic("relic_plus_ap"));
        // start in center
        player.setPosition(WIDTH/2, HEIGHT/2);
        explored[player.getY()][player.getX()] = true;
    }

    public void start() {
        System.out.println("Starting new run. Good luck!");
        gameLoop();
    }

    private void gameLoop() {
        boolean running = true;
        while (running) {
            renderMap();
            System.out.println("HP: " + player.getHp() + "/" + player.getMaxHp() + "  AP: " + player.getAp() + "  Weapon: " + (player.getWeapon()!=null?player.getWeapon().getName():"None"));
            System.out.println("Inventory: " + player.getInventory().size() + " items. (type 'i' to inspect)");
            System.out.print("Move (w/a/s/d) or (i)nventory or (q)uit run: ");
            String cmd = in.nextLine().trim().toLowerCase();
            if (cmd.equals("q")) {
                System.out.println("You abandoned your run.");
                break;
            } else if (cmd.equals("i")) {
                inspectInventory();
                continue;
            } else if (cmd.equals("w")||cmd.equals("a")||cmd.equals("s")||cmd.equals("d")) {
                move(cmd);
                // after move: check encounter
                if (rnd.nextDouble() < 0.5) {
                    Enemy e = EnemyDatabase.createRandomEnemyForRegion(getRegionAt(player.getX(), player.getY()));
                    System.out.println("Encounter! A " + e.getName() + " appears!");
                    boolean survived = combat(Arrays.asList(e));
                    if (!survived) {
                        System.out.println("You died. Run ends.");
                        break; // exit game loop to return to main menu
                    }
                } else if (rnd.nextDouble() < 0.3) {
                    Item it = ItemDatabase.createRandomLootForRegion(getRegionAt(player.getX(), player.getY()));
                    System.out.println("You found: " + it.getName() + " - " + it.getDescription());
                    player.getInventory().add(it);
                }
            } else {
                System.out.println("Unknown command.");
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
        System.out.println("Map Legend: [ ] unexplored, [P] you\n");
        for (int r = 0; r < HEIGHT; r++) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < WIDTH; c++) {
                if (player.getX() == c && player.getY() == r) {
                    line.append("[P]");
                } else if (explored[r][c]) {
                    line.append("[ ]");
                } else {
                    line.append(" . ");
                }
                if (c < WIDTH-1) line.append("-");
            }
            System.out.println(line.toString());
        }
        System.out.println();
    }

    private void move(String dir) {
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
            return;
        }
        player.setPosition(nx, ny);
        explored[ny][nx] = true;
        System.out.println("You moved to (" + nx + "," + ny + ") - Region " + getRegionAt(nx, ny));
    }

    private void inspectInventory() {
        while (true) {
            System.out.println("--- Inventory ---");
            for (int i = 0; i < player.getInventory().size(); i++) {
                Item it = player.getInventory().get(i);
                System.out.println(i+1 + ") " + it.getName() + " - " + it.getDescription());
            }
            System.out.println("e) Equip weapon/armor, u) Use consumable, b) back");
            System.out.print("Choice: ");
            String c = in.nextLine().trim().toLowerCase();
            if (c.equals("b")) break;
            if (c.equals("e")) {
                System.out.print("Enter item number to equip: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= player.getInventory().size()) { System.out.println("Invalid"); continue; }
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
            } else if (c.equals("u")) {
                System.out.print("Enter item number to use: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= player.getInventory().size()) { System.out.println("Invalid"); continue; }
                    Item sel = player.getInventory().get(idx);
                    if (sel instanceof Consumable) {
                        ((Consumable)sel).use(player);
                        player.getInventory().remove(idx);
                        System.out.println("Used " + sel.getName());
                    } else {
                        System.out.println("That item is not usable.");
                    }
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
            } else {
                System.out.println("Unknown command.");
            }
        }
    }

    private boolean combat(List<Enemy> enemies) {
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
                    Item it = ItemDatabase.createRandomLootForRegion(getRegionAt(player.getX(), player.getY()));
                    System.out.println("Enemy dropped: " + it.getName());
                    player.getInventory().add(it);
                }
            }
        }
        return survived;
    }

    private void playerTurn(List<Enemy> enemies) {
        while (player.getAp() > 0 && player.isAlive() && enemies.stream().anyMatch(Enemy::isAlive)) {
            System.out.println("--- Player Turn --- AP: " + player.getAp());
            System.out.println("1) Attack (2 AP)  2) Use Consumable (1 AP)  3) Inspect enemies  4) End Turn");
            System.out.print("Choice: ");
            String c = in.nextLine().trim();
            if (c.equals("1")) {
                if (player.getAp() < 2) { System.out.println("Not enough AP."); continue; }
                // pick enemy
                List<Enemy> alive = new ArrayList<>();
                for (Enemy e : enemies) if (e.isAlive()) alive.add(e);
                if (alive.isEmpty()) break;
                for (int i = 0; i < alive.size(); i++) System.out.println((i+1)+") " + alive.get(i).getName() + " HP:" + alive.get(i).getHp());
                System.out.print("Target: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= alive.size()) { System.out.println("Invalid"); continue; }
                    Enemy target = alive.get(idx);
                    int dmg = player.attack();
                    System.out.println("You hit " + target.getName() + " for " + dmg + " damage.");
                    target.takeDamage(dmg);
                    player.spendAp(2);
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
            } else if (c.equals("2")) {
                if (player.getAp() < 1) { System.out.println("Not enough AP."); continue; }
                // use consumable
                List<Consumable> cons = player.getConsumables();
                if (cons.isEmpty()) { System.out.println("No consumables."); continue; }
                for (int i = 0; i < cons.size(); i++) System.out.println((i+1)+") " + cons.get(i).getName() + " - " + cons.get(i).getDescription());
                System.out.print("Use which: ");
                try {
                    int idx = Integer.parseInt(in.nextLine().trim()) - 1;
                    if (idx < 0 || idx >= cons.size()) { System.out.println("Invalid"); continue; }
                    Consumable citem = cons.get(idx);
                    citem.use(player);
                    player.getInventory().remove(citem);
                    player.spendAp(1);
                } catch (NumberFormatException ex) { System.out.println("Invalid"); }
            } else if (c.equals("3")) {
                for (Enemy e : enemies) System.out.println(e.getName() + " HP:" + e.getHp());
            } else if (c.equals("4")) {
                break;
            } else {
                System.out.println("Unknown");
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
}
