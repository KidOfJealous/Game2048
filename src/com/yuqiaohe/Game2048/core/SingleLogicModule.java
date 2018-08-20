package com.yuqiaohe.Game2048.core;

import java.net.InetAddress;
import java.sql.*;

public class SingleLogicModule extends LogicModule{
    private int HighestScore;
    private int Highestdul;
    private int scoredul;
    private String player;
    private Connection con;
    private Statement stmt;
    private boolean DataBase_Active;
    private int[][] valuedul = new int[4][4];
    private int[][][] terminationdul = new int[4][4][2];

    public int getHighestScore() {
        return HighestScore;
    }
    public void reflash(){if(HighestScore<score)HighestScore=score;}
    public void clear(){HighestScore=0;}
    public SingleLogicModule(ImageBlock[][] i){
        super(i);
        player = "default0";
        try{player=InetAddress.getLocalHost().getHostName();}catch (Exception e){e.printStackTrace();}
    }
    public void Dulto()
    {
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                //value[i][j] = valuedul[i][j];
                termination[i][j][0]=terminationdul[i][j][0];
                termination[i][j][1]=terminationdul[i][j][1];
            }
    }
    public void toDul()
    {
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                valuedul[i][j] = value[i][j];
                terminationdul[i][j][0]=termination[i][j][0];
                terminationdul[i][j][1]=termination[i][j][1];
            }
        Highestdul=HighestScore;
        scoredul=score;
    }
    public void load()
    {
        if(!DataBase_Active)
        {
            Restart();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Game2048(player_name VARCHAR(40) NOT NULL,");
        for (int i = 0; i < 16; ++i) {
            sb.append("value_");
            sb.append(i);
            sb.append(" INT DEFAULT 0,");
        }
        sb.append("HighestScore INT DEFAULT 0,");
        sb.append("Score INT DEFAULT 0,");
        sb.append("PRIMARY KEY  (player_name)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        try{
        stmt.executeUpdate(sb.toString());
        stmt.executeUpdate("INSERT IGNORE INTO Game2048(player_name) VALUES('"+player+"');");
        ResultSet rs = stmt.executeQuery("SELECT * FROM Game2048 WHERE player_name='"+player+"';");
        int x=0;
        while (rs.next()) {
            for (int i = 0; i < 4; ++i)
                for(int j=0;j<4;++j){
                    value[i][j]=rs.getInt(i*4+j+2);
                    if(value[i][j]>131072)value[i][j]=0;//simple security check
                    if(value[i][j]!=0)x++;
                }
            if(x<2) Restart();
            HighestScore=rs.getInt("HighestScore");
            score=rs.getInt("Score");
            if(score>3932100)score=0;//simple security check
        }
        toDul();
        rs.close();
        }catch (SQLException e){e.printStackTrace();}
    }
    public void store()
    {
        if(!DataBase_Active)return;
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Game2048 SET ");
        for(int i =0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                sb.append("value_");
                sb.append(i*4+j);
                sb.append(" =");
                sb.append(valuedul[i][j]);
                sb.append(",");
            }
        sb.append("HighestScore =");
        sb.append(Highestdul);
        sb.append(",Score =");
        sb.append(scoredul);
        sb.append(" WHERE player_name='" + player + "'");
        try{
            stmt.executeUpdate(sb.toString());
        }catch (SQLException e){e.printStackTrace();}
    }
    public boolean LogDataBase()
    {
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/YUQIAO?useUnicode=true&characterEncoding=gbk&autoReconnect=true&useSSL=false" ;
        String username = "root" ;
        String password = "heyuqi@o99MS" ;
        Connection con = DriverManager.getConnection(url, username, password);
        stmt = con.createStatement();
        }catch (Exception e){e.printStackTrace();return DataBase_Active=false;}
        return DataBase_Active=true;
    }
    public boolean quitDataBase()
    {
        if(!DataBase_Active)return true;
        try{
            if(con!=null)con.close();
            if(stmt!=null)stmt.close();
        }catch (SQLException e){e.printStackTrace();return false;}
        return true;
    }
    @Override
    public void Restart()
    {
        super.Restart();
        toDul();
        store();
    }
    @Override
    public boolean move(int e)
    {
        flag = false;
        MyKeyEvent k = keyMap.get(e);
        if(k!=null)k.MyKeyPressed();
        if(flag)
        {
            store();
            toDul();
        }
        else Dulto();
        return flag;
    }
    public void setBackBlocks()
    {
        for(int i=0;i<4;++i)
            for(int j =0;j<4;++j)
            {
                imageBlocks[i][j].setValue( value[i][j] );
                imageBlocks[i][j].setX(50+80*j+termination[i][j][1]*80 );
                imageBlocks[i][j].setY(100+80*i+termination[i][j][0]*80 );
                imageBlocks[i][j].setSize( 72 );
            }
    }
    public void MoveBackAllBlocks()
    {
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                int x = -termination[i][j][1]*10;
                int y = -termination[i][j][0]*10;
                if(x==0&&y==0)continue;
                imageBlocks[i][j].move(x,y);
                imageBlocks[i][j].setSize(64);
            }
    }

}
