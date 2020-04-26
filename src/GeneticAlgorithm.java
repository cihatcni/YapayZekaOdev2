import java.util.ArrayList;

class GeneticAlgorithm {

    private Maze maze;                      //Algoritmada kullanılacak labirent
    private int initialPopulation;          //Başlangıçtaki popülasyon büyüklüğü
    private int mutationRate;               //Mutasyon oranı (yüzde)
    private ArrayList<Road> roads;          //Oluşan yollar
    private ArrayList<Road> finishedRoads;  //Sonuca ulaşan yollar
    private int chooseCount;                //Her döngü sonunda seçilecek popülasyon büyüklüğü sayısı
    private FunctionType type;              //Fonksiyon tipi
    private boolean isStopWhenFound;        //İlk yol bulunduğunda dursun mu kontrolü için
    private int maxLoopCount;               //Maksimum döngü sayısını belirtir.

    private boolean isFind;                 //İlk yol bulunduğunda true olur.
    private int maxLength;                  //Bir yolun olabilecek maksimum uzunluğu


    GeneticAlgorithm(Maze maze, int initialPopulation, int mutationRate, int chooseCount, FunctionType type, boolean isStopWhenFound, int maxLoopCount) {
        this.initialPopulation = initialPopulation;
        this.mutationRate = mutationRate;
        this.maze = maze;
        this.chooseCount = chooseCount;
        this.type = type;
        this.isStopWhenFound = isStopWhenFound;
        this.maxLoopCount = maxLoopCount;
        this.finishedRoads = new ArrayList<>();
        this.roads = new ArrayList<>();
        this.maxLength = maze.getSize() * maze.getSize();
    }

    private void createPopulation() {

        int K = maze.getSize() / 2; //Başlangıçta rastgele oluşturalacak yol uzunluğu
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

        int count = 0;  //Kaçıncı döngüde olduğunu sayar.
        isFind = false; //Yol bulundu mu false olarak seçilir.

        while (!isFind && count < maxLoopCount) {

            //FITNESS FUNCTION
            for (Road road : roads)
                fitnessFunc(road);

            //SELECTION PART 1
            //En iyi yola yakın puanı olanlar ile devam edilir.
            int minFitPoint = getFittestRoadPoint() - 5;
            ArrayList<Road> selectedRoads = new ArrayList<>();
            for (Road road : roads)
                if (road.getFitPoint() > minFitPoint && road.getLength() > 0)
                    selectedRoads.add(road);
            roads = selectedRoads;

            //SELECTION PART 2
            //Yollar puanlarına göre sıralanır. Belirlenen miktarda olanı seçilir.
            roads.sort(Road::compareTo);
            if (roads.size() > chooseCount)
                roads.subList(chooseCount, roads.size()).clear();

            //CROSSOVER
            int j = roads.size() - 1;
            for (int i = 0; i < roads.size() / 2; i++, j--)
                crossover(roads.get(i), roads.get(j));


            //MUTASION
            int mutation = (int) (Math.random() * 1000) % 100;
            if (mutation <= mutationRate) {
                System.out.println("MUTATION!!!!!");
                int selectedRoad = (int) (Math.random() * 1000) % roads.size(); //Hangi yolda mutasyon olacağı
                for (int i = 0; i < roads.size() / 5; i++) { // Yol uzunluğunun 5'te biri kadar gende mutasyon olacak.
                    int changed = (int) (Math.random() * 1000) % roads.get(selectedRoad).getLength(); //Hangi gende olacak
                    int newWay = (int) (Math.random() * 1000 % 4) + 1; //Yeni değeri ne olacak
                    roads.get(selectedRoad).getRoad().set(changed, newWay);
                }
            }

            //ADD NEW WAYS TO ROAD
            //Yolun sonundan gidilebilecek diğer yönler eklenir.
            selectedRoads = new ArrayList<>();
            for (Road road : roads)
                if (!road.isFinished()) {
                    for (int i = 1; i <= 4; i++) {
                        //Geldiği noktaya geri dönmemesi için kontrol
                        if ((road.getLastElement() == 1 && i == 3) || (road.getLastElement() == 3 && i == 1) ||
                                (road.getLastElement() == 2 && i == 4) || (road.getLastElement() == 4 && i == 2))
                            continue;

                        Road road1 = road.clone();
                        road1.addRoad(i);
                        if (!selectedRoads.contains(road1))
                            selectedRoads.add(road1);
                    }
                }

            roads = selectedRoads;
            count++;
            System.out.println("----------- SIZE: " + roads.size() + "----- MIN: " + minFitPoint + "------ " + count);
        }

        System.out.println("ALGORITHM COMPLETED.");
        System.out.println("COUNT: " + count);
        System.out.println("FINISHED ROADS: " + finishedRoads.size());
        //Sonuçların frame üzerinde gösterilmesi
        if (finishedRoads.size() > 0) {
            MainFrame.sonucBulunduMuText.setText("ISLEM TAMAMLANDI. " + finishedRoads.size() + " TANE SONUC BULUNDU.");
            if (isStopWhenFound)
                MainFrame.ilkYolBulundugundaText.setText("ILK SONUC " + count + ". ADIMDA BULUNDU.");
        } else {
            MainFrame.sonucBulunduMuText.setText("SONUC BULUNAMADI.");
            MainFrame.ilkYolBulundugundaText.setText("TOPLAM " + count + " ADET DONGU GERCEKLESTIRILDI.");
        }
    }

