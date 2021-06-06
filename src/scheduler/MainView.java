package scheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainView extends JFrame {

    int firstPosition;
    ArrayList<Integer> diskQueue;
    int[][] outputData;
    String[] outputColumnNames = {"번호", "위치", "거리", "누적", "편차"};
    DefaultTableModel outputModel = new DefaultTableModel(outputColumnNames, 0);
    JLabel totalRunTimeLabel;
    JLabel averageTimeLabel;
    GraphPanel graphPanel;
    JRadioButton outDirectionButton;

    public MainView(int mode) {
        if (mode == 0)
            setTitle("First Come First Served Scheduling");
        else if (mode == 1)
            setTitle("Shortest Seek Time First Scheduling");
        else if (mode == 2)
            setTitle("SCAN Scheduling");
        else if (mode == 3)
            setTitle("Shortest Latency Time First Scheduling");

        setSize(1000, 500);
        setResizable(false);

        Font font = new Font("맑은 고딕", Font.PLAIN, 15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(2, 2, 2, 2);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(new JPanel(), BorderLayout.NORTH);
        container.add(new JPanel(), BorderLayout.SOUTH);
        container.add(new JPanel(), BorderLayout.WEST);
        container.add(new JPanel(), BorderLayout.EAST);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        container.add(mainPanel, BorderLayout.CENTER);

        JLabel firstPositionLabel = new JLabel("초기 위치");
        firstPositionLabel.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        mainPanel.add(firstPositionLabel, gbc);

        JLabel diskQueueLabel = new JLabel("디스크 큐");
        diskQueueLabel.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(diskQueueLabel, gbc);

        if (mode == 2) {
            JLabel startDirectionLabel = new JLabel("시작 방향 : ");
            startDirectionLabel.setFont(font);
            gbc.gridx = 2;
            gbc.gridy = 0;
            mainPanel.add(startDirectionLabel, gbc);

            outDirectionButton = new JRadioButton("바깥쪽");
            outDirectionButton.setFont(font);
            gbc.gridx = 3;
            gbc.gridy = 0;
            mainPanel.add(outDirectionButton, gbc);

            JRadioButton inDirectionButton = new JRadioButton("안쪽");
            inDirectionButton.setFont(font);
            gbc.gridx = 4;
            gbc.gridy = 0;
            mainPanel.add(inDirectionButton, gbc);

            ButtonGroup directionButtonGroup = new ButtonGroup();
            directionButtonGroup.add(outDirectionButton);
            directionButtonGroup.add(inDirectionButton);
            outDirectionButton.setSelected(true);

            JPanel trashPanel = new JPanel();
            gbc.gridx = 5;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            mainPanel.add(trashPanel, gbc);
        } else {
            JPanel trashPanel = new JPanel();
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            mainPanel.add(trashPanel, gbc);
        }

        JTextField firstPositionField = new JTextField(5);
        firstPositionField.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 0.1;
        mainPanel.add(firstPositionField, gbc);

        JTextField diskQueueField = new JTextField(40);
        diskQueueField.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(diskQueueField, gbc);

        JButton generateButton = new JButton("생성");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder temp = new StringBuilder();
                firstPositionField.setText(String.valueOf((int) (Math.random() * 400)));
                for (int i = 0; i < 19; i++) {
                    temp.append(String.valueOf((int) (Math.random() * 400) + ","));
                }
                temp.append(String.valueOf((int) (Math.random() * 400)));
                diskQueueField.setText(String.valueOf(temp));
            }
        });
        generateButton.setFont(font);
        gbc.gridx = 2;
        gbc.gridy = 1;
        mainPanel.add(generateButton, gbc);

        JButton runButton = new JButton("실행");
        runButton.setFont(font);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstPosition = Integer.parseInt(firstPositionField.getText());
                diskQueue = new ArrayList<>();
                outputModel.setNumRows(0);
                StringTokenizer stringTokenizer = new StringTokenizer(diskQueueField.getText(), ",");
                while (stringTokenizer.hasMoreTokens()) {
                    diskQueue.add(Integer.parseInt(stringTokenizer.nextToken()));
                }
                if (mode == 0) {
                    outputData = SchedulingService.getInstance().FCFSScheduling(firstPosition, diskQueue);
                } else if (mode == 1) {
                    outputData = SchedulingService.getInstance().SSTFScheduling(firstPosition, diskQueue);
                } else if (mode == 2) {
                    boolean startDirection = outDirectionButton.isSelected();
                    outputData = SchedulingService.getInstance().SCANScheduling(startDirection, firstPosition, diskQueue);
                } else if (mode == 3) {
                    outputData = SchedulingService.getInstance().SLTFScheduling(firstPosition, diskQueue);
                }
                int totalRunTime = outputData[outputData.length - 1][3];
                int averageTime = totalRunTime / diskQueue.size();
                for (int i = 0; i < outputData.length; i++) {
                    outputModel.addRow(new Integer[]{outputData[i][0], outputData[i][1], outputData[i][2], outputData[i][3], outputData[i][4]});
                }
                totalRunTimeLabel.setText("총 실행 시간 : " + totalRunTime);
                if (mode == 3)
                    averageTimeLabel.setText("평균 회전 시간 : " + averageTime);
                else
                    averageTimeLabel.setText("평균 이동 시간 : " + averageTime);
                graphPanel.setResult(outputData);
                graphPanel.repaint();
                repaint();
                pack();
            }
        });
        gbc.gridx = 3;
        gbc.gridy = 1;
        mainPanel.add(runButton, gbc);

        totalRunTimeLabel = new JLabel("총 실행 시간 : ");
        totalRunTimeLabel.setFont(font);
        totalRunTimeLabel.setHorizontalAlignment(JLabel.LEFT);
        gbc.gridx = 4;
        gbc.gridy = 1;
        mainPanel.add(totalRunTimeLabel, gbc);

        if (mode == 3)
            averageTimeLabel = new JLabel("평균 회전 시간 : ");
        else
            averageTimeLabel = new JLabel("평균 이동 시간 : ");
        averageTimeLabel.setFont(font);
        averageTimeLabel.setHorizontalAlignment(JLabel.LEFT);
        gbc.gridx = 5;
        gbc.gridy = 1;
        mainPanel.add(averageTimeLabel, gbc);

        JPanel trashPanel1 = new JPanel();
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(trashPanel1, gbc);

        graphPanel = new GraphPanel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(graphPanel, gbc);

        JTable outputTable = new JTable(outputModel);
        outputTable.getTableHeader().setReorderingAllowed(false);
        outputTable.getTableHeader().setResizingAllowed(false);
        outputTable.setEnabled(false);
        outputTable.setFont(font);

        JScrollPane outputTableScrollPane = new JScrollPane(outputTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        mainPanel.add(outputTableScrollPane, gbc);

        JPanel trashPanel2 = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(trashPanel2, gbc);

        pack();
        setVisible(true);
    }
}
