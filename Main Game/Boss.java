// Boss.java
import java.util.*;

public class Boss extends Enemy {
    private double dodgeChance = 0.0;
    private int stage = 1;
    private Random rnd = new Random();

    public Boss(String id, String name, int maxHp, int speed, int ap, int damage, int armor, double dodgeChance, int stage) {
        super(id, name, maxHp, speed, ap, damage, armor);
        this.dodgeChance = dodgeChance;
        this.stage = Math.max(1, stage);
        // use stage as difficulty baseline
        setDifficulty(this.stage);
    }

    @Override
    public void takeTurn(Player player) {
        if (!isAlive()) return;

        processStatusEffectsStartTurn();

        // Taunt probability increases with stage
        double tauntChance = 0.45 + 0.15 * (stage - 1);

        // AP costs
        final int BASIC_COST = 2;
        final int HEAVY_COST = 3;
        final int SPECIAL_COST = 4;

        // Pre-turn optional taunt
        if (Text.roll(tauntChance)) {
            System.out.println(Text.randomBossTaunt(this.id));
            try { Thread.sleep(600); } catch (InterruptedException ignored) {}
        }

        // Weighting for choices grows with stage to favor heavier/special moves
        double specialBase = 0.15 + 0.10 * (stage - 1);
        double heavyBase = 0.35 + 0.10 * (stage - 1);
        double basicBase = Math.max(0.05, 1.0 - specialBase - heavyBase);

        // Loop while boss has enough AP for at least a basic action and both live
        while (getAp() >= BASIC_COST && isAlive() && player.isAlive()) {
            // boss shows thinking message before each action
            System.out.println(name + " is thinking...");
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}

            // With some chance boss taunts mid-turn
            double midTauntChance = 0.20 + 0.10 * (stage - 1);
            if (Text.roll(midTauntChance)) {
                System.out.println(Text.randomBossTaunt(this.id));
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            }

            int roll = rnd.nextInt(100);
            int choice;
            double s = specialBase * 100;
            double h = s + heavyBase * 100;
            if (roll < s) choice = 0;
            else if (roll < h) choice = 1;
            else choice = 2;

            // ensure we can afford the chosen action, fallback to cheaper if not
            if (choice == 0 && getAp() < SPECIAL_COST) {
                if (getAp() >= HEAVY_COST) choice = 1;
                else if (getAp() >= BASIC_COST) choice = 2;
                else break;
            } else if (choice == 1 && getAp() < HEAVY_COST) {
                if (getAp() >= BASIC_COST) choice = 2;
                else break;
            }

            switch (choice) {
                case 0: // special
                    System.out.println(name + " prepares a special move!");
                    performSpecial(player);
                    spendAp(SPECIAL_COST);
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                    break;
                case 1: // heavy
                    System.out.println(name + " performs a heavy strike!");
                    player.takeDamage(damage + 3 + stage);
                    spendAp(HEAVY_COST);
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                    break;
                default: // basic
                    switch (id) {
                        case "hipon": hiponBasic(player); break;
                        case "gulod": gulodBasic(player); break;
                        case "sales": salesBasic(player); break;
                        case "dechavez": dechavezBasic(player); break;
                        default: basic(player); break;
                    }
                    spendAp(BASIC_COST);
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                    break;
            }

            // chance to chain another action increases with stage
            double chain = 0.05 * stage;
            if (getAp() >= BASIC_COST && rnd.nextDouble() < chain) {
                System.out.println(name + " seizes the momentum and acts again!");
                try { Thread.sleep(400); } catch (InterruptedException ignored) {}
                continue; // do another loop iteration if AP remains
            } else {
                // allow loop to re-evaluate normally
            }
        }
    }

    private void basic(Player p) { System.out.println(name + " strikes!"); p.takeDamage(damage); }

private void hiponBasic(Player p) {
    int dmg = damage + 1;
    System.out.println(name + " lashes a slippery tentacle!");
    p.takeDamage(dmg);
    System.out.println(p.getName() + " takes " + dmg + " damage!");

    if (Text.roll(0.20 + 0.05 * (stage - 1))) {
        StatusEffect se = new StatusEffect(StatusEffect.Kind.CRIT_DOWN, "Slippery Droplet", "Minor crit chance reduction", 2, 0, -0.05);
        p.applyStatus(se);
        System.out.println(p.getName() + " is affected by " + se.getName() + " (" + se.getDescription() + ")");
    }
}

private void gulodBasic(Player p) {
    int dmg = damage + 2 + (stage / 1);
    System.out.println(name + " slams the ground!");
    p.takeDamage(dmg);
    System.out.println(p.getName() + " takes " + dmg + " damage!");
}

private void salesBasic(Player p) {
    int dmg = damage + 3 + (stage / 1);
    System.out.println(name + " slashes with mirage blades!");
    p.takeDamage(dmg);
    System.out.println(p.getName() + " takes " + dmg + " damage!");
}

private void dechavezBasic(Player p) {
    int dmg = damage + 4 + (stage / 1);
    System.out.println(name + " scratches with audit talons!");
    p.takeDamage(dmg);
    System.out.println(p.getName() + " takes " + dmg + " damage!");
}

private void performSpecial(Player p) {
    switch (id) {
        case "hipon":
            System.out.println(name + " unleashes a wave of ink and goo!");
            StatusEffect goo = new StatusEffect(StatusEffect.Kind.SLIPPERY, "Super Goo", "Crit and movement hindered", 3 + stage / 1, 0, -0.10 - 0.02 * (stage - 1));
            p.applyStatus(goo);
            System.out.println(p.getName() + " is affected by " + goo.getName() + " (" + goo.getDescription() + ")");
            break;

        case "gulod":
            System.out.println(name + " roars an earth-splitting roar that weakens your defenses!");
            StatusEffect roar = new StatusEffect(StatusEffect.Kind.ARMOR_DOWN, "Ground Roar", "Armor reduced", 4 + stage / 1, 2 + stage / 1, 0.0);
            p.applyStatus(roar);
            System.out.println(p.getName() + " is affected by " + roar.getName() + " (" + roar.getDescription() + ")");
            break;

        case "sales":
            System.out.println(name + " casts 'Mirage Contract' — your resistances falter!");
            StatusEffect contract = new StatusEffect(StatusEffect.Kind.GENERIC, "Contracted Doubt", "Temporary resistance drop", 3 + stage / 1, 0, -0.10 - 0.03 * (stage - 1));
            p.applyStatus(contract);
            System.out.println(p.getName() + " is affected by " + contract.getName() + " (" + contract.getDescription() + ")");
            break;

        case "dechavez":
            System.out.println(name + " invokes the 'Unfair Clause' — a devastating penalty!");
            int loss = Math.max(1, p.getHp() / (3 - Math.min(2, stage - 1)));
            p.takeDamage(loss);
            System.out.println(p.getName() + " takes " + loss + " damage!");
            break;

        default:
            int dmg = damage + 2 + stage;
            System.out.println(name + " uses a mysterious power!");
            p.takeDamage(dmg);
            System.out.println(p.getName() + " takes " + dmg + " damage!");
            break;
    }
}


    @Override
    public String getMovesDescription() {
        return "Basic Attack (2 AP) - " + damage + " dmg; Heavy (3 AP) - " + (damage+3) + " dmg; Special (4 AP) - unique effect (stronger with stage).";
    }
}
