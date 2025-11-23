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
    // Low Evasion (0.05), weak early enemy
    e = new SimpleEnemy("vaper", "Performative Vaper", 12 + (currentStage-1)*1, 3, 2, 3 + currentStage, 1, 0.05);

    Move g1 = new Move(
        "bane_ghost_hipak",
        "Bane Ghost Hipak",
        "Blows thick poisoned air at you matcha flavored, using bane technique.",
        1,  // reduced base damage
        1.05,  // slightly lower multiplier
        new StatusEffect(StatusEffect.Kind.POISON, "Vape Poison", "Toxic air damage.", 2, 1, 0.0),  // shorter poison
        0,
        ElementType.NONE
    );
    Move g2 = new Move(
        "nicotine_rage",
        "Nicotine Induced Rage",
        "The vaper goes into berserk slapping everything with his vape.",
        1,
        0.8,  // lower multiplier
        null,
        0,
        ElementType.NONE
    );
    e.addMove(g1); e.addMove(g2);
    break;

case "tdro_talipapa":
    // Traffic Enforcer – low HP, weak STUN/ARMOR_DOWN
    e = new SimpleEnemy("tdro_talipapa", "Tdro ng Talipapa", 14 + (currentStage-1)*1, 4, 3, 3 + currentStage, 1, 0.05);

    Move tt1 = new Move(
        "traffic_whistle",
        "Traffic Whistle Blow",
        "Blasts his whistle to stun anyone who crosses recklessly.",
        1,
        0.3,
        new StatusEffect(StatusEffect.Kind.STUN, "Stunned by Whistle", "You are stunned for 1 turn.", 1, 0, 0.0),
        0,
        ElementType.NONE
    );
    Move tt2 = new Move(
        "baton_swing",
        "Baton Swing",
        "Swings his baton expertly, lowering your defense.",
        1,
        0.8,
        new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Defense Weakened", "Your armor is weakened for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(tt1);
    e.addMove(tt2);
    break;
//Libjo enemies
case "meralco_reader":
    e = new SimpleEnemy("meralcoman", "Meralco Reader", 14 + (currentStage-1)*2, 4, 3, 6 + currentStage, 1, 0.00);

    Move m1 = new Move(
        "Reader_Throw",
        "Reader Throw",
        "Throws a reader at you.",
        1,
        0.8,
        new StatusEffect(StatusEffect.Kind.JOLT, "Minor Jolting", "Minor jolting", 1, 0, 0.0),
        0,
        ElementType.ELECTRIC
    );
    Move m2 = new Move(
        "overcharge",
        "Overcharge Jab",
        "A fast jab that sparks.",
        1,
        0.7,
        new StatusEffect(StatusEffect.Kind.BURN, "Light Burn", "Light burning damage.", 1, 1, 0.0),
        0,
        ElementType.FIRE
    );
    e.addMove(m1); e.addMove(m2);
    break;

case "conductor_jeep":
    e = new SimpleEnemy("conductor_jeep", "Conductor ng Jeep", 13 + (currentStage-1)*1, 4, 3, 3 + currentStage, 1, 0.05);

    Move c1 = new Move(
        "shout_horn",
        "Shout & Horn Blast",
        "Blasts his horn and shouts orders, stunning you in confusion.",
        1,
        0.8,
        new StatusEffect(StatusEffect.Kind.STUN, "Shocked by Horn", "You are stunned for 1 turn.", 1, 0, 0.0),
        0,
        ElementType.NONE
    );
    Move c2 = new Move(
        "block_route",
        "Block Route",
        "Blocks the path, making it harder to react quickly.",
        1,
        0.7,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Conductor", "Your speed is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(c1); e.addMove(c2);
    break;

case "badjao_nanlilimos":
    e = new SimpleEnemy("badjao_nanlilimos", "Badjao na Nanlilimos", 12 + (currentStage-1)*1, 3, 2, 3 + currentStage, 1, 0.05);

    Move b1 = new Move(
        "dirty_water_splash",
        "Splash Dirty Water",
        "Throws dirty water that poisons you over time.",
        1,
        0.7,
        new StatusEffect(StatusEffect.Kind.POISON, "Contaminated Water", "You take poison damage for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    Move b2 = new Move(
        "grab_feet",
        "Grab Your Feet",
        "Trips you up, slowing your movement.",
        1,
        0.6,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Trip", "Your speed is reduced for 1 turn.", 1, 1, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(b1); e.addMove(b2);
    break;

case "tambay_7_11":
    e = new SimpleEnemy("tambay_7_11", "Tambay sa 7-11", 13 + (currentStage-1)*1, 3, 3, 3 + currentStage, 1, 0.05);

    Move t1 = new Move(
        "push_cart",
        "Push Cart",
        "Hits you with a shopping cart, lowering your defense.",
        1,
        0.7,
        new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Defense Lowered", "Your armor is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    Move t2 = new Move(
        "throw_slushie",
        "Throw Slushie",
        "Throws a slushie at you, stunning you for 1 turn.",
        1,
        0.8,
        new StatusEffect(StatusEffect.Kind.STUN, "Stunned by Slushie", "You are stunned for 1 turn.", 1, 0, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(t1); e.addMove(t2);
    break;

case "tau_gamma_member":
    e = new SimpleEnemy("tau_gamma_member", "Tau Gamma Member", 14 + (currentStage-1)*1, 4, 3, 3 + currentStage, 2, 0.05);

    Move gg1 = new Move(
        "mocking_shout",
        "Mocking Shout",
        "Intimidates you, lowering your critical hit chance.",
        1,
        0.8,
        new StatusEffect(StatusEffect.Kind.CRIT_DOWN, "Crit Down", "Your critical chance is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    Move gg2 = new Move(
        "chaotic_push",
        "Chaotic Push",
        "Pushes you around, slowing your actions.",
        1,
        0.7,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Push", "Your speed is reduced for 1 turn.", 1, 1, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(gg1); e.addMove(gg2);
    break;

case "tau_gamma_officer":
    e = new SimpleEnemy("tau_gamma_officer", "Tau Gamma Officer", 16 + (currentStage-1)*2, 5, 4, 4 + currentStage, 2, 0.06);

    Move o1 = new Move(
        "command_slam",
        "Command Slam",
        "Strikes with authority, stunning you.",
        1,
        0.85,
        new StatusEffect(StatusEffect.Kind.STUN, "Stunned by Officer", "You are stunned for 1 turn.", 1, 0, 0.0),
        0,
        ElementType.NONE
    );
    Move o2 = new Move(
        "armor_crush",
        "Armor Crush",
        "Crushes your defenses with heavy strikes, lowering your armor.",
        1,
        0.75,
        new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Defense Weakened", "Your armor is lowered for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(o1); e.addMove(o2);
    break;

// gulod enemies

case "gas_station_boy":
    // Gas Station Boy – quick but glassy, can POISON and SLOW
    e = new SimpleEnemy("gas_station_boy", "Gas Station Boy ng Total", 25 + (currentStage-1)*3, 6, 5, 6 + currentStage, 2, 0.08);

    Move gsb1 = new Move(
        "spill_fuel",
        "Spill Fuel",
        "Spills fuel at your feet, causing lingering poison.",
        2,
        0.9,
        new StatusEffect(StatusEffect.Kind.POISON, "Fuel Poison", "Take poison damage for 3 turns.", 3, 2 + currentStage/2, 0.0),
        0,
        ElementType.NONE
    );
    Move gsb2 = new Move(
        "slippery_rush",
        "Slippery Rush",
        "Dashes around recklessly, slowing your reaction time.",
        1,
        1.0,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Rush", "Your speed is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(gsb1); e.addMove(gsb2);
    break;

case "lpu_highschooler":
    // LPU Highschooler – annoying, can STUN and CRIT_DOWN
    e = new SimpleEnemy("lpu_highschooler", "LPU High Schooler", 17 + (currentStage-1)*2, 5, 6, 5 + currentStage, 2, 0.1);

    Move lpu1 = new Move(
        "loud_guitar",
        "Loud Guitar",
        "Plays a guitar loudly, stunning you in annoyance.",
        1,
        1.0,
        new StatusEffect(StatusEffect.Kind.STUN, "Stunned by Noise", "You are stunned for 1 turn.", 1, 0, 0.0),
        0,
        ElementType.NONE
    );
    Move lpu2 = new Move(
        "mocking_laugh",
        "Mocking Laugh",
        "Mocks your combat skills, lowering your critical chance.",
        1,
        0.9,
        new StatusEffect(StatusEffect.Kind.CRIT_DOWN, "Crit Down", "Your critical chance is reduced for 3 turns.", 3, 2 + currentStage/2, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(lpu1); e.addMove(lpu2);
    break;

case "fishball_vendor":
    // Nagbebenta ng Fishball – can ARMOR_DOWN and POISON
    e = new SimpleEnemy("fishball_vendor", "Nagbebenta ng Fishball", 19 + (currentStage-1)*3, 6, 4, 6 + currentStage, 2, 0.07);

    Move fv1 = new Move(
        "oily_throw",
        "Oily Throw",
        "Throws oily fishballs at you, reducing armor.",
        1,
        0.85,
        new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Defense Lowered", "Your armor is reduced for 3 turns.", 3, 2 + currentStage/2, 0.0),
        0,
        ElementType.NONE
    );
    Move fv2 = new Move(
        "spoiled_bite",
        "Spoiled Bite",
        "Throws a spoiled fishball, poisoning you.",
        1,
        0.9,
        new StatusEffect(StatusEffect.Kind.POISON, "Spoiled Fish Poison", "Take poison damage for 3 turns.", 3, 2 + currentStage/2, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(fv1); e.addMove(fv2);
    break;

case "dali_employee":
    // Dali Employee – fast, weak but multiple attacks, can SLOW
    e = new SimpleEnemy("dali_employee", "Dali Employee", 18 + (currentStage-1)*2, 7, 5, 5 + currentStage, 2, 0.1);

    Move de1 = new Move(
        "tray_smack",
        "Tray Smack",
        "Hits with a tray, slowing your speed.",
        1,
        0.9,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Tray", "Your speed is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    Move de2 = new Move(
        "coffee_spill",
        "Coffee Spill",
        "Spills hot coffee at you, basic damage.",
        1,
        1.0,
        null,
        0,
        ElementType.FIRE
    );
    e.addMove(de1); e.addMove(de2);
    break;

case "bayakos_tito":
    e = new SimpleEnemy("bayakos_tito", "Bayakos (Tito)", 35 + (currentStage-1)*5, 8, 5, 8 + currentStage, 2, 0.05);

    Move bt1 = new Move(
        "giant_slap",
        "Giant Slap",
        "A massive slap that stuns you.",
        2,
        1.0,
        new StatusEffect(StatusEffect.Kind.STUN, "Stunned by Tito", "You are stunned for 1 turn.", 1, 0, 0.0),
        0,
        ElementType.NONE
    );
    Move bt2 = new Move(
        "crushing_grip",
        "Crushing Grip",
        "Reduces your armor with a powerful grip.",
        2,
        1.0,
        new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Defense Crushed", "Your armor is reduced for 3 turns.", 3, 3 + currentStage/2, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(bt1); e.addMove(bt2);
    break;

case "samgyup_employee":
    // Samgyup Employee – medium tanky, SLOW + CRIT_DOWN
    e = new SimpleEnemy("samgyup_employee", "Samgyup Employee", 22 + (currentStage-1)*3, 6, 5, 6 + currentStage, 2, 0.08);

    Move se1 = new Move(
        "hot_plate_smash",
        "Hot Plate Smash",
        "Slams a hot plate, slowing your movements.",
        1,
        0.9,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Hot Plate", "Your speed is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.FIRE
    );
    Move se2 = new Move(
        "mocking_flavor",
        "Mocking Flavor",
        "Taunts you while cooking, lowering critical chance.",
        1,
        0.85,
        new StatusEffect(StatusEffect.Kind.CRIT_DOWN, "Crit Down", "Your critical chance is reduced for 3 turns.", 3, 2, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(se1); e.addMove(se2);
    break;


    //city hall enemies
case "calamares_vendor":
    e = new SimpleEnemy("calamares_vendor", "Calamares Vendor", 35 + (currentStage-1)*5, 8, 6, 7 + currentStage, 3, 0.08);

    Move cv1 = new Move(
        "squid_splash",
        "Squid Splash",
        "Throws oily calamares, poisoning you over time.",
        2,
        1.0,
        new StatusEffect(StatusEffect.Kind.POISON, "Calamares Poison", "Take poison damage for 3 turns.", 3, 3 + currentStage/2, 0.0),
        0,
        ElementType.NONE
    );
    Move cv2 = new Move(
        "hot_pan_smash",
        "Hot Pan Smash",
        "Slams a hot pan, lowering your armor.",
        2,
        1.0,
        new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Armor Cracked", "Your armor is reduced for 3 turns.", 3, 3 + currentStage/2, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(cv1); e.addMove(cv2);
    break;

case "security_guard":
    e = new SimpleEnemy("security_guard", "Security Guard", 40 + (currentStage-1)*6, 9, 7, 8 + currentStage, 3, 0.05);

    Move sg1 = new Move(
        "shotgun_blast",
        "Shotgun Blast",
        "Fires a shotgun at you, heavy damage.",
        2,
        1.1,
        null,
        0,
        ElementType.NONE
    );
    Move sg2 = new Move(
        "forceful_grab",
        "Forceful Grab",
        "Hits hard with brute force, lowering your armor.",
        2,
        0.95,
        new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Armor Broken", "Your armor is weakened for 3 turns.", 3, 3 + currentStage, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(sg1); e.addMove(sg2);
    break;

case "vilma_supporter":
    e = new SimpleEnemy("vilma_supporter", "Vilma Supporter", 32 + (currentStage-1)*4, 7, 5, 6 + currentStage, 2, 0.07);

    Move vs1 = new Move(
        "banner_hit",
        "Banner Hit",
        "Hits you with a campaign banner, slowing you.",
        1,
        0.85,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Banner", "Your speed is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    Move vs2 = new Move(
        "mocking_shout",
        "Mocking Shout",
        "Mocks your actions, lowering your critical chance.",
        1,
        0.8,
        new StatusEffect(StatusEffect.Kind.CRIT_DOWN, "Crit Down", "Your critical chance is reduced for 3 turns.", 3, 3, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(vs1); e.addMove(vs2);
    break;

case "cashier":
    e = new SimpleEnemy("cashier", "Cashier", 30 + (currentStage-1)*3, 6, 5, 5 + currentStage, 2, 0.08);

    Move cs1 = new Move(
        "scanner_beep",
        "Scanner Beep",
        "Scans items at your head, slowing your reactions.",
        1,
        0.8,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Scanner", "Your speed is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    Move cs2 = new Move(
        "expired_food",
        "Expired Food Toss",
        "Throws expired food, poisoning you.",
        1,
        0.85,
        new StatusEffect(StatusEffect.Kind.POISON, "Spoiled Food Poison", "You take poison damage for 3 turns.", 3, 2 + currentStage/2, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(cs1); e.addMove(cs2);
    break;

case "beking_official":
    e = new SimpleEnemy("beking_official", "Beking Official sa City Hall", 36 + (currentStage-1)*4, 7, 6, 6 + currentStage, 2, 0.09);

    Move bo1 = new Move(
        "fashion_slash",
        "Fashion Slash",
        "Strikes flamboyantly, slowing you.",
        1,
        0.9,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Flair", "Your speed is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    Move bo2 = new Move(
        "taunt_throw",
        "Taunt Throw",
        "Throws sarcasm at you, lowering critical chance.",
        1,
        0.85,
        new StatusEffect(StatusEffect.Kind.CRIT_DOWN, "Crit Down", "Your critical chance is reduced for 3 turns.", 3, 3, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(bo1); e.addMove(bo2);
    break;

case "mama_nagreklamo":
    e = new SimpleEnemy("mama_nagreklamo", "Mama na Nagrereklamo", 38 + (currentStage-1)*5, 8, 5, 7 + currentStage, 2, 0.07);

    Move mr1 = new Move(
        "loud_complaint",
        "Loud Complaint",
        "Complains loudly, slowing your actions.",
        1,
        0.85,
        new StatusEffect(StatusEffect.Kind.SLOW, "Slowed by Complaint", "Your speed is reduced for 2 turns.", 2, 1, 0.0),
        0,
        ElementType.NONE
    );
    Move mr2 = new Move(
        "slam_bag",
        "Slam Bag",
        "Slams a bag at you, lowering your armor.",
        2,
        0.9,
        new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Armor Lowered", "Your armor is reduced for 3 turns.", 3, 3 + currentStage/2, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(mr1); e.addMove(mr2);
    break;

case "scholar_ebd":
    e = new SimpleEnemy("scholar_ebd", "Scholar ni EBD", 40 + (currentStage-1)*5, 8, 6, 7 + currentStage, 2, 0.08);

    Move sebd1 = new Move(
        "flashy_wallet",
        "Flashy Wallet Hit",
        "Shows off cash and hits you, stunning you.",
        1,
        0.9,
        new StatusEffect(StatusEffect.Kind.STUN, "Stunned by Flashy Wallet", "You are stunned for 1 turn.", 1, 0, 0.0),
        0,
        ElementType.NONE
    );
    Move sebd2 = new Move(
        "money_spray",
        "Money Spray",
        "Throws cash at you, poisoning you with greed.",
        1,
        0.85,
        new StatusEffect(StatusEffect.Kind.POISON, "Greed Poison", "Take poison damage for 3 turns.", 3, 3, 0.0),
        0,
        ElementType.NONE
    );
    e.addMove(sebd1); e.addMove(sebd2);
    break;





            case "hipon":
                // Boss: Very High Evasion (0.25)
                e = new Boss("hipon", "Hipon of Libjo", 25 + (currentStage - 1)*5, 4, 4, 5 + currentStage, 1, 1, 0.40, 0.35);
                Move h1 = new Move("tentacle_lash", "Tentacle Lash", "A sticky tentacle lash.", 2, 1.0, new StatusEffect(StatusEffect.Kind.CRIT_DOWN, "Wet Goo", "Slippery -crit down", 2, 0, -0.10), 3, ElementType.NONE);
                Move h2 = new Move("ink_blast", "Ink Blast", "Dark ink that chills and reduces attack.", 3, 1.1,new StatusEffect(StatusEffect.Kind.GENERIC, "Chilled Ink", "Reduces attack", 2, 0, -0.10), 2, ElementType.ICE);
                e.addMove(h1); e.addMove(h2);
                break;

            case "gulod":
                // Boss: Low Evasion (0.05) - Tanky, heavily armored
                e = new Boss("gulod", "Terror of Gulod", 55 + (currentStage - 1)*8, 3, 4, 8 + currentStage, 3, 2, 0.10, 0.05);
                Move gA = new Move("earth_slam", "Earth Slam", "Smashes the ground.", 2, 1.0, null, 0, ElementType.NONE);
                Move gB = new Move("lava_fury", "Lava Fury", "Burning onslaught.", 4, 1.4, new StatusEffect(StatusEffect.Kind.BURN, "Lava Burn", "Burning damage", 3, 2, 0.0), 3, ElementType.FIRE);
                e.addMove(gA); e.addMove(gB);
                break;

            case "sales":
                // Boss: High Evasion (0.20) - Mirage/Illusion-based
                e = new Boss("sales", "Sales the Corrupt Mirage", 80 + (currentStage - 1)*12, 4, 4, 10 + currentStage, 4, 3, 0.05, 0.20);
                Move sA = new Move("mirage_blade", "Mirage Blade", "Spectral slashes from illusions.", 2, 1.0, null, 0, ElementType.NONE);
                Move sB = new Move("electro_contract", "Electro Contract", "A shocking contract that jolts and spreads.", 3, 1.1, new StatusEffect(StatusEffect.Kind.JOLT, "Contracted Jolt", "Chance to stun", 1, 0, 0.0), 1, ElementType.ELECTRIC);
                e.addMove(sA); e.addMove(sB);
                break;

            case "dechavez":
                // Boss: Moderate Evasion (0.10) - Highly precise and skilled
                e = new Boss("dechavez", "DeChavez, The True Chairman", 120 + (currentStage-1)*20, 5, 4, 14 + currentStage, 6, 4, 0.02, 0.10);
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
        if (currentStage == 1) 
    pool = new String[] {
        "performativevaper", 
        "tdro_talipapa", 
        "meralco_reader", 
        "conductor_jeep", 
        "badjao_nanlilimos", 
        "tambay_7_11", 
        "tau_gamma_member", 
        "tau_gamma_officer"
    };
else if (currentStage == 2) 
    pool = new String[] {
        "gas_station_boy", 
        "lpu_highschooler", 
        "fishball_vendor", 
        "dali_employee", 
        "bayakos_tito", 
        "samgyup_employee"
    };
else 
    pool = new String[] {
        "calamares_vendor", 
        "security_guard", 
        "vilma_supporter", 
        "cashier", 
        "beking_official", 
        "mama_nagreklamo", 
        "scholar_ebd"
    };


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