import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SudokuGUI {

    private final static int[][] START_BOARD = {
            {7, 8, 0, 4, 0, 0, 1, 2, 0},
            {6, 0, 0, 0, 7, 5, 0, 0, 9},
            {0, 0, 0, 6, 0, 1, 0, 7, 8},
            {0, 0, 7, 0, 4, 0, 2, 6, 0},
            {0, 0, 1, 0, 5, 0, 9, 3, 0},
            {9, 0, 4, 0, 6, 0, 0, 0, 5},
            {0, 7, 0, 3, 0, 0, 0, 1, 2},
            {1, 2, 0, 0, 0, 7, 4, 0, 0},
            {0, 4, 9, 2, 0, 6, 0, 0, 7}
    };

    private SudokuSolver solver;
    private JPanel pane;
    private Timer timer;
    private Color color;

    private Image backgroundImage;
    private Image invalidSign;
    private Image validSign;

    private List<int[][]> rect;

    private static final int RECT_SIZE = 50;
    public static final int GRID_SIZE = 9;
    private int counter;
    private int rectX;
    private int rectY;
    private int checkMarkSwitch; // 1 = green, 0 = red, -1 = neither
    private boolean solutionSwitch;
    private boolean keyPressSwitch;


    @SuppressWarnings("serial")
    private void createAndShowGUI() {
        //We create the JFrame
        JFrame frame = new JFrame(this.getClass().getSimpleName());
        frame.setFocusable(true);
        frame.setResizable(false);
        solver = new SudokuSolver(START_BOARD);
        rect = new ArrayList<>();
        rect = solver.returnList();
        solutionSwitch = false;
        keyPressSwitch = false;
        checkMarkSwitch = -1;
        rectX = -100;
        rectY = -100;
        counter = 0;


        try {
            backgroundImage = ImageIO.read(new File("img/grid.jpg"));
            invalidSign = ImageIO.read(new File("img/cross.png"));
            validSign = ImageIO.read(new File("img/check.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Creates our JPanel that's going to draw every rectangle
        pane = new JPanel() {
            //Specifies the size of our JPanel
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(450, 520);
            }

            // Paints the background image, adds numbers from the current grid and add a rectangle of selected color
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(backgroundImage, 0, 0, this);
                g2d.setStroke(new BasicStroke(1));
                g2d.setFont(new Font("Arial", Font.PLAIN, 16));

                for (int i = 0; i < GRID_SIZE; i++) {
                    for (int j = 0; j < GRID_SIZE; j++) {
                        g.setColor(Color.BLACK);
                        // Print all numbers except zeros
                        if (rect.get(counter)[i][j] != 0) {
                            String text = Integer.toString(rect.get(counter)[i][j]);
                            g.drawString(text, RECT_SIZE * j + 20, RECT_SIZE * i + 30); //magic numbers to center number

                            if (solutionSwitch) {
                                if (START_BOARD[i][j] == 0 && rect.get(counter)[i][j] != 0) {

                                    color = Color.GREEN;
                                    if (counter < rect.size() - 1) { //Check that we are not at the end of the list
                                        if (rect.get(counter + 1)[i][j] == 0) {
                                            color = Color.RED;
                                        }
                                    }
                                    rectX = j;
                                    rectY = i;
                                }
                            }
                        }
                    }
                }
                g2d.setStroke(new BasicStroke(4));
                g2d.setColor(color);
                g2d.drawRect(RECT_SIZE * rectX, RECT_SIZE * rectY + 1, RECT_SIZE, RECT_SIZE);
                if (checkMarkSwitch == 0) {
                    g2d.drawImage(invalidSign, 20, 463, this);
                }
                if (checkMarkSwitch == 1) {
                    g2d.drawImage(validSign, 20, 463, this);
                }


            }
        };

        JButton solve = new JButton("Solve");
        JButton restart = new JButton("Restart");
        pane.setLayout(null);
        solve.setBounds(150, 475, 70, 20);
        pane.add(solve);
        restart.setBounds(230, 475, 70, 20);
        pane.add(restart);

        solve.addActionListener(e -> {
            // This starts the Timer
            timer = new Timer(300, listener);
            timer.setInitialDelay(1000);
            timer.start();
            solutionSwitch = true;
        });

        restart.addActionListener(e -> {
            frame.dispose();
            createAndShowGUI();
        });


        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (keyPressSwitch) {
                    int num = e.getKeyChar() - '0';
                    if(num > 0 && num <= 9 &&
                            solver.valid(rect.get(counter), num, rectY, rectX) &&
                            START_BOARD[rectY][rectX] == 0) {
                        rect.get(counter)[rectY][rectX] = num;
                        checkMarkSwitch = 1;

                    } else if((e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                            e.getKeyCode() == KeyEvent.VK_DELETE) &&
                            START_BOARD[rectY][rectX] == 0) { // If escape is pressed, delete selected number
                        rect.get(counter)[rectY][rectX] = 0;
                        checkMarkSwitch = -1;
                    } else {
                        checkMarkSwitch = 0;
                    }
                    rectX = -100;
                    rectY = -100;
                    pane.repaint();
                    keyPressSwitch = false;
                }

            }
        });

        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getY() < 450) {
                    rectX = e.getX() / 50;
                    rectY = e.getY() / 50;
                    color = Color.ORANGE;
                    keyPressSwitch = true;
                    pane.repaint();
                }

            }
        });

        // Add everything to the frame
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // This will be executed every time the Timer is fired
    private ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            checkMarkSwitch = -1;
            if (counter < rect.size() - 1) {
                counter++;
            } else {
                timer.stop();
                solutionSwitch = false;
                rectX = -100;
                rectY = -100;
            }
            pane.repaint();
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SudokuGUI()::createAndShowGUI);
    }

}
