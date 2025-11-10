public class SimpleEnemy extends Enemy {
    public SimpleEnemy(String id, String name, int maxHp, int speed, int maxAp, int damage, int armor) {
        super(id,name,maxHp,speed,maxAp,damage,armor);
    }

    @Override
    public void takeTurn(Player p) {
        while (ap >= 2 && isAlive()) {
            attack(p);
            ap -= 2;
        }
    }
}
