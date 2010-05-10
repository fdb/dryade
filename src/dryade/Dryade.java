package dryade;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;


public class Dryade {
    public double stepSize; // The initial length of one 'F' segment
    public double stepDecrease; // How much shorter the line gets in the next recursion level
    public double angle; // The initial angle of one '+' or '-' operation.
    public double radiansAngle;
    public long lines; // number of lines generated
    char[][] rule = new char[255][];
    public String prodText;
    boolean inputMode = false;
    double transX, transY;
    AffineTransform transform;

    double PI = Math.PI;
    Queue transformQueue = new Queue();
    double width, height;
    Stroke defaultStroke = new BasicStroke(1.0F);


    void setup() {
        loadRules();
        prodText = "EEE";
        stepSize = 90; // The initial length of one 'F' segment
        stepDecrease = 3; // How much shorter the line gets in the next recursion level
        angle = 90; // The initial angle of one '+' or '-' operation.
        lines = 0; // number of lines generated
    }

    void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 0.2f));
        radiansAngle = angle * PI / 180;
        Rectangle clip = g2.getClipBounds();
        float width = clip.width;
        float height = clip.height;
        transX = 190;
        transY = height - 100;
        g2.translate(transX, transY);
        g2.rotate(-PI / 2);
        drawLetter(g2, prodText, stepSize, 0);
        if (prodText.length() > 1) {
            transformQueue.push(g2.getTransform());
            drawLetter(g2, prodText.substring(1), stepSize / stepDecrease, 1);
            g2.setTransform((AffineTransform) transformQueue.pop());
        }
    }

    void drawLetter(Graphics2D g2, String text, double stepSize, int curDepth) {
        Rectangle2D p = new Rectangle2D.Double(0f, 0f, stepSize, stepSize);
        char myLetter = text.toCharArray()[0];
        char[] myRule = rule[myLetter];
        int posInRule = 0;
        while (posInRule < myRule.length) {
            char command = myRule[posInRule];
            switch (command) {
                case 'F':
                    g2.fill(p);
                    lines++;
                    g2.translate(0, stepSize);
                    if (text.length() > 1) {
                        transformQueue.push(g2.getTransform());
                        drawLetter(g2, text.substring(1), stepSize / stepDecrease, curDepth + 1);
                        g2.setTransform((AffineTransform) transformQueue.pop());
                    }
                    break;
                case 'G':
                    g2.translate(0, stepSize);
                    break;
                case '+':
                    g2.rotate(radiansAngle);
                    break;
                case '-':
                    g2.rotate(-radiansAngle);
                    break;
                case '|':
                    g2.rotate(PI);
                    break;
                case '[':
                    transformQueue.push(g2.getTransform());
                    break;
                case ']':
                    g2.setTransform((AffineTransform) transformQueue.pop());
                    break;
            }
            posInRule++;
        }
    }

    void loadRules() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("res/letters.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map.Entry e : properties.entrySet()) {
            String key = e.getKey().toString();
            String value = e.getValue().toString();
            char c = key.charAt(0);
            rule[c] = value.toCharArray();
        }

    }

    class Queue {
        Object[] q = new Object[1000];
        int qLength = 0;

        void push(Object o) {
            q[qLength] = o;
            qLength++;
        }

        Object pop() {
            Object o = q[qLength - 1];
            qLength--;
            return o;
        }
    }

}
