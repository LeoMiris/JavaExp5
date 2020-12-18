package CoreCalculation;

import DataManager.*;
import UserInterface.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class MainCalculate extends Thread{
    public DataManager data_;
    public MainFrame ui_;
    public Vector<VectorNode> final_result = new Vector<>();
    public Vector<Integer> final_keys = new Vector<>();
    public Vector<Double> influency = new Vector<>();
    public int thread_num = 220;
    public int calculated = 0;
    public MainCalculate(MainFrame Ui)
    {
        ui_ = Ui;
    }
    public void setData_(DataManager data)
    {
        data_ = data;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run()
    {
        HashMap<Integer, HashMapNode> temp_map = null;
        try {
            temp_map = data_.getMap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
            for(int j = 0; j < thread_num; j++)
            {
                if(running_threads[j] == null || !running_threads[j].isAlive())
                {
                    if(calculated >= key_num)
                    {
                        break;
                    }
                    running_threads[j] = new SingleNodeCalculate(data_, final_result, (Integer)keys.next());
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
            Runnable updateUi = new Runnable() {
                @Override
                public void run() {
                    ui_.totalBar.setValue(calculated);
                    //ui_.tableModel = new DefaultTableModel();
                }
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

        if(final_result.size() != 0) {
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
        }
        Runnable updateTable = new Runnable() {
            @Override
            public void run() {
                ui_.tableModel.addRow(final_keys);
            }
        };
        EventQueue.invokeLater(updateTable);
    }
}
