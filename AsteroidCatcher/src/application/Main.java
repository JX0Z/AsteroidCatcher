package application;
	
import java.awt.Canvas;

import javafx.animation.AnimationTimer;
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
	public double speed = 1.5;
	public boolean platLeft = false; 
	public boolean platRight = false;
	@Override
	public void start(Stage game) {
		try {
			
			StackPane root = new StackPane();
			game.setScene(new Scene(root,500,700));
			game.show();
			
			Circle[] cirArray = new Circle[5];
			for(int i=0;i<5;i++) {
				cirArray[i]= new Circle(0,0,(int)(Math.random()*8+8));
				cirArray[i].setTranslateX((int)(Math.random()*460+20)-250);
				cirArray[i].setTranslateY(-350-i*150);
				root.getChildren().add(cirArray[i]);

			}
			
			Rectangle platform = new Rectangle(100, 20);
			platform.setTranslateX(0);
			platform.setTranslateY(340);
			root.getChildren().add(platform);

			game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
				if(event.getCode()==KeyCode.LEFT) {
					platLeft = true;
				}
				if(event.getCode()==KeyCode.RIGHT) {
					platRight = true;
				}
			});
			game.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
				if(event.getCode()==KeyCode.LEFT) {
					platLeft = false;
				}
				if(event.getCode()==KeyCode.RIGHT) {
					platRight = false;
				}
			});
			
	        new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	            	speed*=1.00005;
	            	
	            	if (platLeft && !(platform.getTranslateX()<=-200))platform.setTranslateX(platform.getTranslateX()-3);
	            	if (platRight && !(platform.getTranslateX()>=200))platform.setTranslateX(platform.getTranslateX()+3);
	            	
	            	
	            	
	            	for(int i=0;i<5;i++) {
	            		cirArray[i].setTranslateY(cirArray[i].getTranslateY()+speed);
	            		
	            		if(cirArray[i].getTranslateX()<=platform.getTranslateX()+50 &&
	            		   cirArray[i].getTranslateX()>=platform.getTranslateX()-50 && 
	            		   cirArray[i].getTranslateY()<=platform.getTranslateY()+10 &&
		            	   cirArray[i].getTranslateY()>=platform.getTranslateY()-10)
	            			{
	            				cirArray[i].setTranslateX((int)(Math.random()*460+20)-250);
	            				cirArray[i].setTranslateY(cirArray[i].getTranslateY()-700);
	            				System.out.println("Caught an Asteroid");
	            			}
	            		
	            		if (cirArray[i].getTranslateY()>360) {
	            			System.out.println("You lose");
	            			stop();
	            		}
	            		
	            	}
	            }
	        }.start();
	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
