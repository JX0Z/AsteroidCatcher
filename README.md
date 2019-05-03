# AsteroidCatcher
## Description
Our game will involve a small platform at the bottom of the screen, and several falling balls. These balls will be randomly generated at the top of the screen and will fall down to the bottom. The goal of the game is to catch as many balls as you can by moving the platform for the falling balls to land on it. There will be a score counter in the top right corner.


## Rules
The balls will be continuously falling and the user much catch all of them by moving horizontally. If the user is not below the ball when it reaches y coordinate 0, the game is over. Therefore the ball has to be completely over the platform for it to be caught, and cannot be on the side or a corner. 


## Controls
The controls will be the left and right arrow keys, as well as the esc key to pause the game where you have the option to resume or quit.


## Animated Component
The balls will be falling down the screen. As the game gets harder when the player plays for a longer period of time, the rate in which the balls fall down will increase. If a ball hits the ground without the platform being there, it will expand to fill the screen. The platform will also be animated so it can move horizontally.


## Storing The Game ‘State’
There will be a limit of 5 balls on the screen at a time, all stored in an array. This means that when each ball reaches the bottom its y coordinate will reset to the top, and its x coordinate will be randomly generated. The platform that the player controls will simply have an x coordinate that can increase or decrease to move it horizontally, while the y coordinate is a final value. For a ball to be considered caught, it’s centre coordinate must be in an within x values of the platform that none of it touches a corner of the platform. The platform itself will also have a specific x value range, and these increase or decrease as it’s moved horizontally. When a ball is caught, it will simply disappear and while this happens your score will be incremented. On the contrary to reiterate when the player has lost, the ball they were unable to catch will be programmed so that it expands and fills the screen.
