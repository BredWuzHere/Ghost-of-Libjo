import java.util.*;

public class Boss extends Enemy {
    // Stage is already inherited/set via setDifficulty, but we keep this field if it's used elsewhere.
    private int stageLevel;
    // We'll use a new field to store the drop chance/stage info from the 10-parameter call.
    private double dropChance; 
    
    // The previous Enemy/Boss constructor expected 10 arguments:
    // (id, name, maxHp, speed, ap, damage, armor, stageInfo1, dropChanceInfo, evasion)
    
    public Boss(String id, String name, int maxHp, int speed, int ap, int damage, int armor, int stageInfo1, double dropChanceInfo, double evasion) {
        
        // 1. Call the base Enemy constructor (8 parameters: id, name, maxHp, speed, ap, damage, armor, evasion)
        super(id, name, maxHp, speed, ap, damage, armor, evasion); 
        
        // 2. Assign the two extra Boss-specific parameters:
        this.stageLevel = stageInfo1;
        this.dropChance = dropChanceInfo;
        
        // NOTE: The line 'this.stage=stage;' from your original code is ambiguous 
        // as 'stage' is now 'stageInfo1', and the stage is usually set via e.setDifficulty().
    }
    
    @Override 
    public void takeTurn(Player player) {
        // NOTE: If you implemented the STUN/DOT logic in the base Enemy class, 
        // this method should reference the full implementation from the Enemy class, 
        // including checks for isAlive() after DOT and hasStatusEffect(Kind.STUN).
        processStatusEffectsStartTurn();
        
        if (!isAlive()) return; // Check if DOT killed the boss
        if (hasStatusEffect(StatusEffect.Kind.STUN)) return; // Check if STUN made it skip turn

        Random rnd = new Random();
        while (ap>0 && isAlive() && player.isAlive()) {
            
            // Boss AI logic: Find the most expensive move it can afford (greedy)
            Move chosen = null;
            for (Move m: moves) { 
                if (m.getApCost() <= ap) { 
                    if (chosen==null || m.getApCost() > chosen.getApCost()) {
                        chosen = m; 
                    } 
                } 
            }
            
            if (chosen == null) break;
            
            System.out.println(getName() + " uses " + chosen.getName() + "!");
            chosen.perform(this, player, null);
            spendAp(chosen.getApCost());
            try { Thread.sleep(400); } catch (InterruptedException ex) {}
        }
    }
}