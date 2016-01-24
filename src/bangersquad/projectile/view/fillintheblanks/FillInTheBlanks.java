/**
 * 
 */
package bangersquad.projectile.view.fillintheblanks;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * @author feilan
 *
 */
public class FillInTheBlanks extends AnchorPane {

	private static final String FXML_LOCATION = "FillInTheBlanks.fxml";
	
	@FXML
	private TextFlow textFlow;
	
	private String[] prompts = new String[0];

	/**
	 * Initializes a new <code>FillInTheBlanks</code>. This object consists of a text string with <code>TextField</code>s
	 * inserted in place of the blanks.
	 */
	public FillInTheBlanks() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_LOCATION));
		
		loader.setRoot(this);
		loader.setController(this);
		
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
        
	/**
	 * Specifies a text prompt to be displayed in this object's <code>TextField</code>s. 
	 * The prompts are applied in the order in which they are provided.
	 * <p>
	 * <pre>
	 * {@code
	 * fillInTheBlanks.update("January _, _", _);	// I now have two TextFields, one for each blank
	 * fillInTheBlanks.setPrompts("Day", "Month");	// the first blank's prompt is "Day", the second blank's prompt is "Month" 
	 * }
	 * </pre>
	 * @param prompts	a series or array of prompts to use
	 */
	public void setPrompts(String... prompts) {
		this.prompts = prompts;
		applyPrompts();
	}
	
	/**
	 * Updates the text of this <code>FillInTheBlanks</code>. A special symbol, specified by <code>blankRegex</code>, can be used
	 * to indicate the locations of blanks.
	 * <p>
	 * For this version of <code>update()</code>, the text must be an array of <code>String</code>s. Blank-symbol detection is done on a per-piece basis.
	 * For instance, using a <code>blankRegex</code> of <code>"_"</code> on the piece <code>"_*x + "</code> would not yield any blanks.
	 * @param textPieces	the array of <code>String</code>s to which this <code>FillInTheBlanks</code>'s text will be set
	 * @param blankRegex	the regular expression (symbol) to use for finding blanks
	 */
	public void update(String[] textPieces, String blankRegex) {
		TextField blankText;
		Text nonBlankText;
		ObservableList<Node> children = textFlow.getChildren();
		
		children.clear();

		for (String piece : textPieces) {
			if (piece.matches(blankRegex)) {	// this is a blank
				blankText = new TextField();
				blankText.setPrefWidth(50);	// TODO: calculate TextField width based on font size, max input len
				// input validation: ints only
				// restrict length of input
				children.add(blankText);				
			} else {
				nonBlankText = new Text(piece);
				children.add(nonBlankText);
			}
		}		
		
		applyPrompts();
	}

	/**
	 * Updates the text of this <code>FillInTheBlanks</code>. A special symbol, specified by <code>blankRegex</code>, can be used
	 * to indicate the locations of blanks.
	 * <p>
	 * For this version of <code>update()</code>, the text must be a <code>List</code> of <code>String</code>s. Blank-symbol detection is done on a per-piece basis.
	 * For instance, using a <code>blankRegex</code> of <code>"_"</code> on the piece <code>"_*x + "</code> would not yield any blanks.
	 * @param textPieces	the <code>List</code> of <code>String</code>s to which this <code>FillInTheBlanks</code>'s text will be set
	 * @param blankRegex	the regular expression (symbol) to use for finding blanks
	 */
	public void update(List<String> textPieces, String blankRegex) {
		TextField blankText;
		Text nonBlankText;
		ObservableList<Node> children = textFlow.getChildren();
		
		children.clear();

		for (String piece : textPieces) {
			if (piece.matches(blankRegex)) {	// this is a blank
				blankText = new TextField();
				blankText.setPrefWidth(50);	// TODO: calculate TextField width based on font size, max input len
				// input validation: ints only
				// restrict length of input
				children.add(blankText);				
			} else {
				nonBlankText = new Text(piece);
				children.add(nonBlankText);
			}
		}
		
		applyPrompts();
	}

	/**
	 * Updates the text of this <code>FillInTheBlanks</code>. A special symbol, specified by <code>blankRegex</code>, can be used
	 * to indicate the locations of blanks.
	 * @param text			the <code>String</code> to which this <code>FillInTheBlanks</code>'s text will be set
	 * @param blankRegex	the regular expression (symbol) to use for finding blanks
	 */
	public void update(String text, String blankRegex) {		
		TextField blankText;
		Text nonBlankText;
		ArrayList<String> textPieces = new ArrayList<>(Arrays.asList(text.split(blankRegex, -1))); // we use this version of split so there can be blank elems at the end
		ObservableList<Node> children = textFlow.getChildren();
		
		children.clear();

		for (int i = 0; i < textPieces.size() - 1; i++) {	
			if (textPieces.get(i).matches("")) {
				continue;
			}

			if (!textPieces.get(i + 1).matches("")) {
				textPieces.add(++i, "");
			}
		}

		for (String piece : textPieces) {
			if (piece.matches("")) {	// this is a blank
				blankText = new TextField();
				blankText.setPrefWidth(50);	// TODO: calculate TextField width based on font size, max input len
				// input validation: ints only
				// restrict length of input
				children.add(blankText);				
			} else {
				nonBlankText = new Text(piece);
				children.add(nonBlankText);
			}
		}
		
		applyPrompts();
	}

	/**
	 * Returns a <code>String</code> containing the text of this <code>FillInTheBlanks</code>. 
	 * Anything inside the blanks' <code>TextField</code>s is also included in this <code>String</code>. 
	 * 
	 * @return	the text of this <code>FillInTheBlanks</code>
	 */
	public String getText() {
		StringBuilder sb = new StringBuilder();

		for (Node child : textFlow.getChildren()) {
			if (child instanceof TextField) {
				sb.append(((TextField) child).getText());
			} else if (child instanceof Text) {
				sb.append(((Text) child).getText());
			}
		}

		return sb.toString();
	}

	private void applyPrompts() {
		int i = 0;
		int l = prompts.length;
		
		for (Node child : textFlow.getChildren()) {
			if (i >= l) {
				break;
			}
			
			if (child instanceof TextField) {
				((TextField) child).setPromptText(prompts[i++]); 
			}
		}
	}

}
