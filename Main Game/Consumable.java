public class Consumable extends Item {
    public enum ConsumableType { HEAL, BUFF }
    public ConsumableType type;
    public int amount;

    public Consumable(String id, String name, String description, ConsumableType type, int amount) {
        super(id,name,description); this.type = type; this.amount = amount;
    }

    public void use(Player p) {
        if (type == ConsumableType.HEAL) {
            p.heal(amount);
            System.out.println(p.getName() + " heals " + amount + " HP.");
        } else {
            System.out.println("Used consumable with no effect.");
        }
    }
}
