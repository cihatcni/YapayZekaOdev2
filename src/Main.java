import java.util.Scanner;

public class Main {

    static Maze maze;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Maze Size: " );
        int N = scanner.nextInt(); //Labirent büyüklüğü
        System.out.print("Barrier Count: ");
        int K = scanner.nextInt(); //Labirentte bulunacak engel sayısı
        System.out.println();

        boolean isMazeOk = false; //Kullanıcı labirenti onaylandı mı kontrolü için
        while (!isMazeOk) {
            maze = new Maze(N, K);
            maze.printMaze();
            System.out.print("For Accept Enter 1: "); //Görüntülenen labirent eğer algoritma için uygunsa kullanıcı 1'e basar.
            int x = scanner.nextInt();
            if(x==1)
                isMazeOk = true;
        }
        scanner.close();
        
        MainFrame frame = new MainFrame();
		frame.setVisible(true);
    }

}
