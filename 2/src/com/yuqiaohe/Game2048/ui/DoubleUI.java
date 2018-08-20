package com.yuqiaohe.Game2048.ui;

import com.yuqiaohe.Game2048.core.DoubleLogicModule;
import com.yuqiaohe.Game2048.core.ImageBlock;
import com.yuqiaohe.Game2048.core.Opp;
import com.yuqiaohe.Game2048.model.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DoubleUI extends UI{
    private ImageBlock[][] imageBlocks2 = new ImageBlock[4][4];
    private DoubleLogicModule lg;
    private Runnable rs;
    private String s1="",s2="";
    private int step2;
    private TextArea t = new TextArea( "Waiting" );
    @Override
    protected void addButton()
    {
        JButton ConcedeButton = new JButton("Concede");
        JButton ExitButton = new JButton("Exit");
        JButton BackButton = new JButton("<");
        ConcedeButton.setBounds(7, 1, 80, 30);
        ExitButton.setBounds( 7,29,80,30 );
        BackButton.setBounds( 292,1,40,30 );
        GameView.add(ConcedeButton);
        GameView.add(ExitButton);
        GameView.add(BackButton);
        ConcedeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lg.concede();
                Reflash();
                GameView.setEnabled( false );
            }
        });
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lg.concede();
                System.exit(0);
            }
        });
        BackButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lg.concede();
                BackToMenu();
            }
        } );
    }
    @Override
    protected void drawBackGround(Graphics g, JPanel jp) {
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                g.drawImage(images[0],14+j*80,64+i*80,72,72,jp);
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                g.drawImage(images[0],354+j*80,64+i*80,72,72,jp);
    }
    @Override
    protected void drawBlocks(Graphics g,JPanel jp)
    {
        drawblocks(g,jp,imageBlocks);
        drawblocks(g,jp,imageBlocks2 );
    }
    @Override
    protected void init_endFrame(){
        endFrame = new JFrame(  );
        endFrame.setVisible( false );
        endFrame.setLocationRelativeTo(null);
        endFrame.setSize(200,200);
        t.setText( "Waiting" );
        endFrame.add(t);
        endFrame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        endFrame.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Opp.sendOne( 1 );
                try{Opp.getSocket().close();}catch (IOException ie){ie.printStackTrace();}
                BackToMenu();
                Enable();
            }
        } );
    }
    @Override
    protected void init_GameView()
    {
        GameView = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.red);
                g.setFont(font);
                g.drawString("Your Score is:"+ lg.getScore(), 92, 20);
                g.drawString( s1,92,50 );
                g.drawString("Your Op's Score is:"+ lg.getOpScore() , 422, 20);
                g.drawString( s2,432,50 );
                drawBackGround( g,this );
                drawBlocks( g,this );
            }
        };
        GameView.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int s = e.getKeyCode();
                if(s==KeyEvent.VK_UP||s==KeyEvent.VK_DOWN||s==KeyEvent.VK_LEFT||s==KeyEvent.VK_RIGHT)
                {
                    lg.send(s);
                }
            }
        } );
        GameView.setLayout( null );
        getImages(GameView.getToolkit());
        parent.add(GameView);
        GameView.setVisible( true );
        GameView.setFocusable( true );
    }
    private void setText(int [] tmp)
    {
        if(tmp[0]==1&&tmp[1]==1)
        {
            if(tmp[2]==1)
            {
                t.setText( "you conceded" );
                s1 = "you conceded";
                s2 = "your op win";
            }
            else if(tmp[3]==1)
            {
                t.setText( "your op conceded" );
                s1 = "you win";
                s2 = "your op conceded";
                lg.send( 1 );
            }
            else if(lg.getOpScore()>lg.getScore())
            {
                t.setText( "you lose" );
                s1 = "you lose";
                s2 = "your op win";
            }
            else if(lg.getScore()>lg.getOpScore())
            {
                t.setText( "you win" );
                s1 = "you win";
                s2 = "your op lose";
            }
            else
            {
                t.setText( "draw" );
            }
            show_end();
        }
        else if(tmp[0]==1&&tmp[1]==0)
        {
            s1 = "Wait For Your Op";
            s2 = "";
        }
        else if(tmp[0]==0&&tmp[1]==1)
        {
            s1 ="";
            s2 = "Wait For You";
        }
        else
        {
            s1 = "";
            s2 = "";
        }
    }
    private void init_Rs()
    {
        rs = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    int who = lg.Receive();
                    Anime( who );
                    int [] tmp = lg.checkPosition();
                    setText( tmp );
                    Reflash();
                    if(tmp[0]==1&&tmp[1]==1)
                    {
                        Opp.close();
                        break;
                    }
                }
            }
        };
    }
    private void Anime(int who)
    {
        if(who==0)AnimeLeft();
        else AnimeRight();
    }
    private void AnimeLeft()
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
                        lg.MoveLeftBlocks();
                        Reflash();
                    }
                    else
                    {
                        lg.setLeftBlocks();
                        Reflash();
                        this.cancel();
                        step++;
                    }
                }
            }
        }, shorttime,shorttime);
    }
    @Override
    protected void show_end(){
        Unable();
        endFrame.setVisible( true );
    }
    private void AnimeRight(){
        step2 = 0;
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (this) {
                    if(step2<7)
                    {
                        step2++;
                        lg.MoveRightBlocks();
                        Reflash();
                    }
                    else
                    {
                        lg.setRightBlocks();
                        Reflash();
                        this.cancel();
                        step2++;
                    }
                }
            }
        }, shorttime,shorttime);
    }
    public DoubleUI(GameFrame sg)
    {
        super((sg));
        init_GameView();
        init_Rs();
        init_endFrame();
        init_errorFrame();
        addButton();
        parent.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                lg.concede();
            }
        } );
        lg = new DoubleLogicModule( imageBlocks,imageBlocks2 );
        DoubleUI.super.lg=DoubleUI.this.lg;
        new Thread( new Runnable() {
            @Override
            public void run() {
                Unable();
                Opp.readOne();
                Enable();
                new Thread( rs ).start();
            }
        } ).start();
    }
}
