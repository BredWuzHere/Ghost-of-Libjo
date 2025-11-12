// ItemDatabase.java
import java.util.*;
 
public class ItemDatabase {
    private static Map<String, Item> prototypes = new HashMap<>();
    private static Random rnd = new Random();
    private static int currentStage = 1;

    static {
        // Weapons
        prototypes.put("iron_sword", new Weapon("iron_sword","Iron Sword","A sturdy iron sword.",6,0.05,1.5,100,ElementType.NONE));
        prototypes.put("fire_staff", new Weapon("fire_staff","Fire Staff","A staff imbued with flame.",5,0.08,1.6,100,ElementType.FIRE));
        prototypes.put("dev_sword", new Weapon("dev_sword","Dev Sword","A dev sword.",2000,1.0,10.5,100,ElementType.NONE));

        // Armor
        prototypes.put("leather_armor", new Armor("leather_armor","Leather Armor","Light protective leather.",1,0.05,100));
        prototypes.put("chain_armor", new Armor("chain_armor","Chain Armor","Better protection.",3,0.10,100));

        // Relics
        prototypes.put("relic_plus_ap", new Relic("relic_plus_ap","Relic of Swiftness","+1 Max AP.",1,0.0,0.0,0));
        prototypes.put("relic_crit", new Relic("relic_crit","Relic of Precision","+10% Crit Chance.",0,0.10,0.0,0));
        prototypes.put("elixir_of_life", new Relic("elixir_of_life","Elixir of Life","Permanently increases Max HP by 50.",0,0.0,0.0,50));

        // Consumables
        prototypes.put("hp_potion", new Consumable("hp_potion","Health Potion","Restores 10 HP.", Consumable.ConsumableType.HEAL, 10));
    }

    public static void setStage(int stage) {
        currentStage = Math.max(1, stage);
    }

    public static Item createItem(String id) {
        Item proto = prototypes.get(id);
        if (proto == null) return null;

        if (proto instanceof Weapon) {
            Weapon p = (Weapon) proto;
            Weapon w = new Weapon(p.getId(), p.getName(), p.getDescription(), p.getDamage(), p.critChance, p.critDamage, p.getDurability(), p.getElement());

            // Assign moves depending on weapon
            switch (id) {
                case "fire_staff":
                    StatusEffect burn = new StatusEffect(StatusEffect.Kind.BURN,"Ignition","Burns for 4 HP/turn",3,4,0.0);
                    Move m1 = new Move("fire_burst","Fire Burst","Engulfs enemy in flames.",2,1.0,burn,3, ElementType.FIRE);
                    Move m2 = new Move("ember_slap","Ember Slap","A quick ember strike.",1,0.7,null,0, ElementType.FIRE);
                    w.addMove(m1); w.addMove(m2);
                    break;

                case "iron_sword":
                    Move s1 = new Move("slash","Slash","A basic slash.",2,1.0,null,0, ElementType.NONE);
                    Move s2 = new Move("cleave","Cleave","A heavy cleave.",3,1.6,null,0, ElementType.NONE);
                    w.addMove(s1); w.addMove(s2);
                    break;

                case "dev_sword":
                    Move d1 = new Move("equinox_slash","Equinox Slash","Obliterates foes in one hit.",0,10.0,null,0, ElementType.NONE);
                    Move d2 = new Move("suayans_move","The Power of Suayan","Uses the soul of Suayan to trash talk the enemy.",0,100.6,null,0, ElementType.NONE);
                    w.addMove(d1); w.addMove(d2);
                    break;

                default:
                    Move defaultMove = new Move("bash","Bash","A simple bash.",2,1.0,null,0, ElementType.NONE);
                    w.addMove(defaultMove);
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
        }

        return null;
    }

    public static Weapon createWeapon(String id) {
        return (Weapon) createItem(id);
    }

    public static Armor createArmor(String id) {
        return (Armor) createItem(id);
    }

    public static Relic createRelic(String id) {
        return (Relic) createItem(id);
    }

    public static Consumable createConsumable(String id) {
        return (Consumable) createItem(id);
    }

    // Loot generator
    public static Item createRandomLootForRegion(int region) {
        List<String> pool = new ArrayList<>();
        if (currentStage == 1) {
            pool.add("hp_potion"); pool.add("iron_sword"); pool.add("leather_armor"); pool.add("relic_crit");
        } else if (currentStage == 2) {
            pool.add("hp_potion"); pool.add("fire_staff"); pool.add("chain_armor"); pool.add("relic_plus_ap");
        } else {
            pool.add("hp_potion"); pool.add("fire_staff"); pool.add("chain_armor"); pool.add("relic_crit");
        }
        return createItem(pool.get(rnd.nextInt(pool.size())));
    }
}
