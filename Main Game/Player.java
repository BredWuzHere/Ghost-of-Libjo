import java.util.*;

public class Player {
    private String name; private int hp,maxHp,speed,ap,maxAp,armorRating; private double resistance; private double baseCrit=0.05;
    private Weapon weapon; private Armor armor; private Relic equippedRelic; private ArrayList<Item> inventory=new ArrayList<>(); private List<StatusEffect> statusEffects=new ArrayList<>(); private int x=0,y=0;
    public Player(String name,int maxHp,int speed,int maxAp){this.name=name;this.maxHp=maxHp;this.hp=maxHp;this.speed=speed;this.maxAp=maxAp;this.ap=maxAp;this.armorRating=0;this.resistance=0.0;}
    public String getName(){return name;} public int getHp(){return hp;} public int getMaxHp(){return maxHp;} public int getAp(){return ap;} public int getX(){return x;} public int getY(){return y;} public void setPosition(int x,int y){this.x=x;this.y=y;}
    public ArrayList<Item> getInventory(){return inventory;} public List<Consumable> getConsumables(){ List<Consumable> list=new ArrayList<>(); for(Item it:inventory) if(it instanceof Consumable) list.add((Consumable)it); return list; }
    public Weapon getWeapon(){return weapon;} public void equipWeapon(Weapon w){this.weapon=w;} public void equipArmor(Armor a){this.armor=a; this.armorRating=a.getArmorRating(); this.resistance=a.getResistance();} public void setEquippedRelic(Relic r){this.equippedRelic=r;}
    public void resetAp(){this.ap=this.maxAp;} public void spendAp(int a){this.ap=Math.max(0,this.ap-a);} public void addMaxHp(int n){this.maxHp+=n; this.hp+=n;}
    public double getCritChance(){ double bonus=(equippedRelic!=null)?equippedRelic.getCritBonus():0.0; double mod=0.0; for(StatusEffect s:statusEffects) if(s.getKind()==StatusEffect.Kind.CRIT_DOWN) mod+=s.getDblValue(); return Math.max(0.0, baseCrit + bonus + mod); }
    public void heal(int amount){ this.hp = Math.min(maxHp, hp+amount); } public boolean isAlive(){ return hp>0; }
    public void takeDamage(int dmg){ int armorReduction = armorRating; for(StatusEffect s: statusEffects) if (s.getKind()==StatusEffect.Kind.ARMOR_DOWN) armorReduction = Math.max(0, armorReduction - s.getIntValue()); int finalDmg = Math.max(0, dmg - armorReduction); this.hp = Math.max(0, this.hp - finalDmg); System.out.println(name + " takes " + finalDmg + " damage (after armor). HP=" + hp + "/" + maxHp); }
    public void applyStatus(StatusEffect s) { statusEffects.add(s); }
    public void processStatusEffectsStartTurn() { List<StatusEffect> toRemove=new ArrayList<>(); for(StatusEffect s:statusEffects){ if(s.getKind()==StatusEffect.Kind.BURN){ int burn=s.getIntValue(); hp=Math.max(0,hp-burn); System.out.println(name + " suffers " + burn + " burn damage. HP=" + hp + "/" + maxHp); } s.tick(); if(s.isExpired()) toRemove.add(s);} statusEffects.removeAll(toRemove); }
    public int attack(){ int dmg = (weapon!=null)?weapon.getEffectiveDamage():1; double crit = getCritChance() + (weapon!=null?weapon.critChance:0.0); boolean c = Math.random() < crit; if (c) dmg = (int)Math.round(dmg * (weapon!=null?weapon.critDamage:1.5)); return dmg; }
    public int getSpeed(){ return speed; } public Weapon getWeaponRef(){ return weapon; }

    // --- Added getters to let the InstanceGame show live player HUD ---
    public int getArmorRating() { return armorRating; }
    public double getResistance() { return resistance; }
    public Relic getEquippedRelic() { return equippedRelic; }
    public List<StatusEffect> getStatusEffects() { return Collections.unmodifiableList(statusEffects); }
}
