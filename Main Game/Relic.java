public class Relic extends Item {
    public int extraAp;
    public double critBonus;
    public double resistanceBonus;
    public int addMaxHp;

    public Relic(String id, String name, String description, int extraAp, double critBonus, double resistanceBonus, int addMaxHp) {
        super(id,name,description);
        this.extraAp = extraAp; this.critBonus = critBonus; this.resistanceBonus = resistanceBonus; this.addMaxHp = addMaxHp;
    }
}
