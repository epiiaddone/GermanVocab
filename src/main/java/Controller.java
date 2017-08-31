import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javax.xml.soap.Text;
import model.Word;
import model.Datasource;
import javafx.scene.control.TextField;

public class Controller {
  @FXML
  private TextField searchGerman;

  @FXML
  private TextField enterGerman;

  @FXML
  private TextField enterEnglish;

  @FXML
  private TextField remove;

  @FXML
  private Button submitButton;

  @FXML
  private Button removeButton;

  @FXML
  private Button searchButton;

  @FXML
  private Button randomButton;

  @FXML
  private Button answerButton;

  @FXML
  private Button correctButton;

  @FXML
  private Button falseButton;

  @FXML
  private Button resetButton;

  @FXML
  private Button leastButton;

  @FXML
  private Button listButton;

  @FXML
  private TextArea area;

  private Word currentWord;


  @FXML
  public void initialize() {
    submitButton.setDisable(true);
    removeButton.setDisable(true);
    searchButton.setDisable(true);
    answerButton.setDisable(true);
    falseButton.setDisable(true);
    correctButton.setDisable(true);
    leastButton.setDisable(true);
    area.setEditable(false);
  }

  @FXML
  public void onListWordsClicked(){
    // singleton class
    Datasource datasource = Datasource.getInstance();

    if(!datasource.open()) {
      System.out.println("Can't open datasource");
      return;
    }

    List<Word> words = datasource.queryGerman();
    if(words == null) {
      System.out.println("No artists!");
      return;
    }

    StringBuilder sb = new StringBuilder();
    for(Word word: words){
      sb.append(word.toString());
      sb.append("\n");
    }

    area.setText(sb.toString());

    datasource.close();
  }

  @FXML
  public void onSearchClicked(){
    Datasource datasource = Datasource.getInstance();

    if(!datasource.open()){
      System.out.println("Can't open datasource");
      return;
    }

    List<Word> words = datasource.queryGermanForWord(searchGerman.getText());
    datasource.close();

    StringBuilder sb = new StringBuilder();
    for(Word word: words){
      sb.append(word.toString());
      sb.append("\n");
    }

    area.setText(sb.toString());
    searchGerman.setText("");
  }

  @FXML
  public void onRandomClicked(){
    Datasource datasource = Datasource.getInstance();

    if(!datasource.open()){
      System.out.println("Can't open datasource");
      return;
    }

    currentWord =  datasource.queryGermanRandom();

    area.setText(currentWord.getGerman());

    randomButton.setDisable(true);
    answerButton.setDisable(false);
    leastButton.setDisable(true);
    searchButton.setDisable(true);
    listButton.setDisable(true);
    submitButton.setDisable(true);
    removeButton.setDisable(true);
    resetButton.setDisable(true);

  }

  @FXML
  public void onAnswerClicked(){
    area.setText(currentWord.getEnglish());
    answerButton.setDisable(true);
    correctButton.setDisable(false);
    falseButton.setDisable(false);


  }

  @FXML
  public void onCorrectClicked(){
    randomButton.setDisable(false);
    answerButton.setDisable(true);
    correctButton.setDisable(true);
    falseButton.setDisable(true);
    leastButton.setDisable(false);

    Datasource datasource = Datasource.getInstance();

    if(!datasource.open()){
      System.out.println("Can't open datasource");
      return;
    }

    datasource.updateCorrect(currentWord);
    datasource.close();
    area.setText("");

    searchButton.setDisable(false);
    listButton.setDisable(false);
    submitButton.setDisable(false);
    removeButton.setDisable(false);
    resetButton.setDisable(false);

      }

  @FXML
  public void onFalseClicked(){
    randomButton.setDisable(false);
    answerButton.setDisable(true);
    correctButton.setDisable(true);
    falseButton.setDisable(true);
    leastButton.setDisable(false);

    Datasource datasource = Datasource.getInstance();

    if(!datasource.open()){
      System.out.println("Can't open datasource");
      return;
    }

    datasource.updateFalse(currentWord);
    datasource.close();
    area.setText("");

    searchButton.setDisable(false);
    listButton.setDisable(false);
    submitButton.setDisable(false);
    removeButton.setDisable(false);
    resetButton.setDisable(false);

  }


  @FXML
  public void onSubmitClicked(){
    Datasource datasource = Datasource.getInstance();

    if(!datasource.open()){
      System.out.println("Can't open datasource");
      return;
    }

    datasource.insertNewWord(enterGerman.getText(), enterEnglish.getText());
    datasource.close();

    enterEnglish.setText("");
    enterGerman.setText("");
    area.setText("New word added");

  }

  @FXML
  public void onRemoveClicked(){
    Datasource datasource = Datasource.getInstance();

    if(!datasource.open()){
      System.out.println("Can't open datasource");
      return;
    }

    datasource.removeByID(remove.getText());
    datasource.close();

    remove.setText("");
    area.setText("");
  }

  @FXML
  public void handleSubmitKeyReleased(){
    String text1 = enterGerman.getText();
    String text2 = enterEnglish.getText();
     boolean disableButtons = text1.isEmpty() || text2.isEmpty() ||
         text1.trim().isEmpty() || text2.trim().isEmpty() || randomButton.isDisabled();
     submitButton.setDisable(disableButtons);
  }

  @FXML
  public void handleRemoveKeyReleased() {
    String text = remove.getText();
    boolean disableButtons = text.isEmpty() || text.trim().isEmpty() || randomButton.isDisabled();
    removeButton.setDisable(disableButtons);
  }

  @FXML
  public void handleSearchKeyReleased(){
    String text = searchGerman.getText();
    boolean disableButtons = text.isEmpty() || text.trim().isEmpty() || randomButton.isDisabled();
    searchButton.setDisable(disableButtons);
  }

  @FXML
  public void onResetClicked(){
    leastButton.setDisable(true);
    Datasource datasource = Datasource.getInstance();

    if(!datasource.open()){
      System.out.println("Can't open datasource");
      return;
    }
    datasource.resetStatistics();
    datasource.close();

    area.setText("Statistics reset");
  }

  @FXML
  public void onLeastClicked(){
    randomButton.setDisable(true);
    leastButton.setDisable(true);
    searchButton.setDisable(true);
    listButton.setDisable(true);
    submitButton.setDisable(true);
    removeButton.setDisable(true);
    resetButton.setDisable(true);

    Datasource datasource = Datasource.getInstance();

    if(!datasource.open()){
      System.out.println("Can't open datasource");
      return;
    }

    currentWord = datasource.queryLeastCorrectWord();
    datasource.close();

    area.setText(currentWord.getGerman());

    answerButton.setDisable(false);
  }


}
