package com.robalascott.qshake.qshake.Database;

/**Robert Scott
 * QShake
 * Question
 * Object Class for Question Type
 */
public class Question {
	private int id;
	private String question;
	private String type;

	public Question(){}
	public Question(int i, String q1, String type) {
		this.id = i;
		this.question = q1;
		this.type = type;
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

	public Question returnAll(){
		Question q1 = new Question();
		q1.id = this.id;
		q1.question = this.question;
		q1.type = this.type;
		return q1;
	}
	public String toStringAll(){
		return this.id + " " + this.question + " " + this.type;
	}
}