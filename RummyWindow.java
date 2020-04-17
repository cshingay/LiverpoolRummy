package rummy;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
//import rummy.*;

public class RummyWindow {

	private JFrame frame = new JFrame();
	
	private JButton btnNewCard = new JButton();
	private JButton btnShowCard = new JButton();
	private JButton btnDiscard = new JButton();
	private 	JButton btnDown = new JButton();
	private JButton btnBuy = new JButton();
	private JButton btnNext = new JButton();

	private JTextPane instructions = new JTextPane();
	private JTextPane txtpnRound = new JTextPane();
	
	private JList[] downCardsArr;
	private JButton[] playerLabelArr;
	
	private JLabel playerLabel = new JLabel();
	
	private JTextField txtAns = new JTextField();

	public Integs next = new Integs(0);
	public Integs done = new Integs(0); //0 for haven't picekd up, 1 for have
	public Integs goingDown = new Integs(0);
	public Integs buying = new Integs(0);

	public Integs pickUp = new Integs(2);
	public Stris storeCards = new Stris();
	public Stris txtAnsInp = new Stris();
	//int indexSelected = -1;
	public LinkedList<RummyPlayer> players = new LinkedList<RummyPlayer>();
	public LinkedList<Stris> plrCards = new LinkedList<Stris>();
	
	public int numPlayers;
	public JTextPane txtpnPoints = new JTextPane();
	public JLabel lblPoints = new JLabel("Points: ");
	public JList list = new JList();
	private final JScrollPane scrollPane = new JScrollPane();
	public int[] selected;
	
	public void separateCards(String list, int num, int stIndex, Card[] down) {
		String copyLis = list.toString();
		int i = 0;
		while(i<num) {
			Card toIns = createCard(copyLis);	
			
			int loc = copyLis.indexOf('/');
			copyLis = copyLis.substring(loc+1);
			down[i+stIndex] = toIns;
			i++;
		}
	}

	public static LinkedList<Card> createDeck(int players) {
		LinkedList<Card> deck = new LinkedList<Card>();
		
		int numDecks = 2;
		if(players >= 5) {
			numDecks = 3;
		}
		if(players >= 8) {
			System.out.println("uh oh :O ");
		}
		for(int j = 0; j<numDecks; j++) {
			for(int i = 1; i<14; i++) {
				Card heartCard = new Card(i, 'h');
				Card spadeCard = new Card(i, 's');
				Card diamCard = new Card(i, 'd');
				Card clubCard = new Card(i, 'c');
				
				deck.add(heartCard);
				deck.add(spadeCard);
				deck.add(clubCard);
				deck.add(diamCard);
			}
			Card joker1 = new Card(14, 'r');
			Card joker2 = new Card(14, 'b');
			
			deck.add(joker1);
			deck.add(joker2);
		}
		return deck;
	}
	
