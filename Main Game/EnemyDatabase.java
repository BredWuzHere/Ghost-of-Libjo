import java.util.*;

public class EnemyDatabase {
    private static Random rnd = new Random();
    private static int currentStage = 1;

    public static void setStage(int stage) { currentStage = Math.max(1, stage); }

    public static Enemy createEnemy(String id) {
        Enemy e;
        // New parameters: maxHp, speed, ap, damage, armor, EVASION
        
        switch (id) {
            /* ... existing code above ... */

case "performativevaper":
    // Low Evasion (0.05). Fixing ID consistency and adding POISON status effect.
    e = new SimpleEnemy("vaper", "Performative Vaper", 15 + (currentStage-1)*2, 5, 4, 4 + currentStage, 1, 0.05);
    
    // FIX 1: Using the correct ID for the new move "bane_ghost_hipak"
    // FIX 2: Adding a POISON status effect to the move. Assuming intValue=1 for 1 damage per turn for 3 turns.
    Move g1 = new Move("bane_ghost_hipak", "Bane Ghost Hipak", "Blows thick poisoned air at you matcha flavored, using bane technique.", 2, 1.15, new StatusEffect(StatusEffect.Kind.POISON, "Vape Poison", "Toxic air damage.", 3, 1 + currentStage/2, 0.0), 0, ElementType.NONE);
                        
    Move g2 = new Move("quick_stab", "Quick Stab", "A fast, weak stab.", 1, 0.6, null, 0, ElementType.NONE);
    e.addMove(g1); e.addMove(g2);
    break;

case "meralco_reader":
    // Very Low Evasion (0.00) - Heavy Armor
    e = new SimpleEnemy("meralcoman", "Meralco Reader", 20 + (currentStage-1)*4, 5, 4, 10 + currentStage, 1, 0.00);
    
    Move m1 = new Move("Reader_Throw", "Reader Throw", "Throws a reader at you.", 2, 1.0,new StatusEffect(StatusEffect.Kind.JOLT, "Minor Jolting", "Minor jolting", 1, 0, 0.0), 1, ElementType.ELECTRIC);
    Move m2 = new Move("overcharge", "Overcharge Jab", "A fast jab that sparks.", 1, 0.4, null, 0, ElementType.FIRE);
    e.addMove(m1); e.addMove(m2);
    break;

case "slime":
    // Moderate Evasion (0.15) - Slippery target
    e = new SimpleEnemy("slime", "Slime", 8 + (currentStage-1)*1, 3, 4, 2 + currentStage/2, 0, 0.15);
    Move s1 = new Move("ooze_slap", "Oozing Slap", "A sloppy, sticky hit.", 2, 1.0, null, 0, ElementType.NONE);
    Move s2 = new Move("spark_splash", "Spark Splash", "A small electric splash.", 1, 0.5,new StatusEffect(StatusEffect.Kind.JOLT, "Minor Jolting", "Minor jolting", 1, 0, 0.0), 1, ElementType.ELECTRIC);
    e.addMove(s1); e.addMove(s2);
    break;
    
// --- NEW ENEMIES FOR STATUS EFFECTS ---

case "ice_shaman":
    // CRIT_DOWN / SLOW Enemy
    e = new SimpleEnemy("ice_shaman", "Frost Shaman", 15 + (currentStage-1)*3, 4, 4, 3 + currentStage, 0, 0.10);
    // Move 1: Slow / CRIT_DOWN effect (dblValue = crit reduction)
    Move i1 = new Move("freezing_curse", "Freezing Curse", "Chills the target, slowing them and dulling senses.", 3, 0.8,new StatusEffect(StatusEffect.Kind.CRIT_DOWN, "Freezing Chill", "Reduced crit and speed.", 3, 0, -0.15), 1, ElementType.ICE);
    // Move 2: Basic Slow attack
    Move i2 = new Move("ice_shard", "Ice Shard", "A sharp shard that pierces.", 1, 0.6, new StatusEffect(StatusEffect.Kind.SLOW, "Ice Glaze", "Minor speed reduction.", 2, 0, -0.10), 0, ElementType.ICE);
    e.addMove(i1); e.addMove(i2);
    break;

case "sand_duelist":
    // SLIPPERY / ARMOR_DOWN Enemy
    e = new SimpleEnemy("sand_duelist", "Sand Duelist", 17 + (currentStage-1)*3, 6, 4, 5 + currentStage, 1, 0.25); // High base evasion
    // Move 1: ARMOR_DOWN effect (intValue = armor reduction)
    Move d1 = new Move("sand_kick", "Sand Kick", "Kicks sand in the enemy's face, reducing their defense.", 2, 1.0, new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Gritty Armor", "Armor reduced.", 2, -2, 0.0), 1, ElementType.NONE);
    // Move 2: SLIPPERY/evasion buff self-move (targets self-target for a buff, status is applied to self)
    Move d2 = new Move("wind_dodge", "Wind Dodge", "Dances with the wind, making them harder to hit.", 1, 0.0,new StatusEffect(StatusEffect.Kind.SLIPPERY, "Wind Step", "Evasion increased.", 2, 0, 0.20), 0, ElementType.NONE);
    // NOTE: This move needs logic in the Move.perform() method to target 'this' (the caster) when targetCount=0 and StatusEffect is present.
    e.addMove(d1); e.addMove(d2);
    break;

case "fire_elemental":
    // BURN Enemy (DOT)
    e = new SimpleEnemy("fire_elemental", "Fire Elemental", 22 + (currentStage-1)*4, 3, 4, 8 + currentStage, 0, 0.00);
    // Move 1: BURN effect (intValue = DOT damage)
    Move f1 = new Move("flame_spit", "Flame Spit", "Spits a ball of fire, causing heavy burns.", 3, 1.2, new StatusEffect(StatusEffect.Kind.BURN, "Major Burn", "Heavy burning damage.", 3, 4 + currentStage, 0.0), 1, ElementType.FIRE);
    // Move 2: Basic fire damage
    Move f2 = new Move("heat_wave", "Heat Wave", "A localized wave of heat.", 1, 0.7, null, 0, ElementType.FIRE);
    e.addMove(f1); e.addMove(f2);
    break;

case "orc":
    // Very Low Evasion (0.00) - Heavy, slow
    e = new SimpleEnemy("orc", "Orc", 18 + (currentStage-1)*3, 4, 4, 6 + currentStage, 2, 0.00);
    Move o1 = new Move("axe_chop", "Axe Chop", "A solid chop with an axe.", 2, 1.0, null, 0, ElementType.NONE); 
    Move o2 = new Move("crushing_blow", "Crushing Blow", "A slower, heavier smash that can stagger.", 3, 1.6, new StatusEffect(StatusEffect.Kind.STUN, "Stagger", "AP penalty next turn", 2, 0, 0.0), 2, ElementType.NONE);
    e.addMove(o1); e.addMove(o2);
    break;
    
/* ... existing code below (rat, bosses, default) ... */

            case "hipon":
                // Boss: Very High Evasion (0.25)
                e = new Boss("hipon", "Hipon of Libjo", 30 + (currentStage - 1)*5, 4, 4, 5 + currentStage, 1, 1, 0.40, 0.35);
                Move h1 = new Move("tentacle_lash", "Tentacle Lash", "A sticky tentacle lash.", 2, 1.0, new StatusEffect(StatusEffect.Kind.CRIT_DOWN, "Wet Goo", "Slippery -crit down", 3, 0, -0.10), 3, ElementType.NONE);
                Move h2 = new Move("ink_blast", "Ink Blast", "Dark ink that chills and reduces attack.", 3, 1.1,new StatusEffect(StatusEffect.Kind.GENERIC, "Chilled Ink", "Reduces attack", 2, 0, -0.10), 2, ElementType.ICE);
                e.addMove(h1); e.addMove(h2);
                break;

            case "gulod":
                // Boss: Low Evasion (0.05) - Tanky, heavily armored
                e = new Boss("gulod", "Terror of Gulod", 40 + (currentStage - 1)*8, 3, 4, 8 + currentStage, 3, 2, 0.10, 0.05);
                Move gA = new Move("earth_slam", "Earth Slam", "Smashes the ground.", 2, 1.0, null, 0, ElementType.NONE);
                Move gB = new Move("lava_fury", "Lava Fury", "Burning onslaught.", 4, 1.4, new StatusEffect(StatusEffect.Kind.BURN, "Lava Burn", "Burning damage", 3, 2, 0.0), 3, ElementType.FIRE);
                e.addMove(gA); e.addMove(gB);
                break;

            case "sales":
                // Boss: High Evasion (0.20) - Mirage/Illusion-based
                e = new Boss("sales", "Sales the Corrupt Mirage", 60 + (currentStage - 1)*12, 4, 4, 10 + currentStage, 4, 3, 0.05, 0.20);
                Move sA = new Move("mirage_blade", "Mirage Blade", "Spectral slashes from illusions.", 2, 1.0, null, 0, ElementType.NONE);
                Move sB = new Move("electro_contract", "Electro Contract", "A shocking contract that jolts and spreads.", 3, 1.1, new StatusEffect(StatusEffect.Kind.JOLT, "Contracted Jolt", "Chance to stun", 1, 0, 0.0), 1, ElementType.ELECTRIC);
                e.addMove(sA); e.addMove(sB);
                break;

            case "dechavez":
                // Boss: Moderate Evasion (0.10) - Highly precise and skilled
                e = new Boss("dechavez", "DeChavez, The True Auditor", 120 + (currentStage-1)*20, 5, 4, 14 + currentStage, 6, 4, 0.02, 0.10);
                Move dd1 = new Move("audit_strike", "Audit Strike", "A precise, brutal strike.", 2, 1.1, null, 0, ElementType.NONE);
                Move dd2 = new Move("cold_audit", "Cold Audit", "Freezing debuff that weakens defenses.", 4, 1.3,  new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Cold Audit", "Armor down and attack reduced", 3, -2, -0.10), 3, ElementType.ICE);
                e.addMove(dd1); e.addMove(dd2);
                break;

            

            default:
                // Default: Low Evasion (0.05)
                e = new SimpleEnemy("rat", "Giant Rat", 6 + (currentStage-1), 4, 4, 2 + currentStage/2, 0, 0.05);
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

    public static List<Enemy> createEnemiesForRegion(int region) {
        List<Enemy> out = new ArrayList<>();
        String[] pool;
        if (currentStage == 1) pool = new String[] {"goblin","slime","rat"};
        else if (currentStage == 2) pool = new String[] {"goblin","orc","slime"};
        else pool = new String[] {"orc","thundermegaknight","goblin"};

        String id = pool[rnd.nextInt(pool.length)];
        out.add(createEnemy(id));

        double base;
        if (currentStage == 1) base = 0.35;
        else if (currentStage == 2) base = 0.45;
        else base = 0.65;

        double chance = base;
        while (Math.random() < chance) {
            String nextId = pool[rnd.nextInt(pool.length)];
            out.add(createEnemy(nextId));
            chance *= 0.5;
            if (out.size() >= 5) break;
        }

        return out;
    }

    public static Enemy createRandomEnemyForRegion(int region) {
        List<Enemy> list = createEnemiesForRegion(region);
        return list.get(0);
    }

    public static String getUniqueDropFor(String enemyId) {
        switch(enemyId) { case "hipon": return "hipon_tentacle"; case "gulod": return "gulod_amulet"; case "dechavez": return "dechavez_claw"; case "sales": return "mirage_shard"; default: return null; }
    }
}