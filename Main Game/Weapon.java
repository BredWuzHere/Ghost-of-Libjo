public class Weapon extends Item {
    private int damage;
    double critChance;
    double critDamage;
    private int durability; // 0-100
    private ElementType element;

    public Weapon(String id, String name, String description, int damage, double critChance, double critDamage, int durability, ElementType element) {
        super(id,name,description);
        this.damage = damage; this.critChance = critChance; this.critDamage = critDamage; this.durability = durability; this.element = element;
    }

    public int getDamage() { return damage; }
    public int getDurability() { return durability; }

    public int getEffectiveDamage() {
        if (durability <= 0) return Math.max(1, (int)Math.round(damage * 0.25));
        double frac = durability / 100.0;
        // for every 25% lost (<=0.75, <=0.5, <=0.25) reduce damage by 20% each
        double modifier = 1.0;
        if (frac <= 0.75) modifier -= 0.2;
        if (frac <= 0.50) modifier -= 0.2;
        if (frac <= 0.25) modifier -= 0.2;
        int eff = (int)Math.round(damage * modifier);
        return Math.max(1, eff);
    }

    public void degrade(int amount) {
        durability = Math.max(0, durability - amount);
        if (durability == 0) System.out.println(name + " broke!");
    }

    public ElementType getElement() { return element; }
}
