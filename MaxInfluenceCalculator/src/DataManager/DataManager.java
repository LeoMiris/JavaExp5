package DataManager;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class DataManager{
    public File linksFile = null;
    //public File nodesFile = null;
    public int linksNum = 0;
    public Vector<String> linksFileRaw = new Vector<>();
    public int calculateState = 0;  //1-pause; 2-stop
    //public Vector nodesFileRaw = new Vector();
    //public Queue<Integer> nodes = null; //Nodes will be here!

    public DataManager()
    {
    }

    public void LoadFile()throws FileNotFoundException {
        linksFileRaw = new Vector<>();
        calculateState = 0;
        if(linksFile == null)
        {
            throw new FileNotFoundException();
        }

        linksNum = 0;
        Thread ReadFile = new Thread(() -> {
            LineNumberReader reader = null;
            FileReader input = null;
            String[] pairs;

            try{
                input = new FileReader(linksFile);
                reader = new LineNumberReader(input);
                synchronized ("") {
                    while ((true)) {
                        String str = reader.readLine();
                        if (str == null) {
                            break;  //EOF
                        }
                        pairs = str.split(" ");
                        String str_line = pairs[0] + " " + pairs[1] + " " + pairs[2] + " " + pairs[3];
                        linksFileRaw.add(str_line);
                        linksNum++;
                    }
                }
            }
            catch (FileNotFoundException e){
                linksFile = null;
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally {
                try
                {
                    if(reader != null)
                        reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                try{
                    if(input != null)
                        input.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        ReadFile.start();

    }

    public synchronized HashMap<Integer, HashMapNode> getMap() throws FileNotFoundException {
        HashMap<Integer, HashMapNode> map = new HashMap<>();
        HashMapNode temp;
        AdjNode adjTemp;
        if (linksFileRaw == null)
            throw new FileNotFoundException();
        //int size = linksFileRaw.size();
        String str_line;
        String[] pairs;
        for (String s : linksFileRaw) {
            str_line = s;//.toString();
            pairs = str_line.split(" "); //!!!!!!!
            temp = new HashMapNode();
            temp.nodeKey = StringToInt(pairs[0]);
            adjTemp = new AdjNode();
            adjTemp.adjNodeKey = StringToInt(pairs[1]);
            adjTemp.direction = (pairs[2].equals("-1")) ? -1 : 1;
            adjTemp.possibility = DecimalStringToLong(pairs[3]);
            if (map.containsKey(temp.nodeKey)) {
                HashMapNode temp2temp = /*(HashMapNode)*/ map.get(temp.nodeKey);
                temp2temp.adjNode.add(adjTemp);
            } else {
                temp.adjNode.add(adjTemp);
                map.put(temp.nodeKey, temp);
            }
            //添加邻接节点
            if(!map.containsKey(adjTemp.adjNodeKey))
            {
                HashMapNode adjNodeTemp = new HashMapNode();
                adjNodeTemp.nodeKey = adjTemp.adjNodeKey;
                adjNodeTemp.activated = 0;
                map.put(adjNodeTemp.nodeKey, adjNodeTemp);
            }
        }

        return map;
    }

//Test code
/*public static void main(String[] args) throws FileNotFoundException {
    DataManager dm = new DataManager();
    dm.linksFile = new File("C:\\Users\\Miris\\Desktop\\大作业相关文件\\links.txt");
    dm.LoadFile();
}*/
    public static int StringToInt(String str)
    {
        int value = 0;
        int length = str.length();
        int i = 0;
        while(i < length)
        {
            value = value*10 + str.charAt(i++)-'0';
        }

        return value;
    }
    public static long DecimalStringToLong(String str)  //17x bigger
    {
        int index = str.indexOf('.');
        if(index == -1)
            return -1;
        long value = 0;
        int i = index + 1;
        int length = str.length();
        char a;
        while(i < length || i < index+18)
        {
            try {
                a = str.charAt(i++);
            }catch(IndexOutOfBoundsException e)
            {
                a = '0';
            }
            value = value*10 + a-'0';
        }
        return value;
    }
}
