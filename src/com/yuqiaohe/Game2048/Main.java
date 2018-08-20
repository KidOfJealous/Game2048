package com.yuqiaohe.Game2048;
import com.yuqiaohe.Game2048.model.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        frame.setTitle("2048 Game");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);}
}