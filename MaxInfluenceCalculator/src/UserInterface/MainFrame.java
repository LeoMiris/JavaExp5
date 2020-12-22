package UserInterface;

import CoreCalculation.MainCalculate;
import DataManager.DataManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
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
    public JProgressBar currentBar;
    public JProgressBar totalBar;
    public JTable influenceList;
    public DefaultTableModel tableModel;
    protected JTextField linksFilePath;
    public JButton selectLinksFile;
    public JButton startButton;
    public JButton pauseButton;
    public JButton stopButton;
    public ButtonGroup calcMode;
    public JRadioButton greedyMode;
    public JRadioButton nodeDegreeMode;
    public JRadioButton nodeOutDegreeMode;
    public JComboBox<String> calculationAccuracy = new JComboBox<>();
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

        /*JPanel_Ex workControl = new JPanel_Ex(new GridBagLayout()); //网袋布局
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
        workControl.add(totalProcess, constraints, 3,4,1,2);*/



        //JPanel listPanel = new JPanel();
        //JScrollPane listPane = new JScrollPane();
        //listPanel.add(listPane, BorderLayout.CENTER);
        //listPanel.add(influenceList.getTableHeader(), BorderLayout.NORTH);
        //listPanel.add(influenceList);


        //JSplitPane spilt = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, workControl, listPanel);
        //spilt.setDividerLocation(480);


        //frame.setLayout(new GridLayout(1, 2));
        //frame.getContentPane().add(workControl);
        frame = new JFrame("Influence Calculator");
        JPanel fileSelect = new JPanel();

        fileSelect.setLayout(new FlowLayout(FlowLayout.CENTER, 5,10));
        fileSelect.add(new JLabel("关系文件路径", JLabel.RIGHT));
        linksFilePath.setColumns(30);
        fileSelect.add(linksFilePath);
        fileSelect.add(selectLinksFile);
        fileSelect.setBackground(Color.lightGray);
        fileSelect.setBorder(new LineBorder(Color.black));

        JPanel workControl = new JPanel();
        BorderLayout layout = new BorderLayout(20, 10);
        workControl.setLayout(layout);
        JPanel radioButtons = new JPanel();
        radioButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        radioButtons.add(greedyMode);
        radioButtons.add(nodeDegreeMode);
        radioButtons.add(nodeOutDegreeMode);
        radioButtons.setBackground(new Color(0xd9e1e8));

        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        progressPanel.add(currentBar);
        progressPanel.add(currentProcess);
        progressPanel.add(totalBar);
        progressPanel.add(totalProcess);

        progressPanel.setBackground(new Color(0xfbfdfe));

        JPanel controlPanel = new JPanel();
        GridBagLayout layout1 = new GridBagLayout();
        //layout1.setConstraints(new GridBagConstraints());
        calculationAccuracy.addItem("智能优化");
        calculationAccuracy.addItem("全部");
        calculationAccuracy.addItem("最小");
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));
        controlPanel.add(calculationAccuracy);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(startButton);

        controlPanel.setBackground(new Color(0xfbfdfe));

        workControl.setBackground(new Color(0xfbfdfe));
        workControl.add(radioButtons, BorderLayout.NORTH);
        workControl.add(progressPanel, BorderLayout.CENTER);
        workControl.add(controlPanel, BorderLayout.EAST);

        frame.add(fileSelect, BorderLayout.NORTH);
        frame.add(workControl, BorderLayout.CENTER);
        //frame.add(fileSelect);
        //frame.getContentPane().add(listPanel, BorderLayout.CENTER);

        frame.setSize(600, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    //main函数，也许它会一直在这里
    public static void main(String[]args)
    {
        MainFrame frame = new MainFrame("2333");
        frame.Show();
        //MainFrameThread t = new MainFrameThread(frame);
    }

    //设置按钮属性
    protected void setButtons()
    {
        selectLinksFile = new JButton("选择关系文件");
        startButton = new JButton("开始");
        pauseButton = new JButton("暂停");
        stopButton = new JButton("中止");
        calcMode = new ButtonGroup();
        greedyMode = new JRadioButton("贪心算法");
        nodeDegreeMode = new JRadioButton("结点度数优先");
        nodeOutDegreeMode = new JRadioButton("结点出度优先");

        greedyMode.setBackground(new Color(0x9baec8));
        nodeDegreeMode.setBackground(new Color(0xd9e1e8));
        nodeOutDegreeMode.setBackground(new Color(0xd9e1e8));
        greedyMode.addActionListener(e -> {
            calculationAccuracy.setEnabled(true);
            greedyMode.setBackground(new Color(0x9baec8));
            nodeDegreeMode.setBackground(new Color(0xd9e1e8));
            nodeOutDegreeMode.setBackground(new Color(0xd9e1e8));
        });
        nodeDegreeMode.addActionListener(e -> {
            calculationAccuracy.setEnabled(false);
            nodeDegreeMode.setBackground(new Color(0x9baec8));
            greedyMode.setBackground(new Color(0xd9e1e8));
            nodeOutDegreeMode.setBackground(new Color(0xd9e1e8));
        });
        nodeOutDegreeMode.addActionListener(e -> {
            calculationAccuracy.setEnabled(false);
            nodeOutDegreeMode.setBackground(new Color(0x9baec8));
            greedyMode.setBackground(new Color(0xd9e1e8));
            nodeDegreeMode.setBackground(new Color(0xd9e1e8));
        });

        calcMode.add(greedyMode);
        calcMode.add(nodeDegreeMode);
        calcMode.add(nodeOutDegreeMode);
        greedyMode.setSelected(true);

        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);


        selectLinksFile.addActionListener(new selectLinksFileAL());
        startButton.addActionListener(new startButtonAL());
        pauseButton.addActionListener(new pauseButtonAL());
        stopButton.addActionListener(new stopButtonAL());
    }

    //设置界面上的文本
    protected void setTexts()
    {
        influenceList = new JTable();
        linksFilePath = new JTextField();
        currentBar = new JProgressBar();
        totalBar = new JProgressBar();
        JLabel linkFilePath = new JLabel("关系文件路径", JLabel.CENTER);
        currentProcess = new JLabel("当前操作进度", JLabel.CENTER);
        totalProcess = new JLabel("总体操作进度", JLabel.CENTER);

        Vector<String> tableHeader = new Vector<>();
        tableHeader.add("节点");
        tableHeader.add("影响力");
        tableModel = new DefaultTableModel(tableHeader, 0);
        influenceList.setModel(tableModel);
        influenceList.setMinimumSize(new Dimension(400,400));
        linksFilePath.setEditable(false);

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



    class startButtonAL implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(linksFile == null)
            {
                return;
            }
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            selectLinksFile.setEnabled(false);

            final SwingWorker<Object, Void> worker = new SwingWorker<>() {
                @Override
                protected Object doInBackground() {
                    data_.calculateState = 0;
                    //totalBar.setMaximum(data_.getMap().size());
                    totalBar.setStringPainted(true);
                    currentBar.setStringPainted(true);
                    if(calcMode.isSelected(greedyMode.getModel())) {
                        mc = new MainCalculate(mc.ui_, 1);
                        mc.setData_(data_);
                        mc.start();
                    }
                    else if(calcMode.isSelected(nodeDegreeMode.getModel()))
                    {
                        mc = new MainCalculate(mc.ui_, 2);
                        mc.setData_(data_);
                        mc.start();
                    }
                    else if(calcMode.isSelected(nodeOutDegreeMode.getModel()))
                    {
                        mc = new MainCalculate(mc.ui_, 3);
                        mc.setData_(data_);
                        mc.start();
                    }
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
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);


            data_.calculateState = 2;
        }
    }
}
