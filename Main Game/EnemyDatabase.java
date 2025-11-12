// EnemyDatabase.java
import java.util.*;

public class EnemyDatabase {
    private static Random rnd = new Random();
    // current campaign stage affects which enemies show up
    private static int currentStage = 1;

    public static void setStage(int stage) {
        currentStage = Math.max(1, stage);
    }

    public static Enemy createEnemy(String id) {
        switch (id) {
            case "goblin": return new SimpleEnemy("goblin","Goblin", 10, 4, 4, 3, 0);
            case "slime": return new SimpleEnemy("slime","Slime", 6, 3, 4, 2, 0);
            case "orc": return new SimpleEnemy("orc","Orc", 14, 3, 4, 5, 1);
            case "stage_boss":
            case "region_boss": return new Boss("region_boss","Region Boss", 30 + (currentStage-1)*10, 3 + (currentStage-1), 4 + (currentStage-1), 6 + (currentStage-1)*2, 2 + (currentStage-1));
            default: return new SimpleEnemy("rat","Giant Rat", 8, 4, 4, 2, 0);
        }
    }

    /**
     * Create a stage-aware enemy (for boss calls): accepts a 'base' id like "stage_boss" and returns a boss tuned to currentStage.
     */
    public static Enemy createEnemyForStage(String id, int stage) {
        setStage(stage);
        if ("stage_boss".equals(id) || "region_boss".equals(id)) {
            // stronger boss per stage
            return createEnemy("region_boss");
        }
        return createEnemy(id);
    }

    public static Enemy createRandomEnemyForRegion(int region) {
        // Choose enemies by current campaign stage and region
        if (currentStage == 1) {
            if (region == 1) {
                String[] pool = {"goblin","slime"};
                return createEnemy(pool[rnd.nextInt(pool.length)]);
            } else if (region == 2) {
                String[] pool = {"goblin","slime"};
                return createEnemy(pool[rnd.nextInt(pool.length)]);
            } else if (region == 3) {
                String[] pool = {"slime","goblin"};
                return createEnemy(pool[rnd.nextInt(pool.length)]);
            } else {
                String[] pool = {"goblin","slime"};
                return createEnemy(pool[rnd.nextInt(pool.length)]);
            }
        } else if (currentStage == 2) {
            if (region == 1) {
                String[] pool = {"goblin","orc"};
                return createEnemy(pool[rnd.nextInt(pool.length)]);
            } else if (region == 2) {
                String[] pool = {"orc","goblin"};
                return createEnemy(pool[rnd.nextInt(pool.length)]);
            } else if (region == 3) {
                String[] pool = {"orc","slime"};
                return createEnemy(pool[rnd.nextInt(pool.length)]);
            } else {
                String[] pool = {"goblin","orc","slime"};
                return createEnemy(pool[rnd.nextInt(pool.length)]);
            }
        } else {
            // late stage: tougher enemies more often
            String[] pool = {"orc","orc","goblin","slime"};
            return createEnemy(pool[rnd.nextInt(pool.length)]);
        }
    }
}
