package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * Players
 * Object Class for Player Type
 */
public class Players {
	private String Name;
	private String picID;
	private int turn;
	
	public Players(String name, String picid){
		this.setName(name);
		this.setPicID(picid);
		this.setTurn(0);
	}

	public Players(String name, String picid,int t){
		this.setName(name);
		this.setPicID(picid);
		this.setTurn(t);
	}
	public Players() {
		this.setTurn(0);
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPicID() {
		return picID;
	}

	public void setPicID(String picID) {
		this.picID = picID;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public void addTurn(){
		this.turn++;
	}
	
	
	public String toStringAll(){
		return this.Name + " " + this.picID + " " + Integer.toString(this.turn);
	}
}
