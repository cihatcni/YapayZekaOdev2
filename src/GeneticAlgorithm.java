import java.util.ArrayList;

public class GeneticAlgorithm {

    private int initialPopulation;
    private int mutationRate;
    private ArrayList<Road> roads;
    private Maze maze;
    private ArrayList<Road> finishedRoads;
    private int chooseCount;
    boolean isFind;

    public GeneticAlgorithm(Maze maze, int initialPopulation, int mutationRate, int chooseCount) {
        this.maze = maze;
        this.initialPopulation = initialPopulation;
        this.mutationRate = mutationRate;
        this.chooseCount = chooseCount;
        this.roads = new ArrayList<>();
        finishedRoads = new ArrayList<>();
    }

    private void createPopulation() {

        int K = maze.getSize();
        for (int i = 0; i < initialPopulation; i++) {
            Road road = new Road();
            for (int j = 0; j < K; j++) {
                int num = (int) (Math.random() * 1000 % 4) + 1;
                road.addRoad(num);
            }
            roads.add(road);
        }

    }

    void startAlgorithm() {
        System.out.println("GENETIC ALGORITHM STARTED.");
        createPopulation();
        int count = 0;
        isFind = false;

        while (!isFind && count < 10000) {
            //FITNESS FUNCTION
            for (Road road : roads)
                fitnessFuncWallLong(road);
            /*System.out.println("BEFORE SELECTION");
            System.out.println(roads);
            System.out.println("SIZE: " + roads.size());*/

            //SELECTION PART 1
            int minFitPoint = getAvarageFitPoint()/2;
            ArrayList<Road> selectedRoads = new ArrayList<>();
            for (Road road : roads)
                if (road.getFitPoint() > minFitPoint)
                    selectedRoads.add(road);
            roads = selectedRoads;

            //SELECTION PART 2
            roads.sort(Road::compareTo);
            if(roads.size()>chooseCount)
                roads.subList(chooseCount, roads.size()).clear();

            /*System.out.println("Selected Roads");
            System.out.println(roads);*/

            //CROSSOVER
            int j = roads.size() - 1;
            for (int i = 0; i < roads.size() / 2; i++, j--)
                crossover(roads.get(i), roads.get(j));


            //MUTASION
            int mutation = (int) (Math.random() * 1000) % 100;
            if (mutation <= mutationRate) {
                System.out.println("MUTATION!!!!!");
                int selectedRoad = (int) (Math.random() * 1000) % roads.size(); //Hangi yolda mutasyon olacağı
                int changed = (int) (Math.random() * 1000) % roads.get(selectedRoad).getLength(); //Hangi gende olacak
                int newWay = (int) (Math.random() * 1000 % 4) + 1; //Yeni değeri ne olacak
                roads.get(selectedRoad).getRoad().set(changed, newWay);
            }

            //ADD NEW WAYS TO ROAD
            selectedRoads = new ArrayList<>();
            for (Road road : roads)
                if (!road.isFinished()) {
                    for (int i = 1; i <= 4; i++) {
                        Road road1 = road.clone();

                        if((road1.getLastElement() ==1 && i==3) || (road1.getLastElement() ==3 && i==1))
                            continue;
                        if((road1.getLastElement() ==2 && i==4) || (road1.getLastElement() ==4 && i==2))
                            continue;

                        road1.addRoad(i);
                        if (!selectedRoads.contains(road1))
                            selectedRoads.add(road1);
                    }
                }
            roads = selectedRoads;

            count++;
            System.out.println("--------------- SIZE: " + roads.size() + "---------- MIN: " + minFitPoint + "-------------- " + count);
        }
        System.out.println("ALGORITHM COMPLETED.");
        System.out.println("COUNT: " + count);
        System.out.println("FINISHED ROADS: " + finishedRoads.size());
    }

    private void fitnessFuncWallLong(Road road) {
        Point active = maze.getStartPoint().clone();
        road.clearMap();
        int fitPoint = 0;
        boolean isPointChanged = false;
        int i;
        for (i = 0; i < road.getRoad().size(); i++) {
            int num = road.getRoad().get(i);
            isPointChanged = false;
            if (num == 1 && active.getY() - 1 >= 0) {         //SOL
                active.addXY(0, -1);
                isPointChanged = true;
            } else if (num == 2 && active.getX() - 1 >= 0) {  //YUKARI
                active.addXY(-1, 0);
                isPointChanged = true;
            } else if (num == 3 && active.getY() + 1 < maze.getSize()) {  //SAĞ
                active.addXY(0, 1);
                isPointChanged = true;
            } else if (num == 4 && active.getX() + 1 < maze.getSize()) {  //AŞAĞI
                active.addXY(1, 0);
                isPointChanged = true;
            }

            if (!isPointChanged || active.isWall() || !road.addVisited(active.clone()))
                break;
            else
                fitPoint++;
        }

        road.setFitPoint(fitPoint);

        if (!isPointChanged || active.isWall())
            if (road.getLength() >= i + 1)
                road.getRoad().subList(i + 1, road.getLength()).clear();

        if (!road.isFinished() && active.equals(maze.getFinishPoint())) {
            System.out.println("ÇIKIŞA ULAŞTI");
            System.out.println(road);
            road.setFinished(true);
            finishedRoads.add(road);
            isFind = true;
        }


    }

    private void fitnessFuncManhattan(Road road) {

        Point start = maze.getStartPoint();
        Point active = start.clone();

        for (int i = 0; i < road.getRoad().size(); i++) {
            int num = road.getRoad().get(i);
            boolean isPointChanged = false;
            if (num == 1 && active.getY() - 1 >= 0) {         //SOL
                active.addXY(0, -1);
                isPointChanged = true;
            } else if (num == 2 && active.getX() - 1 >= 0) {  //YUKARI
                active.addXY(-1, 0);
                isPointChanged = true;
            } else if (num == 3 && active.getY() + 1 < maze.getSize()) {  //SAĞ
                active.addXY(0, 1);
                isPointChanged = true;
            } else if (num == 4 && active.getX() + 1 < maze.getSize()) {  //AŞAĞI
                active.addXY(1, 0);
                isPointChanged = true;
            }

            if (!isPointChanged || active.isWall() || active.equals(maze.getFinishPoint()))
                break;
        }

        road.setFitPoint(active.getDiff(maze.getFinishPoint()));
    }

    private void crossover(Road road1, Road road2) {
        int pos = (int) (Math.random() * 1000) % road1.getLength();
        if (pos == 0)
            pos++;
        for (int i = pos; i < road2.getLength() && i < road1.getLength(); i++) {
            int tmp = road1.getRoad().get(i);
            road1.getRoad().set(i, road2.getRoad().get(i));
            road2.getRoad().set(i, tmp);
        }
    }

    private int getAvarageFitPoint() {

        int sum = 0;
        for (Road road : roads) sum += road.getFitPoint();
        return sum / roads.size();

    }

    private int getFittestRoadPoint() {
        int max = 0;
        for (Road road : roads)
            if (road.getFitPoint() > max)
                max = road.getFitPoint();
        return max;
    }


}
