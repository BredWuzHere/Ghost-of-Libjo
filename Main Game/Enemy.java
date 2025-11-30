// Patched Enemy.java - uses unified statuses and speed/evasion math
import java.util.*;

public class Enemy extends Entity {
    protected List<Move> moves = new ArrayList<>();
    protected int stage = 1;
    protected double evasion; // 0.0 .. 1.0

    public Enemy(String id,String name,int maxHp,int speed,int ap,int damage,int armor, double evasion){
        super(id,name,maxHp,speed,ap,damage,armor);
        this.evasion = evasion;
    }

    public void addMove(Move m){ moves.add(m); }
    public List<Move> getMoves(){ return moves; }

    /**
     * Check whether an attack from an attacker of given speed hits this enemy.
     * accuracy currently unused but left for future use.
     */
    public boolean isHit(double accuracy, int attackerSpeed) {
        double currentEvasion = this.evasion;

        // status-based SLIPPERY adds to evasion
        double effectEvasionBonus = getActiveStatuses().stream()
            .filter(e -> e.getKind() == StatusEffect.Kind.SLIPPERY)
            .mapToDouble(StatusEffect::getDblValue)
            .sum();
        currentEvasion += effectEvasionBonus;

        // speed difference: enemy.speed - attackerSpeed (each point = 2% miss)
        double speedDiff = (double)this.speed - (double)attackerSpeed;
        double speedModifier = speedDiff * 0.02;
        currentEvasion += speedModifier;

        // clamp
        currentEvasion = Math.max(0.0, Math.min(0.95, currentEvasion));

        if (Math.random() < currentEvasion) {
            System.out.println(getName() + " avoided the attack! (missChance=" + String.format("%.2f", currentEvasion) + ")");
            return false; // Miss
        }
        return true; // Hit
    }

    /**
     * Process statuses and take a turn. Uses unified status handling in Entity.
     */
    public void takeTurn(Player player) {
        processStatusEffectsStartTurn(); // will process DOTs / ticks
        if (!isAlive()) return;
        if (hasStatusEffect(StatusEffect.Kind.STUN)) {
            System.out.println(getName() + " is stunned and cannot act this turn.");
            return;
        }

        double hesitate = (stage==1?0.40:(stage==2?0.15:0.03));
        if (Math.random() < hesitate) { System.out.println(getName() + " hesitates and does nothing."); return; }

        int currentAp = ap;
        while (currentAp > 0 && isAlive() && player.isAlive()) {
            List<Move> affordable = new ArrayList<>();
            for (Move m: moves) if (m.getApCost() <= currentAp) affordable.add(m);
            if (affordable.isEmpty()) break;

            Move chosen = affordable.get(new Random().nextInt(affordable.size()));
            System.out.println(getName() + " chooses " + chosen.getName() + "!");
            chosen.perform(this, player, null);
            currentAp -= chosen.getApCost();
            try { Thread.sleep(300); } catch (InterruptedException ex) {}
        }
    }

    public String getMovesDescription(){ StringBuilder sb=new StringBuilder(); for (Move m: moves) sb.append(m.getName()).append(" (AP:"+m.getApCost()+") "); return sb.toString(); }
    public int getAttackValue(){ return damage; }
    public void setDifficulty(int s){ this.stage = s; }
}
