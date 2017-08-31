package model;

public class Word {

  private int id;
  private String german;
  private String english;
  private int correct;
  private int attempts;

  public int getCorrect() {
    return correct;
  }

  public void setCorrect(int correct) {
    this.correct = correct;
  }

  public int getAttempts() {
    return attempts;
  }

  public void setAttempts(int attempts) {
    this.attempts = attempts;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getGerman() {
    return german;
  }

  public void setGerman(String german) {
    this.german = german;
  }

  public String getEnglish() {
    return english;
  }

  public void setEnglish(String english) {
    this.english = english;
  }

  @Override
  public String toString(){
    return getGerman() + " = " + getEnglish() + " | id =" + getId() + " correct = "
        + getCorrect() +"/" + getAttempts();
  }
}
