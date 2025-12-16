import kareltherobot.World;

public class test {
    public static void main(String[] args) {
        World.setSize(10, 10);
        World.setVisible(true);
        System.out.println(World.checkBeeper(-1, 0));
        World.placeBeepers(1, 5, 2);
    }
}
