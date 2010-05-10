package dryade;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.Field;

public class DryadeFrame extends JFrame {
    DryadeView view;
    Dryade dryade;

    public static void main(String[] args) {
        DryadeFrame frame = new DryadeFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
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
        add(controlsPanel, BorderLayout.SOUTH);

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

    class DryadeView extends Panel {

        public DryadeView() {
        }

        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Rectangle clip = g2.getClipBounds();
            g2.setColor(Color.WHITE);
            g2.fillRect(clip.x, clip.y, clip.width, clip.height);
            g2.setColor(Color.BLACK);
            dryade.draw(g2);
        }
    }
}