package com.yuqiaohe.Game2048.core;
import java.awt.event.*;
import java.util.*;
public class LogicModule {
    protected Map<Integer,MyKeyEvent> keyMap=new HashMap<>();
    protected int[][] value = new int[4][4];
    protected int[][][] termination = new int[4][4][2];
    protected boolean flag;
    private int[][] randint = new int[16][2];
    protected ImageBlock[][] imageBlocks;
    protected int score;
    private void addKeyEvent(){
        keyMap.put( KeyEvent.VK_DOWN, new MyKeyEvent() {
            @Override
            public void MyKeyPressed() {
                flag=false;
                for(int j=0;j<4;++j)
                    for(int i =3;i>=0;--i)
                        for(int k=i-1;k>=0;k--)
                            if(value[k][j]>0)
                            {
                                if (value[i][j]<=0)
                                {
                                    value[i][j]=value[k][j];
                                    value[k][j]=0;
                                    flag = true;
                                    termination[k][j][0]=i-k;
                                    i++;
                                }
                                else if (value[i][j]==value[k][j])
                                {
                                    value[i][j]*=2;
                                    value[k][j]=0;
                                    score+=value[i][j];
                                    flag = true;
                                    termination[k][j][0]=i-k;
                                }
                                break;
                            }
                if(flag) randCreate(1);
            }
        });
        keyMap.put(KeyEvent.VK_UP, new MyKeyEvent() {
            @Override
            public void MyKeyPressed() {
                flag=false;
                for(int j=0;j<4;++j)
                    for(int i =0;i<4;++i)
                        for(int k=i+1;k<4;++k)
                            if(value[k][j]>0)
                            {
                                if(value[i][j]<=0)
                                {
                                    value[i][j]=value[k][j];
                                    value[k][j]=0;
                                    flag=true;
                                    termination[k][j][0]=i-k;
                                    i--;
                                }
                                else if(value[i][j]==value[k][j])
                                {
                                    value[i][j]*=2;
                                    value[k][j]=0;
                                    score+=value[i][j];
                                    flag=true;
                                    termination[k][j][0]=i-k;
                                }
                                break;
                            }
                if(flag) randCreate(1);
            }
        });
        keyMap.put(KeyEvent.VK_LEFT, new MyKeyEvent(){
            @Override
            public void MyKeyPressed() {
                flag=false;
                for(int i=0;i<4;++i)
                    for(int j =0;j<4;++j)
                        for(int k = j+1;k<4;++k)
                            if(value[i][k]>0)
                            {
                                if(value[i][j]<=0)
                                {
                                    value[i][j]=value[i][k];
                                    value[i][k]=0;
                                    flag=true;
                                    termination[i][k][1]=j-k;
                                    j--;
                                }
                                else if(value[i][j]==value[i][k])
                                {
                                    value[i][j]*=2;
                                    value[i][k]=0;
                                    score+=value[i][j];
                                    flag=true;
                                    termination[i][k][1]=j-k;
                                }
                                break;
                            }
                if(flag) randCreate(1);
            }
        });
        keyMap.put(KeyEvent.VK_RIGHT, new MyKeyEvent(){
            @Override
            public void MyKeyPressed()  {
                flag=false;
                for(int i=0;i<4;++i)
                    for(int j =3;j>=0;--j)
                        for(int k=j-1;k>=0;k--)
                            if(value[i][k]>0)
                            {
                                if(value[i][j]<=0)
                                {
                                    value[i][j]=value[i][k];
                                    value[i][k]=0;
                                    flag=true;
                                    termination[i][k][1]=j-k;
                                    j++;
                                }
                                else if(value[i][j]==value[i][k])
                                {
                                    value[i][j]*=2;
                                    value[i][k]=0;
                                    score+=value[i][j];
                                    flag=true;
                                    termination[i][k][1]=j-k;
                                }
                                break;
                            }
                if(flag) randCreate(1);
            }
        });
    }
    public boolean move(int e)
    {
        flag = false;
        MyKeyEvent k = keyMap.get(e);
        if(k!=null)k.MyKeyPressed();
        return flag;
    }
    public int getScore() {
        return score;
    }
    public LogicModule(ImageBlock[][] b)
    {
        imageBlocks = b;
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
                imageBlocks[i][j]=new ImageBlock( 0,0,0,0 );
        addKeyEvent();
    }
    private void randCreate(int num) {
        Random rand = new Random();
        int n;
        int l;
        for (int i = 0; i < num; ++i) {
            l=0;
            for(int j =0;j<4;++j)
                for(int k=0;k<4;++k)
                    if(value[j][k]<=0)
                    {
                        randint[l][0]=j;
                        randint[l++][1]=k;
                    }
            if(l!=0)
            {
                n=rand.nextInt(l);
                int j = randint[n][0];
                int k = randint[n][1];
                value[j][k]=Math.random()>0.1?2:4;
            }
        }
    }
    public boolean checked()
    {
        for(int i =0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                if(value[i][j]<=0)return false;
                if(i+1<4&&value[i+1][j]==value[i][j])return false;
                if(i-1>=0&&value[i-1][j]==value[i][j])return false;
                if(j+1<4&&value[i][j+1]==value[i][j])return false;
                if(j-1>=0&&value[i][j-1]==value[i][j])return false;
            }
            return true;
    }
    public void setAllBlocks()
    {
        for(int i=0;i<4;++i)
        for(int j =0;j<4;++j)
        {
            imageBlocks[i][j].setValue( value[i][j] );
            imageBlocks[i][j].setX(50+80*j );
            imageBlocks[i][j].setY(100+80*i );
            imageBlocks[i][j].setSize( 72 );
        }
    }
    public void Restart()
    {
        for(int i=0;i<4;++i)
            for(int j =0;j<4;++j)
            {
                value[i][j]=0;
            }
            randCreate( 2 );
            setAllBlocks();
            score=0;
    }
    public void checkMove()
    {
        for (int i = 0; i < 4; ++i)
            for(int j=0;j<4;++j){
                termination[i][j][1] = 0;
                termination[i][j][0] = 0;
            }
    }
    public void MoveAllBlocks()
    {
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                int x = termination[i][j][1]*10;
                int y = termination[i][j][0]*10;
                if(x==0&&y==0)continue;
                imageBlocks[i][j].move(x,y);
                imageBlocks[i][j].setSize(64);
            }
    }

}
