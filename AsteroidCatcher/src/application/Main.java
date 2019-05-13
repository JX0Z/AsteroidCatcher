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
			StackPane title = new StackPane();
			Scene titleScene = new Scene(title, 500, 700);
			primaryStage.setScene(titleScene);
			title.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			StackPane game = new StackPane();
			game.setBackground(
					new Background(new BackgroundFill(Color.web("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));// https://stackoverflow.com/questions/22841000/how-to-change-the-color-of-pane-in-javafx
			Scene gameScene = new Scene(game, 500, 700);

			StackPane instruct = new StackPane();
			Scene instructScene = new Scene(instruct, 500, 700);

			Font buttonText = new Font(35);

			// Background Image

			// Creating the options for the title page
			Button start = new Button("Play Now");
			Button instructions = new Button("Rules");
			Button end = new Button("Exit");

			// Game Title
			final Text gameTitle = new Text("ASTEROID CATCHER");
			title.getChildren().add(gameTitle);
			gameTitle.setFont(Font.font(STYLESHEET_CASPIAN, 30));
			gameTitle.setFill(Color.CYAN);
			gameTitle.setX(xPositioning - 60);
			gameTitle.setY(yPositioning + 10);

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
					primaryStage.setScene(instructScene);
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
