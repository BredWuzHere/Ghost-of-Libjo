import java.util.*;

public abstract class Enemy {
    protected String id, name;
    protected int hp, maxHp, speed, ap, maxAp, damage, armor;
    protected List<StatusEffect> statusEffects = new ArrayList<>();
    protected List<Move> moves = new ArrayList<>(); // enemy moves
    // difficulty/stage for AI tuning
    protected int difficulty = 1;

    public Enemy(String id, String name, int maxHp, int speed, int ap, int damage, int armor) {
        this.id = id; this.name = name; this.maxHp = maxHp; this.hp = maxHp;
        this.speed = speed; this.ap = this.maxAp = ap; this.damage = damage; this.armor = armor;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getSpeed() { return speed; }
    public boolean isAlive() { return hp > 0; }
    public void resetAp() { this.ap = this.maxAp; }
    public int getAp() { return ap; }
    public void spendAp(int a) { ap = Math.max(0, ap - a); }

    public void takeDamage(int d) {
        int mitig = Math.max(0, d - armor);
        hp = Math.max(0, hp - mitig);
        System.out.println(name + " takes " + mitig + " damage (after armor). HP=" + hp + "/" + maxHp);
    }

    public void applyStatus(StatusEffect s) { statusEffects.add(s); }

    public void processStatusEffectsStartTurn() {
        List<StatusEffect> remove = new ArrayList<>();
        for (StatusEffect s : statusEffects) {
            if (s.getKind() == StatusEffect.Kind.BURN) {
                int burn = s.getIntValue();
                hp = Math.max(0, hp - burn);
                System.out.println(name + " is burning and takes " + burn + " damage. HP=" + hp + "/" + maxHp);
            }
            s.tick();
            if (s.isExpired()) remove.add(s);
        }
        statusEffects.removeAll(remove);
    }

    public abstract void takeTurn(Player player);

    // difficulty helper so database or creator can set stage-level tuning
    public void setDifficulty(int d) { this.difficulty = Math.max(1, d); }
    public int getDifficulty() { return difficulty; }

    // moves helpers
    public void addMove(Move m) { if (m != null) moves.add(m); }
    public List<Move> getMoves() { return Collections.unmodifiableList(moves); }

    /**
     * Returns a short description of the enemy's available moves/actions.
     * Override in subclasses for custom move lists if desired.
     */
    public String getMovesDescription() {
        if (moves.isEmpty()) {
            return "Basic Attack (2 AP) - deals " + damage + " damage";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            Move mv = moves.get(i);
            sb.append(mv.getName())
              .append(" (AP:")
              .append(mv.getApCost())
              .append(") - ")
              .append(mv.getDescription());
            if (i < moves.size()-1) sb.append("; ");
        }
        return sb.toString();
    }
}
