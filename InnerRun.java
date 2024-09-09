import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

class Run{
    public static void main(String[] args) {
        App app = new App("Ghost");
        Start start = new Start();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RunTreade(start), 0, 500);
        app.add(start);
    }
}

class App extends JFrame{
    App(String titleApp){
        setTitle(titleApp);
        setBounds(150,150,1000,563);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}

class Start extends JPanel{
    int countGhost = 5;
    int [] ghostX =  new int[countGhost];
    int [] ghostY =  new int[countGhost];
    private StartSound playSound = new StartSound();
    private int mouseX, mouseY;
    Start(){
        setBounds(150,150,1000,563);
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e){
                setMousePosition(e.getX(),e.getY());
            }
        });
        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {};
            public void mouseEntered(MouseEvent e) {};
            public void mouseExited(MouseEvent e) {};
            public void mousePressed(MouseEvent e) {
                playSound.setStart();
                playSound.run();
                for(int i=0;i<countGhost;i++){
                    int clickX = ghostX[i]-(int)e.getX();
                    int clickY = ghostY[i]-(int)e.getY();
                    if((clickX>-50&&clickX<50) && (clickY>-50&&clickY<50)){
                        countGhost = countGhost -1;
                    }
                }
            };
            public void mouseReleased(MouseEvent e) {
                playSound.setStop();
            };
        });
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Toolkit.getDefaultToolkit().getImage("./background.jpg"),0,0,this);
        g.drawImage(Toolkit.getDefaultToolkit().getImage("./sight.gif"),this.mouseX,this.mouseY,this);
        g.setColor(new Color(255,255,255));
        g.setFont(new Font("Tahoma", Font.PLAIN,40));
        g.drawString("Ghost Hunter",700,100);
        for(int i=0;i<countGhost;i++){
            g.drawImage(Toolkit.getDefaultToolkit().getImage("./ghost.png"), ghostX[i], ghostY[i], 100,100,this);
        }   
    }
    public void RandomPositionGhost(){
        for(int i=0;i<countGhost;i++){
            this.ghostX[i] = (int)(Math.random() * 950);
            this.ghostY[i] = (int)(Math.random() * 500);
        }  
        repaint();
    }
    public void setMousePosition(int x, int y){
        this.mouseX = x-50;
        this.mouseY = y-50;
        repaint();
    }
}
class StartSound extends Thread{
    boolean isRunning = false;
    public void run() {
        try{
            File sound = new File(System.getProperty("user.dir")+File.separator+"gun.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(sound);
            AudioFormat format =stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
        }catch(Exception e){};
    }
    public void setStart(){
        this.isRunning = true;
    }
    public void setStop(){
        this.isRunning = false;
    }
}
class RunTreade extends TimerTask{
    Start page;
    RunTreade(Start panel){
        this.page = panel;
    }
    @Override
    public void run() {
        this.page.RandomPositionGhost();
    }
}