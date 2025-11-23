import java.util.List;

public class Move {
    private String id;
    private String name;
    private String description;
    private int apCost;
    private double damageMultiplier;
    private StatusEffect applyStatusPrototype;
    private int statusDuration;
    private ElementType element;

    public Move(String id, String name, String description, int apCost, double damageMultiplier,
                StatusEffect applyStatusPrototype, int statusDuration, ElementType element) {
        this.id = id; this.name = name; this.description = description; this.apCost = apCost;
        this.damageMultiplier = damageMultiplier; this.applyStatusPrototype = applyStatusPrototype;
        this.statusDuration = statusDuration; this.element = element == null ? ElementType.NONE : element;
    }

    public String getId(){return id;}
    public String getName(){return name;}
    public String getDescription(){return description;}
    public int getApCost(){return apCost;}
    public double getDamageMultiplier(){return damageMultiplier;}
    public StatusEffect getApplyStatus(){return applyStatusPrototype;}
    public int getStatusDuration(){return statusDuration;}
    public ElementType getElement(){return element;}

    /**
     * Perform the move. 'user' is attacker (Player or Enemy). primaryTarget is target (Player or Enemy).
     * We use the attacker's speed to evaluate hit/miss and unify status application.
     */
    public void perform(Object user, Object primaryTarget, java.util.List<Enemy> otherEnemies) {
        int baseDamage = 0;
        String userName = "Unknown";
        int attackerSpeed = 0;

        if (user instanceof Enemy) {
            Enemy eu = (Enemy) user;
            baseDamage = eu.getAttackValue();
            userName = eu.getName();
            attackerSpeed = eu.getSpeed();
        } else if (user instanceof Player) {
            Player pu = (Player) user;
            baseDamage = pu.attack();
            userName = pu.getName();
            attackerSpeed = pu.getSpeed();
        }

        int dmg = Math.max(1, (int)Math.round(baseDamage * damageMultiplier));
        System.out.println(userName + " uses " + name + " — " + description);

        switch (element) {
          case ELECTRIC:
    if (user instanceof Player) {
        Player pu = (Player) user;

        // --- MAIN TARGET ---
        if (primaryTarget instanceof Enemy) {
            Enemy et = (Enemy) primaryTarget;
            if (!et.isHit(1.0, pu.getSpeed())) break;

            et.takeDamage(dmg);
            System.out.println("Electric shock jolts " + et.getName() + "! (" + dmg + " dmg)");

            // 20% chance to inflict the JOLT status to the main enemy
            if (Math.random() < 0.20) {
                StatusEffect s = new StatusEffect(StatusEffect.Kind.JOLT, "Jolted",
                        "Stunned briefly", 1, 0, 0.0);
                et.applyStatus(s);
                System.out.println(et.getName() + " is jolted and may skip a turn!");
            }

            // --- CHAIN JOLT LOGIC (PLAYER ONLY) ---
            if (otherEnemies != null && !otherEnemies.isEmpty()) {
                int count = Math.min(3, otherEnemies.size()); // max 3 enemies get chain dmg
                double chainRate = (otherEnemies.size() <= 2 ? 0.30 : 0.10);

                for (int i = 0; i < count; i++) {
                    Enemy chainTarget = otherEnemies.get(i);

                    int chainDmg = Math.max(1, (int)Math.round(dmg * chainRate));

                    if (!chainTarget.isHit(1.0, pu.getSpeed())) {
                        System.out.println(chainTarget.getName() + " evaded the chain lightning!");
                        continue;
                    }

                    System.out.println("Chain lightning arcs to " + chainTarget.getName() +
                            " for " + chainDmg + " damage!");
                    chainTarget.takeDamage(chainDmg);

                    // Each chained enemy has 10% chance to receive JOLT
                    if (Math.random() < 0.10) {
                        StatusEffect s2 = new StatusEffect(StatusEffect.Kind.JOLT, "Jolted",
                                "Stunned briefly", 1, 0, 0.0);
                        chainTarget.applyStatus(s2);
                        System.out.println(chainTarget.getName() + " is jolted by the chain lightning!");
                    }
                }
            }

        }
    }

    // --- ENEMY USING ELECTRIC MOVE AGAINST PLAYER ---
    else if (user instanceof Enemy) {
        Enemy eu = (Enemy) user;
        if (primaryTarget instanceof Player) {
            Player pt = (Player) primaryTarget;
            if (pt.avoidsAttackFrom(eu.getSpeed())) break;

            pt.takeDamage(dmg);
            System.out.println("Electricity surges through " + pt.getName() + "! (" + dmg + " dmg)");

            if (Math.random() < 0.20) {
                StatusEffect s = new StatusEffect(StatusEffect.Kind.JOLT, "Jolted",
                        "Stunned briefly", 1, 0, 0.0);
                pt.applyStatus(s);
                System.out.println(pt.getName() + " is jolted and may lose a turn!");
            }
        }
    }
    break;


            case FIRE:
                if (primaryTarget instanceof Player) {
                    Player pt = (Player) primaryTarget;
                    if (pt.avoidsAttackFrom(attackerSpeed)) break;
                    pt.takeDamage(dmg);
                    System.out.println(pt.getName() + " is burned for " + dmg + " damage!");
                    StatusEffect burn = new StatusEffect(StatusEffect.Kind.BURN, "Burn", "Burning damage", statusDuration, Math.max(1,dmg/2), 0.0);
                    pt.applyStatus(burn);
                } else if (primaryTarget instanceof Enemy) {
                    Enemy et = (Enemy) primaryTarget;
                    if (!et.isHit(1.0, attackerSpeed)) break;
                    et.takeDamage(dmg);
                    System.out.println(et.getName() + " is burned for " + dmg + " damage!");
                    StatusEffect burn = new StatusEffect(StatusEffect.Kind.BURN, "Burn", "Burning damage", statusDuration, Math.max(1,dmg/2), 0.0);
                    et.applyStatus(burn);
                }
                break;

            case ICE:
                if (primaryTarget instanceof Player) {
                    Player pt = (Player) primaryTarget;
                    if (pt.avoidsAttackFrom(attackerSpeed)) break;
                    pt.takeDamage(dmg);
                    System.out.println(pt.getName() + " is chilled and weakened (" + dmg + " dmg).");
                    StatusEffect ice = new StatusEffect(StatusEffect.Kind.GENERIC, "Frozen Chill", "Reduced attack/possible skip", statusDuration, 0, -0.10);
                    pt.applyStatus(ice);
                } else if (primaryTarget instanceof Enemy) {
                    Enemy et = (Enemy) primaryTarget;
                    if (!et.isHit(1.0, attackerSpeed)) break;
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
                    if (pt.avoidsAttackFrom(attackerSpeed)) break;
                    pt.takeDamage(dmg);
                } else if (primaryTarget instanceof Enemy) {
                    Enemy et = (Enemy) primaryTarget;
                    if (!et.isHit(1.0, attackerSpeed)) break;
                    et.takeDamage(dmg);
                }
                break;
        }

        // apply secondary (prototype) status if present (already preserves players/enemies)
        if (applyStatusPrototype != null) {
            StatusEffect proto = applyStatusPrototype;
            StatusEffect s = new StatusEffect(proto.getKind(), proto.getName(), proto.getDescription(), statusDuration, proto.getIntValue(), proto.getDblValue());
            if (primaryTarget instanceof Player) {
                Player pt = (Player) primaryTarget;
                pt.applyStatus(s);
                System.out.println(pt.getName() + " is affected by " + s.getName() + " (" + s.getDescription() + ")");
            } else if (primaryTarget instanceof Enemy) {
                Enemy et = (Enemy) primaryTarget;
                et.applyStatus(s);
                System.out.println(et.getName() + " is affected by " + s.getName() + " (" + s.getDescription() + ")");
            }
        }
    }
}
