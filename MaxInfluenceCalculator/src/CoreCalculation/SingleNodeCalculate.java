package CoreCalculation;

import DataManager.DataManager;
import DataManager.HashMapNode;
import DataManager.VectorNode;

import java.io.FileNotFoundException;
import java.util.*;

public class SingleNodeCalculate extends Thread {
    public HashMap<Integer, HashMapNode> hm;
    public DataManager data_;
    public Integer key_;
    Set<Integer> keys;
    public Vector<VectorNode> final_result_;

    public SingleNodeCalculate(DataManager data, Vector<VectorNode> final_result, Integer key)
    {
        data_ = data;
        try {
            hm = data.getMap();
            keys = hm.keySet();
        }catch (FileNotFoundException e)
        {
            hm = null;
            keys = null;
        }
        final_result_ = final_result;
        key_ = key;
    }

    public void RestoreMap()
    {
        for(Integer current_key : keys)
        {
            hm.get(current_key).activated = 0;
        }
    }
    @Override
    public void run()
    {
        double influence = 0;
        for(int i=0; i<5000; i++)
        {
            if(this.isInterrupted())
            {
                return;
            }
            while(hm == null)
            {
                try {
                    hm = data_.getMap();
                    keys = hm.keySet();
                } catch (FileNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
            //HashMap<Integer, HashMapNode> hm = data_.getMap();
            Queue<HashMapNode> queue = new LinkedList<>();
            hm.get(key_).activated = 1;
            queue.offer(hm.get(key_));
            double currentInfluence = 1;
            while(!queue.isEmpty())
            {
                int k = queue.poll().nodeKey;
                int num = hm.get(k).adjNode.size();
                for(int j=0; j<num; j++)
                {
                    if(hm.get(k).adjNode.get(j).direction == 1)
                    {
                        if(hm.get(hm.get(k).adjNode.get(j).adjNodeKey).activated == 1
                                || hm.get(hm.get(k).adjNode.get(j).adjNodeKey).activated == -1)
                            break;
                        else
                        {
                            long real = DataManager.DecimalStringToLong(((Double)Math.random()).toString());
                            if(real >= hm.get(k).adjNode.get(j).possibility)
                            {
                                currentInfluence++;
                                hm.get(hm.get(k).adjNode.get(j).adjNodeKey).activated = 1;
                                queue.offer(hm.get(hm.get(k).adjNode.get(j).adjNodeKey));
                            }
                            else
                                break;
                        }
                    }
                    else if(hm.get(k).adjNode.get(j).direction == -1)
                    {
                        double real = Math.random();
                        for(int a=0; a<17; a++)
                        {
                            real*=10;
                        }
                        if(real >= hm.get(k).adjNode.get(j).possibility)
                        {
                            //currentInfluence--;
                            hm.get(hm.get(k).adjNode.get(j).adjNodeKey).activated = -1;
                        }
                        else
                            break;
                    }
                }
            }
            //System.out.println(currentInfluence);
            influence = influence + (currentInfluence / 5000);
            RestoreMap();
        }

        final_result_.add(new VectorNode(influence, key_));
        //final_result_.add(influence, key_);
        System.out.println("节点" + key_ + "的最大影响力为" + influence);
    }
}
