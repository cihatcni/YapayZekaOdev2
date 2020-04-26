import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

class MainFrame extends JFrame {

	private JTextField initialPopulationText;
	private JTextField chooseCountText;
	private JTextField mutationRateText;
	private JTextField maxLoopCountText;
	static JLabel sonucBulunduMuText;
	static JLabel ilkYolBulundugundaText;
	private GeneticAlgorithm geneticAlgorithm;


	MainFrame() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Yapay Zeka - Genetik Algoritma - Cihat Bozkurt Cüni - 15011041");
		setBounds(100, 100, 1010, 675);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Maze maze = Main.maze;
		
		JPanel panel = new JPanel();
		panel.setBounds(40, 40, 560, 560);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(maze.getSize(),maze.getSize(), 0, 0));

		JLabel lblNewLabel = new JLabel(new javax.swing.ImageIcon(getClass().getResource("brick.jpg")));
		lblNewLabel.setBounds(10, 10, 620, 620);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("YAPAY ZEKA - GENETÝK ALGORÝTMA");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setBounds(640, 10, 344, 33);
		contentPane.add(lblNewLabel_1);
		
		initialPopulationText = new JTextField();
		initialPopulationText.setBounds(640, 79, 344, 20);
		initialPopulationText.setText("100");
		contentPane.add(initialPopulationText);
		initialPopulationText.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Baþlangýç Popülasyon Sayýsý");
		lblNewLabel_2.setBounds(640, 54, 344, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblHerDngSonunda = new JLabel("Her Döngü Sonunda Devam Edecek Popülasyon Büyüklüðü");
		lblHerDngSonunda.setBounds(640, 110, 344, 14);
		contentPane.add(lblHerDngSonunda);
		
		chooseCountText = new JTextField();
		chooseCountText.setColumns(10);
		chooseCountText.setText("100");
		chooseCountText.setBounds(640, 135, 344, 20);
		contentPane.add(chooseCountText);
		
		JLabel lblMutasyonOranyzde = new JLabel("Mutasyon Oraný (Yüzde)");
		lblMutasyonOranyzde.setBounds(640, 166, 344, 14);
		contentPane.add(lblMutasyonOranyzde);
		
		mutationRateText = new JTextField();
		mutationRateText.setColumns(10);
		mutationRateText.setText("5");
		mutationRateText.setBounds(640, 191, 344, 20);
		contentPane.add(mutationRateText);
		
		JLabel lblSonular = new JLabel("SONUÇLAR");
		lblSonular.setHorizontalAlignment(SwingConstants.CENTER);
		lblSonular.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblSonular.setBounds(644, 398, 344, 33);
		contentPane.add(lblSonular);
		
		sonucBulunduMuText = new JLabel("");
		sonucBulunduMuText.setBounds(644, 442, 344, 14);
		contentPane.add(sonucBulunduMuText);
		
		JLabel lblMaksimumDngSays = new JLabel("Maksimum Döngü Sayýsý");
		lblMaksimumDngSays.setBounds(640, 222, 344, 14);
		contentPane.add(lblMaksimumDngSays);
		
		maxLoopCountText = new JTextField();
		maxLoopCountText.setColumns(10);
		maxLoopCountText.setText("10000");
		maxLoopCountText.setBounds(640, 247, 344, 20);
		contentPane.add(maxLoopCountText);
		
		ilkYolBulundugundaText = new JLabel("");
		ilkYolBulundugundaText.setBounds(644, 467, 344, 14);
		contentPane.add(ilkYolBulundugundaText);
		
		JCheckBox stopControl = new JCheckBox("Ýlk Yolu Bulduðunda Dur");
		stopControl.setSelected(true);
		stopControl.setBounds(640, 334, 348, 23);
		contentPane.add(stopControl);

		JLabel lblFitnessFonksiyonu = new JLabel("Fitness Fonksiyonu");
		lblFitnessFonksiyonu.setBounds(640, 278, 344, 14);
		contentPane.add(lblFitnessFonksiyonu);
		
		JComboBox<String> fitBox = new JComboBox<>();
		fitBox.setBounds(640, 303, 344, 20);
		fitBox.addItem("Ýlk duvarla karþýlaþana kadar olan uzunluðuna göre");
		fitBox.addItem("Bitiþ noktasýna yakýnlýðýna göre");
		contentPane.add(fitBox);

		JButton startButton = new JButton("Baþlat");
		startButton.addActionListener(e -> {
			maze.clearVisited();
			int initialPopulation = Integer.parseInt(initialPopulationText.getText());
			int chooseCount = Integer.parseInt(chooseCountText.getText());
			int mutationRate = Integer.parseInt(mutationRateText.getText());
			int loopCount = Integer.parseInt(maxLoopCountText.getText());
			boolean isStopWhenRoadFound = stopControl.isSelected();
			int fitnessFunc = fitBox.getSelectedIndex();
			GeneticAlgorithm.FunctionType type;
			if(fitnessFunc == 1)
				type = GeneticAlgorithm.FunctionType.WALLLONG;
			else
				type = GeneticAlgorithm.FunctionType.MANHATTAN;

			geneticAlgorithm = new GeneticAlgorithm(maze,initialPopulation,mutationRate,chooseCount,type,isStopWhenRoadFound,loopCount);
			geneticAlgorithm.startAlgorithm();
			geneticAlgorithm.updateMazeWithBestRoad();

			panel.removeAll();
			for(int i=0; i<maze.getSize(); i++) {
				for(int j=0; j<maze.getSize(); j++) {
					Button button = new Button();
					if((i==0 & j==0) || (i==maze.getSize()-1 && j==maze.getSize()-1))
						button.setBackground(Color.RED);
					else if(maze.getPoint(i,j).isVisited())
						button.setBackground(Color.MAGENTA);
					else if(maze.getPoint(i, j).isWall())
						button.setBackground(Color.BLACK);
					panel.add(button);
				}
			}
			panel.revalidate();
		});
		startButton.setBounds(644, 364, 344, 23);
		contentPane.add(startButton);
		
		for(int i=0; i<maze.getSize(); i++) {
			for(int j=0; j<maze.getSize(); j++) {
				Button button = new Button();
				if((i==0 & j==0) || (i==maze.getSize()-1 && j==maze.getSize()-1))
					button.setBackground(Color.RED);
				if(maze.getPoint(i, j).isWall())
					button.setBackground(Color.BLACK);
				panel.add(button);
			}
		}
		setLocationRelativeTo(null);
	}
}
