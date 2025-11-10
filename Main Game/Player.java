import java.util.ArrayList;

public class Player {
    private String name;
    private int maxHp;
    private int hp;
    private int speed;
    private int maxApBase;
    private int ap;
    private int armorRatingBase;
    private double resistanceBase; // percent 0-1
    private ArrayList<Item> inventory = new ArrayList<>();
    private Weapon weapon;
    private Armor armor;
    private Relic equippedRelic;
    private int x,y;

    public Player(String name, int maxHp, int speed, int maxApBase) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.speed = speed;
        this.maxApBase = maxApBase;
        this.ap = maxApBase;
        this.armorRatingBase = 0;
        this.resistanceBase = 0.0;
    }

    public int attack() {
        double critChance = getCritChance();
        double critDamage = 1.5; // base
        int damage = 1; // fist
        if (weapon != null) damage = weapon.getEffectiveDamage();
        boolean crit = Math.random() < critChance;
        int result = (int)Math.round(damage * (crit ? critDamage : 1.0));
        // degrade weapon durability slightly
        if (weapon != null) weapon.degrade(5);
        return Math.max(0, result);
    }

    public void takeDamage(int dmg) {
        int effectiveArmor = getArmorRating();
        double resist = getResistance();
        int reduced = Math.max(0, dmg - effectiveArmor);
        int finalDmg = (int)Math.round(reduced * (1.0 - resist));
        this.hp -= finalDmg;
        System.out.println("You took " + finalDmg + " damage (after armor/resist). HP=" + Math.max(0,hp) + "/" + maxHp);
    }

    public boolean isAlive() { return hp > 0; }

    public void resetAp() { this.ap = getMaxAp(); }

    public void spendAp(int n) { this.ap = Math.max(0, this.ap - n); }

    public void heal(int amount) { this.hp = Math.min(maxHp, hp + amount); }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getSpeed() { return speed; }
    public int getAp() { return ap; }
    public int getMaxAp() { return maxApBase + getExtraApFromRelics(); }
    public ArrayList<Item> getInventory() { return inventory; }
    public Weapon getWeapon() { return weapon; }
    public Armor getArmor() { return armor; }

    public void equipWeapon(Weapon w) { this.weapon = w; }
    public void equipArmor(Armor a) { this.armor = a; }

    public int getArmorRating() {
        int ar = armorRatingBase + (armor != null ? armor.getEffectiveArmorRating() : 0);
        return ar;
    }
    public double getResistance() {
        double res = resistanceBase + (armor != null ? armor.getEffectiveResistance() : 0.0);
        // relics can increase resistance
        for (Item it : inventory) if (it instanceof Relic) res += ((Relic)it).getResistanceBonus();
        if (equippedRelic != null) res += equippedRelic.getResistanceBonus();
        return Math.min(0.9, res);
    }
    public double getCritChance() {
        double cc = 0.05; // base 5%
        for (Item it : inventory) if (it instanceof Relic) cc += ((Relic)it).getCritBonus();
        if (equippedRelic != null) cc += equippedRelic.getCritBonus();
        return Math.min(0.9, cc);
    }
    public int getExtraApFromRelics() {
        int extra = 0;
        for (Item it : inventory) if (it instanceof Relic) extra += ((Relic)it).getExtraAp();
        if (equippedRelic != null) extra += equippedRelic.getExtraAp();
        return extra;
    }

    public void setEquippedRelic(Relic r) { this.equippedRelic = r; }

    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void resetStatsAfterEquip() {
        // placeholder if we had complex recalculation
    }

    public java.util.List<Consumable> getConsumables() {
        java.util.List<Consumable> list = new java.util.ArrayList<>();
        for (Item it : inventory) if (it instanceof Consumable) list.add((Consumable)it);
        return list;
    }

    // helpers for other classes
    public void addMaxHp(int amt) { this.maxHp += amt; this.hp += amt; }
    public void addCritChance(double v) { /* handled dynamically by relics */ }

}
