package scheduler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuView extends JFrame {

    public MenuView() {
        setTitle("스케쥴링 정책 선택");
        setSize(370,  100);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font("맑은 고딕", Font.PLAIN, 15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(new JPanel(), BorderLayout.NORTH);
        container.add(new JPanel(), BorderLayout.SOUTH);
        container.add(new JPanel(), BorderLayout.WEST);
        container.add(new JPanel(), BorderLayout.EAST);

        JPanel menuPanel = new JPanel();
        container.add(menuPanel, BorderLayout.CENTER);

        JButton FCFSButton = new JButton("FCFS");
        FCFSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainView(0);
            }
        });
        FCFSButton.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 0;
        menuPanel.add(FCFSButton, gbc);

        JButton SSTFFButton = new JButton("SSTF");
        SSTFFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainView(1);
            }
        });
        SSTFFButton.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 0;
        menuPanel.add(SSTFFButton, gbc);

        JButton SCANButton = new JButton("SCAN");
        SCANButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainView(2);
            }
        });
        SCANButton.setFont(font);
        gbc.gridx = 2;
        gbc.gridy = 0;
        menuPanel.add(SCANButton, gbc);

        JButton SLTFButton = new JButton("SLTF");
        SLTFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainView(3);
            }
        });
        SLTFButton.setFont(font);
        gbc.gridx = 3;
        gbc.gridy = 0;
        menuPanel.add(SLTFButton, gbc);

        setVisible(true);
    }
}
