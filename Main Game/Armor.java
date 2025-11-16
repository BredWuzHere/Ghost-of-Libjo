    public class Armor extends Item {
        public int armorRating;
        public double resistance;
        public int durability;

        public Armor(String id, String name, String description, int armorRating, double resistance, int durability) {
            super(id,name,description);
            this.armorRating = armorRating; this.resistance = resistance; this.durability = durability;
        }
    }
