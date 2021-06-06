package scheduler;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    int[][] result;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.RED);
        g.drawLine(10, 30, 710, 30);
        for (int i = 10; i < 720; i += 35) {
            g.drawLine(i, 20, i, 30);
            g.drawString(String.valueOf((i - 10) * 20 / 35), i - 3, 10);
        }
        if (result != null) {
            g.fillOval((int)((double)10 + result[0][1] * 35 / 20) -3, 30 - 3, 6, 6);
            for (int i = 1; i < result.length; i++) {
                g.fillOval((int)((double)10 + result[i][1] * 35 / 20) - 3, 30 + i * 15 - 3, 6, 6);
                g.drawLine((int)((double)10 + result[i][1] * 35 / 20), 30 + i * 15, (int)((double)10 + result[i - 1][1] * 35 / 20), 30 + (i - 1) * 15);
            }
        }
    }

    public void setResult(int[][] result) {
        this.result = result;
    }
}
