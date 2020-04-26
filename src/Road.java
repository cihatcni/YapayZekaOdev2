import java.util.ArrayList;

public class Road implements Comparable {

    private ArrayList<Integer> road;    //Yol yönleri sırasıyla
    private int fitPoint;               //Puanı
    private boolean isFinished;         //Yol sonuca ulaştı mı kontrolü

    Road() {
        road = new ArrayList<>();
        fitPoint = 0;
        isFinished = false;
    }

    void addRoad(int value) {
        road.add(value);
    }

    int getLastElement() {
        return road.get(road.size()-1);
    }

    void setFitPoint(int fitPoint) {
        this.fitPoint = fitPoint;
    }

    private void setRoad(ArrayList<Integer> road) {
        this.road = road;
    }

    ArrayList<Integer> getRoad() {
        return road;
    }

    int getFitPoint() {
        return fitPoint;
    }

    int getLength() {
        return road.size();
    }

    boolean isFinished() {
        return isFinished;
    }

    void setFinished(boolean finished) {
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

}
