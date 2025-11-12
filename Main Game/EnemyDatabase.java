// EnemyDatabase.java
import java.util.*;

public class EnemyDatabase {
    private static Random rnd = new Random();
    private static int currentStage = 1;

    public static void setStage(int stage) { currentStage = Math.max(1, stage); }

    public static Enemy createEnemy(String id) {
        Enemy e;
        switch (id) {
            case "goblin":
                e = new SimpleEnemy("goblin", "Goblin", 12, 5, 4, 4, 1);
                Move g1 = new Move("club_smack", "Club Smack", "A heavy club swing.", 2, 1.0, null, 0, ElementType.NONE);
                Move g2 = new Move("quick_stab", "Quick Stab", "A fast, weak stab.", 1, 0.6, null, 0, ElementType.NONE);
                e.addMove(g1); e.addMove(g2);
                break;

            case "slime":
                e = new SimpleEnemy("slime", "Slime", 8, 3, 4, 2, 0);
                Move s1 = new Move("ooze_slap", "Oozing Slap", "A sloppy, sticky hit.", 2, 1.0, null, 0, ElementType.NONE);
                Move s2 = new Move("spark_splash", "Spark Splash", "A small electric splash.", 1, 0.5,
                        new StatusEffect(StatusEffect.Kind.GENERIC, "Jolted", "Minor jolting", 1, 0, 0.0), 1, ElementType.ELECTRIC);
                e.addMove(s1); e.addMove(s2);
                break;

            case "orc":
                e = new SimpleEnemy("orc", "Orc", 18, 4, 4, 6, 2);
                Move o1 = new Move("axe_chop", "Axe Chop", "A solid chop with an axe.", 2, 1.0, null, 0, ElementType.NONE);
                Move o2 = new Move("crushing_blow", "Crushing Blow", "A slower, heavier smash that can stagger.", 3, 1.6,
                        new StatusEffect(StatusEffect.Kind.GENERIC, "Stagger", "AP penalty next turn", 2, 1, 0.0), 2, ElementType.NONE);
                e.addMove(o1); e.addMove(o2);
                break;

            case "rat":
                e = new SimpleEnemy("rat", "Giant Rat", 6, 4, 4, 2, 0);
                Move r1 = new Move("rat_bite", "Bite", "A sharp bite.", 1, 0.8, null, 0, ElementType.NONE);
                e.addMove(r1);
                break;

            case "hipon":
                e = new Boss("hipon", "Hipon of Libjo", 30 + (currentStage - 1)*5, 4, 4, 5, 1, 0.40, 1);
                Move h1 = new Move("tentacle_lash", "Tentacle Lash", "A sticky tentacle lash.", 2, 1.0,
                        new StatusEffect(StatusEffect.Kind.GENERIC, "Wet Goo", "Slippery -crit down", 3, 0, -0.10), 3, ElementType.NONE);
                Move h2 = new Move("ink_blast", "Ink Blast", "Dark ink that chills and reduces attack.", 3, 1.1,
                        new StatusEffect(StatusEffect.Kind.GENERIC, "Chilled Ink", "Reduces attack", 2, 0, -0.10), 2, ElementType.ICE);
                e.addMove(h1); e.addMove(h2);
                break;

            case "gulod":
                e = new Boss("gulod", "Terror of Gulod", 40 + (currentStage - 1)*8, 3, 4, 8, 3, 0.10, 2);
                Move gA = new Move("earth_slam", "Earth Slam", "Smashes the ground.", 2, 1.0, null, 0, ElementType.NONE);
                Move gB = new Move("lava_fury", "Lava Fury", "Burning onslaught.", 4, 1.4,
                        new StatusEffect(StatusEffect.Kind.BURN, "Lava Burn", "Burning damage", 3, 2, 0.0), 3, ElementType.FIRE);
                e.addMove(gA); e.addMove(gB);
                break;

            case "sales":
                e = new Boss("sales", "Sales the Corrupt Mirage", 60 + (currentStage - 1)*12, 4, 4, 10, 4, 0.05, 3);
                Move sA = new Move("mirage_blade", "Mirage Blade", "Spectral slashes from illusions.", 2, 1.0, null, 0, ElementType.NONE);
                Move sB = new Move("electro_contract", "Electro Contract", "A shocking contract that jolts and spreads.", 3, 1.1,
                        new StatusEffect(StatusEffect.Kind.GENERIC, "Contracted Jolt", "Chance to stun", 1, 0, 0.0), 1, ElementType.ELECTRIC);
                e.addMove(sA); e.addMove(sB);
                break;

            case "dechavez":
                e = new Boss("dechavez", "DeChavez, The True Auditor", 120, 5, 4, 14, 6, 0.02, 4);
                Move d1 = new Move("audit_strike", "Audit Strike", "A precise, brutal strike.", 2, 1.1, null, 0, ElementType.NONE);
                Move d2 = new Move("cold_audit", "Cold Audit", "Freezing debuff that weakens defenses.", 4, 1.3,
                        new StatusEffect(StatusEffect.Kind.GENERIC, "Cold Audit", "Armor down and attack reduced", 3, 2, -0.10), 3, ElementType.ICE);
                e.addMove(d1); e.addMove(d2);
                break;

            default:
                e = new SimpleEnemy("rat", "Giant Rat", 6, 4, 4, 2, 0);
                Move def = new Move("bite", "Bite", "A bite.", 1, 0.8, null, 0, ElementType.NONE);
                e.addMove(def);
                break;
        }

        e.setDifficulty(currentStage);
        return e;
    }

    public static Enemy createStageBoss(int stage) {
        switch (stage) {
            case 1: return createEnemy("hipon");
            case 2: return createEnemy("gulod");
            case 3: return createEnemy("sales");
            default: return createEnemy("dechavez");
        }
    }

    /**
     * Create a list of enemies for a region using the multi-spawn probabilities you requested.
     * Stage spawn base chances:
     * stage1 base 35%, stage2 base 45%, stage3 base 65%.
     * After each successful spawn the chance for an additional enemy is halved.
     */
    public static List<Enemy> createEnemiesForRegion(int region) {
        List<Enemy> out = new ArrayList<>();
        // base pool depends on currentStage (simplified)
        String[] pool;
        if (currentStage == 1) pool = new String[] {"goblin", "slime", "goblin"};
        else if (currentStage == 2) pool = new String[] {"goblin", "orc", "goblin"};
        else pool = new String[] {"orc", "orc", "goblin"};

        // pick first enemy always
        String id = pool[rnd.nextInt(pool.length)];
        out.add(createEnemy(id));

        // determine base chance to try to spawn additional enemies
        double base;
        if (currentStage == 1) base = 0.35;
        else if (currentStage == 2) base = 0.45;
        else base = 0.65;

        double chance = base;
        while (Math.random() < chance) {
            // spawn another enemy (random)
            String nextId = pool[rnd.nextInt(pool.length)];
            out.add(createEnemy(nextId));
            // reduce chance by half for next potential spawn
            chance *= 0.5;
            // safety cap so we don't spawn too many
            if (out.size() >= 5) break;
        }

        return out;
    }

    public static Enemy createRandomEnemyForRegion(int region) {
        List<Enemy> list = createEnemiesForRegion(region);
        // return the first for compatibility with older code (most call sites now should use createEnemiesForRegion)
        return list.get(0);
    }
}
