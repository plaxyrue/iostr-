import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainClass {

    public static void main(String[] args) {
        Frame f= new FileViewer((args.length == 1)?args[0]:null);

        f.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) { System.exit(0); }
        });
        f.show();
    }

}