// Patched Entity.java - unified status handling, effective armor, speed-based dodge
import java.util.*;

public class Entity {
    protected String id;
    protected String name;
    protected int maxHp;
    protected int hp;
    protected int speed;
    protected int ap;
    protected int maxAp;
    protected int damage;
    protected int armor;
    protected int x=0,y=0;
    protected List<StatusEffect> activeStatuses = new ArrayList<>();
    protected int difficulty = 1;

    public Entity(String id, String name, int maxHp, int speed, int ap, int damage, int armor) {
        this.id = id; this.name = name; this.maxHp = maxHp; this.hp = maxHp;
        this.speed = speed; this.ap = ap; this.maxAp = ap; this.damage = damage; this.armor = armor;
    }

    public String getId(){return id;}
    public String getName(){return name;}
    public int getHp(){return hp;}
    public int getMaxHp(){return maxHp;}
    public int getSpeed(){return speed;}
    public int getAp(){return ap;}
    public int getArmor(){return armor;}
    public int getAttackValue(){return damage;}
    public int getX(){return x;} public int getY(){return y;}
    public void setPosition(int x,int y){this.x=x;this.y=y;}

    public void resetAp(){ this.ap = maxAp; }
    public void spendAp(int n){ this.ap = Math.max(0, this.ap - n); }
    public boolean isAlive(){ return hp > 0; }

    /**
     * Effective armor after status modifiers (ARMOR_DOWN reduces armor by intValue).
     */
    public int getEffectiveArmor() {
        int armorMod = 0;
        for (StatusEffect se : activeStatuses) {
            if (se.getKind() == StatusEffect.Kind.ARMOR_DOWN) {
                armorMod -= se.getIntValue(); // intValue is how much to lower armor
            }
        }
        int eff = armor + armorMod;
        return Math.max(0, eff);
    }

    public void takeDamage(int d) {
        int mitigated = Math.max(0, d - getEffectiveArmor());
        hp -= mitigated;
        if (hp < 0) hp = 0;
        System.out.println(name + " takes " + mitigated + " damage! (HP: " + hp + "/" + maxHp + ")");
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public void applyStatus(StatusEffect s) {
        if (s == null) return;
        activeStatuses.add(s.copy());
    }

    public List<StatusEffect> getActiveStatuses(){ return activeStatuses; }

    /**
     * Process status effects at the start of the entity's turn:
     * - Apply BURN/POISON damage per tick
     * - Tick down durations and remove expired effects
     * - Print messages for relevant effects
     */
    public void processStatusEffectsStartTurn() {
        Iterator<StatusEffect> it = activeStatuses.iterator();
        while (it.hasNext()) {
            StatusEffect se = it.next();
            switch (se.getKind()) {
                case BURN:
                    int burnDmg = se.getIntValue();
                    hp = Math.max(0, hp - burnDmg);
                    System.out.println(name + " suffers " + burnDmg + " burn damage from " + se.getName() + " (" + hp + "/" + maxHp + ")");
                    break;
                case POISON:
                    int poisonDmg = se.getIntValue();
                    hp = Math.max(0, hp - poisonDmg);
                    System.out.println(name + " suffers " + poisonDmg + " poison damage from " + se.getName() + " (" + hp + "/" + maxHp + ")");
                    break;
                case JOLT:
                    // JOLT is treated as a short stun indicator
                    System.out.println(name + " is jolted by " + se.getName() + " (" + se.getRemainingTurns() + " turns left)");
                    break;
                case SLOW:
                case ARMOR_DOWN:
                case CRIT_DOWN:
                case SLIPPERY:
                case GENERIC:
                case STUN:
                    // No tick damage — effect used by other calculations (hasStatusEffect/getEffectiveArmor/avoidsAttackFrom)
                    break;
            }
            se.tick();
            if (se.getRemainingTurns() <= 0) {
                it.remove();
            }
        }
    }

    /**
     * Check if this entity currently has a status effect of a given kind.
     * Treat JOLT as equivalent to STUN for convenience when callers check STUN.
     */
    public boolean hasStatusEffect(StatusEffect.Kind kind) {
        for (StatusEffect se : activeStatuses) {
            if (se.getKind() == kind) return true;
        }
        if (kind == StatusEffect.Kind.STUN) {
            for (StatusEffect se : activeStatuses) if (se.getKind() == StatusEffect.Kind.JOLT) return true;
        }
        return false;
    }

    /**
     * Returns true if an incoming attack is avoided (misses) based on:
     * - the target's SLIPPERY status sum,
     * - speed difference (target.speed - attackerSpeed) * scalar
     * Scalar currently 0.02 (2% per speed point). Clamp between 0.0 and 0.95.
     */
    public boolean avoidsAttackFrom(int attackerSpeed) {
        double statusEvasion = activeStatuses.stream()
            .filter(e -> e.getKind() == StatusEffect.Kind.SLIPPERY)
            .mapToDouble(StatusEffect::getDblValue)
            .sum();

        double speedDiff = (double)this.speed - (double)attackerSpeed;
        double speedModifier = speedDiff * 0.02; // tweakable scalar

        double totalMissChance = statusEvasion + speedModifier;
        totalMissChance = Math.max(0.0, Math.min(0.95, totalMissChance));

        if (Math.random() < totalMissChance) {
            System.out.println(this.name + " dodged the attack! (missChance=" + String.format("%.2f", totalMissChance) + ")");
            return true;
        }
        return false;
    }

    public void setDifficulty(int d){ this.difficulty = Math.max(1,d); }
    public void setMaxAp(int m){ this.maxAp = m; this.ap = Math.min(ap, maxAp); }
}
