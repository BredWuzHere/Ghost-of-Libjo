public class Boss extends Enemy {
    private int specialCooldown = 0;
    public Boss(String id, String name, int maxHp, int speed, int maxAp, int damage, int armor) {
        super(id,name,maxHp,speed,maxAp,damage,armor);
    }

    @Override
    public void takeTurn(Player p) {
        if (specialCooldown == 0 && ap >= 3) {
            System.out.println(name + " uses Crushing Blow!");
            int big = damage + 4;
            p.takeDamage(big);
            ap -= 3;
            specialCooldown = 3;
        }
        while (ap >= 2 && isAlive() && p.isAlive()) {
            attack(p);
            ap -= 2;
        }
        if (specialCooldown > 0) specialCooldown--;
    }
}
