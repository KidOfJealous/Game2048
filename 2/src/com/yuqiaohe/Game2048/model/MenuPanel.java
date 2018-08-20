package com.yuqiaohe.Game2048.model;

import com.yuqiaohe.Game2048.core.Opp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private GameFrame parent;
    public MenuPanel(GameFrame j)
    {
        parent = j;
        setLayout(null);
        JButton SingleButton = new JButton("单人游戏");
        SingleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.remove(parent.getMenu());
                MenuPanel.this.setVisible(false);
                parent.setSize(340,420);
                parent.StartSingleGame();
                parent.validate();
            }
        });
        JButton SetUp = new JButton("创建对战");
        SetUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Opp();
                Opp.sendOne( -1 );
                parent.setLayout(new BorderLayout());
                parent.setSize(680,420);
                parent.remove(parent.getMenu());
                parent.StartGame();
            }
        });
        JButton FindOp = new JButton("加入对战");
        FindOp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Opp();
                Opp.sendOne( 1 );
                parent.startRoom();
                parent.getMenu().setVisible( false );
                parent.remove(parent.getMenu());
            }
        });
        SingleButton.setBounds(120,50,100,40);
        SetUp.setBounds(120,190,100,40);
        FindOp.setBounds(120,330,100,40);
        add(SingleButton);
        add(SetUp);
        add(FindOp);
    }
}
