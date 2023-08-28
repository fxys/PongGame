package fxys;

public class Ball {
    public int x;
    public int y;
    private double dx;
    private double dy;
    private double speedX;
    private double speedY;
    private int b1, b2;
    private final int cx, cy;
    public boolean stopped = false;

    public Ball(int x, int y, double sx, double sy, int cx, int cy) {
        this.x = x;
        this.y = y;
        this.dx = x;
        this.dy = y;
        this.speedX = sx;
        this.speedY = sy;
        this.cx = cx;
        this.cy = cy;
    }

    public void accelerate() {
        this.speedX *= 1.02;
        this.speedY *= 1.02;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setBounds(int b1, int b2) {
        this.b1 = b1;
        this.b2 = b2;
    }

    public void update() {
        this.dx += this.speedX;
        this.dy += this.speedY;

        setLocation((int)this.dx, (int)this.dy);

        if(this.x <= 9) {
            this.x = 10;
            this.speedX *= -1;
        }
        if(this.y <= 9) {
            this.y = 10;
            this.speedY *= -1;
        }
        if(this.x >= cx - 9) {
            this.x = cx - 10;
            this.speedX *= -1;
        }
        if(this.y >= cy - 49 && y <= cy - 40 && this.x >= b1 && this.x <= b2) {
            this.y = cy - 50;
            this.speedY *= -1;
            accelerate();
        }
        if(this.y >= cy - 9) {
            this.y = cy - 10;
            this.speedY = 0;
            this.speedX = 0;
            stopped = true;
        }
    }
}
