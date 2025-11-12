public class Armor extends Item {
    int armorRating; double resistance; int durability;
    public Armor(String id, String name, String desc, int armorRating, double resistance, int durability) { super(id,name,desc); this.armorRating=armorRating; this.resistance=resistance; this.durability=durability; }
    public int getArmorRating(){return armorRating;} public double getResistance(){return resistance;} public int getDurability(){return durability;}
}