    private void fitnessFunc(Road road) {
        Point active = maze.getStartPoint().clone(); //Labirent üzerinde gezinmek için active değişkeni kullanılır.
        int fitPoint = 0;
        boolean isPointChanged = false; //Belirlenen yöne gidemediyse kontrolü için

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

            if (!isPointChanged || active.isWall()) //Eğer belirtilen yöne gidemediyse ya da gittiği yön duvar ise
                break;
            else
                fitPoint++;
        }

        if (type == FunctionType.WALLLONG)
            road.setFitPoint(fitPoint);
        else //type == FunctionType.MANHATTAN
            road.setFitPoint(maxLength - active.getDiff(maze.getFinishPoint()));

        if (!isPointChanged || active.isWall()) //Fordan çıkma sebebi bunlarsa yolun devamındaki genler silinir.
            if (road.getLength() >= i)
                road.getRoad().subList(i, road.getLength()).clear();

        if (!road.isFinished() && active.equals(maze.getFinishPoint())) { //Eğer yol çıkışa ulaşmışsa
            System.out.println("REACHED TO THE FINAL!");
            System.out.println(road);
            road.setFinished(true);
            finishedRoads.add(road.clone());
            if (isStopWhenFound) //İlk yolu bulduğunda durdur aktifse isFind true olur.
                isFind = true;
        }

    }

    private void crossover(Road road1, Road road2) {
        int pos = (int) (Math.random() * 1000) % road1.getLength();
        if (pos == 0)
            pos++;
        //Random bir noktadan itibaren gen değişimi yapılır.
        for (int i = pos; i < road2.getLength() && i < road1.getLength(); i++) {
            int tmp = road1.getRoad().get(i);
            road1.getRoad().set(i, road2.getRoad().get(i));
            road2.getRoad().set(i, tmp);
        }
    }

    private int getFittestRoadPoint() {
        //En iyi yol puanını bulur.
        int max = 0;
        for (Road road : roads)
            if (road.getFitPoint() > max)
                max = road.getFitPoint();
        return max;
    }

    void updateMazeWithBestRoad() {
        //Frame üzerinde gidilecek yolu göstermek için maze üzerinde yolu visited yapar.
        Point active = new Point(0, 0);
        int minIndex = 0;
        int minSize = 0;
        if (finishedRoads.size() > 0) {

            for (int i = 0; i < finishedRoads.size(); i++) {
                if (finishedRoads.get(i).getLength() > minSize) {
                    minSize = finishedRoads.get(i).getLength();
                    minIndex = i;
                }
            }

            Road road = finishedRoads.get(minIndex);
            System.out.println("FINISHED ROAD: " + road);
            for (int i = 0; i < road.getRoad().size(); i++) {
                int num = road.getRoad().get(i);
                if (num == 1 && active.getY() - 1 >= 0) {         //SOL
                    active.addXY(0, -1);
                } else if (num == 2 && active.getX() - 1 >= 0) {  //YUKARI
                    active.addXY(-1, 0);
                } else if (num == 3 && active.getY() + 1 < maze.getSize()) {  //SAĞ
                    active.addXY(0, 1);
                } else if (num == 4 && active.getX() + 1 < maze.getSize()) {  //AŞAĞI
                    active.addXY(1, 0);
                }
                maze.getPoint(active.getX(), active.getY()).setVisited(true);
            }
        }
    }

    public enum FunctionType {
        WALLLONG, MANHATTAN
    }

}
