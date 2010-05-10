package dryade;

import java.awt.*;
import java.awt.geom.AffineTransform;


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
        prodText = "DRYA";
        stepSize = 90; // The initial length of one 'F' segment
        stepDecrease = 3; // How much shorter the line gets in the next recursion level
        angle = 22.5; // The initial angle of one '+' or '-' operation.
        lines = 0; // number of lines generated
    }

    void draw(Graphics2D g2) {
        radiansAngle = angle * PI/180;
        Rectangle clip = g2.getClipBounds();
        float width = clip.width;
        float height = clip.height;
        transX = 190;
        transY = height - 100;
        g2.translate(transX, transY);
        g2.rotate(-PI/2);
        drawLetter(g2, prodText, stepSize, 0);
    }

    void drawLetter(Graphics2D g2, String text, double stepSize, int curDepth) {
        char myLetter = text.toCharArray()[0];
        char[] myRule = rule[myLetter];
        int posInRule = 0;
        while (posInRule < myRule.length) {
            char command = myRule[posInRule];
            switch (command) {
                case 'F':
                    if (text.length() > 1) {
                        transformQueue.push(g2.getTransform());
                        drawLetter(g2, text.substring(1), stepSize / stepDecrease, curDepth + 1);
                        g2.setTransform((AffineTransform) transformQueue.pop());
                    }
                        g2.drawLine(0, 0, 0, (int) Math.ceil(stepSize));
                        lines++;
                    g2.translate(0, stepSize);
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
        String cw45 = "+F+F";
        String cw90 = cw45 + cw45;
        String cw180 = cw90 + cw90;
        String cw360 = cw180 + cw180;
        String ccw45 = "-F-F";
        String ccw90 = ccw45 + ccw45;
        String ccw180 = ccw90 + ccw90;
        String ccw360 = ccw180 + ccw180;
        rule['A'] = ("|[-FF[+++++F]FF][+FF[-----F]FF]").toCharArray();
        rule['A'] = ("|[+FF+FF+FF][-FF-FF-FF]").toCharArray();
        rule['a'] = ("[FFFFF" + ccw90 + "F-F-F][|" + cw360 + "F" + ccw45 + "]").toCharArray();
        rule['B'] = ("[++++FFF----FF]FF[++++FF[F]----FF]FF++++FF").toCharArray();
        rule['b'] = ("[" + cw360 + "][FFFFF][|FFF]").toCharArray();
        rule['C'] = ("[++++FF]FFFF[++++FF]").toCharArray();
        rule['c'] = ("[" + cw90 + "++F][|" + ccw90 + "--F]").toCharArray();
        rule['D'] = ("[----F][++++FFF----FFFF]FFFF[++++FFF][----F]").toCharArray();
        rule['D'] = ("F" + cw180).toCharArray();
        rule['d'] = ("[" + ccw360 + "][FFFFF][|FFF]").toCharArray();
        rule['E'] = ("[++++F]FF[++++F]F[++++F]").toCharArray();
        rule['E'] = ("----F" + ccw180).toCharArray();
        //rule['E'] = ("++++F--F--F--F--F"+ccw180+"|F"+ccw180+ccw45).toCharArray();
        rule['e'] = ("[++++FF--F--F--F--FF" + ccw180 + "F-F]").toCharArray();
        rule['F'] = ("FF[++++F]F[++++F]").toCharArray();
        rule['f'] = ("+[FFF" + cw45 + "][|FFFFFF" + cw45 + "][" + cw45 + "][|" + cw45 + "]").toCharArray();
        rule['G'] = ("[++++FF----F----F]FFFF[++++FF++++F]").toCharArray();
        rule['g'] = ("[FF][|" + cw360 + "FFFFFFFF+F+F+F+F+F+F+F++F++F++FF]").toCharArray();
        rule['H'] = ("[----FF[----FF][++++FF]][++++FF[----FF][++++FF]]").toCharArray();
        rule['h'] = ("[FFFFFF][|FFF][" + cw180 + "FF]").toCharArray();
        rule['I'] = ("[++++F][----F]FFFF[++++F][----F]").toCharArray();
        rule['i'] = ("[GGF][|FFFFF" + ccw45 + "]").toCharArray();
        rule['J'] = ("[----FF++++F]FFFF[++++F][----F]").toCharArray();
        rule['j'] = ("[GGF][|FFFFF" + cw90 + "]").toCharArray();
        rule['K'] = ("[|FF][FF]++++[+++FF][---FF]").toCharArray();
        rule['k'] = ("[FFFFFF][|FFFF][|G--FFFF][++F++F++F++F++F++F++F]").toCharArray();
        rule['L'] = ("[FF][|FF----FF]").toCharArray();
        rule['l'] = ("+FFFFF----F----F-FFFFFF---F----F").toCharArray();
        rule['M'] = ("[-FF-------FFFF][+FF+++++++FFFF]").toCharArray();
        rule['m'] = ("FFF[FF]" + cw180 + "|F" + cw180 + "FF").toCharArray();
        rule['N'] = ("-[F[|+FFF]][|F+[F][|FF]]").toCharArray();
        rule['n'] = ("FFF[FF]" + cw180 + "FF").toCharArray();
        rule['O'] = ("[++++FF----FF]FFFF[++++FF++++FF]").toCharArray();
        rule['o'] = (cw360).toCharArray();
        rule['P'] = ("FF[++++FF----FF]FF++++FF").toCharArray();
        rule['p'] = ("[FF][" + cw360 + "][|FFFFFF]").toCharArray();
        rule['Q'] = ("[++++FF[+F][----FFFF]]FFFF[++++FF]").toCharArray();
        rule['q'] = ("[FF][" + ccw360 + "][|FFFFFF]").toCharArray();
        rule['R'] = ("FF[|--FFF][++++FF----FF]FF++++FF").toCharArray();
        rule['R'] = ("[F" + ccw180 + "][++FFFFF]").toCharArray();
        rule['r'] = ("[FF][" + cw90 + cw45 + "][|FFFFF]").toCharArray();
        rule['S'] = ("++++[FF++++FF++++FFFF][|FF++++FFF++++FFFF]").toCharArray();
        rule['s'] = ("|++++FF" + ccw180 + cw180 + "F").toCharArray();
        rule['T'] = ("FFF[++++FF][----FF]").toCharArray();
        rule['t'] = ("[++++FF][|++++F][|FFFF--F--F--F--F][FF]").toCharArray();
        rule['U'] = ("++++[|F++++FFFF][F----FFFF]").toCharArray();
        rule['u'] = ("|FFF" + ccw180 + "[|F" + ccw45 + "]FF").toCharArray();
        rule['V'] = ("[+FFFF][-FFFF]").toCharArray();
        rule['v'] = ("|-FFF|++FFF").toCharArray();
        rule['W'] = ("|[+F|--F+FF][-F|++F-FF]").toCharArray();
        rule['w'] = ("|-FFF|++FFF|--FFF|++FFF").toCharArray();
        rule['X'] = ("[+FF][-FF]|[+FF][-FF]").toCharArray();
        rule['x'] = ("|-FFFF|+GGGG|+FFFF").toCharArray();
        rule['Y'] = ("FFF[++FF][--FF]").toCharArray();
        rule['y'] = ("|FF" + ccw180 + "[FF]|FFFFF" + cw180).toCharArray();
        rule['Z'] = ("[+FFF-----FFF][|+FFF-----FFF][+++F][|+++F]").toCharArray();
        rule['z'] = ("|----FFFF++++++FFFFFF------FFFF").toCharArray();
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