	public boolean checkSelection() {
		if(list.getSelectedIndex() == -1) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public void reset() {
		for(int i = 0; i<numPlayers; i++) {
			DefaultListModel DLM = new DefaultListModel();
			downCardsArr[i].setModel(DLM);
		}
		frame.revalidate();
		frame.repaint();
	}
	
	public void goingDown(RummyPlayer plr, int round) {
		goingDown.setInt(0);
		int setsruns = determineSetsRuns(round);
		int sets = setsruns/10;
		int runs = setsruns%10;
		String setDown = "";
		Card[] downCards = new Card[sets*3 + runs*4];
		DefaultListModel DLM = new DefaultListModel();

		for(int h = 0; h<sets; h++) {
			updateInstr("\"Select the cards in your set #"+h+" then enter ok!");
			while(txtAnsInp.equals(" ")) { //while
				try {
					TimeUnit.SECONDS.sleep(2);
				}
				catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			}
			
			int[] sele = list.getSelectedIndices();
			
			txtAnsInp.setText(" ");
			for(int i = 0; i<3; i++) {
				DLM.addElement(list.getModel().getElementAt(sele[i]));				
			}
			
			downCardsArr[plr.num].setModel(DLM);
		}
		for(int h = 0; h<runs; h++) {
			updateInstr("Please select the cards in your run #"+h+", then enter ok!");
			while(txtAnsInp.equals(" ")) { //while
				try {
					TimeUnit.SECONDS.sleep(2);
				}
				catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			}
						
			int[] sele = list.getSelectedIndices();
			
			txtAnsInp.setText(" ");
			for(int i = 0; i<3; i++) {
				DLM.addElement(list.getModel().getElementAt(sele[i]));
			}
			
			downCardsArr[plr.num].setModel(DLM);
		}
		updateInstr("If anybody objects, please enter no in the text box. Else, enter ok!");
		while(txtAnsInp.equals(" ")) {
			try {
				TimeUnit.SECONDS.sleep(2);
			}
			catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
		String name = txtAnsInp.getText();
		txtAnsInp.setText(" ");
		
		if(name.contains("n")) {
			System.out.print("oopies");
			updateInstr("uh oh, someone said it wasn't okay! try again another time.");
			DefaultListModel dummy = new DefaultListModel();
			downCardsArr[plr.num].setModel(dummy);
		}
		
		else {
			updateInstr("good job to the player who went down!");
			for(int j = 0; j< downCards.length; j++) {
				plr.discard(createCard( (String) DLM.get(j)));
			}
		}
		
		updatePlayer(plr.num, plr);
		frame.revalidate();
		frame.repaint();
	}
	
	public static Card createCard(String answer) {
		String cardNum = answer.substring(0,1);
		if(cardNum.equals(" ")) {
			answer = answer.substring(1);
			cardNum = answer.substring(0, 1);
		}

		int num = 0;
		try {
			num = Integer.parseInt(cardNum);
			if(num==1) {
				num = 10;
			}
		}
		catch (NumberFormatException e) {
			if(cardNum.equals("a")) {
				num = 1;
			}
			else if(cardNum.equals("j")) {
				if(answer.substring(1).contains("o")) {
					num = 14;
				}
				else {
					num = 11;
				}
			}
			else if(cardNum.equals("q")) {
				num = 12;
			}
			else if(cardNum.equals("k")) {
				num = 13;
			}
		}
		int comma = answer.indexOf(",");
		char su = answer.charAt(comma+1);
		if(su == ' ') {
			su = answer.charAt(comma+2);
		}
		Card retr = new Card(num, su);
		return retr;
		
	}
	
	public static int determineSetsRuns(int round) {
		int sets, runs = 0;
		if(round == 0) {
			sets = 2; runs = 0; }
		else if(round == 1) {
			sets = 1; runs = 1; }
		else if(round == 2) {
			sets = 0; runs = 2; }
		else if(round == 3) {
			sets = 3; runs = 0; }
		else if(round == 4) {
			sets = 2; runs = 1; }
		else if(round == 5) {
			sets = 1; runs = 2; }
		else {
			sets = 0; runs = 3; }
		
		//format: setsruns
		int retr = sets*10 + runs;
		return retr;
	}
	
	public void updatePoints() {
		String points = "";
		sort(players, 0, players.size()-1);
		
		for(int i = 0; i<numPlayers; i++) {
			points+= "Player "+players.get(i).num+": "+ players.get(i).getRoundPoints()+"/" + players.get(i).getPoints()+ "\n";
		}
		txtpnPoints.setText(points);
		frame.revalidate();
		frame.repaint();
	}
	
	public void sort(LinkedList<RummyPlayer> plrs, int low, int high){
		if(low < high) {
			int pi = partition(plrs, low, high);
			sort(plrs, low, pi-1);
			sort(plrs, pi+1, high);
		}
	}
	
	public int partition(LinkedList<RummyPlayer> plrs, int low, int high) {
		int pivot = plrs.get(high).getPoints();
		int i = (low-1);
		for(int j = low; j< high; j++) {
			if(plrs.get(j).getPoints() < pivot){
				i++;
				RummyPlayer temp = plrs.get(i);
				plrs.set(i, plrs.get(j));
				plrs.set(j, temp);
			}
		}
		
		RummyPlayer temp = plrs.get(i+1);
		plrs.set(i+1,  plrs.get(high));
		plrs.set(high, temp);
		return i+1;
	}
	
	public void updateCard(Card disc) {		
		btnDiscard.setText(disc.abbrvCard());
		frame.revalidate();
		frame.repaint();
		
	}
	
	public void updateInstr(String newInstr) {
		instructions.setText(newInstr);

		frame.revalidate();
		frame.repaint();
	}
	
	public void updatePlayer(int player, RummyPlayer plr) {
		playerLabel.setText("Player "+player);
		String cards = "";
		DefaultListModel DLM = new DefaultListModel();
		
		for(int k=0; k<plr.cards.size(); k++) {
			DLM.addElement(plr.cards.get(k).getCard());
		}
		
		list.setModel(DLM);
		frame.revalidate();
		frame.repaint();
	}
	
	public void updateFrame(int round) throws NullPointerException {
		int setsRuns = determineSetsRuns(round);
		txtAns.setText(" ");

		txtpnRound.setText("This round you're playing for: "
				+ setsRuns/10 + " sets and " + setsRuns%10 +" runs!");
		frame.revalidate();
		frame.repaint();
		
		String txt = instructions.getText();
	}
	
	public int getNumPlayers() {
		Integs num = new Integs();
		String words = txtAns.getText();
		try {
			num.setInt(Integer.parseInt(words));
		}
		catch(NumberFormatException i) {
		}
		numPlayers = num.getNum();
		downCardsArr = new JList[numPlayers];
		playerLabelArr = new JButton[numPlayers];

		for(int i = 0; i< numPlayers; i++) {
			int xFactor = i%4;
			int yFactor = i/4;
			
			JButton newName = new JButton();
			newName.setText("Player "+i+":");
			//newName.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
			newName.setBounds(30 + xFactor * 163, 341 + yFactor * 91, 111, 16);
			playerLabelArr[i] = newName;
			frame.getContentPane().add(newName);
			Integs eye = new Integs(i);
			newName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					/*
					JDialog d = new JDialog(frame, "Player "+eye.getNum()+":");
					JTextPane lab = new JTextPane();
					lab.setText(plrCards.get(eye.getNum()).getText());
					lab.setEditable(false);
					d.setSize(300, 100);
					
					d.setLocation(50, 50);
					d.add(lab);
					d.setVisible(true); */
					JOptionPane.showMessageDialog(null, plrCards.get(eye.getNum()).getText(), "Player "+eye.getNum()+":", JOptionPane.PLAIN_MESSAGE);
				}
			});
			
			JList newCards = new JList();
			
			JScrollPane scrollCards = new JScrollPane();
			scrollCards.setBounds(16 + 163*xFactor, 362+yFactor*89, 154, 60);
			
			newCards.setBackground(new Color(245, 245, 245));
			newCards.setForeground(new Color(0, 0, 0));
			newCards.setFont(new Font("Lucida Grande", Font.ITALIC, 11));
			downCardsArr[i] = newCards;
			scrollCards.setViewportView(newCards);
			frame.getContentPane().add(scrollCards);			
		}
		
