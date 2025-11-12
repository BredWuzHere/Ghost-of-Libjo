import java.util.*;
public class Weapon extends Item {
    private int damage; double critChance; double critDamage; private int durability; private ElementType element; private List<Move> moves = new ArrayList<>();
    public Weapon(String id, String name, String desc, int damage, double critChance, double critDamage, int durability, ElementType element) {
        super(id,name,desc); this.damage=damage; this.critChance=critChance; this.critDamage=critDamage; this.durability=durability; this.element=element;
    }
    public int getDamage(){return damage;} public int getDurability(){return durability;} public ElementType getElement(){return element;} public List<Move> getMoves(){return moves;} public void addMove(Move m){moves.add(m);}
    public int getEffectiveDamage(){ double frac=durability/100.0; double penalty=1.0; if(frac<=0) return 1; if(frac<=0.25) penalty=0.6; else if(frac<=0.5) penalty=0.8; else if(frac<=0.75) penalty=0.9; return (int)Math.max(1, Math.round(damage*penalty)); }
    public void performMove(int moveIndex, Player actor, Enemy target) {
        if (moveIndex<0||moveIndex>=moves.size()) { System.out.println("Invalid move."); return; }
        Move m = moves.get(moveIndex);
        if (actor.getAp() < m.getApCost()) { System.out.println("Not enough AP for " + m.getName()); return; }
        actor.spendAp(m.getApCost());
        int base = getEffectiveDamage();
        double dmg = base * m.getDamageMultiplier();
        double critTotal = actor.getCritChance() + this.critChance;
        boolean crit = Math.random() < critTotal;
        if (crit) dmg = dmg * this.critDamage;
        int finalDmg = Math.max(0, (int)Math.round(dmg));
        System.out.println(actor.getName() + " uses " + m.getName() + " — " + m.getDescription());
        if (crit) System.out.println("Critical hit!");
        target.takeDamage(finalDmg);
        if (m.getApplyStatus() != null) {
            StatusEffect s = new StatusEffect(m.getApplyStatus().getKind(), m.getApplyStatus().getName(), m.getApplyStatus().getDescription(), m.getStatusDuration(), m.getApplyStatus().getIntValue(), m.getApplyStatus().getDblValue());
            target.applyStatus(s);
            System.out.println(target.getName() + " is affected by " + s.getName() + " (" + s.getDescription() + ") for " + s.getRemainingTurns() + " turns.");
        }
    }
}
