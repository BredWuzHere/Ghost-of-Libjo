import java.util.*;

public class ItemDatabase {
    private static Map<String, Item> prototypes = new HashMap<>();
    private static Random rnd = new Random();
    static {
        // Weapons
        prototypes.put("iron_sword", new Weapon("iron_sword","Iron Sword","A sturdy iron sword.", 6, 0.05, 1.5, 100, ElementType.NONE));
        prototypes.put("fire_staff", new Weapon("fire_staff","Fire Staff","A staff imbued with flame.", 5, 0.08, 1.6, 100, ElementType.FIRE));
        // Armor
        prototypes.put("leather_armor", new Armor("leather_armor","Leather Armor","Light protective leather.", 1, 0.05, 100));
        prototypes.put("chain_armor", new Armor("chain_armor","Chain Armor","Better protection.", 3, 0.10, 100));
        // Relics
        prototypes.put("relic_plus_ap", new Relic("relic_plus_ap","Relic of Swiftness","+1 Max AP.", 1, 0.0, 0.0, 0));
        prototypes.put("relic_crit", new Relic("relic_crit","Relic of Precision","+10% Crit Chance.", 0, 0.10, 0.0, 0));
        // Consumables
        prototypes.put("hp_potion", new Consumable("hp_potion","Health Potion","Restores 10 HP.", ConsumableType.HEAL, 10));
    }

    public static Item createItem(String id) {
        Item proto = prototypes.get(id);
        if (proto == null) return null;
        // clone by creating new instances depending on type
        if (proto instanceof Weapon) {
            Weapon p = (Weapon)proto;
            return new Weapon(p.getId(), p.getName(), p.getDescription(), p.getDamage(), p.critChance, p.critDamage, p.getDurability(), p.getElement());
        } else if (proto instanceof Armor) {
            Armor p = (Armor)proto;
            return new Armor(p.getId(), p.getName(), p.getDescription(), p.armorRating, p.resistance, p.durability);
        } else if (proto instanceof Relic) {
            Relic p = (Relic)proto;
            return new Relic(p.getId(), p.getName(), p.getDescription(), p.extraAp, p.critBonus, p.resistanceBonus, p.addMaxHp);
        } else if (proto instanceof Consumable) {
            Consumable p = (Consumable)proto;
            return new Consumable(p.getId(), p.getName(), p.getDescription(), p.type, p.amount);
        }
        return null;
    }

    public static Weapon createWeapon(String id) { return (Weapon)createItem(id); }
    public static Armor createArmor(String id) { return (Armor)createItem(id); }
    public static Relic createRelic(String id) { return (Relic)createItem(id); }
    public static Consumable createConsumable(String id) { return (Consumable)createItem(id); }

    public static Item createRandomLootForRegion(int region) {
        // simple random selection
        List<String> pool = new ArrayList<>();
        pool.add("hp_potion"); pool.add("iron_sword"); pool.add("leather_armor"); pool.add("relic_crit");
        String pick = pool.get(rnd.nextInt(pool.size()));
        return createItem(pick);
    }
}
