package CoreCalculation;

import DataManager.*;

import java.util.*;

public class SingleNodeCalculate extends Thread {
    public HashMap<Integer, HashMapNode> hm;
    public Integer key_;
    private final Queue<HashMapNode> queue = new LinkedList<>();
    private final Vector<VectorNode> final_result_;
    private final HashMapNode startNode;
    private  Vector<Integer> activated = new Vector<>();
    private final Vector<Integer> setKey = new Vector<>();
    public SingleNodeCalculate(HashMap<Integer, HashMapNode>map, Vector<Integer> setKey, Vector<VectorNode> final_result, Integer key)
    {
        hm = map;
        final_result_ = final_result;
        key_ = key;
        startNode = hm.get(key_);
        this.setKey.addAll(setKey);
    }

    public void RestoreMap()
    {
        for (Map.Entry<Integer, HashMapNode> integerHashMapNodeEntry : hm.entrySet()) {
            integerHashMapNodeEntry.getValue().activated = 0;
        }

    }
    @Override
    public void run()
    {
        double influence = 0;
        double currentInfluence;
        HashMapNode currentNode;
        AdjNode currentAdjNode;
        startNode.activated = 1;

        Date t1 = new Date();
        //System.out.println("Calculate Thread-" + this.getId() + " started at " + new Date());

        for(int i=0; i<5000; i++)
        {
            activated.add(startNode.nodeKey);
            //activated.addAll(setKey);
            activated.addAll(setKey);

            for(Integer key : activated)
            {
                queue.add(hm.get(key));
            }
            currentInfluence = 1;
            while(!queue.isEmpty())
            {
                currentNode = queue.poll();
                int num = currentNode.adjNode.size();
                for(int j=0; j<num; j++)
                {
                    currentAdjNode = currentNode.adjNode.get(j);
                    if(currentAdjNode.direction == 1 && !activated.contains(currentAdjNode.adjNodeKey) && Math.random() <= currentAdjNode.possibility)
                    {
                                currentInfluence++;
                                queue.offer(hm.get(currentAdjNode.adjNodeKey));
                                activated.add(currentAdjNode.adjNodeKey);
                                break;
                    }
                    else if(currentAdjNode.direction == -1 &&Math.random() <= currentAdjNode.possibility)
                    {
                            activated.add(currentAdjNode.adjNodeKey);
                            break;
                    }
                }
            }

            influence += currentInfluence;
            //RestoreMap();
            activated = new Vector<>();
            //activated.removeAllElements();

        }
        //System.out.println(t1.getTime() - new Date().getTime());
        final_result_.add(new VectorNode(influence/5000, key_));
        //System.out.println("节点" + key_ + "的最大影响力为" + influence/5000);

    }
}
