import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            clearScreen();
            System.out.println("=== GHOST OF LIBJO ===");
            System.out.println("1) Play Game");
            System.out.println("2) About Game");
            System.out.println("3) Developer Mode (playtest)");
            System.out.println("4) Quit Game");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();
            if (choice.equals("1")) {
                InstanceGame game = new InstanceGame(in);
                game.start();
            } else if (choice.equals("2")) {
                clearScreen();
                System.out.println("GHOST OF LIBJO\n- Turn-based combat with AP\n- 4 Regions\n- Developer Mode");
                System.out.println("\nPress Enter to return to menu...");
                in.nextLine();
            } else if (choice.equals("3")) {
                clearScreen();
                System.out.println("Starting Developer Mode (max stats)...");
                InstanceGame game = new InstanceGame(in, true);
                game.start();
            } else if (choice.equals("4")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
                System.out.println("Press Enter to continue...");
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
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
}
