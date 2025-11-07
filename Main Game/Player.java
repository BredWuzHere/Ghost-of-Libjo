import java.util.ArrayList;

public class Player {
    private String name;

    // Stats
    private int maxHealth = 100;
    private int currentHealth = 100;
    private int attackPower = 10;
    private int defense = 5;

    // Inventory
    private Inventory inventory = new Inventory();

    // Checkpoint
    private String currentArea = "Libjoe";
    private String checkpoint = "Libjoe";

    // Player name
    public Player() {
        this.name = "Banlag";
    }
    
    // Indicates that Player is Dead
    public boolean isDead() {
        return currentHealth <= 0;
    }

    // Healing
    public void heal(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }
    
    // Indicates that Player is Full Health
    public void fullHealth() {
        currentHealth = maxHealth;
    }

    // Collecting Items
    public void addItem(Item item) {
        inventory.addItem(item);
        System.out.println("Obtained: " + item.getName());
    }

    // Display Inventory
    public void displayInventory() {
        inventory.displayInventory();
    }

    // Attacking
    public int attackingPower() {
        return attackPower;
    }

    // Checkpoint
    public void setCheckpoint(String area) {
        checkpoint = area;
    }

    // Respawn
    public void respawnCheckpoint() {
        fullHealth();
        currentArea = checkpoint;
    }

    // Player Status
    public void displayStatus() {
        System.out.println("\n=== " + name + " ===");
        System.out.println("Health: " + currentHealth + "/" + maxHealth);
        System.out.println("Attack: " + attackPower);
        System.out.println("Defense: " + defense);
    }

    public void setCurrentArea(String area) { currentArea = area; }
    public String getCurrentArea() { return currentArea; }
    public String getName() { return name; }
    public int getHealth() { return currentHealth; }
}
