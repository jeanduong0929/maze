package com.jean.maze;

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
            { 0, 1 }, // Right
            { 1, 0 }, // Down
            { 0, -1 }, // Left
            { -1, 0 } // Up
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

            // Set the maze dimensions
            String[] arr = line.split(" ");
            this.rows = Integer.parseInt(arr[0]);
            this.cols = Integer.parseInt(arr[1]);

            // Initialize the maze array
            this.maze = new String[rows][cols];

            // Read the maze from the file
            int j = 0;
            while ((line = reader.readLine()) != null && j < rows) {
                for (int i = 0; i < line.length() && i < cols; i++) {
                    this.maze[j][i] = String.valueOf(line.charAt(i));
                }
                j++;
            }
        }

        // Initialize visited array
        visited = new boolean[rows][cols];
    }

    public void run() {
        // Get starting coords
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
        if (method.equalsIgnoreCase("s")) {
            stackDFS(stack);
        } else if (method.equalsIgnoreCase("q")) {
            queueBFS(queue);
        }
    }

    private void stackDFS(Stack<Point> stack) {
        Point point = stack.pop();
        int x = point.getX();
        int y = point.getY();

        // Check if we reached the goal
        if (maze[x][y].equals("*")) {
            goalReached = true;
            noMoreSteps = true;
            return;
        }

        // Update the maze with '@'
        if (maze[x][y].equals(".")) {
            maze[x][y] = "@";
        }

        // Check up, down, left, right for any adjacent cells that can be visited
        for (int[] direction : this.DIRECTIONS) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            try {
                // If the adjacent cell is '.' or '*', add it to the stack
                if (!visited[newX][newY] && (maze[newX][newY].equals(".") || maze[newX][newY].equals("*"))) {
                    stack.push(new Point(newX, newY));

                    // Mark the point as visited
                    visited[newX][newY] = true;
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {
            }
        }
    }

    private void queueBFS(Queue<Point> queue) {
        // Get the x y coords
        Point point = queue.poll();
        int x = point.getX();
        int y = point.getY();

        // Check if goal is reached
        if (maze[x][y].equals("*")) {
            goalReached = true;
            noMoreSteps = true;
            return;
        }

        // Update curr coord with '@'
        if (maze[x][y].equals(".")) {
            maze[x][y] = "@";
        }

        // Check for adjacent cells that are valid points
        for (int[] directions : DIRECTIONS) {
            int newX = x + directions[0];
            int newY = y + directions[1];

            // If the adjacent cell is '.' or '*', add it to the queue
            try {
                if (visited[newX][newY] != true && (maze[newX][newY].equals(".") || maze[newX][newY].equals("*"))) {
                    queue.offer(new Point(newX, newY));
                    visited[newX][newY] = true;
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {
            }
        }
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
