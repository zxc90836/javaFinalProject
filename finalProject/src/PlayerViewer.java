import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PlayerViewer{

	private RankTypeHandler rankTypeHandler = new RankTypeHandler();
	private PlayerInfoReader reader;
	private PlayerInfoWriter writer;
	private String fileName;
	// Constructor
	public PlayerViewer(String fileName) {

		// create reader and writer
		// TODO
		this.fileName = fileName;
		reader = new PlayerInfoReader(this.fileName + ".txt");
		writer = new PlayerInfoWriter(this.fileName + ".txt");
		ArrayList<Player> list= reader.readAllPlayers();
		for(Player e:list)
		System.out.printf("%s\n",e);

	}
	public Player getUserInfo(){
		ArrayList<Player> list= reader.readAllPlayers();
		return list.get(0);
	}
	/*private String readName(){
		return JOptionPane.showInputDialog(this, "Please input the player's name");
	}
	private Rank readRank(){
		return rankTypeHandler.produceRank(JOptionPane.showInputDialog(this, "Please input the player's rank"));
	}
	private int readAverageDamage(){
		return Integer.parseInt(JOptionPane.showInputDialog(PlayerViewer.this, "Please input the player's averageDamage"));
	}
	private void addPlayer() {
		String name;
		int averageDamage,id,times;
		Rank rank;
		double totalScore;
		//input name
		//String Name = ;
		name = readName();
		rank = readRank();
		try {
			averageDamage = readAverageDamage();
		} catch (NumberFormatException ex) {
			averageDamage = 0;
		}

		// write a record to the text file
		// TODO		
		writer.addPlayer(name, rank.getName(), averageDamage);
		updateTable();
	}

	private void updateTable() {
		remove(tableWithScrollBar);
		fillData();
		SwingUtilities.updateComponentTreeUI(this);
	}

	private void fillData() {
		
		// read all records from the text file
		// TODO
		Object[][] data= reader.readAllPlayers();
		playersTable = new JTable(data, COLUME_NAMES);
		tableWithScrollBar = new JScrollPane(playersTable);
		add(tableWithScrollBar, BorderLayout.CENTER);
	}

	private class MyEventHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addButton) {
				addPlayer();
			} else if (e.getSource() == updateButton) {
				updateTable();
			}
		}
	}*/
}
