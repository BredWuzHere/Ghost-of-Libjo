public abstract class Enemy {
    protected String id;
    protected String name;
    protected int maxHp;
    protected int hp;
    protected int speed;
    protected int maxAp;
    protected int ap;
    protected int damage;
    protected int armor;

    public Enemy(String id, String name, int maxHp, int speed, int maxAp, int damage, int armor) {
        this.id = id; this.name = name; this.maxHp = maxHp; this.hp = maxHp; this.speed = speed; this.maxAp = maxAp; this.ap = maxAp; this.damage = damage; this.armor = armor;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getSpeed() { return speed; }
    public boolean isAlive() { return hp > 0; }
    public void resetAp() { this.ap = maxAp; }
    public void takeDamage(int amount) {
        int reduced = Math.max(0, amount - armor);
        this.hp -= reduced;
        System.out.println(name + " took " + reduced + " damage. HP=" + Math.max(0,hp));
    }
    public void attack(Player p) {
        System.out.println(name + " attacks you for " + damage + " damage.");
        p.takeDamage(damage);
    }
    public abstract void takeTurn(Player p);
}
