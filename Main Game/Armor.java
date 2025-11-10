public class Armor extends Item {
    int armorRating;
    double resistance;
    int durability;

    public Armor(String id, String name, String description, int armorRating, double resistance, int durability) {
        super(id,name,description);
        this.armorRating = armorRating; this.resistance = resistance; this.durability = durability;
    }

    public int getEffectiveArmorRating() {
        if (durability <= 0) return 0;
        double frac = durability/100.0;
        double mod = 1.0;
        if (frac <= 0.75) mod -= 0.15;
        if (frac <= 0.50) mod -= 0.15;
        if (frac <= 0.25) mod -= 0.15;
        return (int)Math.round(armorRating * mod);
    }
    public double getEffectiveResistance() {
        if (durability <= 0) return 0.0;
        return resistance;
    }

    public void degrade(int amt) { durability = Math.max(0, durability-amt); if (durability==0) System.out.println(name + " is ruined!"); }
}
