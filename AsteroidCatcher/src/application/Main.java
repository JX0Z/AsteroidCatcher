package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.*;
import javafx.event.EventHandler;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import java.io.*;

import javax.swing.JOptionPane;

public class Main extends Application {
	public double speed = 1.5;
	public boolean platLeft = false;
	public boolean platRight = false;
	public int powerSlowTime = 0;
	public int powerLargeTime = 0;
	public int score = 0;
	static int xPositioning = 160, yPositioning = 50, ySpacing = 150;

	@Override
	public void start(Stage primaryStage) {
		try {
			Pane title = new Pane();
			Scene titleScene = new Scene(title, 500, 700);
			primaryStage.setScene(titleScene);
			title.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			StackPane game = new StackPane();
			game.setBackground(
					new Background(new BackgroundFill(Color.web("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));// https://stackoverflow.com/questions/22841000/how-to-change-the-color-of-pane-in-javafx
			Scene gameScene = new Scene(game, 500, 700);

			Pane instruct = new Pane();
			Scene instructScene = new Scene(instruct, 500, 700);

			Font buttonText = new Font(35);

			// Background  - Title
						BackgroundImage titleBackground= new BackgroundImage(new Image("https://i.pinimg.com/originals/82/56/f8/8256f8aeff53903ff47cd278de3e5695.jpg",
								500,700,false,true),
						        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
						          BackgroundSize.DEFAULT);
						//setting it to the pane
						title.setBackground(new Background(titleBackground));
						
						//Background - Rules
						BackgroundFill rulesBackground = new BackgroundFill(Color.VIOLET, new CornerRadii(1),
						         new Insets(0.0,0.0,0.0,0.0));// or null for the padding
						//setting to the pane
						instruct.setBackground(new Background(rulesBackground));
						
						// Background  - Main Game
						game.setBackground(
								new Background(new BackgroundFill(Color.web("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));// https://stackoverflow.com/questions/22841000/how-to-change-the-color-of-pane-in-javafx

			// Creating the options for the title page
			Button start = new Button("Play Now");
			Button instructions = new Button("Rules");
			Button end = new Button("Exit");

			// Game Title
			final Text gameTitle = new Text("ASTEROID CATCHER");
			title.getChildren().add(gameTitle);
			gameTitle.setFont(Font.font(STYLESHEET_CASPIAN, 30));
			gameTitle.setFill(Color.CYAN);
			gameTitle.setTranslateX(xPositioning - 60);
			gameTitle.setTranslateY(yPositioning + 10);

			// Adding the PLAY NOW button to the pane
			title.getChildren().add(start);
			start.setTranslateX(xPositioning - 35);
			start.setTranslateY(yPositioning + ySpacing + 50);
			start.setPrefSize(250, 100);
			start.setFont(buttonText);

			// Adding RULES button to the pane
			title.getChildren().add(instructions);
			instructions.setTranslateX(xPositioning - 35);
			instructions.setTranslateY(yPositioning + ySpacing + 180);
			instructions.setPrefSize(250, 100);
			instructions.setFont(buttonText);

			// Adding EXIT button to the pane
			title.getChildren().add(end);
			end.setTranslateX(xPositioning - 35);
			end.setTranslateY(yPositioning + ySpacing + 310);
			end.setPrefSize(250, 100);
			end.setFont(buttonText);

			primaryStage.setScene(titleScene);
			primaryStage.show();

			Text gameOverText = new Text();
			gameOverText.setText("Game over");
			gameOverText.setTranslateX(0);
			gameOverText.setTranslateY(0);
			gameOverText.setFont(Font.font(STYLESHEET_CASPIAN, 50));
			gameOverText.setFill(Color.WHITE);
			gameOverText.setVisible(false);

			// Init Power up
			Circle powerSlow = new Circle(0, 0, 10, Color.GREEN);
			powerSlow.setTranslateY((int) ((Math.random() * -1000) - 2000));
			powerSlow.setTranslateX((int) (Math.random() * 460 + 20) - 250);
			game.getChildren().add(powerSlow);

			Circle powerLarge = new Circle(0, 0, 10, Color.YELLOW);
			powerLarge.setTranslateY((int) ((Math.random() * -1000) - 2000));
			powerLarge.setTranslateX((int) (Math.random() * 460 + 20) - 250);
			game.getChildren().add(powerLarge);

			// Init 5 Circles
			Circle[] cirArray = new Circle[5];
			for (int i = 0; i < 5; i++) {
				cirArray[i] = new Circle(0, 0, (int) (Math.random() * 8 + 8));
				cirArray[i].setTranslateX((int) (Math.random() * 460 + 20) - 250);
				cirArray[i].setTranslateY(-350 - i * 150);
				game.getChildren().add(cirArray[i]);
			}
			// Init scorebox
			Text scoreBox = new Text();
			scoreBox.setText("0");
			scoreBox.setTranslateX(-230);
			scoreBox.setTranslateY(-340);
			Circle scoreCircle = new Circle(50, Color.WHITE);
			scoreCircle.setTranslateX(-250);
			scoreCircle.setTranslateY(-350);
			game.getChildren().add(scoreCircle);
			game.getChildren().add(scoreBox);
			// Init Player Platform
			Rectangle platform = new Rectangle(100, 20, Color.BLUE);
			platform.setTranslateX(0);
			platform.setTranslateY(340);
			game.getChildren().add(platform);
			game.getChildren().add(gameOverText);

			AnimationTimer gameLoop = new AnimationTimer() {
				@Override
				public void handle(long now) {
					// Speeds up game by a tiny amount every frame
					speed *= 1.0003;
					// Moves platform according to boolean as long as the platform isn't at the max
					if (platLeft && !(platform.getTranslateX() <= -gameScene.getWidth() / 2 + platform.getWidth() / 2))
						platform.setTranslateX(platform.getTranslateX() - 10);
					if (platRight && !(platform.getTranslateX() >= gameScene.getWidth() / 2 - platform.getWidth() / 2))
						platform.setTranslateX(platform.getTranslateX() + 10);

					if (powerSlowTime > 0)
						powerSlowTime--;
					if (powerLargeTime > 0)
						powerLargeTime--;

					// For loop for the asteroids
					for (int i = 0; i < 5; i++) {
						// Moves asteroid slowly if powerup is active
						if (powerLargeTime > 0) {
							platform.setWidth(200);

						} else
							platform.setWidth(100);
						if (powerSlowTime > 0) {
							cirArray[i].setTranslateY(cirArray[i].getTranslateY() + 1.5);

						}
						// Moves asteroid regularly if not
						else
							cirArray[i].setTranslateY(cirArray[i].getTranslateY() + speed);
						// Checks if asteroid is caught by the platform
						if (cirArray[i].getTranslateX() <= platform.getTranslateX() + platform.getWidth() / 2
								&& cirArray[i].getTranslateX() >= platform.getTranslateX() - platform.getWidth() / 2
								&& cirArray[i].getTranslateY() <= platform.getTranslateY() + platform.getHeight() / 2
								&& cirArray[i].getTranslateY() >= platform.getTranslateY() - platform.getHeight() / 2) {
							// Resets coordinates to a random X and a high Y
							cirArray[i].setTranslateX((int) (Math.random() * 460 + 20) - 250);
							cirArray[i].setTranslateY(cirArray[i].getTranslateY() - 700);
							// Updates score
							score++;
							scoreBox.setText(String.valueOf(score));
						}
						// Checks if a asteroid hits the bottom
						if (cirArray[i].getTranslateY() > 360) {
							gameOverText.setVisible(true);
							// Freezes all asteroids
							speed = 0;
							powerSlowTime=0;
							powerLargeTime=0;
							// Expands the asteroid to fill the screen
							if (cirArray[i].getRadius() < Math
									.sqrt(Math.pow(gameScene.getWidth(), 2) + Math.pow(gameScene.getHeight(), 2))) {
								cirArray[i].setRadius(cirArray[i].getRadius() + 10);
							} else {
								// Stops the game
								System.out.println("You lose");
								stop();
							}
						}
					}
					// Moves powerup slowly if powerup is active
					if (powerSlowTime > 0) {
						powerSlow.setTranslateY(powerSlow.getTranslateY() + 1.5);
						powerLarge.setTranslateY(powerLarge.getTranslateY() + 1.5);

					}
					// Moves powerup regularly if not
					else {
						powerSlow.setTranslateY(powerSlow.getTranslateY() + speed);
						powerLarge.setTranslateY(powerLarge.getTranslateY() + speed);
					}

					// Checks if powerup is caught by the platform
					if (powerSlow.getTranslateX() <= platform.getTranslateX() + platform.getWidth() / 2
							&& powerSlow.getTranslateX() >= platform.getTranslateX() - platform.getWidth() / 2
							&& powerSlow.getTranslateY() <= platform.getTranslateY() + platform.getHeight() / 2
							&& powerSlow.getTranslateY() >= platform.getTranslateY() - platform.getHeight() / 2) {
						// Sets the powerup time 1200 frames or 20 seconds
						powerSlowTime = 600;
						powerSlow.setTranslateY((int) ((Math.random() * -1000) - 2000));
						powerSlow.setTranslateX((int) (Math.random() * 460 + 20) - 250);
					}
					// If power up is missed move to a random X and a random high Y
					if (powerSlow.getTranslateY() > 350) {
						powerSlow.setTranslateY((int) ((Math.random() * -1000) - 2000));
						powerSlow.setTranslateX((int) (Math.random() * 460 + 20) - 250);
					}
					// Checks if powerup is caught by the platform
					if (powerLarge.getTranslateX() <= platform.getTranslateX() + platform.getWidth() / 2
							&& powerLarge.getTranslateX() >= platform.getTranslateX() - platform.getWidth() / 2
							&& powerLarge.getTranslateY() <= platform.getTranslateY() + platform.getHeight() / 2
							&& powerLarge.getTranslateY() >= platform.getTranslateY() - platform.getHeight() / 2) {
						// Sets the powerup time 1200 frames or 20 seconds
						powerLargeTime = 600;
						powerLarge.setTranslateY((int) ((Math.random() * -1000) - 2000));
						powerLarge.setTranslateX((int) (Math.random() * 460 + 20) - 250);
					}
					// If power up is missed move to a random X and a random high Y
					if (powerLarge.getTranslateY() > 350) {
						powerLarge.setTranslateY((int) ((Math.random() * -1000) - 2000));
						powerLarge.setTranslateX((int) (Math.random() * 460 + 20) - 250);
					}
				}
			};

			// Event Mouse Click
			end.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println("Application exited");
					System.exit(0);
				}
			});
			instructions.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println("Game instructions displaying");
					// New scene is displayed
					primaryStage.setScene(instructScene);
					Button back = new Button("HOME");
					final Text rules = new Text("RULES");
					Text rules2 = new Text("The goal of this game is to collect as many asteroids as you can");//centre this just below "RULES" Title
					Text rules3 = new Text("Move this platform using the LEFT & RIGHT arrow keys to catch these asteroids");
					Text rules9 = new Text("Once an asteroid is not caught the game is lost");
					Text rules4 = new Text("The game gets faster as you progress");
					Text green = new Text("Green");
					green.setFill(Color.GREEN);
					Text rules5 = new Text(" balls are POWER UPS* that slow down the game");
					Text yellow = new Text("Yellow");
					yellow.setFill(Color.GOLD);
					Text rules6 = new Text(" balls are POWER UPS* that increase the size of your platform");
					Text rules7 = new Text("*If power ups are not caught the game still continues");
					final Text rules8 = new Text("H A V E  F U N !");
					Rectangle platform = new Rectangle(100, 20, Color.BLUE);
					Circle demoCircle = new Circle(0, 0, 8);
					Circle demoCircle1 = new Circle(0,0,15);
					Circle demoCircle2 = new Circle(0,0,24);
					Circle demoCircle3 = new Circle(0,0,5);
					Circle demoCircle4 = new Circle(0,0,12,Color.GREEN);
					Circle demoCircle5 = new Circle(0,0,12,Color.YELLOW);
					
					instruct.getChildren().add(back);
					instruct.getChildren().add(rules);
					instruct.getChildren().add(rules2);
					instruct.getChildren().add(rules3);
					instruct.getChildren().add(rules4);
					instruct.getChildren().add(green);
					instruct.getChildren().add(rules5);
					instruct.getChildren().add(yellow);
					instruct.getChildren().add(rules6);
					instruct.getChildren().add(platform);
					instruct.getChildren().add(demoCircle);
					instruct.getChildren().add(demoCircle1);
					instruct.getChildren().add(demoCircle2);
					instruct.getChildren().add(demoCircle3);
					instruct.getChildren().add(demoCircle4);
					instruct.getChildren().add(demoCircle5);
					instruct.getChildren().add(rules7);
					instruct.getChildren().add(rules8);
					instruct.getChildren().add(rules9);
					
					back.setTranslateX(10);
					back.setTranslateY(10);
					//Returns back to home page when clicked
					back.setOnAction(goToHomepage ->{
						primaryStage.setScene(titleScene);
						System.out.println("Returned to home page");
					});
					
					rules.setTranslateX(xPositioning - 150);
					rules.setTranslateY(yPositioning + 20);
					rules.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 30));
					rules.setFill(Color.CADETBLUE);
					
					Font rulesText = new Font(15);
					
					rules2.setFont(rulesText);
					rules2.setTranslateX(25);
					rules2.setTranslateY(ySpacing);
					
					rules3.setTranslateX(5);
					rules3.setTranslateY(yPositioning + ySpacing + 50);
					platform.setTranslateX(45);
					platform.setTranslateY(yPositioning + ySpacing + 100);
					demoCircle.setTranslateX(350);
					demoCircle.setTranslateY(yPositioning + ySpacing + 100);
					demoCircle1.setTranslateX(375);
					demoCircle1.setTranslateY(yPositioning + ySpacing + 150);
					demoCircle2.setTranslateX(450);
					demoCircle2.setTranslateY(yPositioning + ySpacing + 140);
					demoCircle3.setTranslateX(380);
					demoCircle3.setTranslateY(yPositioning + ySpacing + 90);
					
					rules9.setTranslateX(5);
					rules9.setTranslateY(yPositioning + ySpacing + 150);
					
					rules4.setTranslateX(5);
					rules4.setTranslateY(yPositioning + ySpacing + 200);
					
					green.setTranslateX(5);
					green.setTranslateY(yPositioning + ySpacing + 275);
					rules5.setTranslateX(42.5);
					rules5.setTranslateY(yPositioning + ySpacing + 275);
					demoCircle4.setTranslateX(450);
					demoCircle4.setTranslateY(yPositioning + ySpacing + 275);
					
					yellow.setTranslateX(5);
					yellow.setTranslateY(yPositioning + ySpacing + 350);
					rules6.setTranslateX(44.5);
					rules6.setTranslateY(yPositioning + ySpacing + 350);
					demoCircle5.setTranslateX(460);
					demoCircle5.setTranslateY(yPositioning + ySpacing + 350);
					
					rules7.setTranslateX(5);
					rules7.setTranslateY(yPositioning + ySpacing + 400);
					
					rules8.setTranslateX(xPositioning);
					rules8.setTranslateY(yPositioning + ySpacing + 470);
					rules8.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
					
					
					

				}
			});
			start.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println("Game began");
					primaryStage.setScene(gameScene);
					gameLoop.start();
				}
			});

			// Set booleans based on key presses
			primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.LEFT) {
					platLeft = true;
				}
				if (event.getCode() == KeyCode.RIGHT) {
					platRight = true;
				}
				if (event.getCode() == KeyCode.ESCAPE) {
					primaryStage.setScene(titleScene);
					System.out.println("Game stopped");
					gameLoop.stop();
					powerSlow.setTranslateY((int) ((Math.random() * -1000) - 2000));
					powerSlow.setTranslateX((int) (Math.random() * 460 + 20) - 250);
					powerLarge.setTranslateY((int) ((Math.random() * -1000) - 2000));
					powerLarge.setTranslateX((int) (Math.random() * 460 + 20) - 250);
					for (int i = 0; i < 5; i++) {
						cirArray[i].setRadius((int) (Math.random() * 8 + 8));
						cirArray[i].setTranslateX((int) (Math.random() * 460 + 20) - 250);
						cirArray[i].setTranslateY(-350 - i * 150);
					}
					scoreBox.setText("0");
					scoreBox.setTranslateX(-230);
					scoreBox.setTranslateY(-340);
					scoreCircle.setTranslateX(-250);
					scoreCircle.setTranslateY(-350);
					platform.setTranslateX(0);
					platform.setTranslateY(340);
					speed = 1.5;
					platLeft = false;
					platRight = false;
					powerSlowTime = 0;
					powerLargeTime = 0;
					score = 0;
					gameOverText.setVisible(false);
				}
			});
			// Set booleans to false based on key releases
			primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
				if (event.getCode() == KeyCode.LEFT) {
					platLeft = false;
				}
				if (event.getCode() == KeyCode.RIGHT) {
					platRight = false;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
