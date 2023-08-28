package fxys;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;

public class Window implements KeyListener {
    private static final JFrame frame = new JFrame();
    private static final Panel pane = new Panel();
    private static Ball ball;
    private static final int cx = 600;
    private static final int cy = 800;
    private static int platformw = 200;
    private static int platform = cx / 2 - platformw / 2;
    private static final Random r = new Random();
    private static final Font font = new Font("Century Gothic", Font.PLAIN, 22);
    private static final JLabel title = label("Pong", 50, 50);
    private static State s = State.MENU;

    public Window() {
        ball = new Ball(cx / 2, cy / 2, r.nextInt(2) == 0 ? 3 : -3, r.nextInt(2) == 0 ? 3 : -3, cx - 30, cy - 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(cx + 16, cy + 16);
        frame.setTitle("Pong");
        frame.setResizable(false);
        frame.setContentPane(pane);
        pane.setLayout(null);
        frame.add(title);
        frame.add(button("Easy", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                platformw = 200;
                refresh();
            }
        }, cx / 3 / 2 - 60, cy - 100, 120, 40));
        frame.add(button("Medium", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                platformw = 100;
                refresh();
            }
        }, cx / 3 / 2 + cx / 3 - 60, cy - 100, 120, 40));
        frame.add(button("Hard", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                platformw = 50;
                refresh();
            }
        }, cx / 3 / 2 + cx / 3 * 2 - 60, cy - 100, 120, 40));
        frame.setVisible(true);
        frame.addKeyListener(this);
        Thread t = new Thread(() -> {
            try {
                while(true) {
                    update();
                    Thread.sleep(15);
                }
            } catch (InterruptedException v) {
                v.printStackTrace();
            }
        });
        t.start();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                t.interrupt();
                e.getWindow().dispose();
            }
        });
    }

    public static void update() {
        pane.repaint();
    }

    private static void refresh() {
        ball = new Ball(cx / 2, cy / 2, r.nextInt(2) == 0 ? 3 : -3, r.nextInt(2) == 0 ? 3 : -3, cx - 30, cy - 50);
        ball.stopped = false;
        platform = cx / 2 - platformw / 2;
        title.setText(null);
        s = State.GAME;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if(s == State.GAME) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && platform < cx - 40 - platformw) platform += 10;
            if (e.getKeyCode() == KeyEvent.VK_LEFT && platform > 10) platform -= 10;
            ball.setBounds(platform, platform + platformw);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    static class Panel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRect(0, 0, cx + 20, cy + 20);

            if(s == State.GAME) {
                g2d.setColor(new Color(30, 30, 30));
                g2d.fillRect(15, 15, cx - 30, cy - 50);
                ball.update();
                if(ball.stopped) {
                    title.setText("Pong");
                    s = State.MENU;
                } else {
                    g2d.setColor(new Color(150, 150, 150));
                    g2d.fillOval(15 + ball.x - 10, 15 + ball.y - 10, 20, 20);
                    g2d.fillRoundRect(15 + platform, cy - 75, platformw, 10, 8, 8);
                }
            }
        }
    }

    public static JLabel label(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font.deriveFont((float) (font.getSize() * 1.5)));
        lbl.setBounds(x, y, 500, 50);
        lbl.setForeground(Color.white);
        return lbl;
    }

    public static JLabel button(String t, MouseAdapter ma, int x, int y, int w, int h) {
        final Color[] bg = { new Color(84, 84, 84, 255) };
        final Color[] tx = { new Color(255, 255, 255, 255) };
        final Color highlight = new Color(84, 84, 84, 100);
        final JLabel btn = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                if(s == State.MENU) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(bg[0]);
                    g2d.fillRoundRect(0, 0, w, h, 12, 12);
                    FontMetrics metrics = g.getFontMetrics(font);
                    int x = (getWidth() - metrics.stringWidth(t)) / 2;
                    int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
                    g2d.setFont(font);
                    g2d.setColor(tx[0]);
                    g2d.drawString(t, x, y);
                }
            }
        };
        btn.setBounds(x, y, w, h);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(ma);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                bg[0] = highlight;
                tx[0] = new Color(194, 194, 194, 255);
                btn.repaint();
            }

            public void mouseExited(MouseEvent e) {
                bg[0] = new Color(84, 84, 84, 255);
                tx[0] = new Color(255, 255, 255, 255);
                btn.repaint();
            }
        });
        return btn;
    }

    enum State {
        GAME,
        MENU
    }
}
