public class Consumable extends Item {
    public enum ConsumableType { HEAL, BUFF }
    ConsumableType type; int amount;
    public Consumable(String id, String name, String desc, ConsumableType type, int amount) { super(id,name,desc); this.type=type; this.amount=amount; }
    public void use(Player p) {
        if (type==ConsumableType.HEAL) { p.heal(amount); System.out.println(p.getName() + " healed " + amount + " HP."); }
        else System.out.println("Used " + getName());
    }
}
