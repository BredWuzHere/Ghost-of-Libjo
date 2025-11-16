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

    public void perform(Object user, Object primaryTarget, java.util.List<Enemy> otherEnemies) {
        int baseDamage = 0;
        String userName = "Unknown";
        if (user instanceof Enemy) {
            Enemy eu = (Enemy) user;
            baseDamage = eu.getAttackValue();
            userName = eu.getName();
        } else if (user instanceof Player) {
            Player pu = (Player) user;
            baseDamage = pu.attack();
            userName = pu.getName();
        }

        int dmg = Math.max(1, (int)Math.round(baseDamage * damageMultiplier));
        System.out.println(userName + " uses " + name + " — " + description);

        switch (element) {
            case ELECTRIC:
                if (primaryTarget instanceof Player) {
                    Player pt = (Player) primaryTarget;
                    pt.takeDamage(dmg);
                    System.out.println("Electricity jolts through! (" + dmg + " dmg)");
                    if (Math.random() < 0.20) {
                        StatusEffect s = new StatusEffect(StatusEffect.Kind.GENERIC, "Jolted", "Stunned briefly", 1, 0, 0.0);
                        pt.applyStatus(s);
                        System.out.println(pt.getName() + " is jolted and may lose a turn!");
                    }
                } else if (primaryTarget instanceof Enemy) {
                    Enemy et = (Enemy) primaryTarget;
                    et.takeDamage(dmg);
                    System.out.println(et.getName() + " is jolted (" + dmg + " dmg).");
                }
                if (otherEnemies != null && !otherEnemies.isEmpty()) {
                    Enemy bounce = otherEnemies.get(0);
                    int bounceDmg = Math.max(1, (int)Math.round(dmg * 0.25));
                    System.out.println("The electric arc jumps to " + bounce.getName() + " for " + bounceDmg + " damage!");
                    bounce.takeDamage(bounceDmg);
                }
                break;
            case FIRE:
                if (primaryTarget instanceof Player) {
                    Player pt = (Player) primaryTarget;
                    pt.takeDamage(dmg);
                    System.out.println(pt.getName() + " is burned for " + dmg + " damage!");
                    StatusEffect burn = new StatusEffect(StatusEffect.Kind.BURN, "Burn", "Burning damage", statusDuration, Math.max(1,dmg/2), 0.0);
                    pt.applyStatus(burn);
                } else if (primaryTarget instanceof Enemy) {
                    Enemy et = (Enemy) primaryTarget;
                    et.takeDamage(dmg);
                    System.out.println(et.getName() + " is burned for " + dmg + " damage!");
                    StatusEffect burn = new StatusEffect(StatusEffect.Kind.BURN, "Burn", "Burning damage", statusDuration, Math.max(1,dmg/2), 0.0);
                    et.applyStatus(burn);
                }
                break;
            case ICE:
                if (primaryTarget instanceof Player) {
                    Player pt = (Player) primaryTarget;
                    pt.takeDamage(dmg);
                    System.out.println(pt.getName() + " is chilled and weakened (" + dmg + " dmg).");
                    StatusEffect ice = new StatusEffect(StatusEffect.Kind.GENERIC, "Frozen Chill", "Reduced attack/possible skip", statusDuration, 0, -0.10);
                    pt.applyStatus(ice);
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
