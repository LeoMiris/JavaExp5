package DataManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/*public class adjHashMap extends HashMap {

    public Object put(Object key, hashMapNode value) {
        if (value.adjNodes != null) {
            adjNode nodes = value.adjNodes;
            while (nodes != null) {
                if (super.containsKey(nodes.nodeID)) ;
                else {
                    hashMapNode temp = new hashMapNode();
                    temp.nodeID = nodes.nodeID;
                    super.put(nodes.nodeID, temp);
                }
                nodes = nodes.next;
            }
        }
        return super.put(key, value);
    }

    //Get keys for traverse.
    @Override
    public Set keySet() {
        return super.keySet();
    }

    @Override
    public Object clone() {
        if(super.isEmpty())
        {
            return null;
        }

        adjHashMap newMap = new adjHashMap();
        Set keySets = super.keySet();
        Iterator keyIterator = keySets.iterator();
        Object key;
        while(keyIterator.hasNext())
        {
            key = keyIterator.next();
            newMap.put(key, super.get(key));
        }

    }

}

    class hashMapNode {
        int activated = 0;
        int nodeID = -1;
        adjNode adjNodes = null;
    }

    class adjNode {
        int nodeID = -1;
        int adjNodeActivatePossibility = -1;
        adjNode next = null;
    }
}
*/
