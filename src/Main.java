import java.util.Scanner;

public class Main {

    static Maze maze;

    public static void main(String[] args) {
        boolean isMazeOk = false;
        Scanner scanner = new Scanner(System.in);
        while (!isMazeOk) {
            maze = new Maze(20, 20);
            maze.printMaze();
            System.out.print("Onaylamak için 1: ");
            int x = scanner.nextInt();
            if(x==1)
                isMazeOk = true;
        }
        scanner.close();

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(maze, 10000, 20,100);
        geneticAlgorithm.startAlgorithm();
    }

}
