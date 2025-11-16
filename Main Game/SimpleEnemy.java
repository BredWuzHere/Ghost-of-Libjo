public class SimpleEnemy extends Enemy { 
    // The constructor must now accept 8 parameters (7 existing + evasion)
    public SimpleEnemy(String id, String name, int maxHp, int speed, int ap, int damage, int armor, double evasion) { 
        // Passes all 8 parameters to the base Enemy class
        super(id, name, maxHp, speed, ap, damage, armor, evasion); 
    } 
}