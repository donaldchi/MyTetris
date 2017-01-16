/*
  Sound Control view class. Control all sound of the game.
  Created by chi on 2016/10/18.
  Copyright © 2016年 chi. All rights reserved.
*/
  
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
 
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.JOptionPane; 
 
public class SoundControl extends Thread {
    // variable
    private Player player;
    private BufferedInputStream bStream;
    private Boolean isPlaying;
    private int totalLength; //the length of media file

    // constructor
    // public SoundControl(String file) {
        // setFile(file);
    // }

    public int stopMusic() {
        try{
            int pos = bStream.available();
            player.close();
            bStream.close();
            player = null;
            bStream = null;
            isPlaying = false;
            return pos;
        } catch(Exception e) {
            return 0;
        }
    }

    // public void stopMusic() {
    //     try {
    //         if (player != null) {
    //             player.close();
    //         }
    //         if (bStream != null) {
    //             bStream.close();
    //         }
    //     } catch (IOException e) {
    //         System.out.println("IOException");
    //     } finally {
    //         player = null;
    //         bStream = null;
    //         isPlaying = false;
    //     }
    // }

    public void play(int pos, String file) { //play and resume
        try {
            bStream = new BufferedInputStream(new FileInputStream(file));
            player = new Player(bStream);

            isPlaying = true;
            totalLength = bStream.available();

            if(pos>0) bStream.skip(pos); //skip some times if resumed
            new Thread(
                new Runnable() {
                    public void run() {
                        try{
                            player.play();
                        } catch (Exception e) { 
                            JOptionPane.showMessageDialog (
                            null,
                            "Can not read music file!",
                            "確認",
                            JOptionPane.INFORMATION_MESSAGE,
                            null
                            );
                        }
                    }
                }
            ).start();
        } catch (Exception e) { 
            JOptionPane.showMessageDialog (
                null,
                "Error in playing music file!",
                "確認",
                JOptionPane.INFORMATION_MESSAGE,
                null
            );            
        }
    }

    // public void setFile(String file) {
    //     try {
    //         bStream = new BufferedInputStream(new FileInputStream(file));
    //         player = new Player(bStream);
    //     } catch (IOException e) {
    //         System.out.println("IOException");
    //     } catch (JavaLayerException e) {
    //         System.out.println("JavaLayerException");
    //     }
    // }

    public boolean isPlay(){
        return isPlaying;
    }

    public void setIsPlay(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isEnd() {
        try {
            if(totalLength == bStream.available())
            return true;
        } catch (Exception e){ }
        
        return false;
    }
}