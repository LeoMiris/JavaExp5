package DataManager;

public class AdjNode {
    public int adjNodeKey;
    public int direction;
    public double possibility;

    public AdjNode()
    {
        adjNodeKey = -1;
        direction = 0;
        possibility = -1;
    }

    public AdjNode(int adjNodeKey, int direction, double possibility)
    {
        this.adjNodeKey = adjNodeKey;
        this.direction = direction;
        this.possibility = possibility;
    }

}
