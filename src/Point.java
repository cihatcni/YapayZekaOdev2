public class Point {

    private int x;  //X koordinatı
    private int y;  //Y koordinatı
    private boolean isVisited;  //Ziyaret edildi mi kontrolü
    private boolean isWall;     //Duvar mı kontrolü

    Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.isVisited = false;
        this.isWall = false;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    boolean isWall() {
        return isWall;
    }

    void setWall(boolean wall) {
        isWall = wall;
    }

    void addXY(int x, int y) {
        this.x += x;
        this.y += y;
        this.isWall = Main.maze.getPoint(this.x, this.y).isWall;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", isVisited=" + isVisited +
                ", isWall=" + isWall +
                '}';
    }

    protected Point clone() {
        return new Point(x, y);
    }

    int getDiff(Point point) {
        return Math.abs(x - point.x) + Math.abs(y - point.y);
    }

    boolean isVisited() {
        return isVisited;
    }

    void setVisited(boolean visited) {
        isVisited = visited;
    }

    @Override
    public boolean equals(Object obj) {
        Point point = (Point) obj;
        return this.x == point.getX() && this.y == point.getY();
    }

}
