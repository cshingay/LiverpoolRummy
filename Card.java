package rummy;

public class Card {
	public int number;
	public char suit;
	
	public Card(int num, char su) { 
		number = num;
		suit = su;
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
		String suitName = "you fucked up";
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
			if((suit == 's') || (suit == 'c')) {
				suitName = "black"; }
			else {
				suitName = "red"; }
		}
		
		if(suit == 's') {
			suitName = "spades"; }
		else if(suit == 'd') {
			suitName = "diamonds";}
		else if(suit == 'c') {
			suitName = "clubs";}
		else if(suit == 'h') {
			suitName = "hearts";}
		
		//System.out.println(car+", "+suit);
		return car+", "+suitName;
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
