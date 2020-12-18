package CoreCalculation;

import DataManager.DataManager;
import UserInterface.MainFrame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class MainCalculate extends Thread{
    public DataManager data_;
    public MainFrame ui_;
    public Vector final_result = new Vector();
    public int thread_num = 1;
    int calculated = 0;
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
        HashMap temp_map = data_.getMap();
        Iterator keys = temp_map.keySet().iterator();
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
            Runnable updateProgressBar = new Runnable() {
                @Override
                public void run() {
                    ui_.totalBar.setValue(calculated);
                }
            };
            SwingUtilities.invokeLater(updateProgressBar);
        }
    }
}
