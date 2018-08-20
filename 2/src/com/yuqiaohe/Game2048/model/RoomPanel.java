package com.yuqiaohe.Game2048.model;

import com.yuqiaohe.Game2048.core.Opp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;

public class RoomPanel extends JPanel {
    private LinkedList<MyButton> Buttons;
    private GameFrame parent;
    class MyButton extends JButton{
        private int id;

        public MyButton(String s,int i)
        {
            super(s);
            id =i;
        }
    }
    public RoomPanel(GameFrame sg)
    {
        parent=sg;
        this.setLayout( null );
        Buttons = new LinkedList<MyButton>(  );
        //System.out.println( "A" );
        init();
    }
    public void init()
    {
        JButton ExitButton = new JButton("Exit");
        JButton BackButton = new JButton("<");
        ExitButton.setBounds( 7,29,80,30 );
        BackButton.setBounds( 292,1,40,30 );
        this.add(ExitButton);
        this.add(BackButton);
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Opp.sendOne( -1 );
                System.exit(0);
            }
        });
        BackButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Opp.sendOne( -1 );
                RoomPanel.this.setVisible( false );
                parent.setSize( 340,420 );
                parent.remove( RoomPanel.this);
                parent.add(parent.getMenu());
                parent.getMenu().setFocusable( true );
                parent.getMenu().requestFocus();
                parent.getMenu().setVisible( true );
            }
        } );
        try{
            DataInputStream in = new DataInputStream( Opp.getSocket().getInputStream());
            int num = in.readInt();
            System.out.println( "房间数"+num );
            for(int i=0;i<num;++i)
            {
                int id = in.readInt();
                if(id!=-1)
                {
                    MyButton myButton = new MyButton( "房间"+i,i );
                    myButton.addActionListener( new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Opp.sendOne( myButton.id );
                            parent.setSize(680,420);
                            RoomPanel.this.setVisible( false );
                            parent.remove(parent.getMenu());
                            parent.StartGame();
                        }
                    } );
                    Buttons.add(myButton);
                }
            }
            if(Buttons.size()!=0) {
                int slim = 400 / Buttons.size();
                for (int i = 0; i < Buttons.size(); ++i) {
                    Buttons.get( i ).setBounds( 120, 20 + slim * i, 100, 40 );
                    add( Buttons.get( i ) );
                }
            }
        }catch (IOException e){e.printStackTrace();}

    }
}
