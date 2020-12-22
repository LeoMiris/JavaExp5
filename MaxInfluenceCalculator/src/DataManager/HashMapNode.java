package DataManager;

import java.util.Vector;

public class HashMapNode {
    public int nodeKey = -1;
    public int activated = 0;
    public Vector<AdjNode> adjNode;

    public HashMapNode()
    {
        adjNode = new Vector<>();
    }
    public HashMapNode(int _nodeKey, int _activated)
    {
        nodeKey = _nodeKey;
        activated = _activated;
    }

    public HashMapNode setActivated()
    {
        activated = 1;
        return this;
    }
    public HashMapNode setDeactivated()
    {
        activated = -1;
        return this;
    }
}