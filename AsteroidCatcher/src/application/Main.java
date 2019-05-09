package application;
	
import java.awt.Canvas;
import java.awt.TextArea;

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
import javafx.scene.text.Text;

public class Main extends Application {
	public double speed = 1.5;
	public boolean platLeft = false; 
	public boolean platRight = false;
	public boolean gameOver = false;
	public int powerSlowTime = 0;
	public int powerLargeTime = 0;
	public int score = 0;
	@Override
	public void start(Stage game) {
		try {
			
			StackPane root = new StackPane();
			game.setScene(new Scene(root,500,700));
			game.setTitle("Asteroid Catcher");
			game.show();
			//Init 5 Circles
			Circle[] cirArray = new Circle[5];
			for(int i=0;i<5;i++) {
				cirArray[i]= new Circle(0,0,(int)(Math.random()*8+8));
				cirArray[i].setTranslateX((int)(Math.random()*460+20)-250);
				cirArray[i].setTranslateY(-350-i*150);
				root.getChildren().add(cirArray[i]);
			}
			//Init Power up
			Circle powerSlow = new Circle(0,0,10,Color.GREEN);
			powerSlow.setTranslateY((int)((Math.random()*-1000)-1000));
			powerSlow.setTranslateX((int)(Math.random()*460+20)-250);
			root.getChildren().add(powerSlow);
			
			Circle powerLarge = new Circle(0,0,10,Color.YELLOW);
			powerLarge.setTranslateY((int)((Math.random()*-1000)-1000));
			powerLarge.setTranslateX((int)(Math.random()*460+20)-250);
			root.getChildren().add(powerLarge);
			//Init scorebox
			Text scoreBox = new Text();
			scoreBox.setText("0");
			scoreBox.setTranslateX(-230);
			scoreBox.setTranslateY(-340);
			root.getChildren().add(scoreBox);
			//Init Player Platform
			Rectangle platform = new Rectangle(100, 20, Color.BLUE);
			platform.setTranslateX(0);
			platform.setTranslateY(340);
			root.getChildren().add(platform);
			
			//Set booleans based on key presses
			game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
				if(event.getCode()==KeyCode.LEFT) {
					platLeft = true;
				}
				if(event.getCode()==KeyCode.RIGHT) {
					platRight = true;
				}
			});
			//Set booleans to false based on key releases
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
	            	//Speeds up game by a tiny amount every frame
	            	speed*=1.0005;
	            	//Moves platform according to boolean as long as the platform isn't at the max
	            	if (platLeft && !(platform.getTranslateX()<=-root.getWidth()/2+platform.getWidth()/2))platform.setTranslateX(platform.getTranslateX()-10);
	            	if (platRight && !(platform.getTranslateX()>=root.getWidth()/2-platform.getWidth()/2))platform.setTranslateX(platform.getTranslateX()+10);
	            	
	            	//For loop for the asteroids
	            	for(int i=0;i<5;i++) {
	            		//Moves asteroid slowly if powerup is active
	            		if (powerLargeTime>0) {
	            			platform.setWidth(200);
	            		}
	            		else platform.setWidth(100);
	            		if (powerSlowTime>0) {
	            			cirArray[i].setTranslateY(cirArray[i].getTranslateY()+1);
	            			powerSlowTime--;
	            		}
	            		//Moves asteroid regularly if not
	            		else cirArray[i].setTranslateY(cirArray[i].getTranslateY()+speed);
	            		//Checks if asteroid is caught by the platform
	            		if(cirArray[i].getTranslateX()<=platform.getTranslateX()+platform.getWidth()/2 &&
	            		   cirArray[i].getTranslateX()>=platform.getTranslateX()-platform.getWidth()/2 && 
	            		   cirArray[i].getTranslateY()<=platform.getTranslateY()+platform.getHeight()/2 &&
		            	   cirArray[i].getTranslateY()>=platform.getTranslateY()-platform.getHeight()/2)
	            			{
	            				//Resets coordinates to a random X and a high Y
	            				cirArray[i].setTranslateX((int)(Math.random()*460+20)-250);
	            				cirArray[i].setTranslateY(cirArray[i].getTranslateY()-700);
	            				//Updates score
	            				score++;
	            				scoreBox.setText(String.valueOf(score));
	            			}
	            		//Checks if a asteroid hits the bottom
	            		if (cirArray[i].getTranslateY()>360) {
	            			//Freezes all asteroids
	            			speed=0;
	            			//Expands the asteroid to fill the screen
	            			if (cirArray[i].getRadius()<Math.sqrt(Math.pow(root.getWidth(),2)+Math.pow(root.getHeight(),2))) {
	            				cirArray[i].setRadius(cirArray[i].getRadius()+10);
	            			}
	            			else {
	            				//Stops the game
	            				System.out.println("You lose");
	            				stop();
	            			}
	            		}
	            	}
	         		//Moves powerup slowly if powerup is active
            		if (powerSlowTime>0) {
            			powerSlow.setTranslateY(powerSlow.getTranslateY()+1.5);
            			powerSlowTime--;
            		}
            		//Moves powerup regularly if not
            		else powerSlow.setTranslateY(powerSlow.getTranslateY()+speed);
            		
	         		//Moves powerup slowly if powerup is active
            		if (powerSlowTime>0) {
            			powerLarge.setTranslateY(powerLarge.getTranslateY()+1.5);
            			powerLargeTime--;
            		}
            		//Moves powerup regularly if not
            		else powerLarge.setTranslateY(powerLarge.getTranslateY()+speed);
            		
	            	//Checks if powerup is caught by the platform
	            	if(powerSlow.getTranslateX()<=platform.getTranslateX()+platform.getWidth()/2 &&
	            		powerSlow.getTranslateX()>=platform.getTranslateX()-platform.getWidth()/2 && 
	            	   powerSlow.getTranslateY()<=platform.getTranslateY()+platform.getHeight()/2 &&
	            	   powerSlow.getTranslateY()>=platform.getTranslateY()-platform.getHeight()/2) {
	            		//Sets the powerup time 1200 frames or 20 seconds
	            		powerSlowTime=2500;
	            		//Moves powerup to a random X and a random high Y
	        			powerSlow.setTranslateY(370);
	            	}
	            	//If power up is missed move to a random X and a random high Y
	            	if (powerSlow.getTranslateY()>1000) {
	        			powerSlow.setTranslateY((int)((Math.random()*-1000)-1000));
	        			powerSlow.setTranslateX((int)(Math.random()*460+20)-250);
	            	}
	            	//Checks if powerup is caught by the platform
	            	if(powerLarge.getTranslateX()<=platform.getTranslateX()+platform.getWidth()/2 &&
	            		powerLarge.getTranslateX()>=platform.getTranslateX()-platform.getWidth()/2 && 
	            		powerLarge.getTranslateY()<=platform.getTranslateY()+platform.getHeight()/2 &&
	            		powerLarge.getTranslateY()>=platform.getTranslateY()-platform.getHeight()/2) {
	            		//Sets the powerup time 1200 frames or 20 seconds
	            		powerLargeTime=250;
	            		//Moves powerup to a random X and a random high Y
	            		powerLarge.setTranslateY(370);
	            	}
	            	//If power up is missed move to a random X and a random high Y
	            	if (powerLarge.getTranslateY()>1000) {
	            		powerLarge.setTranslateY((int)((Math.random()*-1000)-1000));
	            		powerLarge.setTranslateX((int)(Math.random()*460+20)-250);
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
