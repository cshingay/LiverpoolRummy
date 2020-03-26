package rummy;
import java.lang.Math;
import java.util.*;
import rummy.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RummyDriver {
	
	public static void main(String[] args) {
		int turn = 0;
		int round = 0;
		int numPlayers;
		boolean pat = false;
		boolean playRound = true;
		int setsRuns = 0;
		
		Card buyCard = new Card();
		Stack<Card> discardPile = new Stack<Card>();
		
		boolean firstTurn = true;
		

		LinkedList<Card> deck = new LinkedList<Card>();
		Scanner reader = new Scanner(System.in);
		
		System.out.println("How many people are playing?");
		numPlayers = reader.nextInt();
		
		/** beginning game loop: runs 7 rounds ***/
		for(round = 0; round<7; round++) {
			playRound = true;
			
			int dealtCards = 6 + round;
			if(round>=5) {
				dealtCards --;
				pat = true;
			}
			setsRuns = determineSetsRuns(round);
			System.out.println("This round you're playing for: "
					+ setsRuns/10 + " sets and " + setsRuns%10 +" runs!");
			
			/*** Creating deck, players & dealing cards according to num playing ***/
			deck = createDeck(deck, numPlayers);
			
			RummyPlayer[] players = new RummyPlayer[numPlayers];
			for(int i = 0; i<numPlayers; i++) {
				players[i] = new RummyPlayer();
			}
			
			for(int i = 0; i<dealtCards; i++) {
				for(int j = 0; j<numPlayers; j++) {
					players[j].drawCard(deck);
				}
			}
			
			/** beginning round loop ***/
			
			while(playRound) {
				if(turn == numPlayers) {
					turn = 0;
				}
				
				/*** picking up card ***/
				
				if(firstTurn) {
					int range = deck.size();
					int rand = (int) (Math.random() * range);
					buyCard = deck.get(rand);
					deck.remove(rand);
					firstTurn = false;
				}
				System.out.println("Player "+turn+"'s cards:");
				players[turn].printCards();
				
				System.out.println("Open card is: " + buyCard.getCard());
				System.out.println("Pick up (0) or mystery? (1)");
				int ans = reader.nextInt();
				if(ans == 0) {
					players[turn].addCard(buyCard);
				}
				else if(ans==1) {
					checkForBuy(deck, players, turn, numPlayers, buyCard, discardPile);
					players[turn].drawCard(deck);
				}
				
				System.out.println("Player "+turn+"'s cards after pickup:");
				players[turn].printCards();
				
				/*** check for going down/out ***/
				
				System.out.println("Going down?");
				String answ = reader.next().toLowerCase();
				if(answ.contains("y")) {
					//future: create method in RummyPlayer class to check if indv. cards are OK
					int sets = setsRuns/10;
					int runs = setsRuns%10;
					
					Card[] downCards = new Card[sets*3 + runs*4];
					int i = 0;
					
					for(int h = 0; h<sets; h++) {
						System.out.println("Enter the cards in your set, one by one"
								+ "\n ie. a,c (enter) a,d (enter) a,s"
								+ "\n for an ace of clubs, diamonds and spades.");
						for(int set = 0; set<3; set++) {
							String name = reader.next();
							Card dummyCard = createCard(name);
							downCards[i] = dummyCard;
							i++;
						}
					}
					for(int h = 0; h<runs; h++) {
						System.out.println("Please enter the cards in your run, one by one"
								+ "\n ie. 2,c (enter) 3,c (enter) 4,c (enter) 5,c (enter)");
						for(int run = 0; run<4; run++) {
							String name = reader.next();
							Card dummyCard = createCard(name);
							downCards[i] = dummyCard;
							i++;
						}
					}
					for(int k=0; k< (sets*3 + runs*4); k++) {
						System.out.println(downCards[k].getCard());
					}
					
					System.out.println("Do any other players object?"
							+ "\n Enter yes if so, and no otherwise");
					
					String checked = reader.next();
					if(checked.contains("y")) {
						System.out.println("uh oh, try again!");
						
					}
					else {
						System.out.println("Good job player "+turn+"!");
						for(int k = 0; k<(sets*3 + runs*4); k++) {
							players[turn].discard(downCards[k]); }
					}
					
					//future note: person saying cards right now, should print out to check too
	
					
				}
				
				/*** discard card ***/
				boolean correct = false;
				while(!correct) {
					System.out.println("Player "+turn+", what card would you like to discard?"
							+ "\n write as format 8,c for 8 of clubs (a = ace; q = queen etc.)");
					players[turn].printCards();

					String discCard = reader.next().toLowerCase();
					Card toDiscard = createCard(discCard);
					
					correct = players[turn].discard(toDiscard);
					buyCard = toDiscard;
				}

				
				if(players[turn].cards.size() == 0) {
					System.out.println("Good job player "+turn+"!"
							+ "\n This round has ended!");
					playRound = false;
				}
				else {
					System.out.println("Player "+turn+"'s cards:");
					players[turn].printCards();	
					turn++;
				}
			}
			
		}
		
		reader.close();
	}
	
	public static LinkedList<Card> createDeck(LinkedList<Card> deck, int players) {
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
	
	public static boolean checkForBuy(LinkedList<Card> deck, RummyPlayer[] players, int accTurn, int numsPlayers, Card buyCard, Stack<Card> discardPile) {
		int turns = accTurn;
		int nums = numsPlayers;
		Scanner read = new Scanner(System.in);
		int buyNext = turns; 
		boolean looped = false;
		boolean done = false;
		int i = 0;
		
		while(!done) {
			buyNext++; i++;
			if(i == nums) {
				done = true;
				break;
			}
			System.out.println("buyNext == "+buyNext);
			if(buyNext == nums) {
				buyNext = 0;
			}
			
			System.out.println("Player "+buyNext+"'s cards:");
			players[buyNext].printCards();
			
			System.out.println("Does player " + buyNext+ " want to buy? (y/n)");
			System.out.println("Open card is "+buyCard.getCard());
			String ans = read.next();
			if(ans.contains("y")) {
				Card addIn = buyCard.copyCard();
				players[buyNext].addCard(addIn);
				players[buyNext].drawCard(deck);
				System.out.println("Player "+buyNext+"'s cards:");
				players[buyNext].printCards();
				return true;
			}
			else {
				continue;
			}
		}
		discardPile.push(buyCard);
		return false;
	}
	
	public static Card createCard(String answer) {
		String cardNum = answer.substring(0,1);
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
				if(answer.contains("o")) {
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
