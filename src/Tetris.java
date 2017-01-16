/*
  Main Game Class
  Created by chi on 2016/10/18.
  Copyright © 2016年 chi. All rights reserved.
*/
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Tetris extends JFrame {
	public final static int SCORE_PER_LINE = 10; //一行の得点
	public final static int SCORE_FOR_UPDATE = SCORE_PER_LINE * 10;
	public final static int DEFAULT_LEVEL = 1;
	public final static int LEVEL_SPEED_TERM = 200;

    private static int blockID;
    private static int nextBlockID;
    private static int blockPos_y; //(0,0)を基準にした横方向の相対位置
    private static int blockPos_x; //(0,0)を基準にした縦方向の相対位置 

    private static boolean isMovable = true;

	private JMenuBar menuBar;
	private JMenu gameMenu, control, windowStyle, about;
	private JMenuItem newGameItem, setBoxColorItem, setBgColorItem, levelUpItem, levelDownItem, exitItem;
	private JMenuItem playItem, pauseItem, resumeItem, stopItem;
	private JRadioButtonMenuItem windowsRadioItem, motifRadioItem, metalRadioItem;
    private ButtonGroup radioBtnContainer;
    private JMenuItem versionItem, helpItem;

    private GameView gScreen;
    private GameControl gControl;

    private boolean isPlaying = true;
    private boolean isLevelup = false;
    private boolean isLeveldown = false;

    private static boolean isRestart = false;

    SoundControl bgm; //background music
    private int curPos;

    public Tetris(String title) {
    	//set screen title and size
    	super(title);
    	setSize(1024, 768);
        setResizable(false);

    	//set main screen position
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setLocation((screenSize.width-getSize().width)/2,(screenSize.height-getSize().height)/2);

    	//set menu();
    	// setMenu();
    	Container container = getContentPane();
    	container.setLayout(new BorderLayout());

        gScreen = new GameView();
        container.add(gScreen, BorderLayout.CENTER);

        gControl = new GameControl(this, 0);
        container.add(gControl, BorderLayout.EAST);

    	//add actionlistener
    	addWindowListener( new WindowAdapter() {
    		public void windowClosing(WindowEvent event) {
    			// stop();
    			System.exit(0);
    		}
    	});

    	addComponentListener( new ComponentAdapter() {
    		public void componentResized(ComponentEvent event) {
    			//canvas.redraw();
    			System.out.print("Redraw the screen!");
                // System.out.print(getWidth()+", " + getHeight());
    		}
    	});

    	setVisible(true);

        addKeyListener(new KeyListener());
        setFocusable(true);

        bgm = new SoundControl();
        curPos = 0;
        bgm.play(curPos, "audio/bgm.mp3");
        
        playGame();
    }

    /*
    * set menu of main screen
    */
    private void setMenu() {
    	gameMenu = new JMenu("Game");
    	newGameItem = new JMenuItem("New Game");
    	setBoxColorItem = new JMenuItem("Set Box Color"); 
    	setBgColorItem = new JMenuItem("Set BackGround Color");
    	levelUpItem = new JMenuItem("Level Up!");
    	levelDownItem = new JMenuItem("Level Down!");
    	exitItem = new JMenuItem("Exit");
    	gameMenu.add(newGameItem);
    	gameMenu.add(setBoxColorItem);
    	gameMenu.add(setBgColorItem);
    	gameMenu.add(levelUpItem);
    	gameMenu.add(levelDownItem);
    	gameMenu.add(exitItem);

    	control = new JMenu("Control");
    	playItem = new JMenuItem("Play");
    	pauseItem = new JMenuItem("Pause");
    	resumeItem = new JMenuItem("Resume");
    	stopItem = new JMenuItem("Stop");
    	control.add(playItem);
    	control.add(pauseItem);
    	control.add(resumeItem);
    	control.add(stopItem);

    	windowStyle = new JMenu("WindowStyle");
    	radioBtnContainer = new ButtonGroup();
    	windowsRadioItem = new JRadioButtonMenuItem("Windows");
    	motifRadioItem = new JRadioButtonMenuItem("Motif", true);
    	metalRadioItem = new JRadioButtonMenuItem("Metal");
    	windowStyle.add(windowsRadioItem);
    	windowStyle.add(motifRadioItem);
    	windowStyle.add(metalRadioItem);
    	radioBtnContainer.add(windowsRadioItem);
    	radioBtnContainer.add(motifRadioItem);
    	radioBtnContainer.add(metalRadioItem);

    	about = new JMenu("About");
    	versionItem = new JMenuItem("Version: 1.0");
    	helpItem = new JMenuItem("Help");
    	about.add(versionItem);
    	about.add(helpItem);

    	menuBar = new JMenuBar();
    	menuBar.add(gameMenu);
    	menuBar.add(control);
    	menuBar.add(windowStyle);
    	menuBar.add(about);

    	addMenuActionListener();
    	setJMenuBar(menuBar);
    }

    /*
    * add action listener for menu
    */
    private void addMenuActionListener() {

    }

    private void reset() {

    }

    /*
        Game Menu Control Function
    */
    public void reStart() { //restart button pressed
        isRestart = true;
        bgm.stopMusic();
        bgm = new SoundControl();
        curPos = 0;
        gControl.resetCurScore();
        bgm.play(curPos, "audio/bgm.mp3");

        SoundControl startBgm = new SoundControl();
        startBgm.play(0, "audio/start.mp3");
    }

    public void quit() {
        bgm.stopMusic();
        System.exit(0);
    }

    //pause button pressed
    public void pause() {
        this.isPlaying = false;
        // curPos = bgm.stopMusic();
    }

    //play button pressed
    synchronized public void play() {
        this.isPlaying = true;
        // bgm.play(curPos, "audio/bgm.mp3");
        notify();
    }

    public void levelUp() {
        this.isLevelup = true;
    }

    public void levelDown() {
        this.isLeveldown = true;
    }

    //run the game
    public void playGame() {

        int i = 0;
        int clearedLines = 0;
        int speed = 1000; //down one grid per second

        Random rnd = new Random();
        blockID = rnd.nextInt(19);
        nextBlockID = 0;

        blockPos_y = rnd.nextInt(9); //position of the first point of each block
        blockPos_x = 0; //blockPos_x changes in two way. 1. with time 2. with down movement

        gScreen.createBlock(blockID, blockPos_y);
        gControl.setNextBlock(nextBlockID);

        SoundControl startBgm = new SoundControl();
        startBgm.play(0, "audio/start.mp3");

        while(true) {
            try{
                //for get focus from gControl pannel
                requestFocus();
                Thread.sleep(speed);
                // System.out.println(bgm.isEnd());
                //pause
                synchronized(this) {
                    if(!isPlaying) {
                        wait();
                    }
                }

                //restart
                if(isRestart) { 
                    isRestart = false;
                    gScreen.reset();                    
                }

                if(isLevelup && speed>200) {
                    gControl.updateLevel(1);
                    speed -= 200;
                    this.isLevelup = false;
                }
                if(isLeveldown && speed <1000) {
                    gControl.updateLevel(-1);
                    speed += 200;
                    this.isLeveldown = false;
                }

                if(gScreen.isMovable_VERTICAL(blockID, blockPos_x, blockPos_y) && isMovable) {
                    gScreen.blockDown(blockID, blockPos_x, blockPos_y);
                    blockPos_x++; //blockPos_x changes in two way. 1. with time 2. with down movement
                } else {
                    clearedLines = gScreen.clearLine();
                    boolean need_update_level = gControl.setScore(clearedLines);
                    if(need_update_level && speed>200) { 
                        gControl.updateLevel(1);
                        speed -= 200;
                    }

                    int curScore = gControl.getScore();

                    if(gScreen.isGameOver()) {
                        if(gControl.isInRank()>0)
                            while(true) {
                                String value = JOptionPane.showInputDialog(this, 
                                    "You are ranked in history ranking.\n"+ 
                                    "Input your name in four characters.");
                                if(value!=null)                                
                                {
                                    gControl.writeRankingFile(gControl.isInRank(), value);
                                    break; 
                                }
                            }
                        
                        int option = JOptionPane.showConfirmDialog(this, 
                                "Your Score: " + curScore + "\n Do you want to continue?",
                                "Game Over!",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);

                        if (option == JOptionPane.YES_OPTION){
                            gScreen.reset();
                        }else if (option == JOptionPane.NO_OPTION){
                            bgm.stopMusic();
                            System.exit(0);
                        }
                    }

                    if(curScore>0 && curScore%50==0) {
                        startBgm.play(0, "audio/fighting.mp3");
                    }

                    // blockID = 1;
                    blockID = nextBlockID;
                    nextBlockID = rnd.nextInt(19);

                    blockPos_y = rnd.nextInt(9); //position of the first point of each block
                    blockPos_x = 0;
                    isMovable = true;
                    
                    gScreen.createBlock(blockID, blockPos_y);
                    gControl.setNextBlock(nextBlockID);                
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }


    /*
        Key Listener class.
    */
    private class KeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent ke) {
            switch (ke.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    blockPos_x = gScreen.moveDown(blockID, blockPos_x, blockPos_y);
                    if(blockPos_x==-1) {
                        isMovable = false;
                        blockPos_x = 0;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    blockPos_y = gScreen.moveLeft(blockID, blockPos_x, blockPos_y);
                    break;
                case KeyEvent.VK_RIGHT:
                    blockPos_y = gScreen.moveRight(blockID, blockPos_x, blockPos_y);
                    break;
                case KeyEvent.VK_UP:
                    blockID = gScreen.moveUP(blockID, blockPos_x, blockPos_y);
                    break;
                case KeyEvent.VK_SPACE://一键到底
                    while(blockPos_x>0) {  
                        blockPos_x = gScreen.moveDown(blockID, blockPos_x, blockPos_y);
                    }
                    if(blockPos_x==-1) {
                        isMovable = false;
                        blockPos_x = 0;
                    }
                    break;
                default:
                    break;
            }
        }
    }

	public static void main(String[] args) {
		System.out.println("Tetris Game!");

        while(true) {
            Tetris tt = new Tetris("Tetris! Joy Your Game!! @chi");
            tt.playGame();
        }
	}
}