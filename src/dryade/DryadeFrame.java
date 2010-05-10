package dryade;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Field;

public class DryadeFrame extends JFrame {
    DryadeView view;
    Dryade dryade;

    public static void main(String[] args) {
        DryadeFrame frame = new DryadeFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    public DryadeFrame() {
        dryade = new Dryade();
        dryade.setup();

        setLayout(new BorderLayout());
        view = new DryadeView();
        add(view, BorderLayout.CENTER);
        JPanel controlsPanel = new JPanel();
        controlsPanel.add(new MetaField("prodText"));
        controlsPanel.add(new MetaSlider("stepSize"));
        controlsPanel.add(new MetaSlider("stepDecrease"));
        controlsPanel.add(new MetaSlider("angle"));
        controlsPanel.add(new ExportButton());
        controlsPanel.add(new ReloadButton());
        add(controlsPanel, BorderLayout.SOUTH);
//        Timer t = new Timer(500, new ReloadButton());
//        t.setRepeats(true);
//        t.start();
    }

    class ExportButton extends JButton implements ActionListener {
        ExportButton() {
            super("Export");
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            DryadeWriter writer = new DryadeWriter(new File("out/dryade.pdf"), dryade);
            try {
                writer.write();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    class MetaField extends JPanel implements DocumentListener {
        JLabel label;
        JTextField textField;
        String fieldName;

        MetaField(String fieldName) {
            this.fieldName = fieldName;
            label = new JLabel(fieldName);
            textField = new JTextField(10);
            textField.getDocument().addDocumentListener(this);
            textField.setText(getValue());
            setLayout(new FlowLayout(FlowLayout.LEFT));            
            add(label);
            add(textField);
        }

        public void insertUpdate(DocumentEvent e) {
            updateValueFromTextField();
        }

        public void removeUpdate(DocumentEvent e) {
            updateValueFromTextField();
        }

        public void changedUpdate(DocumentEvent e) {
            updateValueFromTextField();
        }


        String getValue() {
            try {
                Field field = dryade.getClass().getField(fieldName);
                return (String) field.get(dryade);

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        void setValue(String v) {
            try {
                Field field = dryade.getClass().getField(fieldName);
                field.set(dryade, v);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }




        private void updateValueFromTextField() {
            setValue(textField.getText());
            view.repaint();
        }
    }

    class MetaSlider extends JPanel implements ChangeListener {
        JLabel label;
        DraggableNumber draggable;
        String fieldName;

        MetaSlider(String fieldName) {
            this.fieldName = fieldName;
            label = new JLabel(fieldName);
            draggable = new DraggableNumber();
            draggable.addChangeListener(this);
            draggable.setValue(getValue());
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(label);
            add(draggable);
        }

        double getValue() {
            try {
                Field field = dryade.getClass().getField(fieldName);
                return (Double) field.get(dryade);

            } catch (Exception e) {
                e.printStackTrace();
                return 0d;
            }
        }

        void setValue(double v) {
            try {
                Field field = dryade.getClass().getField(fieldName);
                field.setDouble(dryade, v);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        public void stateChanged(ChangeEvent e) {
            double v = draggable.getValue();
            setValue(v);
            view.repaint();
        }
    }

    class DryadeView extends Panel implements MouseListener, MouseMotionListener {

        public int ox, oy;
        public int cx = 0, cy = -300;

        public DryadeView() {
            addMouseListener(this);
            addMouseMotionListener(this);
        }



        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.translate(cx, cy);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Rectangle clip = g2.getClipBounds();
            g2.setColor(Color.WHITE);
            g2.fillRect(clip.x, clip.y, clip.width, clip.height);
            g2.setColor(Color.BLACK);
            dryade.draw(g2);
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            ox = e.getX();
            oy = e.getY();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            int dx = e.getX() - ox;
            int dy = e.getY() - oy;
            cx += dx;
            cy += dy;
            ox = e.getX();
            oy = e.getY();
            repaint();
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

       class ReloadButton extends JButton implements ActionListener {
        ReloadButton() {
            super("Reload");
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            dryade.loadRules();
            view.repaint();
        }
    }
}