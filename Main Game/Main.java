import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            clearScreen();
            System.out.println("=== Roguelike Console RPG ===");
            System.out.println("1) Play Game");
            System.out.println("2) Developer Mode (playtest, max stats)");
            System.out.println("3) About Game");
            System.out.println("4) Quit");
            System.out.print("Choice: ");
            String c = in.nextLine().trim();
            if (c.equals("1")) {
                InstanceGame g = new InstanceGame(in, false);
                g.start();
            } else if (c.equals("2")) {
                InstanceGame g = new InstanceGame(in, true);
                g.start();
            } else if (c.equals("3")) {
                clearScreen();
                System.out.println("Roguelike Console RPG - Text driven.");
                System.out.println("Explore rooms, fight enemies, collect items, and defeat region bosses.");
                System.out.println("Controls: w/a/s/d to move, i to open inventory.");
                System.out.println("\nPress Enter to return to menu...");
                in.nextLine();
            } else if (c.equals("4")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Unknown choice. Press Enter.");
                in.nextLine();
            }
        }
        in.close();
    }

    private static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i=0;i<50;i++) System.out.println();
        }
    }
}
