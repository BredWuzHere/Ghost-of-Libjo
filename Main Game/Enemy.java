import java.util.*;

public class Enemy extends Entity {
    protected List<Move> moves = new ArrayList<>();
    protected int stage = 1;
    protected double evasion; // New stat for evasion (0.0 to 1.0)
    protected List<StatusEffect> activeEffects = new ArrayList<>(); // Inherited/implied list
    
    // Updated Constructor: Now accepts 'evasion'
    public Enemy(String id,String name,int maxHp,int speed,int ap,int damage,int armor, double evasion){ 
        super(id,name,maxHp,speed,ap,damage,armor); 
        this.evasion = evasion; // Initialize evasion
    }
    
    public void addMove(Move m){ moves.add(m); } public List<Move> getMoves(){ return moves; }
    
    // --- New Evasion Logic ---
    
    // Method to check if an attack hits, considering status effects (SLIPPERY)
    public boolean isHit(double accuracy) {
        double currentEvasion = evasion;
        
        // Add evasion bonus from status effects (SLIPPERY)
        double effectEvasionBonus = activeEffects.stream()
            .filter(e -> e.getKind() == StatusEffect.Kind.SLIPPERY)
            .mapToDouble(StatusEffect::getDblValue)
            .sum();
            
        currentEvasion += effectEvasionBonus;
        
        // The chance to miss is equal to the enemy's evasion rate.
        double missChance = currentEvasion;
        
        // A hit succeeds if a random number (0 to 1) is greater than the miss chance.
        // The player's base accuracy (1.0 or less) is already passed in.
        
        if (Math.random() < missChance) {
            System.out.println(getName() + " avoided the attack!");
            return false; // Miss
        }
        return true; // Hit
    }

    // --- Status Effect Placeholders (from previous turn) ---
    // NOTE: You must include the full methods for these (addStatusEffect, getArmorFromEffects, etc.)
    // as defined in our previous exchange if you use 'activeEffects'.
    public void processStatusEffectsStartTurn() { /* ... implementation for DOT, STUN, and Ticks ... */ }
    public boolean hasStatusEffect(StatusEffect.Kind kind) { /* ... implementation ... */ return false; }


    // --- Take Turn Method (Modified to reflect STUN check) ---
    public void takeTurn(Player player) {
        processStatusEffectsStartTurn(); // Process DOT and Tick status effects
        
        if (!isAlive()) return; 
        if (hasStatusEffect(StatusEffect.Kind.STUN)) return; // Check if STUN skips turn

        double hesitate = (stage==1?0.40:(stage==2?0.15:0.03));
        if (Math.random() < hesitate) { System.out.println(getName() + " hesitates and does nothing."); return; }
        
        int currentAp = ap; // Use AP from Entity base class
        
        while (currentAp > 0 && isAlive() && player.isAlive()) {
            List<Move> affordable = new ArrayList<>();
            for (Move m: moves) if (m.getApCost()<=currentAp) affordable.add(m);
            
            if (affordable.isEmpty()) break;
            
            Move chosen = affordable.get(new Random().nextInt(affordable.size()));
            System.out.println(getName() + " chooses " + chosen.getName() + "!");
            chosen.perform(this, player, null);
            
            // Use spendAp/update AP value
            currentAp -= chosen.getApCost();
            
            try { Thread.sleep(300); } catch (InterruptedException ex) {}
        }
    }
    
    // --- Other Original Methods ---
    public String getMovesDescription(){ StringBuilder sb=new StringBuilder(); for (Move m: moves) sb.append(m.getName()).append(" (AP:"+m.getApCost()+") "); return sb.toString(); }
    public int getAttackValue(){ return damage; }
    public void setDifficulty(int s){ this.stage = s; }
}