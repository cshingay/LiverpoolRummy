package rummy;

public class Stris {
	public String txt;
	
	public Stris(String text) {
		txt = text;
	}
	
	public Stris() {
		txt = " ";
	}
	
	public String getText() {
		return this.txt;
	}
	
	public void setText(String newTxt) {
		txt = newTxt;
	}
	
	public boolean equals(String check) {
		return txt.equals(check);
	}
}
