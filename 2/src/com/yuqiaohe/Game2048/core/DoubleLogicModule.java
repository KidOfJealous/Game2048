package com.yuqiaohe.Game2048.core;


public class DoubleLogicModule extends LogicModule {
    private ImageBlock[][] imageBlocks2;
    private int[][] value2 = new int[4][4];
    private int[][][] termination2 = new int[4][4][2];
    private int OpScore;
    private int[] end;
    private int[] conceded;

    public void concede()
    {
        Opp.sendOne( 0 );
    }
    public DoubleLogicModule(ImageBlock[][] im,ImageBlock[][]im2)
    {
        super(im);
        imageBlocks2=im2;
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
                imageBlocks2[i][j]=new ImageBlock( 0,0,0,0 );
        end = new int[2];
        conceded = new int[2];
        setAllBlocks();
    }
    public int Receive()
    {
        int s,who;
        int[] rec;
        rec = Opp.receive();
        who = rec[0];
        conceded[who]=rec[1];
        end[who]=rec[2];
        if(who==0)score=rec[3];
        else OpScore=rec[3];
        int[][] temp = (who==0)?value:value2;
        int[][][] tmp = (who==0)?termination:termination2;
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j) {
                s = i * 4 + j;
                temp[i][j] = rec[s + 4];
                tmp[i][j][0] = rec[s + 20];
                tmp[i][j][1] = rec[s + 36];
            }
        if(conceded[1]==1||conceded[0]==1)end[0]=end[1]=1;
        return who;
    }
    public int getOpScore() {
        return OpScore;
    }
    public void MoveLeftBlocks()
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
    public void MoveRightBlocks()
    {
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                int x = termination2[i][j][1]*10;
                int y = termination2[i][j][0]*10;
                if(x==0&&y==0)continue;
                imageBlocks2[i][j].move(x,y);
                imageBlocks2[i][j].setSize(64);
            }
    }
    @Override
    public void setAllBlocks()
    {
        setRightBlocks();
        setLeftBlocks();
    }
    public void setLeftBlocks()
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
    public int[] checkPosition()
    {
        int[] pos = new int[4];
        pos[0]=end[0];
        pos[1]=end[1];
        pos[2]=conceded[0];
        pos[3]=conceded[1];
        return pos;
    }
    public void setRightBlocks()
    {
        for(int i=0;i<4;++i)
            for(int j =0;j<4;++j)
            {
                imageBlocks2[i][j].setValue( value2[i][j] );
                imageBlocks2[i][j].setX(390+80*j );
                imageBlocks2[i][j].setY(100+80*i );
                imageBlocks2[i][j].setSize( 72 );
            }
    }
    @Override
    public boolean checked()
    {
        return end[0]==1;
    }
    public void send(int s)
    {
        if(end[0]==1)Opp.sendOne( 1 );
        Opp.sendOne(s);
    }

}
