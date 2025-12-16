import kareltherobot.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Setup
        Scanner input = new Scanner(System.in);
        int width = 0;
        int height = 0;
        ArrayList<int[]> coordinates = new ArrayList<int[]>();

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
        World.setVisible(true);

        input.nextLine();

        placeBeepers(input, width, height, coordinates);

        // Main loop
        while (true) {
            System.out.print("How many steps? ");
            int steps = input.nextInt();
            if (steps == 0) break;
            for (int i = 0; i < steps; i++) {
                step(width, height);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void placeBeepers(Scanner input, int width, int height, ArrayList<int[]> coordinates) {
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
            coordinates.add(new int[] {Integer.parseInt(coords[1]), Integer.parseInt(coords[0])});
            World.repaint();
            System.out.print("Finished? ");
            confirm = input.nextLine();
        }
    }

    public static void step(int width, int height) {
        ArrayList<int[]> adds = new ArrayList<int[]>();
        ArrayList<int[]> removals = new ArrayList<int[]>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int count = 0;
                for (int k = i - 1; k <= i + 1; k++) {
                    for (int l = j - 1; l <= j + 1; l++) {
                        if (k == i && l == j) continue;
                        if (World.checkBeeper(k, l)) count++;
                    }
                }
                if (count == 3 && !World.checkBeeper(j, i)) adds.add(new int[] {j, i});
                else if ((count < 2 || count > 3) && World.checkBeeper(j, i)) removals.add(new int[] {j, i});
            }
        }
        for (int[] coords : adds) World.placeBeepers(coords[0], coords[1], 1);
        for (int[] coords : removals) World.clearBeepers(coords[0], coords[1]);
        World.repaint();
    }
}