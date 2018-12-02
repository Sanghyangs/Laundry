package laundry;

import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

public class LaundayApp extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tabName1 = "빨래감 등록하기";
	private String tabName2 = "빨래 일정 보기";
	
	public LaundayApp() {
        super(new GridLayout(1, 1));
        
        //DataStore.connectDB("localhost", 3306, "laundry", "laundry_user", "laundry_userpwd");
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("/images/laundry.png");
         
        JComponent panel1 = makeTextPanel("빨래감 등록 하는 화면");
        tabbedPane.addTab(tabName1, icon, panel1, "빨래감 등록 하는 화면");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
         
        JComponent panel2 = makeTextPanel("빨래 일정을 확인하는 화면");
        tabbedPane.addTab(tabName2, icon, panel2, "빨래 일정을 확인하는 화면");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
          
        add(tabbedPane);
         
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
     
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
     
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = LaundayApp.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        frame.add(new LaundayApp(), BorderLayout.CENTER);

        frame.setSize(500, 400);
        //frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	UIManager.put("swing.boldMetal", Boolean.FALSE);
            	createAndShowGUI();
            }
        });
    }
}