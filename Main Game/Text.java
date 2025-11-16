import java.util.*;

public class Text {
    private static Random rnd = new Random();
    private static String[] roomLore = new String[] {
        "Whispers echo here.",
        "The floor is damp with old tides.",
        "You hear distant laughter."
    };
    private static String[] cutscenes = new String[] {
        "A glimmer of hope passes by.",
        "Shadows rearrange themselves.",
        "A choir of distant bells rings."
    };
    private static Map<String,String[]> bossTaunts = new HashMap<>();
    static {
        bossTaunts.put("hipon", new String[] {"You taste the sea in your wounds.","My tentacles will caress your fate."});
        bossTaunts.put("gulod", new String[] {"Feel the earth's wrath.","Your defenses crumble like soil."});
        bossTaunts.put("sales", new String[] {"Sign here... for defeat.","Illusions make your pockets lighter."});
        bossTaunts.put("dechavez", new String[] {"Audit complete.","Your errors are my feast."});
    }

    public static boolean roll(double p) { return Math.random() < p; }
    public static String randomRoomLoreForRegion(int r) { return roomLore[rnd.nextInt(roomLore.length)]; }
    public static String randomCutsceneLine() { return cutscenes[rnd.nextInt(cutscenes.length)]; }
    public static String randomBossTaunt(String id) {
        String[] arr = bossTaunts.getOrDefault(id, new String[] {"The boss glares."});
        return arr[rnd.nextInt(arr.length)];
    }
}
