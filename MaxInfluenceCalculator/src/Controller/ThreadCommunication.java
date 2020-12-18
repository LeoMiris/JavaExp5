package Controller;

import DataManager.DataManager;
import UserInterface.MainFrame;



public class ThreadCommunication {
    public MainFrame UiSource;  //To control user interface components.
    public DataManager data_ = null;

    public ThreadCommunication(MainFrame Ui, DataManager data)
    {
        UiSource = Ui;
        data_ = data;
    }
}
