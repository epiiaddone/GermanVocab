package model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Datasource {

  public static final String DB_NAME = "vocabulary.db";
  public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Grichards\\Documents\\"
      + "docsmod\\emplyoment\\git hub demos\\GermanVocab\\" + DB_NAME;


  public static final int INDEX_ID = 1;
  public static final int INDEX_GERMAN = 2;
  public static final int INDEX_ENGLISH = 3;
  public static final int INDEX_CORRECT = 4;
  public static final int INDEX_ATTEMPTS = 5;

  public static final String QUERY_VOCABULARY_GERMAN = "SELECT * FROM vocabulary ORDER BY german "
      + "COLLATE NOCASE";

  public static final String QUERY_GERMAN_FOR_WORD_START = "SELECT * FROM vocabulary WHERE german LIKE "
      + "\'%";

  public static final String QUERY_WORD_BY_ID= "SELECT * FROM vocabulary WHERE id =";

  public static final String QUERY_VOCABULARY_SIZE = "SELECT id FROM vocabulary";

  public static final String INSERT_NEW_WORD = "INSERT INTO vocabulary ( german, english, correct, attempts) "
      + "VALUES ( \'";

  public static final String REMOVE_BY_ID = "DELETE FROM vocabulary WHERE id=";

  public static final String RESET_STATISTICS = "UPDATE vocabulary set correct=0, attempts=0";

  public static final String UPDATE_FALSE = "UPDATE vocabulary SET attempts =";

  public static final String UPDATE_CORRECT = "UPDATE vocabulary SET attempts =";

  public static final String QUERY_LEAST_CORRECT = "Select * FROM vocabulary WHERE attempts >= 1";




  // to make the Datasource a singleton
  private static Datasource instance = new Datasource();

  private Datasource() {
  }

  public static Datasource getInstance() {
    return instance;
  }

  private Connection conn;

  public boolean open(){
    try{
      conn = DriverManager.getConnection(CONNECTION_STRING);
      return true;
          }catch(SQLException e){
      System.out.println("Couldn't connect to database: " + e.getMessage());
      return false;
    }
  }

  public void close(){
    try{
      if(conn!=null){
        conn.close();
      }
    }catch(SQLException e){
      System.out.println("Couldn't close connnection: " + e.getMessage());
    }
  }

  public List<Word> queryGerman(){
    try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(QUERY_VOCABULARY_GERMAN)) {

      List<Word> words = new ArrayList<>();
      while(results.next()){
        Word word = new Word();
        word.setId(results.getInt(INDEX_ID));
        word.setEnglish(results.getString(INDEX_ENGLISH));
        word.setGerman(results.getString(INDEX_GERMAN));
        word.setCorrect(results.getInt(INDEX_CORRECT));
        word.setAttempts(results.getInt(INDEX_ATTEMPTS));
        words.add(word);
      }
      return words;
    }  catch(SQLException e) {
    System.out.println("Query failed: " + e.getMessage());
    return null;
  }
  }

  public List<Word> queryGermanForWord(String germanWord) {

    StringBuilder sb = new StringBuilder(QUERY_GERMAN_FOR_WORD_START);
    sb.append(germanWord);
    sb.append("%\'");

    System.out.println("SQL statement = " + sb.toString());

    try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sb.toString())) {

      List<Word> words = new ArrayList<>();
      while(results.next()){
        Word word = new Word();
        word.setId(results.getInt(INDEX_ID));
        word.setEnglish(results.getString(INDEX_ENGLISH));
        word.setGerman(results.getString(INDEX_GERMAN));
        word.setCorrect(results.getInt(INDEX_CORRECT));
        word.setAttempts(results.getInt(INDEX_ATTEMPTS));
        words.add(word);
      }
      return words;
    }  catch(SQLException e) {
      System.out.println("Query failed: " + e.getMessage());
      return null;
    }
  }

  public int queryVocabularySize(){
    try(Statement statement = conn.createStatement();
    ResultSet results  =statement.executeQuery(QUERY_VOCABULARY_SIZE)){
      int size = 0;
      while(results.next()){
        size++;
      }
      return size;
    }  catch(SQLException e) {
      System.out.println("Query failed: " + e.getMessage());
      return 0;
    }
  }

  public Word queryGermanRandom() {

    List<Word> words = queryGerman();

    Random rn = new Random();
    int result = rn.nextInt(words.size());

    Word word = words.get(result);

    return word;


  }

  public void insertNewWord(String german, String english){
    if(german==null||english==null){
      return;
    }
    if(german=="" || english==""){
      return;
    }

    StringBuilder sb = new StringBuilder(INSERT_NEW_WORD);
    sb.append(german);
    sb.append("\' , \'");
    sb.append(english);
    sb.append("\' ,");
    sb.append(0);
    sb.append(",");
    sb.append(0);
    sb.append(" )");

    try(Statement statement = conn.createStatement()){
      statement.execute(sb.toString());


    }catch(SQLException e){
      System.out.println("Query failed: " + e.getMessage());
    }
  }

  public void removeByID(String idString){
    int id = Integer.parseInt(idString);

    if(id<=0){
      return;
    }

    StringBuilder sb = new StringBuilder(REMOVE_BY_ID);
    sb.append(id);

    try(Statement statement = conn.createStatement()){
      statement.execute(sb.toString());

    }catch(SQLException e){
      System.out.println("Query failed: " + e.getMessage());
    }
  }

  public void resetStatistics(){
    try(Statement statement = conn.createStatement()){
      statement.execute(RESET_STATISTICS);
    }catch(SQLException e){
      System.out.println("Reset failed: " +e.getMessage());
    }
  }
  public void updateFalse(Word word){

    StringBuilder sb = new StringBuilder(UPDATE_FALSE);
    sb.append(word.getAttempts() + 1);
    sb.append(" WHERE id=");
    sb.append(word.getId());

    try(Statement statement = conn.createStatement()){
      statement.execute(sb.toString());
    }catch (SQLException e){
      System.out.println("Update failed: " + e.getMessage());
    }
  }

  public void updateCorrect(Word word){
    StringBuilder sb = new StringBuilder(UPDATE_CORRECT);
    sb.append(word.getAttempts()+ 1);
    sb.append(", correct=");
    sb.append(word.getCorrect()+1);
    sb.append(" WHERE id=");
    sb.append(word.getId());

    try(Statement statement = conn.createStatement()){
      statement.execute(sb.toString());
    }catch (SQLException e){
      System.out.println("Update failed: " + e.getMessage());
    }
  }

  public Word queryLeastCorrectWord(){
    int id = 1;

    try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(QUERY_LEAST_CORRECT)) {

      double ratio = 1.1;

      // get id of least correct word
      while(results.next()){
       double tempRatio = (double) results.getInt(INDEX_CORRECT)/ (double) results.getInt(INDEX_ATTEMPTS);
        if(tempRatio <= ratio){
          ratio=tempRatio;
          id = results.getInt(INDEX_ID);
        }
      }
    }  catch(SQLException e) {
      System.out.println("Error getting id " + e.getMessage());
      return null;
    }

    // get the word using its id
    StringBuilder sb = new StringBuilder(QUERY_WORD_BY_ID);
    sb.append(id);

    try(Statement statement = conn.createStatement()){
      ResultSet results = statement.executeQuery(sb.toString());

      Word word = new Word();
      word.setId(results.getInt(INDEX_ID));
      word.setGerman(results.getString(INDEX_GERMAN));
      word.setEnglish(results.getString(INDEX_ENGLISH));
      word.setCorrect(results.getInt(INDEX_CORRECT));
      word.setAttempts(results.getInt(INDEX_ATTEMPTS));
      return word;
    }catch (SQLException e){
      System.out.println("Query failed: " + e.getMessage());
      return null;
    }
  }


  }


