package rummy;

public class Card {
	public int number;
	public char suit;
	
	public Card(int num, char su) { 
		number = num; //ace == 1, j == 11, q == 12, k == 13, joker == 14
		if(su == '♦') suit = 'd';
		else if(su == '♠') suit = 's';
		else if(su == '♣') suit = 'c';
		else if(su == '♥') suit = 'h';
		else suit = su;
	}
	
	public Card() {
		number = 0;
		suit = ' ';
	}
	
	public Card copyCard() {
		Card retr = new Card(number, suit);
		return retr;
	}
	
	public String getCard() {
		Integer obj = new Integer(number);
		String car = obj.toString();
		String suitName = "uh oh";
		if(number == 1) {
			car = "ace"; }
		else if(number == 11) {
			car = "jack";
		}
		else if(number == 12) {
			car = "queen";
		}
		else if(number == 13) {
			car = "king";
		}
		else if(number == 14) {
			car = "joker";
			if(suit == 'b') {
				suitName = "black"; }
			else {
				suitName = "red"; }
		}
		
		if(suit == 's') {
			suitName = "♠"; }
		else if(suit == 'd') {
			suitName = "♦";}
		else if(suit == 'c') {
			suitName = "♣";}
		else if(suit == 'h') {
			suitName = "♥";}
		
		if(number == 0) {
			return "🂠";
		}
		//System.out.println(car+", "+suit);
		return car+", "+suitName;
	}
	
	public int getPoints() {
		int retr = 0;
		if(number < 9 && number != 1) {
			retr = 5;
		}
		else if(number == 1) {
			retr = 15;
		}
		else if(number == 14) {
			retr = 20;
		}
		else {
			retr = 10;
		}
		return retr;
	}
	
	
	public String abbrvCard() {
		Integer obj = new Integer(number);
		String car = obj.toString();
		String suitName = "you fucked up";
		if(number == 1) {
			car = "A"; }
		else if(number == 11) {
			car = "J";
		}
		else if(number == 12) {
			car = "Q";
		}
		else if(number == 13) {
			car = "K";
		}
		else if(number == 14) {
			car = "Jo";
			if(suit == 'r') {
				suitName = "r";
			}
			else suitName = "b";
		}
		
		if(suit == 's') {
			suitName = "♠"; }
		else if(suit == 'd') {
			suitName = "♦";}
		else if(suit == 'c') {
			suitName = "♣";}
		else if(suit == 'h') {
			suitName = "♥";}
		
		if(number == 0) {
			return "🂠";
		}
		
		//System.out.println(car+", "+suit);
		return car+","+suitName;
	}
	
	public boolean clearCard() {
		number = 0;
		suit = ' ';
		return false;
	}
	
	public boolean equals(Card test) {
		if( (number == test.number) && (suit == test.suit) ) {
			return true;
		}
		else {
			return false;
		}
	}
}
