package rummy;
import java.lang.Math;
import java.util.*;
import java.util.concurrent.TimeUnit;

//import rummy.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* rUmmY UpdAtEs
 * 
 * (16/4/20)
 * > Completed:  
 * 		- allowed to sort own cards in preferential order /\ (38) \/ (40)
 * 		- fixed the putting down jokers
 * 		- created JLIST and simplified SO MUCH
 * 		- fixed summing for rounds (broke sorting)
 * > To Do: 
 * 		- allow to play down on others/own hands
 * 		- when sort own hands, ensure it updates player arrays as well
 * 		- sorting of points broke idk how
 * 
 */

public class RummyDriver {
		
	public static void main(String[] args) throws InterruptedException {
		int turn = 0;
		int round = 0;
		int numPlayers = 0;
		boolean pat = false;
		boolean playRound = true;
		int setsRuns = 0;
		
		
		RummyWindow window = new RummyWindow();
		while(numPlayers == 0) {
			numPlayers = window.getNumPlayers();
		}
		window.updateInstr("Great, you said: "+numPlayers+" are playing!");
			
		for(round = 0; round < 7; round ++) {
			Card buyCard = new Card();
			Stack<Card> discardPile = new Stack<Card>();
			discardPile.push(new Card());
			window.reset();
			
			boolean firstTurn = true;
			LinkedList<Card> deck = new LinkedList<Card>();
			
			window.updateFrame(round);
			playRound = true;
			
			int dealtCards = 6 + round;
			if(round>=5) {
				dealtCards --;
				pat = true;
			}
			deck = createDeck(numPlayers);
			RummyPlayer[] players = new RummyPlayer[numPlayers];
			for(int i = 0; i<numPlayers; i++) {
				players[i] = new RummyPlayer(i);
			}
			
			for(int i = 0; i<dealtCards; i++) {
				for(int j = 0; j<numPlayers; j++) {
					players[j].drawCard(deck);
				}
			}
			window.updatePlayers(players);
			turn = round%numPlayers;
			
			while(playRound) {
				window.done.setInt(0);
				window.pickUp.setInt(2);
				boolean bought = false;
				
				if(turn == numPlayers) {
					turn = 0;
				}
				window.updateInstr("Player "+turn+"'s turn! Pick up a card");

				window.updatePlayer(turn, players[turn]);
				
				/*** picking up card ***/
				
				if(firstTurn) {
					int range = deck.size();
					int rand = (int) (Math.random() * range);
					buyCard = deck.get(rand);
					discardPile.push(buyCard);
					deck.remove(rand);
					firstTurn = false;
				}
				
				window.updateCard(buyCard);
				int k = 0;
				/*try {
					TimeUnit.SECONDS.sleep(3);
				}
				catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
						*/		
				while(window.done.getNum() == 0) {
					try {
						TimeUnit.SECONDS.sleep(2);
						if(window.buying.getNum() == 1) {
							bought = true;
							int buyer = -2;
							while(buyer == -2) {
								buyer = window.buyingSequence();
							}
							if(buyer == -1) {
								bought = false;
								break;
							}
							else {
								if(buyer == turn) {
									window.updateInstr("you can't buy! it's your turn D:");
									bought = false;
								}
								else if(buyer == (turn-1)) {
									window.updateInstr("you can't buy! that's ur discard D:");
									bought = false;
								}
								else {
									players[buyer].addCard(buyCard);
									players[buyer].drawCard(deck);
									discardPile.pop();
									window.updateCard(new Card());
									window.buying.setInt(0);
									window.updateInstr("Player "+turn+", pick up a card!");
									window.updatePlayers(players);

								}
							}
						}
					}
					catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				}
				
				window.updateInstr("Player "+turn+"'s turn! Pick up a card");

				k = window.pickUp.getNum();
				
				window.done.setInt(0);
				window.pickUp.setInt(2);
				
				if(k==0 && bought==false) {
					players[turn].addCard(buyCard);
					discardPile.pop();
					window.updateCard(new Card());
				}
				else if(k==1) {
					players[turn].drawCard(deck);
				}
				players[turn].printCards();
				window.updatePlayer(turn, players[turn]);
				window.updatePlayers(players);

				window.txtAnsInp.setText(" ");
				boolean correct = false;
				
				while(!correct) {
					boolean sel = false;
					while(!sel) {
						window.updateInstr("Player "+turn+", please select the card you want to discard!");
						sel = window.checkSelection();
						if(window.goingDown.getNum() == 1) {
							window.goingDown(players[turn], round);
						}
						
						try {
							TimeUnit.SECONDS.sleep(1);
						}
						catch (InterruptedException ie) {
							Thread.currentThread().interrupt();
						}
					}
					
					window.txtAnsInp.setText(" ");
					
					String disc = (String) window.list.getSelectedValue();
					Card toDiscard = createCard(disc);
					
					correct = players[turn].discard(toDiscard);
					if(!correct) {
						window.updateInstr("whoops, that wasn't in your deck! try again");
					}
					buyCard = toDiscard;
					discardPile.push(buyCard);
				}
				
				window.updateCard(buyCard);
				window.updatePlayer(turn,  players[turn]);
				window.updateInstr("Great turn!");
				
				int j = 0;
				
				if(players[turn].cards.size() == 0) {
					playRound = false;
					j = 1;
					window.updateInstr("good job, player "+turn+"!");
					try {
						TimeUnit.SECONDS.sleep(2);
					}
					catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				}
				
				while(j== 0) {
					
					try {
						TimeUnit.SECONDS.sleep(1);
					}
					catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
					j = window.next.getNum();
				}
				window.next.setInt(0);
				
				turn++;
			}
			window.updatePoints();
			window.updateInstr("Enter ok when you're ready to move on!");
			while(window.txtAnsInp.equals(" ")) {
				try {
					TimeUnit.SECONDS.sleep(1);
				}
				catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			window.txtAnsInp.setText(" ");
			window.updateFrame(round);
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
	
	public static Card createCard(String answer) {
		String cardNum = answer.substring(0,1);
		if(cardNum.equals(" ")) {
			cardNum = answer.substring(1, 2);
			answer = answer.substring(1);
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
		char su;
		try {
			int comma = answer.indexOf(",");
			su = answer.charAt(comma+1);
			if(su == ' ') {
				su = answer.charAt(comma+2);
			}
		}
		catch (StringIndexOutOfBoundsException e) {
			return new Card();
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
}
