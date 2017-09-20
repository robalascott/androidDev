package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * Question
 * Object Class for Question Type
 */
public class Question {
	private int id;
	private String question;
	private String type;
	private int star;
	private int user;
	
	public Question(int i, String string, String string2, String string3,int star4,int user1) {
		this.id = i;
		this.question = string;
		this.type = string3;
		this.star = star4;
		this.user = user1;
	}
	public Question(String string, String string2, String string3,int star4,int user1) {
		this.question = string;
		this.type = string3;
		this.star = star4;
		this.user = user1;
	}
	public Question(){
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int use) {
		this.user = use;
	}

	public Question returnAll(){
		Question q1 = new Question();
		q1.id = this.id;
		q1.question = this.question;
		q1.type = this.type;
		q1.star = this.star;
		q1.user = this.user;
		return q1;
	}
	public String toStringAll(){
		return this.id + " " + this.question + " " + this.type;
	}
}