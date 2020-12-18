package UserInterface;

import javax.swing.*;

public class MainFrameThread extends Thread{
    public MainFrame user_interface_;
    public int current = 0;
    public int total = 0;

    public MainFrameThread(MainFrame user_interface)
    {
        user_interface_ = user_interface;
    }
    @Override
    public void run() {
        while (total <= 100) {
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            current += 2;
            total += 1;
            Runnable updateProcessBar = new Runnable() {
                @Override
                public void run() {
                    user_interface_.currentBar.setValue(current);
                    user_interface_.totalBar.setValue(total);
                }
            };
            SwingUtilities.invokeLater(updateProcessBar);
        }
    }
}