		frame.revalidate();
		frame.repaint();
		
		return num.getNum();
	}
	
	public int buyingSequence() {
		while(txtAnsInp.getText() == " ") {
			try {
				TimeUnit.SECONDS.sleep(2);
				
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		int buyer;
		String check = txtAnsInp.getText();
		if(txtAnsInp.getText().charAt(0) == ' ') {
			check = txtAnsInp.getText().substring(1);
		}
		
		try {
			buyer = Integer.parseInt(check);
		}
		catch (NumberFormatException e) {
			if(txtAnsInp.getText().contains("n")){
				txtAnsInp.setText(" ");
				return -1;
			}
			updateInstr("Whoops, that wasn't right! Try again. If this was an error please enter no");
			txtAnsInp.setText(" ");
			return -2;
		}
		return buyer;
	}
	
	public RummyWindow() throws InterruptedException {
		initialize();
		frame.setVisible(true);		
	}
	
	public void updatePlayers(RummyPlayer[] plrs) {
		players.clear();
		plrCards.clear();
		for(int i = 0; i<numPlayers; i++) {
			players.add(plrs[i]);
			Stris curr = new Stris(plrs[i].getCards());
			plrCards.add(curr);
		}
	}
	
	public RummyPlayer getPlayers(int i) {
		return players.get(i);
	}
	
	private void initialize() throws InterruptedException {	
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 808, 587);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		instructions = new JTextPane();
		instructions.setBackground(new Color(255, 255, 255));
		instructions.setForeground(new Color(0, 0, 128));
		instructions.setEditable(false);
		instructions.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		instructions.setText("Please insert number of players in the text box to the right:");
		instructions.setBounds(36, 49, 316, 60);
		frame.getContentPane().add(instructions);
		
		playerLabel = new JLabel("Player X");
		playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerLabel.setBounds(36, 121, 111, 16);
		frame.getContentPane().add(playerLabel);
		btnNewCard.setForeground(new Color(0, 0, 128));

		btnNewCard.setText("Mystery");
		btnDiscard.setForeground(new Color(0, 0, 128));
		btnDiscard.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		//btnNewCard.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		btnNewCard.setBackground(Color.RED);
		btnNewCard.setBounds(402, 145, 99, 84);
		btnNewCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(done.getNum()+", "+pickUp.getNum());
				if(done.getNum() == 0) {
					pickUp.setInt(1);
					done.setInt(1);
				}
			}
		});
		frame.getContentPane().add(btnNewCard);

		btnDiscard.setText("Discard");
		btnDiscard.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		btnDiscard.setBackground(Color.RED);
		btnDiscard.setBounds(278, 144, 99, 84);
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(done.getNum()+", "+pickUp.getNum());
				if(done.getNum() == 0) {
					pickUp.setInt(0);
					done.setInt(1);
				}
			}
		});
		
		frame.getContentPane().add(btnDiscard);
			
		txtAns = new JTextField();
		txtAns.setFont(new Font("Lucida Grande", Font.ITALIC, 12));
		txtAns.setBounds(384, 49, 316, 60);
		txtAns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtAnsInp.getText().equals(" ")) {
					txtAnsInp.setText(txtAns.getText());
					txtAns.setText(" ");
				}
				else {
					txtAnsInp.setText(txtAns.getText());
					txtAns.setText(" ");
				}
			}
		});
		
		
		frame.getContentPane().add(txtAns);
		txtAns.setColumns(10);
	
		btnDown.setForeground(new Color(0, 0, 128));
		
		btnDown.setText("Going Down");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goingDown.setInt(1);
			}
		});
		btnDown.setBounds(278, 244, 99, 48);
		frame.getContentPane().add(btnDown);
		
		txtpnRound = new JTextPane();
		txtpnRound.setForeground(new Color(0, 0, 128));
		txtpnRound.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		txtpnRound.setEditable(false);
		txtpnRound.setText("going for:");
		txtpnRound.setBounds(166, 21, 383, 16);
		frame.getContentPane().add(txtpnRound);
		
		btnBuy.setText("Buy");
		btnBuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instructions.setText("Please enter the player who wanted to buy:");
				buying.setInt(1);
			}
		});
		btnBuy.setBounds(277, 304, 224, 29);
		frame.getContentPane().add(btnBuy);
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				next.setInt(1);
			}
		});
		btnNext.setForeground(new Color(0, 0, 128));
		btnNext.setText("Next Turn");
		btnNext.setBounds(402, 244, 99, 48);
		
		frame.getContentPane().add(btnNext);
		txtpnPoints.setText("points go here");
		txtpnPoints.setFont(new Font("Lucida Grande", Font.ITALIC, 12));
		txtpnPoints.setBackground(Color.WHITE);
		txtpnPoints.setBounds(558, 157, 142, 157);
		
		frame.getContentPane().add(txtpnPoints);
		lblPoints.setHorizontalAlignment(SwingConstants.CENTER);
		lblPoints.setBounds(569, 121, 111, 16);
		frame.getContentPane().add(lblPoints);
		
		btnShowCard.setText("Show Cards");
		btnShowCard.setBounds(36, 144, 111, 29);
		
		btnShowCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean visible = list.isVisible();
				
				if(!visible) {
					list.setVisible(true);
					btnShowCard.setText("Hide Cards");
					frame.revalidate();
					frame.repaint();
				}
				else {
					list.setVisible(false);
					btnShowCard.setText("Show Cards");
					frame.revalidate();
					frame.repaint();
				}
			}
		});
		frame.getContentPane().add(btnShowCard);
		scrollPane.setBounds(36, 178, 111, 149);
		frame.getContentPane().add(scrollPane);
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int k = list.getSelectedIndex();
				DefaultListModel arr = (DefaultListModel) list.getModel();
				System.out.println(e.getKeyCode());
				if(k == -1) {
					
				}
				else if(e.getKeyCode() == 38) {
					if(k != 0) {
						Object store = list.getModel().getElementAt(k-1);						
						arr.set(k-1, arr.getElementAt(k));
						arr.set(k, store);
					}
				}
				else if(e.getKeyCode() == 40) {
					if(k != (arr.getSize()-1)) {
						Object store = arr.getElementAt(k);
						arr.set(k, arr.getElementAt(k+1));
						arr.set(k+1,  store);
					}
				}
				list.setModel(arr);
				frame.revalidate();
				frame.repaint();
			}
		});
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int[] sel = list.getSelectedIndices();
				selected = sel;
			}
		});
		

		
		list.setVisible(true);
		
		scrollPane.setViewportView(list);
		
		}
}
