import java.util.Objects;

public class Consumable extends Item {
    public enum ConsumableType { HEAL, BUFF_DEFENSE, BUFF_ATTACK }
    public ConsumableType type;
    public int amount;

    public Consumable(String id, String name, String description, ConsumableType type, int amount) {
        super(id, name, description);
        this.type = Objects.requireNonNull(type, "Consumable type");
        this.amount = amount;
    }

    public void use(Player p) {
        switch (type) {
            case HEAL -> {
                p.heal(amount);
                System.out.println(Color.GREEN + p.getName() + " heals " + amount + " HP." + Color.RESET);
            }
            case BUFF_DEFENSE -> {
                    if (amount > 0) {
                        // Buff for 2 or 3 turns (randomly choose 2 or 3)
                        int turns = (Math.random() < 0.5) ? 2 : 3;
                        p.addBuff(amount, turns);
                        System.out.println(Color.CYAN + p.getName() + " defense buffs " + amount + " for " + turns + " turns." + Color.RESET);
                }
            }
            case BUFF_ATTACK -> {
                if (amount > 0) {
                    int turns = (Math.random() < 0.5) ? 2 : 3;
                    p.addAttackBuff(amount, turns);
                    System.out.println(Color.CYAN + p.getName() + " attack buffs " + amount + " for " + turns + "turns." + Color.RESET);
                }
            }
        }
    }
}
