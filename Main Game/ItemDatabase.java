import java.util.*;

public class ItemDatabase {
    private static Map<String, Item> prototypes = new HashMap<>();
    private static Random rnd = new Random();
    private static int currentStage = 1;

    static {
        prototypes.put("iron_sword", new Weapon("iron_sword","Iron Sword","A sturdy iron sword.",6,0.05,1.5,100,ElementType.NONE));
        prototypes.put("fire_staff", new Weapon("fire_staff","Fire Staff","A staff imbued with flame.",5,0.08,1.6,100,ElementType.FIRE));
        prototypes.put("leather_armor", new Armor("leather_armor","Leather Armor","Light protective leather.",1,0.05,100));
        prototypes.put("chain_armor", new Armor("chain_armor","Chain Armor","Better protection.",3,0.10,100));
        prototypes.put("balat_ng_hipon", new Armor("balat_ng_hipon", "Balat ng Hipon", "Matigas sa labas, lambot sa loob.",2,0.00,100));
        prototypes.put("kalawang_armor", new Armor("kalawang_armor", "Kalawang Armor", "Made from pure, authentic, Gulod rust.",4,0.00,100));
        prototypes.put("construction_vest", new Armor("construction_vest","Construction Vest","Safety first, kahit sa gulod battles.",4,0.00,120));
        prototypes.put("ukayukay_jacket", new Armor("ukayukay_jacket","Ukay-Ukay Jacket","Vintage jacket na amopy lumang aparador",3,0.07,90));
        prototypes.put("relic_plus_ap", new Relic("relic_plus_ap","Relic of Swiftness","+1 Max AP.",1,0.0,0.0,0));
        prototypes.put("relic_crit", new Relic("relic_crit","Relic of Precision","+10% Crit Chance.",0,0.10,0.0,0));
        prototypes.put("hp_potion", new Consumable("hp_potion","Health Potion","Restores 10 HP.", Consumable.ConsumableType.HEAL, 10));
        prototypes.put("elixir_of_life", new Relic("elixir_of_life","Elixir of Life","Permanently increases Max HP by 50.",0,0.0,0.0,50));
        prototypes.put("dev_sword", new Weapon("dev_sword","Dev Sword","A dev sword.",2000,100,10.5,100,ElementType.NONE));
        prototypes.put("hipon_tentacle", new Weapon("hipon_tentacle","Hipon Tentacle","A strange slippery tentacle.",2,0.15,1.8,70,ElementType.NONE));
        prototypes.put("gulod_amulet", new Relic("gulod_amulet","Gulod Amulet","Warm, throbbing with heat. Grants modest strength and resilience.",5,0.20,0.10,18));
        prototypes.put("dechavez_claw", new Weapon("dechavez_claw","DeChavez Claw","Sharp and ledger-lined.",8,0.08,1.6,90,ElementType.NONE));
        prototypes.put("mirage_shard", new Weapon("mirage_shard","Mirage Shard","Shimmers with illusion.",22,0.50,2.5,100,ElementType.ICE));
        prototypes.put("blade_of_hepatytis", new Weapon("blade_of_hepatytis","Blade of Hepatytis","penetration.", 18, 0.10, 1.2, 100, ElementType.NONE));
        prototypes.put("fishball_stick", new Weapon("fishball_stick","Fishball Stick Stabber","small but terrible", 16, 0.12, 1.4, 100, ElementType.NONE));
        prototypes.put("electricfan_blade", new Weapon("electricfan_blade","Electric Fan Blade Toss", "Sharp fan blade. Hard to aim but deadly.",   35, 0.05, 0.9, 100, ElementType.NONE));
    }

    public static void setStage(int stage) { currentStage = Math.max(1, stage); }

