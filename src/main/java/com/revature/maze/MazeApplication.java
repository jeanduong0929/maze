package com.revature.maze;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class MazeApplication {
  public static void main(String[] args) throws IOException {
    String filename = "";
    String method = "";
    Scanner scan = new Scanner(System.in);

    exitFilename: {
      while (true) {
        clearScreen();
        System.out.print("Please enter a maze filename: ");
        filename = scan.nextLine();
        if (!fileExist(filename)) {
          clearScreen();
          System.out.println("File not found!");
          System.out.print("Please press enter to continue...");
          scan.nextLine();
          continue;
        }
        break exitFilename;
      }
    }

    exitMethod: {
      while (true) {
        clearScreen();
        System.out.print("Please enter (S) to use a stack, or (Q) to use a queue: ");
        method = scan.nextLine();

        if (!method.equalsIgnoreCase("s") && !method.equalsIgnoreCase("q")) {
          clearScreen();
          System.out.println("Invalid method!");
          System.out.print("Please press enter to continue...");
          scan.nextLine();
          continue;
        }
        break exitMethod;
      }
    }

    MazeSolver solver = new MazeSolver(filename, method, scan);
    solver.run();
    scan.close();
  }

  /* #################### HELPER FUNCTION #################### */

  private static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  private static boolean fileExist(String filename) throws IOException {
    try (InputStream input = MazeApplication.class.getClassLoader().getResourceAsStream("files/" + filename)) {
      if (input == null) {
        return false;
      }
      return true;
    }
  }
}
