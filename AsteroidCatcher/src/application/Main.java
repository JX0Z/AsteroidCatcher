package application;
	
import java.awt.Canvas;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	@Override
	public void start(Stage game) {
		try {
			
			StackPane root = new StackPane();
			game.setScene(new Scene(root,500,700));
			game.show();
			
			Circle[] cirArray = new Circle[5];
			for(int i=0;i<5;i++) {
				cirArray[i]= new Circle(0,0,10);
				root.getChildren().add(cirArray[i]);

			}
			
			Rectangle platform = new Rectangle(50, 10);
			root.getChildren().add(platform);
			platform.setTranslateX(100);
			platform.setTranslateX(0);

			platform.addEventHandler(KeyEvent.ANY, event -> {
				//System.out.println(event.getCode()==KeyCode.LEFTARROW);
			});
	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
