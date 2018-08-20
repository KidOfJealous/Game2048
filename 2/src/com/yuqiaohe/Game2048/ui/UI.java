package com.yuqiaohe.Game2048.ui;

import com.yuqiaohe.Game2048.core.ImageBlock;
import com.yuqiaohe.Game2048.core.LogicModule;
import com.yuqiaohe.Game2048.model.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class UI {
    protected JPanel GameView;
    protected JFrame endFrame;
    protected JFrame errorFrame;
    protected GameFrame parent;
    protected LogicModule lg;
    protected final int shorttime=12;
    public static Image[] images;
    protected Font font = new Font("TimesRoman",Font.BOLD,20);
    protected ImageBlock[][] imageBlocks = new ImageBlock[4][4];
    public int step=8;
    private static final int imageNum = 18;

    public static void getImages(Toolkit t)
    {
        images = new Image[imageNum];
        for(int i =0;i<imageNum;++i)
        {
            images[i]=t.getImage( "/Users/yuqiao.he/IdeaProjects/2/D:/lg_"+i+".JPG");
        }
    }
    public UI(GameFrame sg){parent = sg;}
    protected void addButton(){}
    protected void init_endFrame(){}
    protected void init_GameView(){}
    protected void init_errorFrame(){}
    protected void show_end(){
        endFrame.setVisible( true );}
    public void Unable()
    {
        GameView.setEnabled(false);
        Component[] c = GameView.getComponents();
        for(Component i:c)i.setEnabled( false );
    }
    public void Enable()
    {
        GameView.setEnabled(true);
        Component[] c = GameView.getComponents();
        for(Component i:c)i.setEnabled( true );
        GameView.requestFocus();
    }
    public void BackToMenu()
    {
        GameView.setVisible( false );
        parent.setSize( 340,420 );
        parent.remove( GameView);
        parent.add(parent.getMenu());
        parent.getMenu().setFocusable( true );
        parent.getMenu().requestFocus();
        parent.getMenu().setVisible( true );
    }
    protected void Anime()
    {
        step = 0;
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (this) {
                    if(step<7)
                    {
                        step++;
                        lg.MoveAllBlocks();
                        Reflash();
                    }
                    else
                    {
                        lg.setAllBlocks();
                        Reflash();
                        if(lg.checked())show_end();
                        step++;
                        this.cancel();
                    }
                }
            }
        }, shorttime,shorttime);
    }
    public void addListener()
    {
            GameView.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(step!=8)return;
                lg.checkMove();
                boolean flag = lg.move(e.getKeyCode());
                if(!flag)return;
                Anime();
            }
        });
    }
    protected void drawblocks(Graphics g,JPanel jp,ImageBlock[][] ima)
    {
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                ImageBlock im = ima[i][j];
                if(im.getImage()>0)g.drawImage(images[im.getImage()],im.getX()-im.getSize()/2,im.getY()-im.getSize()/2, im.getSize(),im.getSize(),jp);
            }
    }
    protected void Reflash()
    {
        GameView.repaint(  );
    }
    protected void drawBlocks(Graphics g,JPanel jp)
    {
        drawblocks( g,jp,imageBlocks );
    }
    protected void drawBackGround(Graphics g,JPanel jp)
    {
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                g.drawImage(images[0],14+j*80,64+i*80,72,72,jp);
    }
}
