public class Relic extends Item {
    int extraAp; double critBonus; double resistanceBonus; int addMaxHp;
    public Relic(String id, String name, String desc, int extraAp, double critBonus, double resistanceBonus, int addMaxHp) { super(id,name,desc); this.extraAp=extraAp; this.critBonus=critBonus; this.resistanceBonus=resistanceBonus; this.addMaxHp=addMaxHp; }
    public int getExtraAp(){return extraAp;} public double getCritBonus(){return critBonus;} public double getResistanceBonus(){return resistanceBonus;} public int getAddMaxHp(){return addMaxHp;}
}
