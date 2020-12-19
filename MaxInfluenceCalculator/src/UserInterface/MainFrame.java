package UserInterface;

import CoreCalculation.MainCalculate;
import DataManager.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

public class MainFrame{
    public JFrame frame;
    //实例化的组件们，便于更新UI
    public File linksFile;
    public File nodesFile;
    public JProgressBar currentBar;
    public JProgressBar totalBar;
    public JTable influenceList;
    public DefaultTableModel tableModel;
    protected JTextField linksFilePath;
    protected JTextField nodesFilePath;
    public JButton selectLinksFile;
    public JButton selectNodesFile;
    public JButton startButton;
    public JButton pauseButton;
    public JButton stopButton;
    public ButtonGroup dataMode;
    public JRadioButton linkMode;
    public JRadioButton nodeMode;
    private JLabel linkFilePath;
    private JLabel nodeFilePath;
    private JLabel currentProcess;
    private JLabel totalProcess;

    //用于进行线程之间的通信
    //public MainFrameThread t = new MainFrameThread(this);
    public DataManager data_ = new DataManager();
    MainCalculate mc = new MainCalculate(this);

    //主窗口
    public MainFrame(String title)
    {
        frame = new JFrame(title);
        setButtons();
        setTexts();
    }

    //添加并显示组件
    public void Show()
    {

        JPanel_Ex workControl = new JPanel_Ex(new GridBagLayout()); //网袋布局
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 30;
        constraints.weightx = 60;
        constraints.insets = new Insets(5,2,2,5);

        //组件具体布局
        workControl.add(selectLinksFile, constraints, 5, 0, 1, 1);
        workControl.add(selectNodesFile, constraints, 5, 1, 1,1);
        workControl.add(startButton, constraints, 4,4,2,2);
        workControl.add(pauseButton, constraints, 4, 4, 1,2);
        workControl.add(stopButton, constraints,5,4,1,2);
        workControl.add(linkMode, constraints, 4,2,1,1);
        workControl.add(nodeMode, constraints, 4,3,1,1);
        workControl.add(linksFilePath, constraints, 1,0,4,1);
        workControl.add(nodesFilePath, constraints, 1,1,4,1);
        workControl.add(currentBar, constraints, 0, 2, 3,2);
        workControl.add(totalBar, constraints, 0, 4, 3,2);
        workControl.add(linkFilePath, constraints, 0,0,1,1);
        workControl.add(nodeFilePath, constraints,0,1,1,1);
        workControl.add(currentProcess, constraints, 3, 2,1,2);
        workControl.add(totalProcess, constraints, 3,4,1,2);


        //JPanel listPanel = new JPanel();
        //JScrollPane listPane = new JScrollPane();
        //listPanel.add(listPane, BorderLayout.CENTER);
        //listPanel.add(influenceList.getTableHeader(), BorderLayout.NORTH);
        //listPanel.add(influenceList);


        //JSplitPane spilt = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, workControl, listPanel);
        //spilt.setDividerLocation(480);


        //frame.setLayout(new GridLayout(1, 2));
        //frame.getContentPane().add(workControl);
        frame.getContentPane().add(workControl);
        //frame.getContentPane().add(listPanel, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    //main函数，也许它会一直在这里
    public static void main(String[]args)
    {
        MainFrame frame = new MainFrame("Influence Calculator");
        frame.Show();
        //MainFrameThread t = new MainFrameThread(frame);
    }

    //设置按钮属性
    protected void setButtons()
    {
        selectLinksFile = new JButton("选择关系文件");
        selectNodesFile = new JButton("选择结点文件");
        startButton = new JButton("开始");
        pauseButton = new JButton("暂停");
        stopButton = new JButton("中止");
        dataMode = new ButtonGroup();
        linkMode = new JRadioButton("仅使用关系文件");
        linkMode.addActionListener(e -> selectNodesFile.setEnabled(false));
        nodeMode = new JRadioButton("使用关系文件和节点文件");
        nodeMode.addActionListener(e -> selectNodesFile.setEnabled(true));

        pauseButton.setVisible(false);
        stopButton.setVisible(false);
        dataMode.add(linkMode);
        dataMode.add(nodeMode);
        linkMode.setSelected(true);
        selectNodesFile.setEnabled(false);

        selectLinksFile.addActionListener(new selectLinksFileAL());
        selectNodesFile.addActionListener(new selectNodesFileAL());
        linkMode.addActionListener(new linkModeAL());
        nodeMode.addActionListener(new nodeModeAL());
    }

    //设置界面上的文本
    protected void setTexts()
    {
        influenceList = new JTable();
        linksFilePath = new JTextField();
        nodesFilePath = new JTextField();
        currentBar = new JProgressBar();
        totalBar = new JProgressBar();
        linkFilePath = new JLabel("关系文件路径", JLabel.CENTER);
        nodeFilePath = new JLabel("节点文件路径", JLabel.CENTER);
        currentProcess = new JLabel("当前操作进度", JLabel.CENTER);
        totalProcess = new JLabel("总体操作进度", JLabel.CENTER);

        Vector<String> tableHeader = new Vector<>();
        tableHeader.add("节点");
        tableHeader.add("影响力");
        tableModel = new DefaultTableModel(tableHeader, 0);
        influenceList.setModel(tableModel);
        influenceList.setMinimumSize(new Dimension(400,400));
        linksFilePath.setEditable(false);
        nodesFilePath.setEditable(false);
        nodesFilePath.setEnabled(false);
        startButton.addActionListener(new startButtonAL());
        pauseButton.addActionListener(new pauseButtonAL());
        stopButton.addActionListener(new stopButtonAL());
    }

    //组件的事件相应函数
    class selectLinksFileAL implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser linksFileChooser = new JFileChooser();
            linksFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            linksFileChooser.showDialog(new Label(), "选择关系数据文件");
            linksFile = linksFileChooser.getSelectedFile();
            if(linksFile.exists())
            {
                linksFilePath.setText(linksFile.toString());
                data_.linksFile = linksFile;
                currentBar.setIndeterminate(true);
                totalBar.setIndeterminate(true);
                SwingWorker<Object, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        data_.LoadFile();
                        currentBar.setIndeterminate(false);
                        totalBar.setIndeterminate(false);
                        return null;
                    }
                };
                worker.execute();
            }
        }
    }
    class selectNodesFileAL implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser nodesFileChooser = new JFileChooser();
            nodesFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            nodesFileChooser.showDialog(new Label(), "选择关系数据文件");
            nodesFile = nodesFileChooser.getSelectedFile();
            if(nodesFile.exists())
            {
                nodesFilePath.setText(linksFile.toString());
            }
        }
    }
    class linkModeAL implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectNodesFile.setEnabled(false);
            nodesFilePath.setEnabled(false);
        }
    }
    class nodeModeAL implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectNodesFile.setEnabled(true);
            nodesFilePath.setEnabled(true);
        }
    }
    class startButtonAL implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setVisible(false);
            pauseButton.setVisible(true);
            stopButton.setVisible(true);
            selectLinksFile.setEnabled(false);
            selectNodesFile.setEnabled(false);
            final SwingWorker<Object, Void> worker = new SwingWorker<>() {
                @Override
                protected Object doInBackground() throws Exception {
                    totalBar.setMaximum(data_.getMap().size());
                    totalBar.setStringPainted(true);
                    currentBar.setIndeterminate(true);
                    mc = new MainCalculate(mc.ui_);
                    mc.setData_(data_);
                    mc.start();
                    return null;
                }
            };
            worker.execute();

        }
    }
    class pauseButtonAL implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (pauseButton.getText().equals("暂停")) {
                pauseButton.setText("继续");

                pauseButton.setEnabled(false);
                data_.calculateState = 1;
            }
            else{
                pauseButton.setText("暂停");

                pauseButton.setEnabled(false);
                data_.calculateState = 0;
            }
        }
    }
    class stopButtonAL implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setVisible(true);
            pauseButton.setVisible(false);
            stopButton.setVisible(false);
            if(dataMode.isSelected(linkMode.getModel()))
            {
                selectLinksFile.setEnabled(true);
            }
            else {
                selectLinksFile.setEnabled(true);
                selectNodesFile.setEnabled(true);
            }

            data_.calculateState = 2;
        }
    }
}
