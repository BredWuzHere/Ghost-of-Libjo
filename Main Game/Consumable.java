public class Consumable extends Item {
    ConsumableType type;
    int amount;

    public Consumable(String id, String name, String description, ConsumableType type, int amount) {
        super(id,name,description);
        this.type = type; this.amount = amount;
    }

    public void use(Player p) {
        switch (type) {
            case HEAL:
                p.heal(amount);
                System.out.println("You restored " + amount + " HP.");
                break;
            case TEMP_BUFF:
                System.out.println("You feel empowered briefly (not implemented complex buffs).\n");
                break;
        }
    }
}
