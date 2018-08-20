package com.yuqiaohe.Game2048.ui;
import com.yuqiaohe.Game2048.core.SingleLogicModule;
import com.yuqiaohe.Game2048.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Ref;
import java.util.Timer;
import java.util.TimerTask;

public class SingleUI extends UI{
    private SingleLogicModule lg;
    JButton UndoButton;
    protected void init_GameView()
    {
        GameView = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.red);
                g.setFont(font);
                g.drawString("Your Score:" + lg.getScore(), 92, 50);
                g.drawString("Highest Score:" + lg.getHighestScore(), 92, 20);
                drawBackGround( g,this );
                drawBlocks( g,this );
            }
        };
        GameView.setLayout( null );
        getImages(GameView.getToolkit());
        parent.add(GameView);
        GameView.setVisible( true );
        GameView.requestFocus();
        addListener();
    }
    private void Undo()
    {
        step = 0;
        lg.load();
        lg.setBackBlocks();
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (this) {
                    if(step<7)
                    {
                        step++;
                        lg.MoveBackAllBlocks();
                        Reflash();
                    }
                    else
                    {
                        lg.setAllBlocks();
                        Reflash();
                        GameView.requestFocus();
                        Enable();
                        UndoButton.setEnabled( false );
                        step++;
                        this.cancel();
                    }
                }
            }
        }, shorttime,shorttime);
    }
    @Override
    public void addListener()
    {
        GameView.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(step!=8)return;
                lg.checkMove();
                boolean flag = lg.move(e.getKeyCode());
                if(!flag)return;
                UndoButton.setEnabled( true );
                lg.reflash();
                Anime();
            }
        });
    }
    @Override
    public void Unable()
    {
        super.Unable();
        UndoButton.setEnabled( true );
    }
    protected void init_errorFrame()
    {
        errorFrame = new JFrame("数据库连接失败");
        errorFrame.setVisible( false );
        errorFrame.setLocationRelativeTo(null);
        errorFrame.setSize(200,200);
        errorFrame.add(new TextArea( "你无法读取或保存记录" ));
        errorFrame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        errorFrame.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Enable();
            }
        } );

    }
    private void startGame()
    {
        lg = new SingleLogicModule( imageBlocks );
        super.lg=this.lg;
        if(!lg.LogDataBase()) errorFrame.setVisible( true );
        lg.load();
        if(lg.checked())lg.Restart();
        addButton();
        UndoButton.setEnabled( false );
        lg.setAllBlocks();
        Reflash();
    }
    public SingleUI(GameFrame sg)
    {
        super(sg);
        parent.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                lg.store();
            }
        } );
        init_GameView();
        init_endFrame();
        init_errorFrame();
        startGame();
    }
    protected void init_endFrame()
    {
        endFrame = new JFrame("Game Over");
        endFrame.setVisible( false );
        endFrame.setLocationRelativeTo(null);
        endFrame.setSize(200,200);
        endFrame.add(new TextArea( "Replay?" ));
        endFrame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        endFrame.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Unable();
                lg.Restart();
                Enable();
            }
        } );
    }
    @Override
    protected void addButton()
    {
        JButton ReplayButton = new JButton("Replay");
        UndoButton = new JButton("Undo");
        JButton ClearButton = new JButton("Clear");
        JButton BackButton = new JButton("<");
        ReplayButton.setBounds(7, 1, 80, 30);
        UndoButton.setBounds( 7,29,80,30 );
        ClearButton.setBounds(262,29,70,30);
        BackButton.setBounds( 292,1,40,30 );
        GameView.add(ReplayButton);
        GameView.add(UndoButton);
        GameView.add(ClearButton);
        GameView.add(BackButton);
        ReplayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lg.Restart();
                Reflash();
                GameView.requestFocus();
            }
        });
        UndoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UndoButton.setEnabled( false );
                endFrame.setVisible( false );
                Undo();
            }
        });
        ClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lg.clear();
                lg.store();
                Reflash();
                GameView.requestFocus();
            }
        });
        BackButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lg.store();
                BackToMenu();
                lg.quitDataBase();
            }
        } );
    }
    @Override
    protected void show_end()
    {
        Unable();
        endFrame.setVisible( true );
    }

}
