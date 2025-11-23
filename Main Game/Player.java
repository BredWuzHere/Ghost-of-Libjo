import java.util.*;

public class Player extends Entity {
    private List<Item> inventory = new ArrayList<>();
    private Weapon weapon = null;
    private Armor armor = null;
    private Relic equippedRelic = null;

    public Player(String name, int maxHp, int speed, int ap) {
        super("player", name, maxHp, speed, ap, 4, 2);
    }

    public List<Item> getInventory(){ return inventory; }
    public Weapon getWeapon(){ return weapon; }
    public Armor getEquippedArmor(){ return armor; }
    public void equipWeapon(Weapon w){ this.weapon = w; }
    public void equipArmor(Armor a){ this.armor = a; }
    public void setEquippedRelic(Relic r){ this.equippedRelic = r; }

    public int attack() {
        int base = (weapon != null ? weapon.getEffectiveDamage() : this.damage);
        double critChance = (weapon != null ? weapon.critChance : 0.0);
        if (equippedRelic != null) critChance += equippedRelic.critBonus;
        boolean crit = Math.random() < critChance;
        int result = base;
        if (crit) result = (int)Math.round(base * (weapon!=null ? weapon.critDamage : 1.5));
        return result;
    }

    public List<Consumable> getConsumables() {
        List<Consumable> list = new ArrayList<>();
        for (Item it : inventory) if (it instanceof Consumable) list.add((Consumable)it);
        return list;
    }

    public void processStatusEffectsStartTurn() {
        super.processStatusEffectsStartTurn();
    }

    public List<StatusEffect> getActiveStatuses(){ return super.getActiveStatuses(); }

    public void heal(int amount) {
        super.heal(amount);
    }
}
