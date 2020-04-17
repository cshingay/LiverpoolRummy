/*
 * General Plan: 
 * 		Resource class:
 * 		-Data
 * 			Hand
 * 			Cards
 * 			Turn
 * 		-Methods:
 * 			draw card
 * 			buy card
 * 				-set up as check for interrupt??
 * 			discard
 * 			lay down
 * 			lay on another
 * 			go out
 * 		-superclass: pat hands
 * 		Driver Class:
 * 	
 * 	Creating on the same network to remotely run:
 * 		log me in himachi ?? (idk some minecraft pretend on the same network thing)
 * 
 */

package rummy;
import java.lang.Math;
import java.util.*;

public class RummyPlayer {
	public boolean turn;
	public boolean down;
	public int points = 0;
	public int num = 0;
	public LinkedList<Card> cards;
	
	public RummyPlayer(int plrNum) {
		num = plrNum;
		turn = false;
		down = false;
		points = 0;
		cards = new LinkedList<Card>();
	}
	
	public Card drawCard(LinkedList<Card> deck) {
		int range = deck.size();
		int rand = (int) (Math.random() * range);
		Card removed = deck.get(rand);
		deck.remove(rand);
		cards.add(removed);
		return removed;
	}
	
	public boolean metRoundGoal(int round) {
		LinkedList<Card> copiedCards = new LinkedList<Card>();
		for(int i = 0; i<cards.size(); i++) {
			copiedCards.add(cards.get(i));
		}
		
		return false;
		
	}
	
	public int getRoundPoints() {
		int sum = 0;
		for(int i = 0; i<cards.size(); i++) {
			sum+= cards.get(i).getPoints();
		}
		points+= sum;
		return sum;
	}
	
	public int getPoints() {
		return points;
	}
	
	public LinkedList<Card> checkSet(LinkedList<Card> copy) {
		int maxCount = 0;
		int currCount = 0;
		boolean retrVal = false;
		LinkedList<Card> retrCards = new LinkedList<Card>();
		int num = 0;
		for(int i = 0; i<copy.size();   ) {
			Card dummy = copy.get(i);
			num = dummy.number;
			for(int k = 0; k<copy.size(); k++) {
				if((copy.get(k).number == num) || (copy.get(k).number==14)) {
					currCount++;
					if(currCount == 2) {
						retrVal = true;
						break;
					}
				}
			}
			break;
		}
		int j = 0;
		while(copy.get(j) != null) {
			if(copy.get(j).number == num) {
				retrCards.add(copy.remove(j));
				j--;
			}
			j++;
		}
		return retrCards;
	}
	
	public boolean addCard(Card added) {
		cards.add(added);
		return true;
	}
	
	public boolean discard(Card disc) {
		for(int i = 0; i<cards.size(); i++) {
			if(cards.get(i).equals(disc)) {
				cards.remove(i);
				return true;
			}
			else if((cards.get(i).number == 14) && (disc.number == 14)) {
				cards.remove(i);
				return true;
			}
		}
		System.out.println("wanst in the deck ! D:");
		return false;
	}
	
	public boolean printCards() {
		for(int k=0; k<cards.size(); k++) {
			System.out.println(cards.get(k).getCard());
		}
		return true;
	}
	
	public String getCards() {
		String retr = "";
		for(int k=0; k<cards.size(); k++) {
			retr+=cards.get(k).getCard()+"\n";
		}
		return retr;
	}
}