
public class Boss extends Enemy {
    private int stageLevel;
    private double dropChance; 
    
    
    public Boss(String id, String name, int maxHp, int speed, int ap, int damage, int armor, int stageInfo1, double dropChanceInfo, double evasion) {
        
        super(id, name, maxHp, speed, ap, damage, armor, evasion); 
        
        this.stageLevel = stageInfo1;
        this.dropChance = dropChanceInfo;
        
    }
    
    
    /**
     * @return The stage level this boss is associated with.
     */
    public int getStageLevel() {
        return stageLevel;
    }

    /**
     * @return The chance for this boss to drop items upon defeat.
     */
    public double getDropChance() {
        return dropChance;
    }
    
    
    @Override 
    public void takeTurn(Player player) {
        processStatusEffectsStartTurn();
        
        if (!isAlive()) return; // chckng if dot killed 
        if (hasStatusEffect(StatusEffect.Kind.STUN)) return; // Chck if stunned


        
        while (ap>0 && isAlive() && player.isAlive()) {
            
            // greedy boss actions spend high ap
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