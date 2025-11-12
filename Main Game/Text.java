import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class Text {
    private static final Random rnd = new Random();

    private static final String[] ROOM_LORE_COMMON = {
        "The air tastes of old coins and unfinished promises.",
        "You notice scratchmarks on the wall — someone left in a hurry.",
        "A faint melody echoes from somewhere beneath the floorboards.",
        "The lanterns flicker in a rhythm that sounds almost like breathing.",
        "A mural here depicts a long-forgotten feast. The eyes seem to follow you.",
        "There's a smell of burnt sugar and rain in this room.",
        "The floor is littered with tiny shells and a single, shiny button.",
        "Someone has carved initials into the door frame. The date is indecipherable."
    };

    private static final String[][] ROOM_LORE_BY_REGION = new String[][] {
        { "Tide-worn posters cling to the wall; the sea's influence is obvious.", "Broken nets hang here — something big was hauled through this corridor." },
        { "Old billboards advertise things no one remembers buying.", "A faint smell of oil and copper — this area once hummed with machines." },
        { "Colorful banners hang low, sagging with the weight of years.", "You see footprints leading to a boarded-up stage; music used to live here." },
        { "The stones feel colder here, like they're keeping secrets.", "An altar of discarded papers stands in the corner, stamped with a seal." }
    };

    private static final String[] ENEMY_INSULTS_GENERIC = {
        "You'll taste my claws, whelp!",
        "I've seen scarier pigeons than you!",
        "Stay still — this will be over quickly.",
        "Your ancestors would be disappointed.",
        "I collect screams for breakfast. You look tasty."
    };

    // map per enemy id
    private static final Map<String, String[]> ENEMY_LINES = new HashMap<>();
    static {
        ENEMY_LINES.put("goblin", new String[]{
            "Hehehe, small coin? I'll take it!",
            "Stab, stab, squish!",
            "Get out of my cave, shiny-head!"
        });
        ENEMY_LINES.put("slime", new String[]{
            "Blorp blorp... squish you now.",
            "I envelop and digest! You are tasty.",
            "Oozing happiness, lets melt you."
        });
        ENEMY_LINES.put("orc", new String[]{
            "Feel my axe, weakling!",
            "Orc smash! You no smart!",
            "I will take your head as a hat!"
        });
        ENEMY_LINES.put("rat", new String[]{
            "Squeak! Your heels smell nice.",
            "Tiny human, big teeth!"
        });
        ENEMY_LINES.put("hipon", new String[]{
            "Glub glub, you sank my standards!",
            "Careful where you step — my tentacles have sticky manners."
        });
        ENEMY_LINES.put("gulod", new String[]{
            "ROOOAR! You will be ground to dust!",
            "The earth hungers for your bones."
        });
        ENEMY_LINES.put("sales", new String[]{
            "Sign here to finalize your doom.",
            "No refunds on life choices."
        });
        ENEMY_LINES.put("dechavez", new String[]{
            "I audit souls — your ledger is bankrupt.",
            "Penalties apply; interest is compounding."
        });
    }

    private static final String[][] BOSS_TAUNTS = new String[][] {
        { "Glub glub, you sank my standards! — Hipon of Libjo", "Careful where you step — my tentacles have sticky manners.", "Squeeeek! I sing the song of the deep and you'll dance it." },
        { "Hear the earth mourn your arrogance! — Terror of Gulod", "ROOOAR! Your brittle fears fuel me.", "I will grind your will into the soil." },
        { "Numbers always balance... in my favor. — Sales the Corrupt Mirage", "Sign here for your defeat. No fine print required.", "I sell illusions at a discount; today your hope is on clearance." },
        { "I audit souls — and yours has discrepancies. — DeChavez", "You cannot argue with the ledger of fate.", "I wrote the clauses you ignored; now pay the penalties." }
    };

    private static final String[] CUTSCENE_LINES = {
        "The land shudders — something ancient stirs.",
        "A hush falls over the map as pathways rearrange.",
        "You feel the weight of countless eyes watching the horizon.",
        "The wind whispers of a prize kept beyond a guarded gate."
    };

    public static String randomRoomLoreCommon() {
        return ROOM_LORE_COMMON[rnd.nextInt(ROOM_LORE_COMMON.length)];
    }

    public static String randomRoomLoreForRegion(int region) {
        if (region >= 1 && region <= ROOM_LORE_BY_REGION.length) {
            String[] arr = ROOM_LORE_BY_REGION[region - 1];
            if (arr != null && arr.length > 0 && rnd.nextDouble() < 0.8) {
                return arr[rnd.nextInt(arr.length)];
            }
        }
        return randomRoomLoreCommon();
    }

    public static String randomEnemyLineForId(String id) {
        if (id != null) {
            String key = id.toLowerCase();
            if (ENEMY_LINES.containsKey(key)) {
                String[] arr = ENEMY_LINES.get(key);
                return arr[rnd.nextInt(arr.length)];
            }
        }
        // fallback generic
        return ENEMY_INSULTS_GENERIC[rnd.nextInt(ENEMY_INSULTS_GENERIC.length)];
    }

    public static String randomBossTaunt(String bossId) {
        int idx = -1;
        if ("hipon".equalsIgnoreCase(bossId)) idx = 0;
        else if ("gulod".equalsIgnoreCase(bossId)) idx = 1;
        else if ("sales".equalsIgnoreCase(bossId)) idx = 2;
        else if ("dechavez".equalsIgnoreCase(bossId)) idx = 3;

        if (idx >= 0 && idx < BOSS_TAUNTS.length) {
            String[] arr = BOSS_TAUNTS[idx];
            return arr[rnd.nextInt(arr.length)];
        }
        String[] fallback = { "You are lost in trivialities", "I will enjoy this." };
        return fallback[rnd.nextInt(fallback.length)];
    }

    public static String randomCutsceneLine() {
        return CUTSCENE_LINES[rnd.nextInt(CUTSCENE_LINES.length)];
    }

    public static boolean roll(double probability) {
        return rnd.nextDouble() < probability;
    }
}
