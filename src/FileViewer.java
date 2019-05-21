import java.io.*;
import java.awt.*;
import java.awt.event.*;
//import java.lang.*;

public class FileViewer extends Frame implements ActionListener {
    String directory;
    TextArea textarea;
    String[] strline;
    Button openfile;
    Button operate;
    Button close;
    int  FNV_32_PRIME = 0x01000193;

    public FileViewer() { this(null,null);}
    public FileViewer(String filename) { this(null,filename);}
    public FileViewer(String directory, String filename)
    { super();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose();}
        });
        textarea = new TextArea("",24,80);
        textarea.setEditable(false);
        this.add("Center",textarea);
        Panel p= new Panel();
        p.setLayout(new FlowLayout(FlowLayout.RIGHT,10,5));
        this.add(p,"South");
        Font font = new Font("SansSerif",Font.BOLD,14);
        openfile = new Button("Open File");
        operate = new Button("Operate List");
        close = new Button("Close");
        openfile.addActionListener(this);
        openfile.setActionCommand("open");
        openfile.setFont(font);
        operate.addActionListener(this);
        operate.setActionCommand("operate");
        operate.setFont(font);
        //operate.enable();
        close.addActionListener(this);
        close.setActionCommand("close");
        close.setFont(font);
        p.add(openfile);
        p.add(operate);
        p.add(close);
        this.pack();

        if (directory == null) {
            File f;
            if ((filename != null) && (f= new File(filename)).isAbsolute()) {
                directory = f.getParent();
                filename = f.getName();
            }
            else directory = System.getProperty("user.dir");
        }
        this.directory =directory;
        setFile(directory, filename);
    }

    public void setFile(String directory,String filename) {
        if ((filename == null)||(filename.length()==0)) return;
        File f;
        FileReader in=null;
        try {
            f=new File(directory,filename);
            in=new FileReader(f);
            char[] buffer = new char[4096];
            int len;
            textarea.setText("");
            while((len=in.read(buffer)) !=-1) {
                String s= new String(buffer,0,len);
                textarea.append(s);
            }
            this.setTitle("FileViewer: " + filename);
            textarea.setCaretPosition(0);
        }
        catch (IOException e ) {
            textarea.setText(e.getClass().getName()+": "+e.getMessage());
            this.setTitle("FileVlewer: " + filename + "Исключение в./в.");
        }
        finally {try {if (in!=null)in.close();}catch (IOException e) {}
        }
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        long result;
        String newString;
        if(cmd.equals("open")) {
            FileDialog f = new FileDialog(this,"Open File",FileDialog.LOAD);
            f.setDirectory(directory);
            f.show();
            directory = f.getDirectory();
            setFile(directory,f.getFile());
            readline(f.getFile());
            //operate.enable(true);
            f.dispose();
        }
        else if (cmd.equals("close"))
            this.dispose();
        else if (cmd.equals("operate"))
        {
            for( int i=0; i < strline.length; i++)
            {
                result = readfile(strline[i]);
                newString= String.valueOf(result)+" "+strline[i];
                strline[i]=newString;
            }
            try {
                writefile("C:/Users/плакседес/IdeaProjects/assignment/result.txt");
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }
    public  void readline( String args) {
        BufferedReader reader;
        Integer i=0;
        try {

            String tempt[]= new String[20];
            reader = new BufferedReader(new FileReader(args));
            String line = reader.readLine();
            while (line != null) {
                tempt[i]=line;
                i++;
                // read next line
                line = reader.readLine();
            }
            strline= new String[i-1];
            System.arraycopy(tempt, 0, strline, 0, strline.length);

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  int readfile(String args) {
        FileInputStream reader;
        Integer i=0;
        int result;
        byte[] btArr;

        byte[] tempt= new byte[100];
        try {
            reader = new FileInputStream(args);
            int b;
            while ((b = reader.read()) != -1) {
                tempt[i++]=(byte)b;
            }
            btArr=new byte[i-1];
            System.arraycopy(tempt, 0, btArr, 0, btArr.length);
            reader.close();
            result = FNV1Hash (btArr);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    int FNV1Hash (byte[] buf)
    {
        int hval = 0x811c9dc5; // FNV0 hval = 0

        for(int i = 0; i<buf.length;i++)
        {
            hval = hval ^ buf[i];
            hval = (hval*FNV_32_PRIME);

        }
        hval &= 0x7FFFFFFF;
        return hval;
    }

    public  void writefile(String filename) throws FileNotFoundException
    {

        FileOutputStream out = null;

        try {
            out = new FileOutputStream(filename);
            for (int i=0; i < strline.length;i++) {
                byte c;
                for (int j=0;j< strline[i].length();j++)
                {
                    c =(byte)strline[i].charAt(j);
                    out.write(c);
                }
                c=0x000D;
                out.write(c);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
	