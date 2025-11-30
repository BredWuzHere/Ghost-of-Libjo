public class StatusEffect {
    public enum Kind { 
        BURN, 
        GENERIC, 
        SLIPPERY, 
        ARMOR_DOWN, 
        CRIT_DOWN,
        JOLT,      
        POISON,    
        STUN,      
        SLOW,      
        BUFF_DEFENSE,
        BUFF_ATTACK
    }
    
    private Kind kind;
    private String name;
    private String description;
    private int remainingTurns;
    private int intValue;
    private double dblValue;

    public StatusEffect(Kind kind, String name, String description, int turns, int intValue, double dblValue) {
        this.kind = kind; this.name = name; this.description = description; this.remainingTurns = turns;
        this.intValue = intValue; this.dblValue = dblValue;
    }

    public Kind getKind(){return kind;}
    public String getName(){return name;}
    public String getDescription(){return description;}
    public int getRemainingTurns(){return remainingTurns;}
    public int getIntValue(){return intValue;}
    public double getDblValue(){return dblValue;}

    public void tick(){ if (remainingTurns>0) remainingTurns--; }
    public StatusEffect copy(){ return new StatusEffect(kind, name, description, remainingTurns, intValue, dblValue); }
}