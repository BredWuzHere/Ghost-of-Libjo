import java.util.*;

public class EnemyDatabase {
    private static Random rnd = new Random();
    public static Enemy createEnemy(String id) {
        switch (id) {
            case "goblin": return new SimpleEnemy("goblin","Goblin", 10, 4, 4, 3, 0);
            case "slime": return new SimpleEnemy("slime","Slime", 6, 3, 4, 2, 0);
            case "orc": return new SimpleEnemy("orc","Orc", 14, 3, 4, 5, 1);
            case "region_boss": return new Boss("region_boss","Region Boss", 30, 3, 4, 6, 2);
            default: return new SimpleEnemy("rat","Giant Rat", 8, 4, 4, 2, 0);
        }
    }

    public static Enemy createRandomEnemyForRegion(int region) {
        // vary by region
        if (region == 1) {
            String[] pool = {"goblin","slime"};
            return createEnemy(pool[rnd.nextInt(pool.length)]);
        } else if (region == 2) {
            String[] pool = {"orc","goblin"};
            return createEnemy(pool[rnd.nextInt(pool.length)]);
        } else if (region == 3) {
            String[] pool = {"slime","orc"};
            return createEnemy(pool[rnd.nextInt(pool.length)]);
        } else {
            // region 4 has chance boss
            if (rnd.nextDouble() < 0.15) return createEnemy("region_boss");
            String[] pool = {"goblin","orc","slime"};
            return createEnemy(pool[rnd.nextInt(pool.length)]);
        }
    }
}
