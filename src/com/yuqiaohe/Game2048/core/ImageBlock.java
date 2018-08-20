package com.yuqiaohe.Game2048.core;

public class ImageBlock {
    private int value;
    private int image;
    private int x;
    private int y;


    public void setSize(int size) {
        this.size = size;
    }
    private int size;
    public ImageBlock(int v,int x,int y,int s)
    {
        value=v;
        image=log();
        this.x=x;
        this.y=y;
        size = s;
    }
    public void move(int x,int y)
    {
        this.x+=x;
        this.y+=y;
    }
    public int log()
    {
        int v = value;
        int x = 0;
        while((v>>=1)>0)x++;
        return x;
    }
    public void setValue(int value) {
        this.value = value;
        image=log();
    }
    public int getImage(){return image;}
    public int getX() { return x; }
    public int getSize(){return size;}
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
}
