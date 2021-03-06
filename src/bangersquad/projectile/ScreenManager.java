/**
 * 
 */
package bangersquad.projectile;

import javafx.scene.layout.StackPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;
import javafx.util.Duration;
import java.util.HashMap;

import bangersquad.projectile.view.ScreenController;

/**
 * @author feilan
 *
 */
public class ScreenManager extends StackPane {

	private HashMap<String, Node> screens = new HashMap<>();
	private HashMap<String, ScreenController> screenControllers = new HashMap<>();
	private int FADE_DURATION = 250;
	
	private String current;
	
	/**
	 * 
	 * @param id
	 * @param screen
	 */
	public void addScreen(String id, Node screen) {
		screens.put(id, screen);
	}
	
	/**
	 * 
	 * @param id
	 * @param resource
	 * @return
	 */
	public boolean loadScreen(String id, String resource) {
		boolean screenLoaded = false;
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(resource));
			
			Parent loadedScreen = (Parent) loader.load();
			ScreenController loadedScreenController = (ScreenController) loader.getController(); 
			loadedScreenController.setScreenManager(this);
			
			addScreen(id, loadedScreen);
			addController(id, loadedScreenController);
			
			screenLoaded = true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return screenLoaded;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public boolean setScreen(String id) {
		boolean screenSet = false;
		
		if (screens.get(id) != null) {
			DoubleProperty opacity = opacityProperty();
			
			if (getChildren().isEmpty()) {
				setOpacity(0.0);
				getChildren().add(screens.get(id));
				
				Timeline fadeIn = new Timeline(
					new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
					new KeyFrame(new Duration(FADE_DURATION), new KeyValue(opacity, 1.0))
				);
				fadeIn.play();				
			} else {
				Timeline fade = new Timeline(
					new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
					new KeyFrame(new Duration(FADE_DURATION), (ActionEvent e) -> {
						getChildren().remove(0);
						getChildren().add(0, screens.get(id));
						
						Timeline fadeIn = new Timeline(
							new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
							new KeyFrame(new Duration(FADE_DURATION), new KeyValue(opacity, 1.0))
						);
						fadeIn.play();
					}, new KeyValue(opacity, 0.0))
				);
				fade.play();
			}
			
			screenSet = true;
		}
		
		if (screenSet) {
			screenControllers.get(id).onScreenSet();
		}
		
		return screenSet;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public boolean unloadScreen(String id) {
		// remove() returns null if screen with requested id was not found in screens
		return screens.remove(id) != null;
	}
	
	private void addController(String id, ScreenController controller) {
		screenControllers.put(id, controller);
	}
	
}
