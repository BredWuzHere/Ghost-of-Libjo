public class CityHallEnemies {
        public static class KuyaGuardNaStrikto extends Enemy {
        public KuyaGuardNaStrikto() {
            super("Kuya Guard na Strikto", 85, 10, 8);
        }

        @Override 
        public void specialAbility() {
            System.out.println(name + ": “Boss may ID ka?”");
        }

        public void attack1(Player player) {
            System.out.println(name + " swings his baton.");
            int dmg = 4 + (int)(Math.random() * 3); // 4-6 damage
            player.takeDamage(dmg);
        }


        public void attack2(Player player) {
            System.out.println(name + " tackles you hard to the ground.");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }
    }

    public static class TrafficEnforcerNaMayabang extends Enemy {
        public TrafficEnforcerNaMayabang() {
            super("Traffic Enforcer na Mayabang", 75, 8, 6);
        }

        @Override 
        public void specialAbility() {
            System.out.println(name + " whistles very aggressively! Your Attack goes down.");
        }

        public void attack1(Player player) {
            System.out.println(name + " pokes you with his clipboard.");
            int dmg = 4 + (int)(Math.random() * 3); // 4-6 damage
            player.takeDamage(dmg);
        }


        public void attack2(Player player) {
            System.out.println(name + " writes a fake violation ticket and slaps it onto your forehead.");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }


        public void attack3(Player player) {
            System.out.println(name + " swings his baton in a sharp arc, striking you.");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }
    }

    public static class AuditorNgKaluluwa extends Enemy {
        public AuditorNgKaluluwa() {
            super("Auditor ng Kaluluwa", 80, 9, 5);
        }

        @Override 
        public void specialAbility() {
            System.out.println(name + " files tax and drains HP!");
            currentHP = Math.min(maxHP, currentHP + 6);
        }

        public void attack1(Player player) {
            System.out.println(name + " calculates emotional damage.");
            int dmg = 4 + (int)(Math.random() * 3); // 4-6 damage
            player.takeDamage(dmg);
        }


        public void attack2(Player player) {
            System.out.println(name + " slams a stack of documents against your chest.");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }


        public void attack3(Player player) {
            System.out.println(name + " hits you with missing receipts!");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }
    }

    public static class HenchmanNiChairman extends Enemy {
        public HenchmanNiChairman() {
            super("Henchman ni Chairman", 95, 9, 8);
        }

        @Override 
        public void specialAbility() {
            attackPower += 1;
            System.out.println(name + " intensifies loyalty. Attack increased!");
        }

        public void attack1(Player player) {
            System.out.println(name + " throws a heavy punch.");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }


            public void attack2(Player player) {
            System.out.println(name + " tackles you with his whole body weight.");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }
    }

    public static class PulisNaMayBalaPeroWalangTraining extends Enemy {
        public PulisNaMayBalaPeroWalangTraining() {
            super("Pulis na May Bala (Pero Walang Training)", 65, 18, 2);
        }

        @Override 
        public void specialAbility() {
            System.out.println(name + " fires wildly! Might miss entirely.");
        }

        public void attack1(Player player) {
            System.out.println(name + " fires a shot that hits your shoulder.");
            int dmg = 4 + (int)(Math.random() * 3); // 4-6 damage
            player.takeDamage(dmg);
        }

        public void attack2(Player player) {
            System.out.println(name + " fires and the bullet lands solidly.");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }

        public void attack3(Player player) {
            System.out.println(name + " fires recklessly and the shot lands painfully regardless!");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }
    }

    public static class JanitorNaMayMopOfJustice extends Enemy {
        public JanitorNaMayMopOfJustice() {
            super("Janitor na May Mop of Justice", 90, 8, 9);
        }

        @Override 
        public void specialAbility() {
            System.out.println(name + " shouts: 'Basa yan!' Your defense drops.");
        }

        public void attack1(Player player) {
            System.out.println(name + " swings the sacred mop in a wide arc!");
            int dmg = 5 + (int)(Math.random() * 4); // 5-8 damage
            player.takeDamage(dmg);
        }

        public void attack2(Player player) {
            System.out.println(name + " slaps the floor water towards your shoes. You slip into pain!");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }
    }

    public static class PulisNaMayShotgun extends Enemy {
        public PulisNaMayShotgun() {
            super("Pulis na May Shotgun", 70, 22, 1);
        }

        @Override 
        public void specialAbility() {
            System.out.println(name + " shoots at the ceiling again...");
        }
        
        public void attack1(Player player) {
            System.out.println(name + " fires wildly, but somehow still hits you!");
            int dmg = 5 + (int)(Math.random() * 4); // 5-8 damage
            player.takeDamage(dmg);
    }

        public void attack2(Player player) {
            System.out.println(name + " pumps the shotgun dramatically then shoots your direction!");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }
    }

    public static class LegalAdvisorNaGaslighter extends Enemy {
        public LegalAdvisorNaGaslighter() {
            super("Legal Advisor na Gaslighter", 85, 7, 8);
        }

        @Override 
        public void specialAbility() {
            System.out.println(name + " manipulates your emotions. Damage increases if you're weak.");
        }

        public void attack1(Player player) {
            System.out.println(name + " slams a law book onto you!");
            int dmg = 4 + (int)(Math.random() * 3); // 4-6 damage
            player.takeDamage(dmg);
        }


        public void attack2(Player player) {
            System.out.println(name + " throws legal papers at your head!");
            int dmg = 5 + (int)(Math.random() * 4); // 5-8 damage
            player.takeDamage(dmg);
        }


        public void attack3(Player player) {
            System.out.println(name + " points out all your mistakes, hitting you physically and mentally!");
            int dmg = 6 + (int)(Math.random() * 3); // 6-8 damage
            player.takeDamage(dmg);
        }
    }
}
