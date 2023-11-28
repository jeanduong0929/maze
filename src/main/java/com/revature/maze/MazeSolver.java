package com.revature.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class MazeSolver {
  private final int[][] DIRECTIONS = {
      { 0, 1 },
      { 1, 0 },
      { 0, -1 },
      { -1, 0 }
  };
  private String[][] maze;
  private String filename;
  private String method;
  private int rows;
  private int cols;
  private int startX;
  private int startY;
  private boolean goalReached = false;
  private boolean noMoreSteps = false;
  private boolean[][] visited;
  private final Scanner scan;

  public MazeSolver(String filename, String method, Scanner scan) throws IOException {
    this.filename = filename;
    this.method = method;
    this.scan = scan;

    InputStream input = getClass().getClassLoader().getResourceAsStream("files/" + filename);
    if (input == null) {
      throw new IOException("File not found: " + filename);
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
      String line = reader.readLine();
      if (line == null) {
        throw new IOException("File is empty or first line not readable.");
      }

      String[] arr = line.split(" ");
      this.rows = Integer.parseInt(arr[0]);
      this.cols = Integer.parseInt(arr[1]);

      this.maze = new String[rows][cols];

      int j = 0;
      while ((line = reader.readLine()) != null && j < rows) {
        for (int i = 0; i < line.length() && i < cols; i++) {
          this.maze[j][i] = String.valueOf(line.charAt(i));
        }
        j++;
      }
    }

    visited = new boolean[rows][cols];
  }

  public void run() {
    getStartCoord();

    System.out.println("Solving maze '" + filename + "'.");
    System.out.println("Initial maze:");

    Stack<Point> stack = new Stack<>();
    Queue<Point> queue = new LinkedList<>();
    stack.push(new Point(this.startX, this.startY));
    queue.offer(new Point(this.startX, this.startY));
    visited[this.startX][this.startY] = true;

    while (!noMoreSteps) {
      step(stack, queue);

      if (goalReached || stack.isEmpty() || queue.isEmpty()) {
        System.out.println();
        noMoreSteps = true;
        continue;
      }

      System.out.println();
      printMaze();
      System.out.println();
      pause();
    }

    System.out.print("Finished: ");
    if (goalReached) {
      System.out.println("goal reached!");
    } else {
      System.out.println("no solution possible!");
    }
  }

  /* ==================== HELPER FUNCTIONS ==================== */

  private void step(Stack<Point> stack, Queue<Point> queue) {
    if (method.equals("s")) {
      stackDFS(stack);
    } else if (method.equals("q")) {
      queueBFS(queue);
    }
  }

  /**
   * TODO: Implement the Depth-First Search (DFS) algorithm using a stack.
   * This method should traverse the maze using the DFS approach. It involves
   * exploring
   * as far as possible along each branch before backtracking. This is typically
   * used
   * for more complex mazes where the path to the goal is not straightforward.
   * 
   * @param stack The stack used to keep track of the current path being explored.
   *              As the search progresses, points (or nodes) are pushed onto the
   *              stack
   *              when exploring, and popped off the stack when backtracking.
   */
  private void stackDFS(Stack<Point> stack) {
    throw new RuntimeException("Need to implement stackDFS()!");
  }

  /**
   * TODO: Implement the Breadth-First Search (BFS) algorithm using a queue.
   * This method should traverse the maze using the BFS approach. It is typically
   * used for finding the shortest path in a maze. This algorithm explores the
   * maze
   * level by level, starting at the entrance point and exploring all neighboring
   * points
   * at the present depth before moving to the next level.
   * 
   * @param queue The queue used to manage the order in which points (or nodes)
   *              are explored. Points are enqueued when discovered and dequeued
   *              as they are explored.
   */
  private void queueBFS(Queue<Point> queue) {
    throw new RuntimeException("Need to implement queueBFS()!");
  }

  private void printMaze() {
    for (String[] arr : maze) {
      for (String s : arr) {
        System.out.print(s);
      }
      System.out.println();
    }
  }

  private void pause() {
    System.out.print("Press enter to continue...");
    scan.nextLine();
  }

  private void getStartCoord() {
    exit: {
      for (int i = 0; i < rows; i++) {
        String[] arr = maze[i];
        for (int j = 0; j < cols; j++) {
          String s = arr[j];
          if (s.equals("o")) {
            this.startX = i;
            this.startY = j;
            break exit;
          }
        }
      }
    }
  }
}
