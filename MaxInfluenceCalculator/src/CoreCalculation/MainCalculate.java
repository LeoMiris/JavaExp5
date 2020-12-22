package CoreCalculation;

import DataManager.AdjNode;
import DataManager.DataManager;
import DataManager.HashMapNode;
import DataManager.VectorNode;
import UserInterface.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MainCalculate extends Thread{
    public DataManager data_;
    public MainFrame ui_;
    public Vector<VectorNode> final_result = new Vector<>();
    public int thread_num = Runtime.getRuntime().availableProcessors();
    public int calculated = 0;
    public int currentState = 0;
    private int mode;
    private int nodeNum;
    private final Thread []running_threads = new Thread[thread_num];
    private final HashMap<Integer, HashMapNode>[] maps = new HashMap[thread_num];
    private final HashMap<Integer, HashMapNode> nodeDegree = new HashMap<>();
    private final Vector<Integer> setElem = new Vector<>();
    private final Vector<Integer> skipNode = new Vector<>();
    private HashMap<Integer, HashMapNode> temp_map;
    private Iterator<Integer> keys;
    public int key_num;

    public MainCalculate(MainFrame Ui)
    {
        ui_ = Ui;
    }
    public MainCalculate(MainFrame Ui, int mode)
    {
        ui_ = Ui;
        this.mode = mode;
    }
    public void setData_(DataManager data)
    {
        data_ = data;
        temp_map = data_.getMap();
        key_num = temp_map.size();
        nodeNum = temp_map.size();
        keys = temp_map.keySet().iterator();
    }

    public void run()
    {
        if(mode == 1)   //Greedy al
        {

            for(int i = 0; i < 10; i++)
            {
                calculated = 0;
                int finalI = i;
                Runnable updateTotal = () -> {
                    ui_.totalBar.setMaximum(10);
                    ui_.totalBar.setValue(finalI + 1);
                };
                EventQueue.invokeLater(updateTotal);

                int nodeKey = SingleCalculate();
                if(nodeKey == -1)
                    return;
                setElem.add(nodeKey);
                if(Objects.equals(ui_.calculationAccuracy.getSelectedItem(), "智能优化")) {
                    for (int j = 0; j < nodeNum - 20 && j < final_result.size() * 0.8; j++) {
                        skipNode.add(final_result.get(final_result.size() - j - 1).key_);
                    }
                }
                else if(Objects.equals(ui_.calculationAccuracy.getSelectedItem(), "最小"))
                {
                    if(skipNode.size() > 0)
                        continue;
                    for(int j = 0; j < final_result.size() - 20; j++)
                    {
                        if(skipNode.size() >= nodeNum - 20)
                            continue;
                        skipNode.add(final_result.get(final_result.size() - j -1).key_);
                    }
                }
            }

            File fileTemp = new File("");
            String fileName = fileTemp.getAbsolutePath();
            fileName = fileName+"\\Result"+new Date().toString().replace(' ', '-').replace(':', '-')+".txt";
            try {

                FileWriter writer = new FileWriter(fileName);
                for(Integer cur : setElem)
                {
                    writer.write(cur + "\n");
                }
                writer.write("Total nfluence: " + final_result.get(0).influence_);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDialog resultDialog = new JDialog(ui_.frame, "计算结果", true);
            resultDialog.setLayout(new GridLayout(7, 2));
            resultDialog.add(new JLabel("节点", JLabel.CENTER));
            resultDialog.add(new JLabel("", JLabel.CENTER));

            for (Integer i : setElem) {
                resultDialog.add(new JLabel(i.toString(), JLabel.CENTER));
            }
            resultDialog.add(new JLabel("总影响力", JLabel.CENTER));
            resultDialog.add(new JLabel(final_result.get(0).influence_.toString(), JLabel.CENTER));
            resultDialog.setSize(300, 400);
            resultDialog.setLocationRelativeTo(ui_.frame);
            resultDialog.setVisible(true);

        }
        else if(mode == 2)
        {
            HashMap<Integer, HashMapNode> map = data_.getMap();
            Set<Integer> keySet = map.keySet();
            for(Integer i : keySet) {
                if (nodeDegree.containsKey(i))
                {
                    nodeDegree.get(i).activated+=map.get(i).adjNode.size();
                }
                else
                {
                    nodeDegree.put(i, new HashMapNode(i, map.get(i).adjNode.size()));
                }
                for(AdjNode adjNode : map.get(i).adjNode)
                {
                    if(nodeDegree.containsKey(adjNode.adjNodeKey))
                    {
                        nodeDegree.get(adjNode.adjNodeKey).activated++;
                    }
                    else
                    {
                        nodeDegree.put(adjNode.adjNodeKey, new HashMapNode(adjNode.adjNodeKey, 1));
                    }
                }
            }

            Collection<HashMapNode> result = nodeDegree.values();
            Vector<HashMapNode> degreeResult = new Vector<>(result);
            degreeResult.sort((o1, o2) -> {
                return Integer.compare(o2.activated, o1.activated);
            });

            Vector<Integer> key = new Vector<>();
            //for(HashMapNode node : degreeResult)
            //{
                //if(key.size() >= 10)
                    //break;
                key.add(degreeResult.get(0).nodeKey);
                keys = key.iterator();
            //}
            key_num = 1;
            for(int i = 0; i < 10; i++)
            {
                setElem.add(degreeResult.get(i).nodeKey);
            }

            SingleCalculate();

            File fileTemp = new File("");
            String fileName = fileTemp.getAbsolutePath();
            fileName = fileName+"\\Result"+new Date().toString().replace(' ', '-').replace(':', '-')+".txt";
            try {

                FileWriter writer = new FileWriter(fileName);
                for(Integer cur : setElem)
                {
                    writer.write(cur + "\n");
                }
                writer.write("Total nfluence: " + final_result.get(0).influence_);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDialog resultDialog = new JDialog(ui_.frame, "计算结果", true);
            resultDialog.setLayout(new GridLayout(7, 2));
            resultDialog.add(new JLabel("节点", JLabel.CENTER));
            resultDialog.add(new JLabel("", JLabel.CENTER));

            for (Integer i : setElem) {
                resultDialog.add(new JLabel(i.toString(), JLabel.CENTER));
            }
            resultDialog.add(new JLabel("总影响力", JLabel.CENTER));
            resultDialog.add(new JLabel(final_result.get(0).influence_.toString(), JLabel.CENTER));
            resultDialog.setSize(300, 400);
            resultDialog.setLocationRelativeTo(ui_.frame);
            resultDialog.setVisible(true);

        }
        else if(mode == 3)
        {
            HashMap<Integer, HashMapNode> map = data_.getMap();
            Set<Integer> keySet = map.keySet();
            for(Integer i : keySet) {
                    nodeDegree.put(i, new HashMapNode(i, map.get(i).adjNode.size()));
            }

            Collection<HashMapNode> result = nodeDegree.values();
            Vector<HashMapNode> degreeResult = new Vector<>(result);
            degreeResult.sort((o1, o2) -> {
                return Integer.compare(o2.activated, o1.activated);
            });

            Vector<Integer> key = new Vector<>();
            //for(HashMapNode node : degreeResult)
            //{
            //if(key.size() >= 10)
            //break;
            key.add(degreeResult.get(0).nodeKey);
            keys = key.iterator();
            //}
            key_num = 1;
            for(int i = 0; i < 10; i++)
            {
                setElem.add(degreeResult.get(i).nodeKey);
            }

            SingleCalculate();

            File fileTemp = new File("");
            String fileName = fileTemp.getAbsolutePath();
            fileName = fileName+"\\Result"+new Date().toString().replace(' ', '-').replace(':', '-')+".txt";
            try {

                FileWriter writer = new FileWriter(fileName);
                for(Integer cur : setElem)
                {
                    writer.write(cur + "\n");
                }
                writer.write("Total nfluence: " + final_result.get(0).influence_);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDialog resultDialog = new JDialog(ui_.frame, "计算结果", true);
            resultDialog.setLayout(new GridLayout(7, 2));
            resultDialog.add(new JLabel("节点", JLabel.CENTER));
            resultDialog.add(new JLabel("", JLabel.CENTER));

            for (Integer i : setElem) {
                resultDialog.add(new JLabel(i.toString(), JLabel.CENTER));
            }
            resultDialog.add(new JLabel("总影响力", JLabel.CENTER));
            resultDialog.add(new JLabel(final_result.get(0).influence_.toString(), JLabel.CENTER));
            resultDialog.setSize(300, 400);
            resultDialog.setLocationRelativeTo(ui_.frame);
            resultDialog.setVisible(true);
        }

        //结束
        Runnable updateTable = () -> {
            ui_.currentBar.setIndeterminate(false);
            ui_.totalBar.setMaximum(100);
            ui_.totalBar.setValue(0);
            ui_.pauseButton.setEnabled(false);
            ui_.stopButton.setEnabled(false);
            ui_.startButton.setEnabled(true);
            ui_.selectLinksFile.setEnabled(true);
        };
        EventQueue.invokeLater(updateTable);
        data_.calculateState = 0;
        currentState = 0;
    }
    public int SingleCalculate()
    {
        System.out.println(thread_num);

        while(temp_map == null) {
            try {
                temp_map = data_.getMap();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        //Iterator<Integer> keys = temp_map.keySet().iterator();


        for(int i = 0; i < thread_num; i++)
        {
            running_threads[i] = null;
            maps[i] = data_.getMap();
        }
        while(true)
        {
            //System.out.println("Loop Thread-" + this.getId() + " started at " + new Date());
            if(data_.calculateState != currentState)
            {
                //Pause
                if(data_.calculateState == 1)
                {
                    for(int i = 0; i < thread_num; i++)
                    {
                        if(running_threads[i] != null && running_threads[i].isAlive()) {
                            synchronized (running_threads[i]) {
                                try {
                                    running_threads[i].wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    Runnable enableButton = () -> ui_.pauseButton.setEnabled(true);
                    EventQueue.invokeLater(enableButton);
                    currentState = 1;
                    continue;
                }
                //Stop
                else if(data_.calculateState == 2)
                {
                    /*for(int i = 0; i < thread_num; i++)
                    {
                        if(running_threads[i] != null)
                                running_threads[i].interrupt();
                    }*/
                    currentState = 2;
                    break;
                }
                else
                {
                    for(int i = 0; i < thread_num; i++)
                    {
                        if(running_threads[i] != null && running_threads[i].isAlive())
                            synchronized (running_threads[i]) {
                                running_threads[i].notify();
                            }
                    }
                    Runnable enableButton = () -> ui_.pauseButton.setEnabled(true);
                    EventQueue.invokeLater(enableButton);
                    currentState = 0;
                    continue;
                }
            }

            for(int j = 0; j < thread_num; j++)
            {
                if(running_threads[j] == null || !running_threads[j].isAlive())
                {
                    if(calculated >= key_num)
                    {
                        break;
                    }
                    int currentKey = keys.next();
                    if(skipNode.contains(currentKey))
                    {
                        j--;
                        calculated++;
                        continue;
                    }
                    running_threads[j] = new SingleNodeCalculate(maps[j], setElem, final_result, currentKey);
                    //System.out.println("New Calculation Thread created at " + new Date());
                    running_threads[j].start();
                    //System.out.println("New Calculation Thread started at " + new Date());
                    calculated++;
                }
            }
            if(calculated >= key_num)
            {
                break;
            }
            /*if(final_result.size() != 0) {
                final_result.sort(new Comparator<VectorNode>() {


                    public int compare(VectorNode o1, VectorNode o2) {
                        if ((o1).key_ > (o2).key_) {
                            return 1;
                        } else if ((o1).key_.equals((o2).key_)) {
                            return 0;
                        } else
                            return -1;
                    }
                });
            }*/
            Runnable updateUi = () -> {
                ui_.currentBar.setMaximum(key_num);
                ui_.currentBar.setValue(calculated);
                //ui_.tableModel = new DefaultTableModel();
            };
            EventQueue.invokeLater(updateUi);

            /*SwingWorker updateTable = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    ui_.tableModel.addRow(final_result);
                    return null;
                }
            };
            updateTable.execute();*/
            //System.out.println("Loop Thread-" + this.getId() + " completed at " + new Date());
        }
        int flag = 1;
        while(flag != 0)
        {
            flag = 0;
            for(int i = 0; i < thread_num; i++)
                if (running_threads[i] != null && running_threads[i].isAlive())
                    flag++;
        }

        if(currentState == 0) {
            if (final_result.size() != 0) {
                final_result.sort((o1, o2) -> (o2).influence_.compareTo((o1).influence_));
            }

            return final_result.get(0).key_;
        }

        return -1;
    }
}
