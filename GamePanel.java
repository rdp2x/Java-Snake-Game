import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int body = 6;
    int foodEaten, foodx, foody;
    char direction = 'R';
    boolean running = false;
    Timer t;
    Random r;

    GamePanel() {
        r = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new keyCheck());
        startGame();
    }
    public void startGame() {
        newFood();
        running = true;
        t =new Timer(DELAY, this);
        t.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Draw(g);
    }
    public void Draw(Graphics g) {
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                // Draws the grid
//                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }

            // draws the food in the screen
            g.setColor(Color.RED);
            g.fillOval(foodx, foody, UNIT_SIZE, UNIT_SIZE);

            // for the body of the snake
            for (int i = 0; i < body; i++) {
                if(i == 0) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.BLUE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        } else {
            gameOver(g);
        }

        // displays the scoreboard
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("Score - " + foodEaten, (SCREEN_WIDTH - fm.stringWidth("Score - " + foodEaten)) / 2, g.getFont().getSize());
    }
    public void newFood() {
        foodx = r.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        foody = r.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;

    }
    public void Move() {
        for (int i = body; i  > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y [i - 1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }
    public void checkFood() {
        if((x[0] == foodx) && (y[0] == foody)) {
            body++;
            foodEaten++;
            newFood();
        }
    }
    public void checkCollisions() {
        // checks if head collides with body
        for (int i = body; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        if(x[0] < 0) //checks left border collision
            running = false;
        if(x[0] > SCREEN_WIDTH) //checks right border collision
            running = false;
        if(y[0] > SCREEN_HEIGHT) //checks bottom border collision
            running = false;
        if(y[0] < 0) //checks top border collision
            running = false;
        if (!running)
            t.stop();
    }
    public void gameOver(Graphics g) {
        // displays the "Game Over" text at the end
        g.setColor(Color.red);
        g.setFont(new Font("Jokerman", Font.BOLD, 75));
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - fm.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            Move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }
    public class keyCheck extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')
                        direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U')
                        direction = 'D';
                    break;
            }
        }
    }
}
