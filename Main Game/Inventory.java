import java.util.ArrayList;

public class Inventory {
    private ArrayList<Item> items = new ArrayList<>();

    // Adding Item to Inventory
    public void addItem(Item item) {
        items.add(item);
        System.out.println("Obtained: " + item.getName());
    }

    // Removing Item from Inventory
    public void removeItem(Item item) {
        items.remove(item);
    }

    // Display Inventory
    public void displayInventory() {
        System.out.println("\nInventory: ");

        if (inventory.isEmpty()) {
            System.out.println("Empty.");
        } else {
            for (Item i : inventory) {
                System.out.println("-  " + i.getName());
            }
        }
    }

    public boolean hasItem(String itemName) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public Item getItem(String itemName) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                return i;
            }
        }
        return null;
    }
}
