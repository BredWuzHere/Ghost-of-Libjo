import java.util.*;

public class Text {
    private static Random rnd = new Random();

    // Room lore per stage/region
    private static Map<Integer, String[]> roomLoreMap = new HashMap<>();
    private static Map<Integer, String[]> roomCutscenesMap = new HashMap<>();
    private static Map<Integer, String[]> regionTransitionsMap = new HashMap<>();
     public static boolean roll(double chance) {
        return rnd.nextDouble() < chance;}

    // Boss taunts
    private static Map<String,String[]> bossTaunts = new HashMap<>();

    static {
        // ------------------- Room lore -------------------
        roomLoreMap.put(1, new String[] {
            "Whispers echo here.",
            "The floor is damp with old tides.",
            "You hear distant laughter."
        });
        roomLoreMap.put(2, new String[] {
            "The alley smells of street food.",
            "You notice graffiti hinting at hidden secrets.",
            "Footsteps echo in the distance."
        });
        roomLoreMap.put(3, new String[] {
            "The streets feel tense, danger lurks around every corner.",
            "A storm brews overhead, lightning illuminating your path.",
            "You sense eyes watching you from the shadows."
        });

        // ------------------- Room cutscenes -------------------
        roomCutscenesMap.put(1, new String[] {
            "A glimmer of hope passes by.",
            "Shadows rearrange themselves.",
            "A choir of distant bells rings."
        });
        roomCutscenesMap.put(2, new String[] {
            "A street vendor waves at you.",
            "A cat darts across your path.",
            "You hear the distant sound of a motorbike engine."
        });
        roomCutscenesMap.put(3, new String[] {
            "An eerie fog rolls in.",
            "Sparks fly from broken neon signs.",
            "You feel the ground shake slightly."
        });

        // ------------------- Region transitions -------------------
        regionTransitionsMap.put(1, new String[] {
            "You step into the bustling streets of the next region.",
            "The horizon opens to a new challenge ahead."
        });
        regionTransitionsMap.put(2, new String[] {
            "The cityscape changes as you move to a newer district.",
            "You see signs of fresh dangers and opportunities ahead."
        });
        regionTransitionsMap.put(3, new String[] {
            "The endgame area lies ahead, danger intensifies.",
            "Prepare yourself for the ultimate confrontation."
        });

        // ------------------- Boss taunts -------------------
        bossTaunts.put("hipon", new String[] {"You will taste my kuba power!!","My modtacles will violate you."});
        bossTaunts.put("gulod", new String[] {"Feel the wrath of the SUAYANS!","U cant defeat me dawg."});
        bossTaunts.put("sales", new String[] {"Sign here... for defeat.","Illusions make your pockets lighter."});
        bossTaunts.put("dechavez", new String[] {"Audit complete.","My biceps are more expensive than you."});
    }

    // ------------------- Helper functions -------------------

    public static String roomLoreForStage(int stage) { 
        String[] arr = roomLoreMap.getOrDefault(stage, new String[] {"The area seems ordinary."});
        return arr[rnd.nextInt(arr.length)]; 
    }

    public static String roomCutsceneForStage(int stage) { 
        String[] arr = roomCutscenesMap.getOrDefault(stage, new String[] {"Nothing unusual happens."});
        return arr[rnd.nextInt(arr.length)]; 
    }

    public static String regionTransitionForStage(int stage) { 
        String[] arr = regionTransitionsMap.getOrDefault(stage, new String[] {"You move to the next region."});
        return arr[rnd.nextInt(arr.length)]; 
    }

    public static String randomBossTaunt(String id) {
        String[] arr = bossTaunts.getOrDefault(id, new String[] {"The boss glares."});
        return arr[rnd.nextInt(arr.length)];
    }
}
