import java.util.*;

public class NPC {
    private String id;
    private String displayName;
    private List<String> lines = new ArrayList<>();
    private LinkedHashMap<String, String> dialogueOptions = new LinkedHashMap<>();

    public NPC(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public void addLine(String line) { lines.add(line); }
    public void addOption(String label, String rewardItemId) { dialogueOptions.put(label, rewardItemId); }

    public void interact(Player player, Scanner in) {
        if (!lines.isEmpty()) {
            String intro = lines.get(new Random().nextInt(lines.size()));
            System.out.println(Color.BLUE + "\"" + intro + "\" — " + displayName + Color.RESET);
        } else {
            System.out.println(Color.BRIGHT_BLACK + displayName + " has nothing to say." + Color.RESET);
        }

        System.out.println("\nWhat will you say/do?");
        int idx = 1;
        List<String> keys = new ArrayList<>(dialogueOptions.keySet());
        for (String k : keys) {
            System.out.println(idx + ") " + k);
            idx++;
        }
        System.out.println(idx + ") Leave");

        System.out.print("Choice: ");
        String raw = in.nextLine().trim();
        int choice;
        try { choice = Integer.parseInt(raw); } catch (NumberFormatException ex) { choice = idx; }
        if (choice < 1 || choice > idx) choice = idx;

        if (choice == idx) {
            System.out.println(Color.BLUE + displayName + " nods and goes on their way." + Color.RESET);
            return;
        }

        String option = keys.get(choice-1);
        String rewardId = dialogueOptions.get(option);

        System.out.println("\n" + displayName + " replies...");
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}

        if (rewardId == null) {
            System.out.println(displayName + ": \"I have nothing for that.\"");
        } else {
            Item it = ItemDatabase.createItem(rewardId);
            if (it != null) {
                player.getInventory().add(it);
                System.out.println(Color.GREEN + displayName + ": \"Here, take this — " + it.getName() + ".\" (added to inventory)" + Color.RESET);
            } else {
                System.out.println(displayName + ": \"I intended to give you something, but it seems lost.\"");
            }
        }
    }
}
