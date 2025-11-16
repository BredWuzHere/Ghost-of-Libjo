import java.util.*;

public class NPCDatabase {
    private static Random rnd = new Random();

    public static NPC createNPC(String id) {
        NPC n;
        switch(id) {
            case "merchant":
                n = new NPC("merchant", "Wandering Merchant");
                n.addLine("Care to trade? I have wares from distant lands.");
                n.addLine("Fresh goods! Rare trinkets for the discerning traveler.");
                n.addOption("Buy a potion (free sample)", "hp_potion");
                n.addOption("Ask about rumors", null);
                n.addOption("Trade an old relic (not implemented)", null);
                break;
            case "old_man":
                n = new NPC("old_man", "Old Man by the Wall");
                n.addLine("When I was young this place was different...");
                n.addLine("The sea remembers more than we do.");
                n.addOption("Listen to his tale", "relic_plus_ap");
                n.addOption("Help him with chores", "hp_potion");
                n.addOption("Ignore him", null);
                break;
            case "mystic_child":
                n = new NPC("mystic_child", "Mysterious Child");
                n.addLine("Do you believe in fate?");
                n.addLine("I can see the threads of your future... for a price.");
                n.addOption("Accept the strange gift", "relic_crit");
                n.addOption("Refuse politely", null);
                n.addOption("Ask for a blessing", "hp_potion");
                break;
            default:
                n = new NPC("traveler", "Quiet Traveler");
                n.addLine("Just passing through...");
                n.addOption("Share a meal", "hp_potion");
                n.addOption("Tell a joke", null);
                break;
        }
        return n;
    }

    public static NPC createRandomNPCForRegion(int region) {
        String[] pool;
        if (region == 1) pool = new String[] {"merchant","old_man","traveler"};
        else if (region == 2) pool = new String[] {"mystic_child","merchant","traveler"};
        else pool = new String[] {"merchant","mystic_child","old_man"};
        String id = pool[rnd.nextInt(pool.length)];
        return createNPC(id);
    }
}
