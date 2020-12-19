package CoreCalculation;

import DataManager.*;
import UserInterface.MainFrame;

import javax.swing.*;
import javax.swing.JDialog;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class MainCalculate extends Thread{
    public DataManager data_;
    public MainFrame ui_;
    public Vector<VectorNode> final_result = new Vector<>();
    public int thread_num = 220;
    public int calculated = 0;
    public int currentState = 0;
    public MainCalculate(MainFrame Ui)
    {
        ui_ = Ui;
    }
    public void setData_(DataManager data)
    {
        data_ = data;
    }

    public void run()
    {
        HashMap<Integer, HashMapNode> temp_map = null;
        while(temp_map == null) {
            try {
                temp_map = data_.getMap();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Iterator<Integer> keys = temp_map.keySet().iterator();
        int key_num = temp_map.size();
        calculated = 0;
        Thread []running_threads = new Thread[thread_num];
        for(int i = 0; i < thread_num; i++)
        {
            running_threads[i] = null;
        }
        while(true)
        {
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
                    for(int i = 0; i < thread_num; i++)
                    {
                        if(running_threads[i] != null)
                                running_threads[i].interrupt();
                    }
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
            if(currentState != 0) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(int j = 0; j < thread_num; j++)
            {
                if(data_.calculateState == 0 && (running_threads[j] == null || !running_threads[j].isAlive()))
                {
                    if(calculated >= key_num)
                    {
                        break;
                    }
                    running_threads[j] = new SingleNodeCalculate(data_, final_result, keys.next());
                    running_threads[j].start();
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
                ui_.totalBar.setValue(calculated);
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
            File fileTemp = new File("");
            String fileName = fileTemp.getAbsolutePath();
            fileName = fileName+"\\Result"+new Date().toString().replace(' ', '-').replace(':', '-')+".txt";
            try {

                    FileWriter writer = new FileWriter(fileName);
                    for(VectorNode cur : final_result)
                    {
                        writer.write(cur.key_.toString()+" "+cur.influence_.toString()+"\n");
                    }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (final_result.size() != 0) {
                final_result.sort((o1, o2) -> (o2).influence_.compareTo((o1).influence_));
            }
            JDialog resultDialog = new JDialog(ui_.frame, "计算结果", true);
            resultDialog.setLayout(new GridLayout(11, 2));
            resultDialog.add(new JLabel("节点", JLabel.CENTER));
            resultDialog.add(new JLabel("影响力", JLabel.CENTER));
            for (int i = 0; i < 10 && i < final_result.size(); i++) {
                resultDialog.add(new JLabel(final_result.get(i).key_.toString(), JLabel.CENTER));
                resultDialog.add(new JLabel(final_result.get(i).influence_.toString(), JLabel.CENTER));
            }
            resultDialog.setSize(300, 400);
            resultDialog.setLocationRelativeTo(ui_.frame);
            resultDialog.setVisible(true);
        }

        Runnable updateTable = () -> {
            ui_.currentBar.setIndeterminate(false);
            ui_.totalBar.setMaximum(100);
            ui_.totalBar.setValue(0);
            ui_.pauseButton.setVisible(false);
            ui_.stopButton.setVisible(false);
            ui_.startButton.setVisible(true);
            if(ui_.dataMode.isSelected(ui_.linkMode.getModel()))
            {
                ui_.selectLinksFile.setEnabled(true);
            }
            else {
                ui_.selectLinksFile.setEnabled(true);
                ui_.selectNodesFile.setEnabled(true);
            }
        };
        EventQueue.invokeLater(updateTable);
        data_.calculateState = 0;
        currentState = 0;
    }
}
