import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class PaintBrush extends JFrame {
    private int selectedButton;
    private Color currColor = Color.BLACK;
    private ArrayList<Shape> shapes;

    class PaintPanel extends JPanel implements MouseListener, MouseMotionListener {
        private int x,y,new_x,new_y,orig_x,orig_y;
        private boolean dragged = false;
       private ArrayList<Point> points;
       private Shape selectedShape = null;

        public PaintPanel(){
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(currColor);

            for (Shape shape : shapes){
                g.setColor(shape.color);

                if (shape instanceof Rectangle)
                    g.fillRect(shape.x,shape.y,shape.width,shape.height);
                else if (shape instanceof Oval)
                    g.fillOval(shape.x,shape.y,shape.width,shape.height);
                else if (shape instanceof Line) {
                    g2.setStroke(new BasicStroke(2));

                    if (shape.mouse != null){
                        ArrayList<Point> points = shape.mouse;

                        for (int i = 0; i < points.size()-1; i++){
                            Point p1 = points.get(i);
                            Point p2 = points.get(i+1);

                            g.drawLine(p1.x,p1.y,p2.x,p2.y);
                        }
                    }
                }
            }

            g.setColor(currColor);

            if (selectedButton == 1)
                g.fillRect(Math.min(x, new_x), Math.min(y, new_y), Math.abs(new_x - x), Math.abs(new_y - y));
            else if (selectedButton == 2)
                g.fillOval(Math.min(x, new_x), Math.min(y, new_y), Math.abs(new_x - x), Math.abs(new_y - y));
            else if (selectedButton == 3) {
                g2.setStroke(new BasicStroke(2));

                if (points != null){
                    for (int i = 0; i < points.size()-1; i++){
                        Point p1 = points.get(i);
                        Point p2 = points.get(i+1);

                        g.drawLine(p1.x,p1.y,p2.x, p2.y);
                    }
                }
            }
            else if (selectedButton == 4 && dragged) {
                g.setColor(selectedShape.color);

                if (selectedShape instanceof Rectangle)
                    g.fillRect(selectedShape.x, selectedShape.y, selectedShape.width, selectedShape.height);
                else if (selectedShape instanceof Oval)
                    g.fillOval(selectedShape.x, selectedShape.y, selectedShape.width, selectedShape.height);
            }
        }


        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();

            if (selectedButton == 3)
                points = new ArrayList<Point>();
            else if (selectedButton == 4) {

                   for (Shape shape: shapes){
                       selectedShape = shape;
                       orig_x = selectedShape.x;
                       orig_y = selectedShape.y;

                       if ((shape instanceof Rectangle && x >= shape.x && x <= shape.x+shape.width && y >= shape.y && y <= shape.y+shape.height)
                               || (shape instanceof Oval && (Math.pow((double)(2*x-shape.width-2*orig_x)/shape.width,2) + Math.pow((double)(2*y-shape.height-2*orig_y)/shape.height,2) <= 1))){
                           dragged = true;
                           break;
                       }
                   }

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (selectedButton == 1)
                shapes.add(new Rectangle(Math.min(x, new_x), Math.min(y, new_y), Math.abs(x - new_x), Math.abs(y - new_y),currColor));
            else if (selectedButton == 2)
                shapes.add(new Oval(Math.min(x,new_x),Math.min(y,new_y),Math.abs(x-new_x),Math.abs(y-new_y),currColor));
            else if (selectedButton == 3)
                shapes.add(new Line(points,currColor));
            else if (selectedButton == 4) {
                dragged = false;
                shapes.remove(selectedShape);
                shapes.add(selectedShape);
                selectedShape = null;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {

            new_x = e.getX();
            new_y = e.getY();

            if(selectedButton == 3) {
                points.add(e.getPoint());
            }
            else if (selectedButton == 4 && dragged) {
                selectedShape.x = orig_x  + (new_x - x);
                selectedShape.y = orig_y  + (new_y - y);
            }
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {}
    }
    public PaintBrush(){
        shapes = new ArrayList<Shape>();

        setSize(new Dimension(800,550));
        setMinimumSize(getSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(11, 111, 190));
        setLayout(new BorderLayout(0,3));
        setTitle("Paint!! :D");

        JPanel paintPanel = new PaintPanel(); paintPanel.setBackground(Color.WHITE);
        JPanel top = new JPanel();
        JPanel colors = new JPanel(); colors.setBackground(Color.WHITE);
        JPanel buttons = new JPanel(); buttons.setBackground(Color.WHITE);

        JPanel blue = new JPanel();
        blue.setPreferredSize(new Dimension(75,40));
        blue.setBackground(new Color(15, 169, 204));
        blue.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currColor = new Color(15, 169, 204);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        JPanel red = new JPanel();
        red.setPreferredSize(new Dimension(75,40));
        red.setBackground(new Color(215, 24, 24));
        red.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currColor = new Color(215, 24, 24);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        JPanel green = new JPanel();
        green.setPreferredSize(new Dimension(75,40));
        green.setBackground(new Color(59, 211, 77));
        green.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currColor = new Color(59, 211, 77);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        JPanel yellow = new JPanel();
        yellow.setPreferredSize(new Dimension(75,40));
        yellow.setBackground(new Color(239, 207, 75));
        yellow.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currColor = new Color(239, 207, 75);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        JPanel orange = new JPanel();
        orange.setPreferredSize(new Dimension(75,40));
        orange.setBackground(new Color(245, 108, 29));
        orange.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currColor = new Color(245, 108, 29);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        JPanel purple = new JPanel();
        purple.setPreferredSize(new Dimension(75,40));
        purple.setBackground(new Color(91, 33, 136));
        purple.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currColor = new Color(91, 33, 136);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        JPanel black = new JPanel();
        black.setPreferredSize(new Dimension(75,40));
        black.setBackground(Color.BLACK);
        black.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currColor = Color.BLACK;
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        colors.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        colors.add(blue);
        colors.add(red);
        colors.add(green);
        colors.add(yellow);
        colors.add(orange);
        colors.add(purple);
        colors.add(black);

        JButton dikdortgenCiz = new JButton("Dikdörtgen Çiz");
        dikdortgenCiz.addActionListener(e -> selectedButton = 1);

        JButton ovalCiz = new JButton("Oval Çiz");
        ovalCiz.addActionListener(e -> selectedButton = 2);

        JButton kalemleCiz = new JButton("Kalemle Çiz");
        kalemleCiz.addActionListener(e -> selectedButton = 3);

        JButton tasi = new JButton("Taşı");
        tasi.addActionListener(e -> selectedButton = 4);

        buttons.setLayout(new FlowLayout(FlowLayout.CENTER,15,10));
        buttons.add(dikdortgenCiz);
        buttons.add(ovalCiz);
        buttons.add(kalemleCiz);
        buttons.add(tasi);

        top.setLayout(new GridLayout(2,1));
        top.add(colors);
        top.add(buttons);

        add(top,BorderLayout.NORTH);
        add(paintPanel,BorderLayout.CENTER);

        setVisible(true);
    }

    class Shape{
        private int x;
        private int y;
        private int width;
        private int height;
        private Color color;
        private ArrayList<Point> mouse;

        public Shape(int x,int y,int width,int height,Color color){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
        }

        public Shape(ArrayList<Point> mouse,Color color) {
            this.mouse = mouse;
            this.color = color;
        }

        public String toString(){
            if (mouse == null) return x + " " + y + " " + width + " " + height + " " + color;
            else return mouse.toString() + " " + color;
        }
    }
    class Rectangle extends Shape{
        public Rectangle(int x,int y,int width,int height,Color color){
            super(x,y,width,height,color);
        }

    }

    class Oval extends Shape{
        public Oval(int x,int y,int width,int height,Color color){
            super(x,y,width,height,color);
        }

    }

    class Line extends Shape{
        public Line(ArrayList<Point> mouse, Color color){
            super(mouse,color);
        }

    }

    public static void main(String[] args) {
        new PaintBrush();
    }
}
