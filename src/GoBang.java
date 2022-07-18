import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
public  class GoBang extends JFrame implements WindowListener,MouseListener,Runnable {
    int x = 0;
    int y = 0;
    int flag1 = 0;//棋子边框标记
    int flag2 = 0;//游戏时间标记
    int flag3 = 0;//认输暂停后点击棋盘棋子边框标记
    int flag4 = 0;//线程暂停标记
    boolean canPlay = true;//是否游戏中标记
    int[][] date = new int[23][23];
    ArrayList<Position> PositionsList = new ArrayList<>();
    boolean isBlack = true;
    String message = "黑方先行";
    String theTime;
    int maxTime = 0;
    Thread t = new Thread(this);
    int blockTime, whiteTime = 0;
    String blockMessage = "无限制";
    String whiteMessage = "无限制";
    public GoBang() {
        setLayout(null);
        setBounds(300, 50, 890, 750);
        setTitle("五子棋");
        getContentPane().setBackground(Color.lightGray);
        JButton a1 = new JButton("重新开始");
        a1.setBounds(690, 50, 100, 50);
        JButton a2 = new JButton("游戏设置");
        a2.setBounds(690, 120, 100, 50);
        JButton a3 = new JButton("悔棋");
        a3.setBounds(690, 190, 100, 50);
        JButton a4 = new JButton("认输");
        a4.setBounds(690, 260, 100, 50);
        JButton a5 = new JButton("暂停游戏");
        a5.setBounds(690, 330, 100, 50);
        JButton a6 = new JButton("继续游戏");
        a6.setBounds(690, 400, 100, 50);
        JButton a7 = new JButton("退出");
        a7.setBounds(690, 470, 100, 50);
        a1.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "是否重新开始游戏?", "温馨提示", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                for (int[] ints : date) {
                    Arrays.fill(ints, 0);
                }
                flag1++;
                flag2 = 0;
                flag3 = 0;
                message = "黑方先行";
                canPlay = true;
                isBlack = true;
                blockTime = maxTime;
                whiteTime = maxTime;
                blockMessage = "无限制";
                whiteMessage = "无限制";
                this.repaint();
            }
        });
        a2.addActionListener(e -> {
            String[] time = {"10", "15", "20", "25", "30", "35", "40"};
            theTime = (String) JOptionPane.showInputDialog(null, "请选择最大游戏时间(单位/分)", "时间选择", JOptionPane.INFORMATION_MESSAGE, null, time, time[0]);
            maxTime = Integer.parseInt(theTime) * 60;
            if (maxTime > 0) {
                int result = JOptionPane.showConfirmDialog(this, "设置完成,是否重新开始游戏?");
                if (result == 0) {
                    for (int[] ints : date) {
                        Arrays.fill(ints, 0);
                    }
                    flag2++;
                    flag1++;
                    message = "黑方先行";
                } else {
                    flag1++;
                }
                canPlay = true;
                isBlack = true;
                blockTime = maxTime;
                whiteTime = maxTime;
                blockMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                whiteMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                this.repaint();
            }
        });
        a3.addActionListener(e -> {
            if (canPlay) {
                //遍历棋盘上是否有棋子
                int z = 0;
                for (int[] ints : date) {
                    for (int anInt : ints) {
                        if (anInt != 0) {
                            z++;
                        }
                    }
                }
                //判断是否有棋子
                if (z != 0) {
                    int result = JOptionPane.showConfirmDialog(this, "确认要悔棋吗？");
                    if (result == 0) {
                        flag1++;
                        Position l;
                        l = this.PositionsList.remove(this.PositionsList.size() - 1);
                        this.date[l.listX][l.listY] = 0;
                        if (isBlack) {
                            message = "轮到白方";
                            isBlack = false;
                        } else {
                            message = "轮到黑方";
                            isBlack = true;
                            repaint();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "棋盘上无棋子");
                }
            } else {
                JOptionPane.showMessageDialog(this, "请先开始游戏");
            }
        });
        a4.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(GoBang.this, "是否认输？", "输棋不输品，赢棋不赢人", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                canPlay = false;
                flag3++;
                if (isBlack) {
                    JOptionPane.showMessageDialog(GoBang.this, "黑方已经认输,游戏结束！");
                } else {
                    JOptionPane.showMessageDialog(GoBang.this, "白方已经认输,游戏结束！");
                }
            }
        });
        a5.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(GoBang.this, "是否暂停游戏？", "温馨提示", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                flag3++;
                flag4++;
                canPlay = false;
                message="游戏已暂停";
            }
        });
        a6.addActionListener(e -> {
            if (canPlay) {
                JOptionPane.showConfirmDialog(this,"您已正在游戏中了","温馨提示",JOptionPane.YES_NO_OPTION);
            } else {
                int continue1 = JOptionPane.showConfirmDialog(this, "是否继续游戏？", "温馨提示", JOptionPane.YES_NO_OPTION);
                if (continue1 == 0) {
                    flag3 = 0;
                    flag4 = 0;
                    canPlay = true;
                    if (isBlack) {
                        message = "轮到黑方";
                    } else {
                        message = "轮到白方";
                    }
                }
            }
        });
        a7.addActionListener(e -> {
            int flag = JOptionPane.showConfirmDialog(GoBang.this, "是否真的要退出?", "温馨提示", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (flag == JOptionPane.YES_OPTION)
                System.exit(0);
        });
        a1.setFocusPainted(false);
        a2.setFocusPainted(false);
        a3.setFocusPainted(false);
        a4.setFocusPainted(false);
        a5.setFocusPainted(false);
        a6.setFocusPainted(false);
        a7.setFocusPainted(false);
        add(a1);
        add(a2);
        add(a3);
        add(a4);
        add(a5);
        add(a6);
        add(a7);
        addWindowListener(this);
        addMouseListener(this);
        setVisible(true);
        t.start();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
    public static class Position {
        int listX;
        int listY;
        public Position() {
        }
        public Position(int listY, int listX) {
            this.listX = listY;
            this.listY = listX;
        }
    }
    @Override
    public void paint(Graphics g) {
        int w = Toolkit.getDefaultToolkit().getScreenSize().width;
        int h = Toolkit.getDefaultToolkit().getScreenSize().height;
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        super.paint(g2);
        g2.setColor(Color.black);
        g2.setFont(new Font("宋体", Font.BOLD, 20));
        g2.drawString("游戏信息：", 670, 600);
        g2.drawString("黑方剩余时间：" + blockMessage, 50, 700);
        g2.drawString("白方剩余时间：" + whiteMessage, 450, 700);
        repaint();
        for (int i = 0; i < 22; i++)
            g2.drawLine(45, 15 + 30 * i, 645, 15 + 30 * i);
        for (int i = 1; i < 22; i++)
            g2.drawLine(15 + 30 * i, 45, 15 + 30 * i, 645);
        g2.fillOval(130, 130, 10, 10);
        g2.fillOval(550, 130, 10, 10);
        g2.fillOval(130, 550, 10, 10);
        g2.fillOval(550, 550, 10, 10);
        g2.fillOval(340, 340, 10, 10);
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawRect(660, 580, 215, 30);//游戏信息框
        g2.drawRect(40, 680, 230, 30);//黑方时间框
        g2.drawRect(440, 680, 230, 30);//白方时间框
        g2.setColor(Color.YELLOW);
        g2.drawString("" + message, 770, 600);
        for (int i = 0; i < date.length; i++)
            for (int j = 0; j < date[i].length; j++) {
                if (date[j][i] == 1) {
                    g2.setColor(Color.BLACK);
                    g2.fillOval(i * 30, j * 30, 30, 30);
                } else if (date[j][i] == 2) {
                    g2.setColor(Color.white);
                    g2.fillOval(i * 30, j * 30, 30, 30);
                }
            }
        for (int i = 0; i < PositionsList.size(); i++) {
            if (flag1 == 0) {
                if (i == PositionsList.size() - 1) {
                    g2.setColor(Color.yellow);
                    g2.drawRect(x * 30 - 1, y * 30 - 1, 33, 33);
                    repaint();
                }
            }
        }
        g.drawImage(bi, 0, 0, this);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if (flag3 == 0) {
            flag1 = 0;
            if (canPlay) {
                x = e.getX();
                y = e.getY();
                if (x >= 45 && x <= 645 && y >= 45 && y <= 645) {
                    x = x / 30;
                    y = y / 30;
                    System.out.println("x:" + x + ",y:" + y);
                    if (date[y][x] == 0) {
                        if (isBlack) {
                            date[y][x] = 1;
                            isBlack = false;
                            message = "轮到白方";
                        } else {
                            date[y][x] = 2;
                            isBlack = true;
                            message = "轮到黑方";
                        }
                    } else {
                        JOptionPane.showConfirmDialog(this, "有棋子了!");
                    }
                    boolean winFlag = this.checkWin();
                    int sum = 0;
                    for (int[] ints : date)
                        for (int anInt : ints) {
                            sum += anInt;
                        }
                    if (winFlag) {
                        JOptionPane.showConfirmDialog(this, "游戏结束：" + (date[y][x] == 1 ? "黑方" : "白方") + "胜利！");
                        message="游戏结束";
                        canPlay = false;
                    } else if (sum == 661) {
                        JOptionPane.showConfirmDialog(this, "游戏结束：平局！");
                        canPlay = false;
                    }
                    this.PositionsList.add(new Position(y, x));
                    System.out.println("************************");
                    for (int[] datum : date) {
                        for (int i : datum) System.out.print(i);
                        System.out.println();
                    }
                    repaint();
                }
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
    @Override
    public void windowOpened(WindowEvent e) {
    }
    @Override
    public void windowClosing(WindowEvent e) {
        int flag = JOptionPane.showConfirmDialog(this, "是否真的要退出？", "温馨提示", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (flag == JOptionPane.YES_OPTION)
            System.exit(0);
    }
    @Override
    public void windowClosed(WindowEvent e) {
    }
    @Override
    public void windowIconified(WindowEvent e) {
    }
    @Override
    public void windowDeiconified(WindowEvent e) {
    }
    @Override
    public void windowActivated(WindowEvent e) {
    }
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    private boolean checkWin() {
        boolean flag = false;
        int count = 1;
        int color = date[y][x];
        int i = 1;
        while (color == date[y][x + i]) {
            count++;
            i++;
        }
        i = 1;
        while (color == date[y][x - i]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
        }
        int i2 = 1;
        int count2 = 1;
        while (color == date[y + i2][x]) {
            count2++;
            i2++;
        }
        i2 = 1;
        while (color == date[y - i2][x]) {
            count2++;
            i2++;
        }
        if (count2 >= 5) {
            flag = true;
        }
        int i3 = 1;
        int count3 = 1;
        while (color == date[y + i3][x - i3]) {
            count3++;
            i3++;
        }
        i3 = 1;
        while (color == date[y - i3][x + i3]) {
            count3++;
            i3++;
        }
        if (count3 >= 5) {
            flag = true;
        }
        int i4 = 1;
        int count4 = 1;
        while (color == date[y + i4][x +i4]) {
            count4++;
            i4++;
        }
        i4 = 1;
        while (color == date[y -i4][x - i4]) {
            count4++;
            i4++;
        }
        if (count4 >= 5) {
            flag = true;
        }
        return flag;
    }
    @Override
    public void run() {
            while (true) {
            if (flag4 == 0) {
                if (maxTime > 0) {
                    if (isBlack) {
                        blockTime--;
                        if ((blockTime / 60 - blockTime / 3600 * 60) == 0) {
                            if ((blockTime - blockTime / 60 * 60) == 1) {
                                JOptionPane.showConfirmDialog(this, "你已超时！游戏结束：白方胜利！");
                                canPlay = false;
                                maxTime = 0;
                            }
                        }
                    } else {
                        whiteTime--;
                    }
                    if ((whiteTime / 60 - whiteTime / 3600 * 60) == 0) {
                        if ((whiteTime - whiteTime / 60 * 60) == 1) {
                            JOptionPane.showConfirmDialog(this, "你已超时！游戏结束：黑方胜利！");
                            canPlay = false;
                            maxTime = 0;
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (flag2 != 0) {
                    blockMessage = blockTime / 3600 + ":" + (blockTime / 60 - blockTime / 3600 * 60) + ":" + (blockTime - blockTime / 60 * 60);
                    whiteMessage = whiteTime / 3600 + ":" + (whiteTime / 60 - whiteTime / 3600 * 60) + ":" + (whiteTime - whiteTime / 60 * 60);
                }
                repaint();
            }
        }
    }
    public static void main(String[] args) {
        new GoBang();
    }
}


