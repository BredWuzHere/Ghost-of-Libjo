import java.util.*;

public class Boss extends Enemy {
    private int stageLevel;
    private double dropChance; 
    
    
    public Boss(String id, String name, int maxHp, int speed, int ap, int damage, int armor, int stageInfo1, double dropChanceInfo, double evasion) {
        
        super(id, name, maxHp, speed, ap, damage, armor, evasion); 
        
        this.stageLevel = stageInfo1;
        this.dropChance = dropChanceInfo;
        
    }
    
    @Override 
    public void takeTurn(Player player) {
        processStatusEffectsStartTurn();
        
        if (!isAlive()) return; // chckng if dot killed 
        if (hasStatusEffect(StatusEffect.Kind.STUN)) return; // Chck if stunned

        Random rnd = new Random();
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