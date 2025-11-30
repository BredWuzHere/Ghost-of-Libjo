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

    public void takeDamage(int d) {
        int mitigated = Math.max(0, d - armor);
        hp -= mitigated;
        if (hp < 0) hp = 0;
        System.out.println(name + " takes " + mitigated + " damage! (HP: " + hp + "/" + maxHp + ")");
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public void applyStatus(StatusEffect s) {
        if (s == null) return;
        // If this is a buff, apply its immediate effect (defense or attack)
        if (s.getKind() == StatusEffect.Kind.BUFF_DEFENSE) {
            this.armor += s.getIntValue();
            System.out.println(name + " gains +" + s.getIntValue() + " armor from " + s.getName() + ".");
        } else if (s.getKind() == StatusEffect.Kind.BUFF_ATTACK) {
            this.damage += s.getIntValue();
            System.out.println(name + " gains +" + s.getIntValue() + " attack from " + s.getName() + ".");
        }
        activeStatuses.add(s.copy());
    }

    public List<StatusEffect> getActiveStatuses(){ return activeStatuses; }

    public void processStatusEffectsStartTurn() {
        Iterator<StatusEffect> it = activeStatuses.iterator();
        while (it.hasNext()) {
            StatusEffect se = it.next();
            if (se.getKind() == StatusEffect.Kind.BURN) {
                int dmg = se.getIntValue();
                hp = Math.max(0, hp - dmg);
                System.out.println(name + " suffers " + dmg + " burn damage from " + se.getName() + " (" + hp + "/" + maxHp + ")");
            }
            se.tick();
            if (se.getRemainingTurns() <= 0) {
                // If a buff expired, remove its effect
                if (se.getKind() == StatusEffect.Kind.BUFF_DEFENSE) {
                    this.armor = Math.max(0, this.armor - se.getIntValue());
                    System.out.println(name + "'s buff " + se.getName() + " has expired. (-" + se.getIntValue() + " armor)");
                } else if (se.getKind() == StatusEffect.Kind.BUFF_ATTACK) {
                    this.damage = Math.max(0, this.damage - se.getIntValue());
                    System.out.println(name + "'s buff " + se.getName() + " has expired. (-" + se.getIntValue() + " attack)");
                }
                it.remove();
            }
        }
    }

    public void setDifficulty(int d){ this.difficulty = Math.max(1,d); }
    public void setMaxAp(int m){ this.maxAp = m; this.ap = Math.min(ap, maxAp); }
}
