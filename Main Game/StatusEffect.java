public class StatusEffect {
    public enum Kind { BURN, SLIPPERY, ARMOR_DOWN, CRIT_DOWN, STUN, GENERIC }
    private final Kind kind;
    private int remainingTurns;
    private final int intValue;
    private final double dblValue;
    private final String name;
    private final String description;

    public StatusEffect(Kind kind, String name, String description, int remainingTurns, int intValue, double dblValue) {
        this.kind = kind; this.name = name; this.description = description; this.remainingTurns = remainingTurns;
        this.intValue = intValue; this.dblValue = dblValue;
    }
    public Kind getKind(){ return kind; } public int getRemainingTurns(){ return remainingTurns; } public void tick(){ if (remainingTurns>0) remainingTurns--; } public boolean isExpired(){ return remainingTurns<=0; }
    public int getIntValue(){ return intValue; } public double getDblValue(){ return dblValue; } public String getName(){ return name; } public String getDescription(){ return description; }
}
