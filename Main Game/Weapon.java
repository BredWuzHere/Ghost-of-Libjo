import java.util.*;

public class Weapon extends Item {
    private int damage;
    public double critChance;
    public double critDamage;
    private int durability;
    private ElementType element;
    private List<Move> moves = new ArrayList<>();

    public Weapon(String id, String name, String description, int damage, double critChance, double critDamage, int durability, ElementType element) {
        super(id,name,description);
        this.damage = damage; this.critChance = critChance; this.critDamage = critDamage; this.durability = durability; this.element = element;
    }

    public int getDamage(){ return damage; }
    public int getDurability(){ return durability; }
    public ElementType getElement(){ return element; }
    public List<Move> getMoves(){ return moves; }
    public void addMove(Move m){ moves.add(m); }

    public int getEffectiveDamage() {
        double pct = (100 - durability) / 100.0;
        int reductions = (int)(pct / 0.25);
        double mult = 1.0;
        for (int i=0;i<reductions;i++) mult *= 0.8;
        return Math.max(1, (int)Math.round(damage * mult));
    }

    public void performMove(int moveIndex, Player user, Enemy target) {
        if (moveIndex < 0 || moveIndex >= moves.size()) return;
        Move m = moves.get(moveIndex);
        m.perform(user, target, null);
        durability = Math.max(0, durability - 5);
        if (durability == 0) System.out.println(Color.BRIGHT_RED + name + " is now broken!" + Color.RESET);
    }
}
