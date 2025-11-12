// SimpleEnemy.java
import java.util.Random;

public class SimpleEnemy extends Enemy {
    private Random rnd = new Random();

    public SimpleEnemy(String id, String name, int maxHp, int speed, int ap, int damage, int armor) {
        super(id, name, maxHp, speed, ap, damage, armor);
    }

    @Override
    public void takeTurn(Player player) {
        if (!isAlive()) return;

        // Apply start-of-turn status effects
        processStatusEffectsStartTurn();

        // Costs: moves define their own AP cost; fallback weak cost
        final int WEAK_COST = 1;

        // Hesitation probability per stage/difficulty:
        // stage1 -> 0.40, stage2 -> 0.15, stage3 -> 0.03 (clamped)
        double hesitate;
        if (getDifficulty() <= 1) hesitate = 0.40;
        else if (getDifficulty() == 2) hesitate = 0.15;
        else hesitate = 0.03;

        // Behavior chances (when not hesitating)
        double tauntChance = 0.25 + 0.10 * (getDifficulty() - 1); // more taunts at higher difficulty
        double chainBase = 0.10 * getDifficulty(); // chance to chain a follow-up
        double weakWhenSkippingChance = 0.35; // chance to do a weak action if not doing full move

        // Loop while enemy has at least the minimal cost and both combatants are alive
        while (isAlive() && player.isAlive() && getAp() >= WEAK_COST) {
            // first roll for hesitation: if true, enemy hesitates/do nothing this actor-turn
            if (rnd.nextDouble() < hesitate) {
                System.out.println(name + " hesitates...");
                try { Thread.sleep(400); } catch (InterruptedException ignored) {}
                break; // skip acting this actor-turn
            }

            // thinking message and optional taunt
            System.out.println(name + " is thinking...");
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            if (Text.roll(tauntChance)) {
                System.out.println(Text.randomEnemyLineForId(this.id));
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            }

            // Decide whether to attempt a full-cost move: prefer available highest-impact move
            Move chosen = null;
            // pick a move that fits available AP; prefer highest AP cost (more impactful)
            for (int i = moves.size() - 1; i >= 0; i--) {
                Move m = moves.get(i);
                if (m.getApCost() <= getAp()) {
                    chosen = m;
                    break;
                }
            }

            boolean usedMove = false;
            if (chosen != null) {
                // chance to actually execute chosen move (higher difficulty -> more likely)
                double execChance = 0.60 + 0.15 * (getDifficulty() - 1); // stage1 ~0.60, stage3 ~0.90
                if (rnd.nextDouble() < execChance) {
                    // perform chosen move
                    System.out.println(name + " uses " + chosen.getName() + " — " + chosen.getDescription());
                    // calculate damage using enemy base damage * move multiplier
                    int dmg = Math.max(1, (int)Math.round(damage * chosen.getDamageMultiplier()));
                    player.takeDamage(dmg);
                    // apply status if present
                    if (chosen.getApplyStatus() != null) {
                        StatusEffect proto = chosen.getApplyStatus();
                        StatusEffect s = new StatusEffect(proto.getKind(), proto.getName(), proto.getDescription(), chosen.getStatusDuration(), proto.getIntValue(), proto.getDblValue());
                        player.applyStatus(s);
                        System.out.println(player.getName() + " is affected by " + s.getName() + " (" + s.getDescription() + ") for " + s.getRemainingTurns() + " turns.");
                    }
                    spendAp(chosen.getApCost());
                    usedMove = true;
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                    // possible follow-up at higher difficulties
                    if (getAp() > 0 && rnd.nextDouble() < chainBase) {
                        System.out.println(name + " follows up with an extra strike!");
                        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
                        // use a cheap available move or do a weak jab
                        Move follow = null;
                        for (int i = moves.size()-1; i>=0; i--) { if (moves.get(i).getApCost() <= getAp()) { follow = moves.get(i); break; } }
                        if (follow != null) {
                            int fdmg = Math.max(1, (int)Math.round(damage * follow.getDamageMultiplier()));
                            System.out.println(name + " uses " + follow.getName() + "!");
                            player.takeDamage(fdmg);
                            if (follow.getApplyStatus() != null) {
                                StatusEffect proto = follow.getApplyStatus();
                                StatusEffect s2 = new StatusEffect(proto.getKind(), proto.getName(), proto.getDescription(), follow.getStatusDuration(), proto.getIntValue(), proto.getDblValue());
                                player.applyStatus(s2);
                                System.out.println(player.getName() + " is affected by " + s2.getName() + " for " + s2.getRemainingTurns() + " turns.");
                            }
                            spendAp(follow.getApCost());
                            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
                        } else if (getAp() >= WEAK_COST) {
                            int weakDmg = Math.max(1, (int)Math.round(damage * 0.5));
                            System.out.println(name + " does a quick jab!");
                            player.takeDamage(weakDmg);
                            spendAp(WEAK_COST);
                            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
                        }
                    }
                }
            }

            if (!usedMove) {
                // either couldn't pick a full move or chance failed: do weak or hesitate
                if (getAp() >= WEAK_COST && rnd.nextDouble() < weakWhenSkippingChance) {
                    int weakDmg = Math.max(1, (int)Math.round(damage * 0.5));
                    System.out.println(name + " does a quick jab (weak)!");
                    player.takeDamage(weakDmg);
                    spendAp(WEAK_COST);
                    try { Thread.sleep(800); } catch (InterruptedException ignored) {}
                } else {
                    System.out.println(name + " hesitates...");
                    try { Thread.sleep(400); } catch (InterruptedException ignored) {}
                    break; // end actor-turn on hesitation
                }
            }

            // loop will continue if AP remains and both alive
        }
    }

    @Override
    public String getMovesDescription() {
        return super.getMovesDescription();
    }
}
