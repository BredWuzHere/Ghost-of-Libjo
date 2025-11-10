import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("=== ROGUELIKE: CONSOLE EDITION ===");
            System.out.println("1) Play Game");
            System.out.println("2) About Game");
            System.out.println("3) Quit Game");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();
            if (choice.equals("1")) {
                InstanceGame game = new InstanceGame(in);
                game.start();
            } else if (choice.equals("2")) {
                System.out.println("Roguelike: Console Edition\n" +
                        "- Turn-based combat with Action Points (AP)\n" +
                        "- 4 Regions to explore (map divided into 4 quadrants)\n" +
                        "- Polymorphic items: Weapons, Armor, Relics, Consumables\n" +
                        "- If you die, your run resets (new InstanceGame)\n");
            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        in.close();
    }
}
