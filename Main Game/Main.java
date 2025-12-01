import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            clearScreen();
            displayTitle();
            System.out.println("\n" + Color.YELLOW + "1) Play Game" + Color.RESET);
            System.out.println(Color.YELLOW + "2) Developer Mode (playtest, max stats)" + Color.RESET);
            System.out.println(Color.YELLOW + "3) About Game" + Color.RESET);
            System.out.println(Color.YELLOW + "4) Quit" + Color.RESET);
            System.out.print(Color.CYAN + "Choice: " + Color.RESET);
            String c = in.nextLine().trim();
            if (c.equals("1")) {
                InstanceGame g = new InstanceGame(in, false);
                g.start();
            } else if (c.equals("2")) {
                InstanceGame g = new InstanceGame(in, true);
                g.start();
            } else if (c.equals("3")) {
                clearScreen();
                System.out.println(Color.BRIGHT_BLUE + "Roguelike Console RPG - Text driven." + Color.RESET);
                System.out.println(Color.BRIGHT_BLUE + "Explore rooms, fight enemies, collect items, and defeat region bosses." + Color.RESET);
                System.out.println(Color.BRIGHT_BLUE + "Controls: w/a/s/d to move, i to open inventory." + Color.RESET);
                System.out.println("\nPress Enter to return to menu...");
                in.nextLine();
            } else if (c.equals("4")) {
                System.out.println(Color.BRIGHT_YELLOW + "Goodbye!" + Color.RESET);
                break;
            } else {
                System.out.println("Unknown choice. Press Enter.");
                in.nextLine();
            }
        }
        in.close();
    }

    private static void displayTitle() {
    System.out.println();
    System.out.println(Color.RED +
            "         ██████╗ ██╗  ██╗ ██████╗ ███████╗████████╗\n" +
            "        ██╔════╝ ██║  ██║██╔═══██╗██╔════╝╚══██╔══╝\n" +
            "        ██║  ███╗███████║██║   ██║███████╗   ██║   \n" +
            "        ██║   ██║██╔══██║██║   ██║╚════██║   ██║   \n" +
            "        ╚██████╔╝██║  ██║╚██████╔╝███████║   ██║   \n" +
            "         ╚═════╝ ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   \n" +
                        "\n" +

            "                     ██████╗ ███████╗\n" +
            "                    ██╔═══██╗██╔════╝\n" +
            "                    ██║   ██║█████╗  \n" +
            "                    ██║   ██║██╔══╝  \n" +
            "                    ╚██████╔╝██║     \n" +
            "                     ╚═════╝ ╚═╝     \n" +
                        " \n" +

            "██████╗  █████╗ ████████╗ █████╗ ███╗   ██╗ ██████╗  █████╗ ███████╗\n" +
            "██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗████╗  ██║██╔════╝ ██╔══██╗██╔════╝\n" +
            "██████╔╝███████║   ██║   ███████║██╔██╗ ██║██║  ███╗███████║███████╗\n" +
            "██╔══██╗██╔══██║   ██║   ██╔══██║██║╚██╗██║██║   ██║██╔══██║╚════██║\n" +
            "██████╔╝██║  ██║   ██║   ██║  ██║██║ ╚████║╚██████╔╝██║  ██║███████║\n" +
            "╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝\n"
            + Color.RESET);
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
