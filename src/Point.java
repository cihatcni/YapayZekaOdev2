public class Point {

    private int x;
    private int y;
    private boolean isVisited;
    private boolean isWall;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.isVisited = false;
        this.isWall = false;
    }

    public Point(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public void addXY(int x, int y) {
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

    public int getDiff(Point point) {
        return Math.abs(x - point.x) + Math.abs(y - point.y);
    }


    @Override
    public boolean equals(Object obj) {
        Point point = (Point) obj;
        return this.x == point.getX() && this.y == point.getY();
    }

}
