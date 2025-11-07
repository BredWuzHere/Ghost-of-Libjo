public class Main {
    public static void main(String[] args) {
        // Create player
        Player player = new Player();

        System.out.println("Welcome, " + player.getName() + "!");
        player.displayStatus();

        // Show inventory (initially empty)
        player.displayInventory();

        // Create items
        Item potion = new Item("Small Potion", "Restores 20 HP");
        Item sword  = new Item("Rusty Sword", "A weak blade");

        // Add items to player inventory
        player.addItem(potion);
        player.addItem(sword);

        // Display inventory after obtaining items
        player.displayInventory();

        // Move player to a new area and set checkpoint
        System.out.println("\n-- Traveling to Cavern --");
        player.setCurrentArea("Cavern");
        System.out.println("Current area: " + player.getCurrentArea());

        System.out.println("-- Setting checkpoint to Cavern --");
        player.setCheckpoint("Cavern");

        // Simulate respawn (restores to full health and teleports to checkpoint)
        System.out.println("\n-- Respawning to checkpoint --");
        player.respawnCheckpoint();
        player.displayStatus();
        System.out.println("Arrived at: " + player.getCurrentArea());
    }
}
