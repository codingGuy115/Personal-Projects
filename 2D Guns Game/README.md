---2D GUNS GAME---

**PROJECT BACKGROUND:**
I started working on this project in my senior year of high school, after I had finished taking AP Computer Science A and was looking for a new project to start. I had always been interested in developing video games from the moment I started coding. During the Computer Science course I took, we were introduced to the .swing Java library for graphics, and I used this library because this is what I was familiar with at the time. However, this project does NOT make use of common game design patterns (such as batch rendering, the game loop, a singleton window, etc) simply because I had not learned these yet, and they would be hard to implement anyway with the framework I had chosen to begin with. So, this project was more of a learning experience to see what kinds of things I could implement.

**PROJECT DESCRIPTION:** 
This project is a small video game developed in Java using Java's .swing library for rudimentary fixed-function graphics. It makes use of many object-oriented programming concepts, including classes for the game objects, and inheritance (each game component, such as a player, inherits from JComponent in order to be able to be added to the JFrame). The general structure is as follows: The GameMain class inherits from JFrame and extends ActionListener, essentially creating the base for an object which can display a window and has input-receiving capabilities. It holds a Timer object which controls the speed at which the window can be updated. Additionally, it holds a list of all the game objects that need to be updated each timer tick. Much of the player controls are defined in the GameMain constructor, while the events are processed inside the actionPerformed() method that we override in the gameMain class. Upon running the project, the main() function in the gameMain file will create a new instance of the gameMain class, calling the constructor which initializes everything and starts the timer, allowing the game to run until the user closes the window or performs an escape sequence. 

**Gameplay**: The player is a blue rectangle and can be controlled to go left and right using keys A and D, and space to jump. The player can swap projectile modes which are indicated at the top of the screen as green, red, and blue boxes. The current gun mode is designated with two black rectangles that appear at the top and bottom of the correct color mode box. To fire green and red modes, press K. To fire blue mode, left click on the screen. The projectiles can be used to destroy the tan colored boxes. 

Green: standard bullet.

Red: shotgun, a couple pellets are initialized at the player and given a random Y velocity in a specific range, to give a 'random spread' effect. 

Blue: grenade launcher. A projectile is given X and Y velocity depending on the distance the player is from where they click on the screen. The farther it is, the larger the velocity. The grenade follows simple simulated gravity. 

*DEMO*
![2D Guns Game demo](https://github.com/user-attachments/assets/1368fe3f-cd30-42c0-a837-096056c07921)


