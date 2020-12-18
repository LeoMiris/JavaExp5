package CoreCalculation;

import DataManager.DataManager;
import DataManager.HashMapNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class SingleNodeCalculate extends Thread {
    public HashMap<Integer, HashMapNode> map_;
    public DataManager data_;
    public String key_;
    public Vector final_result_;

    public SingleNodeCalculate(DataManager data, Vector final_result, String key)
    {
        data_ = data;
        map_ = data.getMap();
        final_result_ = final_result;
        key_ = key;
    }

    @Override
    public void run()
    {
        double influence = 0;
        for(int i=0; i<5000; i++)
        {
            HashMap<Integer, HashMapNode> hm = map_;
            Queue<HashMapNode> queue = new LinkedList<HashMapNode>();
            hm.get(DataManager.StringToInt(key_)).activated = 1;
            queue.offer(hm.get(DataManager.StringToInt(key_)));
            double infl = 1;
            while(!queue.isEmpty())
            {
                int k = queue.poll().nodeKey;
                int num = hm.get(k).adjNode.size();
                for(int j=0; j<num; j++)
                {
                    if(hm.get(k).adjNode.get(j).direction == 1)
                    {
                        if(hm.get(hm.get(k).adjNode.get(j).adjNodeKey).activated == 1)
                            break;
                        else
                        {
                            long real = DataManager.DecimalStringToLong(((Double)Math.random()).toString());
                            if(real >= hm.get(k).adjNode.get(j).possibility)
                            {
                                infl++;
                                hm.get(hm.get(k).adjNode.get(j).adjNodeKey).activated = 1;
                                queue.offer(hm.get(hm.get(k).adjNode.get(j).adjNodeKey));
                            }
                            else
                                break;
                        }
                    }
                    else if(hm.get(k).adjNode.get(j).direction == -1)
                    {
                        if(hm.get(hm.get(k).adjNode.get(j).adjNodeKey).activated == 0)
                            break;
                        else
                        {
                            double real = Math.random();
                            for(int a=0; a<17; a++)
                            {
                                real*=10;
                            }
                            if(real >= hm.get(k).adjNode.get(j).possibility)
                            {
                                infl--;
                                hm.get(hm.get(k).adjNode.get(j).adjNodeKey).activated = 0;
                            }
                            else
                                break;
                        }
                    }
                }
            }
            System.out.println(infl);
            influence = influence + (infl / 5000);
        }
        final_result_.add((int)influence, key_);
        System.out.println("节点" + key_ + "的最大影响力为" + influence);
    }
}