    public static Item createItem(String id) {
        Item proto = prototypes.get(id);
        if (proto == null) return null;
        if (proto instanceof Weapon) {
            Weapon p = (Weapon) proto;
            Weapon w = new Weapon(p.getId(), p.getName(), p.getDescription(), p.getDamage(), p.critChance, p.critDamage, p.getDurability(), p.getElement());
            if ("fire_staff".equals(id)) {
                StatusEffect burn = new StatusEffect(StatusEffect.Kind.BURN,"Ignition","Burns for a few HP/turn",3,4,0.0);
                Move m1 = new Move("fire_burst","Fire Burst","Engulfs enemy in flames.",2,1.0,burn,3,ElementType.FIRE);
                Move m2 = new Move("ember_slap","Ember Slap","A quick ember strike.",1,0.7,null,0,ElementType.FIRE);
                w.addMove(m1); w.addMove(m2);
            } else if ("fishball_stick".equals(id)) {
                Move m1 = new Move("tusok_strike","Tusok Strike","A quick tusok.",2,8.0,null,0,ElementType.NONE);
                Move m2 = new Move("fishball_fury","Fishball Fury","Unleash the vendors fishball.",3,1.3,null,0,ElementType.NONE);
                w.addMove(m1); w.addMove(m2);

            } else if ("blade_of_hepatytis".equals(id)) {
                Move m1 = new Move("toxic_slash","Toxic Slash","A contaminated blade swipe.",2,1.1,null,0,ElementType.POISON);
                Move m2 = new Move("viral_cleave","Viral Cleave","A heavy infected strike.",3,1.6,null,0,ElementType.POISON);
                w.addMove(m1); w.addMove(m2);

            } else if ("de_chavez_claw".equals(id)) {
                Move m1 = new Move("scratch","Scratch","A sharp claw swipe.",2,1.1,null,0,ElementType.NONE);
                Move m2 = new Move("rabid_rip","Rabid Rip","A wild tearing attack.",3,1.6,null,0,ElementType.NONE);
                w.addMove(m1); w.addMove(m2);
            
            } else if ("electricfan_blade".equals(id)) {
                Move m1 = new Move("fan_slash","Fan Slash","A spinning slice from a broken electric fan.",2,1.1,null,0,ElementType.NONE);
                Move m2 = new Move("live_wire_spin","Live Wire Spin","A charged spinning attack.",3,1.3,null,0,ElementType.ELECTRIC);
                w.addMove(m1); w.addMove(m2);
            
            } else if ("iron_sword".equals(id)) {
                Move m1 = new Move("slash","Slash","A basic slash.",2,1.0,null,0,ElementType.NONE);
                Move m2 = new Move("cleave","Cleave","A heavy cleave.",3,1.6,null,0,ElementType.NONE);
                w.addMove(m1); w.addMove(m2);

            } else if ("hipon_tentacle".equals(id)) {
                Move m1 = new Move("slimy_whip","Slimy Whip","A sticky tentacle strike that slightly reduces enemy speed.",2,1.0,null,0,ElementType.NONE);
                Move m2 = new Move("hipon_frenzy","Hipon Frenzy","Rapid multi-tentacle assault.",3,1.6,null,0,ElementType.NONE);
                w.addMove(m1); w.addMove(m2);

            } else if ("dev_sword".equals(id)) {
    // FIX: Added the specific enum value JOLT
                StatusEffect jolt = new StatusEffect(StatusEffect.Kind.JOLT, "Jolts", "Electrifies enemies and stuns for a few HP/turn", 1, 100, 0.0);
    
                Move m1 = new Move("equinox_slash", "Equinox Slash", "Obliterate Foes In One hit.", 0, 10.0, null, 0, ElementType.NONE);
                Move m2 = new Move("suayan_move", "Suayan Strike", "Trash talk the enemies.", 0, 100.6, jolt, 5, ElementType.ELECTRIC);
                w.addMove(m1); w.addMove(m2);
            } else {
                Move m = new Move("bash","Bash","A simple bash.",2,1.0,null,0,ElementType.NONE); w.addMove(m);
            }
            return w;
        } else if (proto instanceof Armor) {
            Armor p = (Armor) proto;
            return new Armor(p.getId(), p.getName(), p.getDescription(), p.armorRating, p.resistance, p.durability);
        } else if (proto instanceof Relic) {
            Relic p = (Relic) proto;
            return new Relic(p.getId(), p.getName(), p.getDescription(), p.extraAp, p.critBonus, p.resistanceBonus, p.addMaxHp);
        } else if (proto instanceof Consumable) {
            Consumable p = (Consumable) proto;
            return new Consumable(p.getId(), p.getName(), p.getDescription(), p.type, p.amount);
        } else {
            return new Item(proto.getId(), proto.getName(), proto.getDescription()) {};
        }
    }

    public static Weapon createWeapon(String id){ return (Weapon) createItem(id); }
    public static Armor createArmor(String id){ return (Armor) createItem(id); }
    public static Relic createRelic(String id){ return (Relic) createItem(id); }
    public static Consumable createConsumable(String id){ return (Consumable) createItem(id); }

    public static Item createRandomLootForRegion(int region) {
        List<String> pool = new ArrayList<>();
        if (currentStage==1) { pool.add("hp_potion"); pool.add("iron_sword"); pool.add("leather_armor"); pool.add("relic_crit"); pool.add("fishball_stick"); pool.add("balat_ng_hipon"); pool.add("kalawang_armor"); pool.add("ukayukay_jacket"); pool.add("hipon_tentacle"); }
        else if (currentStage==2) { pool.add("hp_potion"); pool.add("fire_staff"); pool.add("chain_armor"); pool.add("relic_plus_ap"); pool.add("electricfan_blade"); pool.add("construction_vest"); pool.add("blase_of_hepatytis"); pool.add("de_chavez_claw");}
        else { pool.add("hp_potion"); pool.add("fire_staff"); pool.add("chain_armor"); pool.add("relic_crit"); }
        return createItem(pool.get(rnd.nextInt(pool.size())));
    }
}
