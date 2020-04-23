import java.util.ArrayList;
import java.util.HashMap;

public class Road implements Comparable {

    private ArrayList<Integer> road;
    private int fitPoint;
    private boolean isFinished;
    private HashMap<Integer,Point> isVisited;
    private static int count;

    public Road() {
        road = new ArrayList<>();
        fitPoint = 0;
        isFinished = false;
        isVisited = new HashMap<>();
        count=0;
    }

    void addRoad(int value) {
        road.add(value);
    }

    int getRoadPoint(int index) {
        return road.get(index);
    }

    int getLastElement() {
        return road.get(road.size()-1);
    }

    public void setFitPoint(int fitPoint) {
        this.fitPoint = fitPoint;
    }

    public void setRoad(ArrayList<Integer> road) {
        this.road = road;
    }

    public ArrayList<Integer> getRoad() {
        return road;
    }

    public int getFitPoint() {
        return fitPoint;
    }

    public int getLength() {
        return road.size();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    @Override
    public String toString() {
        return "Road{" +
                "road=" + road +
                ", fitPoint=" + fitPoint +
                "}\n";
    }

    @Override
    protected Road clone() {
        Road road = new Road();
        road.setRoad((ArrayList<Integer>) this.road.clone());
        return road;
    }

    @Override
    public boolean equals(Object obj) {
        Road road = (Road) obj;
        return  this.road.equals(road);
    }


    @Override
    public int compareTo(Object o) {
        Road road = (Road) o;
        if(this.getFitPoint()>road.getFitPoint())
            return -1;
        else if(this.getFitPoint()==road.getFitPoint())
            return 0;
        return 1;
    }

    boolean addVisited(Point point) {
        if(!isVisited.containsValue(point)) {
            isVisited.put(count++, point);
            return true;
        }

        return false;
    }

    void clearMap() {
        this.isVisited.clear();
    }
}
