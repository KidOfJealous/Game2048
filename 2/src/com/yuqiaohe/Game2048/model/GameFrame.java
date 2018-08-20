package com.yuqiaohe.Game2048.model;

import com.yuqiaohe.Game2048.core.Opp;
import com.yuqiaohe.Game2048.ui.DoubleUI;
import com.yuqiaohe.Game2048.ui.SingleUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {
    private SingleUI singleGame = null;
    private DoubleUI doubleGame = null;
    private MenuPanel menu;

    private RoomPanel room = null;

    public static GameFrame mainFrame;

    public GameFrame() {
        setSize( 340, 420 );
        menu = new MenuPanel(this);
        menu.setVisible( true );
        add( menu );
        mainFrame = this;
    }

    public RoomPanel getRoom() {
        return room;
    }
    public MenuPanel getMenu() {
        return menu;
    }

    public void StartGame() {
        doubleGame = new DoubleUI( this );
        repaint(  );
    }

    public void StartSingleGame() {
        singleGame = new SingleUI(this);
        repaint(  );
    }
    public void startRoom(){
        room = new RoomPanel(this);
        add(room);
        room.setVisible( true );
        room.setFocusable( true );
        room.requestFocus();
    }
}
