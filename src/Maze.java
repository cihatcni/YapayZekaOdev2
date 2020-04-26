class Maze {

    private int N;              //Labirent boyu
    private int K;              //Engel sayısı
    private Point[][] maze;     //Labirent
    private Point startPoint;   //Başlangıç noktası
    private Point finishPoint;  //Bitiş noktası

    Maze(int n, int k) {
        N = n;
        K = k;
        maze = new Point[N][N];
        mazeCreator();
    }

    private void mazeCreator() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                maze[i][j] = new Point(i, j);

        int startX; //Engel başlangıç X noktası
        int startY; //Engel balangıç Y noktası
        int way;    //Engel ilerleme yönü (yatay/dikey)

        for (int i = 0; i < K; i++) {
            boolean isFree = false;
            while (!isFree) {
                way = (int) (Math.random() * 1000) % 2;
                startX = (int) (Math.random() * 1000) % N;
                startY = (int) (Math.random() * 1000) % N;

                isFree = true;
                if (way == 0) { //DİKEY YÖNDE
                    for (int j = 0; j < 4; j++) {
                        if (startX + j < N) {
                            if (maze[startX + j][startY].isWall()) { //Seçilen yol dolu ise
                                isFree = false;
                                break;
                            }
                        } else {
                            isFree = false;
                            break;
                        }
                    }
                    if (!isFree)    //Eğer doluysa
                        way = 1;    //Diğer yönde denemesi için
                    else
                        for (int j = 0; j < 4; j++)
                            maze[startX + j][startY].setWall(true);
                }

                isFree = true;
                if (way == 1) {//YATAY YÖNDE
                    for (int j = 0; j < 4; j++) {
                        if (startY + j < N) {
                            if (maze[startX][startY + j].isWall()) {
                                isFree = false;
                                break;
                            }
                        } else {
                            isFree = false;
                            break;
                        }
                    }
                    if (isFree)
                        for (int j = 0; j < 4; j++)
                            maze[startX][startY + j].setWall(true);
                }
            }
        }

        maze[0][0].setWall(false);
        maze[N-1][N-1].setWall(false);
        startPoint = maze[0][0];
        finishPoint = maze[N-1][N-1];

    }

    void printMaze() {

        for (int i = 0; i < N + 2; i++)
            System.out.print("+ ");

        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.print("+ ");
            for (int j = 0; j < N; j++) {
                if (maze[i][j].isWall())
                    System.out.print("+ ");
                else
                    System.out.print("  ");
            }
            System.out.println("+ ");
        }

        for (int i = 0; i < N + 2; i++)
            System.out.print("+ ");
        System.out.println();
    }

    Point getStartPoint() {
        return startPoint;
    }

    Point getFinishPoint() {
        return finishPoint;
    }

    int getSize() {
        return N;
    }

    Point getPoint(int x, int y) {
        return maze[x][y];
    }

    void clearVisited() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                maze[i][j].setVisited(false);
    }
}
