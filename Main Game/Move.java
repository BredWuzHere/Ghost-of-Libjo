// Move.java
import java.util.List;

public class Move {
    private String id;
    private String name;
    private String description;
    private int apCost;
    private double damageMultiplier; // multiply user's base damage
    private StatusEffect applyStatusPrototype; // a prototype StatusEffect to apply (may be null)
    private int statusDuration; // duration for the status effect applied by this move
    private ElementType element; // NONE, ELECTRIC, FIRE, ICE

    public Move(String id, String name, String description, int apCost, double damageMultiplier,
                StatusEffect applyStatusPrototype, int statusDuration, ElementType element) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.apCost = apCost;
        this.damageMultiplier = damageMultiplier;
        this.applyStatusPrototype = applyStatusPrototype;
        this.statusDuration = statusDuration;
        this.element = element == null ? ElementType.NONE : element;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getApCost() { return apCost; }
    public double getDamageMultiplier() { return damageMultiplier; }
    public StatusEffect getApplyStatus() { return applyStatusPrototype; }
    public int getStatusDuration() { return statusDuration; }
    public ElementType getElement() { return element; }

    /**
     * Perform the move.
     *
     * @param user the Enemy or Player performing the move (we'll use to access base damage).
     * @param primaryTarget player (when called in enemy AI) or enemy (when called by player) -- using Object to avoid tight coupling.
     * @param otherTargets optional list of other living enemies (for electric bounce etc). Can be null.
     *
     * Note: this method prints action messages and applies damage/status. It doesn't know all rules of your Player class,
     * but it uses the methods most games have (takeDamage, applyStatus). If your Player/Enemy API differs adjust accordingly.
     */
    public void perform(Object user, Object primaryTarget, List<Enemy> otherEnemies) {
        int baseDamage = 0;
        String userName = "Unknown";
        // try to extract base damage and names
        if (user instanceof Enemy) {
            Enemy eu = (Enemy) user;
            baseDamage = eu.damage;
            userName = eu.getName();
        } else if (user instanceof Player) {
            Player pu = (Player) user;
            baseDamage = pu.attack(); // assumes Player has getAttackValue(); if not, adjust to getWeaponDamage()
            userName = pu.getName();
        }

        int dmg = Math.max(1, (int)Math.round(baseDamage * damageMultiplier));

        // Print the action line
        System.out.println(userName + " uses " + name + " — " + description);

        // Handle element-specific behavior
        switch (element) {
            case ELECTRIC:
                // Jolt primary target
                if (primaryTarget instanceof Player) {
                    Player pt = (Player) primaryTarget;
                    pt.takeDamage(dmg);
                    System.out.println("Electricity jolts through! (" + dmg + " dmg)");
                    // small chance to apply a stun-like GENERIC effect (user of StatusEffect.Kind.GENERIC)
                    if (Math.random() < 0.20) {
                        StatusEffect s = new StatusEffect(StatusEffect.Kind.GENERIC, "Jolted", "Stunned briefly", 1, 0, 0.0);
                        pt.applyStatus(s);
                        System.out.println(pt.getName() + " is jolted and may lose a turn!");
                    }
                    // bounce damage to next enemy if provided
                    if (otherEnemies != null && !otherEnemies.isEmpty()) {
                        Enemy bounce = otherEnemies.get(0);
                        int bounceDmg = Math.max(1, (int)Math.round(dmg * 0.25)); // 25% bounce
                        System.out.println("The electric arc jumps to " + bounce.getName() + " for " + bounceDmg + " damage!");
                        bounce.takeDamage(bounceDmg);
                    }
                } else if (primaryTarget instanceof Enemy) {
                    Enemy et = (Enemy) primaryTarget;
                    et.takeDamage(dmg);
                    System.out.println(et.getName() + " is jolted (" + dmg + " dmg).");
                    if (Math.random() < 0.20) {
                        StatusEffect s = new StatusEffect(StatusEffect.Kind.GENERIC, "Jolted", "Stunned briefly", 1, 0, 0.0);
                        et.applyStatus(s);
                        System.out.println(et.getName() + " is jolted and may lose a turn!");
                    }
                    if (otherEnemies != null && otherEnemies.size() > 0) {
                        Enemy bounce = otherEnemies.get(0);
                        int bounceDmg = Math.max(1, (int)Math.round(dmg * 0.25));
                        System.out.println("Electric arc jumps to " + bounce.getName() + " for " + bounceDmg + " damage!");
                        bounce.takeDamage(bounceDmg);
                    }
                }
                break;

            case FIRE:
                if (primaryTarget instanceof Player) {
                    Player pt = (Player) primaryTarget;
                    pt.takeDamage(dmg);
                    System.out.println(pt.getName() + " is burned for " + dmg + " damage!");
                    // apply burn status (use existing Kind.BURN)
                    StatusEffect burn = new StatusEffect(StatusEffect.Kind.BURN, "Burn", "Burning damage", statusDuration, dmg/2, 0.0);
                    pt.applyStatus(burn);
                    System.out.println(pt.getName() + " will burn for " + burn.getIntValue() + " each turn for " + burn.getRemainingTurns() + " turns.");
                } else if (primaryTarget instanceof Enemy) {
                    Enemy et = (Enemy) primaryTarget;
                    et.takeDamage(dmg);
                    System.out.println(et.getName() + " is burned for " + dmg + " damage!");
                    StatusEffect burn = new StatusEffect(StatusEffect.Kind.BURN, "Burn", "Burning damage", statusDuration, dmg/2, 0.0);
                    et.applyStatus(burn);
                }
                break;

            case ICE:
                if (primaryTarget instanceof Player) {
                    Player pt = (Player) primaryTarget;
                    pt.takeDamage(dmg);
                    System.out.println(pt.getName() + " is chilled and weakened (" + dmg + " dmg).");
                    // apply generic ice effect (you should interpret this in your status processing logic)
                    StatusEffect ice = new StatusEffect(StatusEffect.Kind.GENERIC, "Frozen Chill", "Reduced attack/possible skip", statusDuration, 0, -0.10);
                    pt.applyStatus(ice);
                    System.out.println(pt.getName() + " feels slowed and weaker.");
                } else if (primaryTarget instanceof Enemy) {
                    Enemy et = (Enemy) primaryTarget;
                    et.takeDamage(dmg);
                    System.out.println(et.getName() + " is chilled and weakened.");
                    StatusEffect ice = new StatusEffect(StatusEffect.Kind.GENERIC, "Frozen Chill", "Reduced attack/possible skip", statusDuration, 0, -0.10);
                    et.applyStatus(ice);
                }
                break;

            case NONE:
            default:
                if (primaryTarget instanceof Player) {
                    Player pt = (Player) primaryTarget;
                    pt.takeDamage(dmg);
                } else if (primaryTarget instanceof Enemy) {
                    Enemy et = (Enemy) primaryTarget;
                    et.takeDamage(dmg);
                }
                break;
        }

        // if the move had an explicit status prototype, apply it
        if (applyStatusPrototype != null) {
            if (primaryTarget instanceof Player) {
                Player pt = (Player) primaryTarget;
                StatusEffect proto = applyStatusPrototype;
                StatusEffect s = new StatusEffect(proto.getKind(), proto.getName(), proto.getDescription(), statusDuration, proto.getIntValue(), proto.getDblValue());
                pt.applyStatus(s);
                System.out.println(pt.getName() + " is affected by " + s.getName() + " (" + s.getDescription() + ")");
            } else if (primaryTarget instanceof Enemy) {
                Enemy et = (Enemy) primaryTarget;
                StatusEffect proto = applyStatusPrototype;
                StatusEffect s = new StatusEffect(proto.getKind(), proto.getName(), proto.getDescription(), statusDuration, proto.getIntValue(), proto.getDblValue());
                et.applyStatus(s);
                System.out.println(et.getName() + " is affected by " + s.getName() + " (" + s.getDescription() + ")");
            }
        }
    }
}
