package CoreCalculation;

import DataManager.DataManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class MainCalculate extends Thread{
    public DataManager data_;
    public Vector final_result = new Vector();
    public MainCalculate(DataManager data)
    {
        data_ = data;
    }

    @Override
    public void run()
    {
        HashMap temp_map = data_.getMap();
        Iterator keys = temp_map.keySet().iterator();
        int key_num = temp_map.size();
        int calculated = 0;
        Thread []running_threads = new Thread[10];
        for(int i = 0; i < 10; i++)
        {
            running_threads[i] = null;
        }
        while(true)
        {
            for(int j = 0; j < 100; j++)
            {
                if(running_threads[j] == null || !running_threads[j].isAlive())
                {
                    if(calculated >= key_num)
                    {
                        break;
                    }
                    running_threads[j] = new SingleNodeCalculate(data_, final_result, (String)keys.next());
                    running_threads[j].start();
                    calculated++;
                }
            }
        }
    }
}
