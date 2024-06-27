import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable{
    //SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;

    final int tileSize = originalTileSize*scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = maxScreenCol * tileSize;
    final int screenHeight = maxScreenRow * tileSize;

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    int playerX1 = 50;
    int playerX2 = maxScreenCol*tileSize-50;
    int playerY1 = screenHeight/2-tileSize/2;
    int playerY2 = screenHeight/2-tileSize/2;
    int radius = 30;
    int ballX = screenWidth/2-radius/2;
    int ballY = screenHeight/2-radius/2;
    int playerSpeed = 4;
    int ballSpeed = 6;
    int playerWidth = 10;
    int playerHeight = screenHeight/6;
    double rand1 = Math.random()*2;
    double rand2 = Math.random()*2;
    boolean directionY = boolify(rand1);
    boolean directionX = boolify(rand2);
    double direction = 0.95-Math.random()*0.33;
    int dx = (int) (ballSpeed*Math.cos(direction));
    int dy = (int) (ballSpeed*Math.sin(direction));
    int Point1 = 0;
    int Point2 = 0;


    public boolean boolify(double a){
        if (a>1){
            return true;
        }
        else{
            return false;
        }
    }
    


    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){

        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null){
            
            update();
            repaint();

            try {

                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;

                if(remainingTime < 0){
                    remainingTime = 0;
                }
                
                    Thread.sleep((long) remainingTime);

                    nextDrawTime += drawInterval;
            } catch (Exception e) {
                
            }
            
        }

    }
    public void update(){

        if(keyH.upPressed1 == true){
            playerY1 -= playerSpeed;
        }
        if(keyH.downPressed1 == true){
            playerY1 += playerSpeed;
        }
        if(keyH.upPressed2 == true){
            playerY2 -= playerSpeed;
        }
        if(keyH.downPressed2 == true){
            playerY2 += playerSpeed;
        }
        if (ballX<playerX1+playerWidth){
            if (ballY+radius<playerY1||ballY>playerY1+playerHeight){
                Point2 += 1;
                ballX = screenWidth/2-radius/2;
                ballY = screenHeight/2-radius/2;

                
            }
            else{
                directionX = !directionX;
            }
        }
        if (ballX+radius>playerX2){
            if (ballY+radius<=playerY2||ballY>playerY2+playerHeight){
                Point1 += 1;
                ballX = screenWidth/2-radius/2;
                ballY = screenHeight/2-radius/2;
            }
            else{
                directionX = !directionX;
            }
        }
        if ((ballY<0)||(ballY+radius>screenHeight)){
            directionY = !directionY;
        }
        if (directionX){
            ballX += dx;
        }
        else{
            ballX -= dx;
        }
        if (directionY){
            ballY += dy;
        }
        else{
            ballY -= dy;
        }



    }
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);
        
        g2.fillRect(playerX1,playerY1,playerWidth,screenHeight/6);
        g2.fillRect(playerX2,playerY2,playerWidth,screenHeight/6);
        g2.fillRect(ballX, ballY, radius, radius);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,30));
        g2.drawString("Player1's points: "+Integer.toString(Point1), screenWidth/8, screenHeight/3);
        g2.drawString("Player2's points: "+Integer.toString(Point2), screenWidth/8*4, screenHeight/3);

        g2.dispose();

    }

    
}
