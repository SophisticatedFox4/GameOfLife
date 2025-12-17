import kareltherobot.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Setup
        Scanner input = new Scanner(System.in);
        int width, height;
        boolean custom = true;
        System.out.print("Create world? ");
        if (input.nextLine().equalsIgnoreCase("y")) {
            int[] sizes = createWorld(input);
            width = sizes[0];
            height = sizes[1];
        } else {
            custom = false;
            System.out.print("Name? ");
            World.readWorld(input.nextLine());
            width = World.numberOfAvenues();
            height = World.numberOfStreets();
        }

        World.setVisible(true);

        if (custom == true) placeBeepers(input, width, height);

        System.out.print("Speed (ms): ");
        int speed = input.nextInt();

        // Main loop
        while (true) {
            System.out.print("How many steps? ");
            int steps = input.nextInt();
            if (steps == 0)
                break;
            for (int i = 0; i < steps; i++) {
                step(width, height);
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        input.close();
    }

    public static int[] createWorld(Scanner input) {
        int width = 0, height = 0;

        // Create World
        try {
            System.out.print("Width: ");
            width = input.nextInt();

            System.out.print("Height: ");
            height = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Please enter integers");
            System.exit(1);
        }

        World.setSize(width, height);
        return new int[] {width, height};
    }

    public static void placeBeepers(Scanner input, int width, int height) {
        input.nextLine();
        String confirm = "";
        while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("yes")) {
            System.out.print("Enter coords: ");
            String[] coords = input.nextLine().split(" ");
            if (coords.length != 2) {
                System.out.println("Please enter two numbers separated by spaces.");
                continue;
            } else if (Integer.parseInt(coords[1]) > width || Integer.parseInt(coords[0]) > height) {
                System.out.println("Please make sure your coordinates are in-bounds.");
                continue;
            }
            World.placeBeepers(Integer.parseInt(coords[1]), Integer.parseInt(coords[0]), 1);
            World.repaint();
            System.out.print("Finished? ");
            confirm = input.nextLine();
        }
    }

    public static void step(int width, int height) {
        ArrayList<int[]> adds = new ArrayList<int[]>();
        ArrayList<int[]> removals = new ArrayList<int[]>();
        for (int i = 1; i < width; i++) {
            for (int j = 1; j < height; j++) {
                int count = 0;
                for (int k = i - 1; k <= i + 1; k++) {
                    for (int l = j - 1; l <= j + 1; l++) {
                        if (k == i && l == j)
                            continue;
                        if (World.checkBeeper(l, k))
                            count++;
                    }
                }
                if (count == 3 && !World.checkBeeper(j, i))
                    adds.add(new int[] { j, i });
                else if ((count < 2 || count > 3) && World.checkBeeper(j, i))
                    removals.add(new int[] { j, i });
            }
        }
        if (adds.size() == 0 && removals.size() == 0) {
            System.out.println("Game has reached stability.");
            System.exit(0);
        }
        for (int[] coords : adds)
            World.placeBeepers(coords[0], coords[1], 1);
        for (int[] coords : removals)
            World.clearBeepers(coords[0], coords[1]);
        World.repaint();
    }
}