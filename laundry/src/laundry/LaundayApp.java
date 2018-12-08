package laundry;

import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class LaundayApp extends JPanel {
	private static final long serialVersionUID = 1L;

	private String tabName1 = "빨래감 등록하기";
	private String tabName2 = "빨래 일정 보기";
	private String tabName3 = "세탁 결과등록";
	private String tabName4 = "전체 의류 목록";

	public LaundayApp() {
		super(new GridLayout(1, 1));

		//DataStore.connectDB("localhost", 3306, "laundry", "laundry_user", "laundry_userpwd");
		JTabbedPane tabbedPane = new JTabbedPane();
		ImageIcon icon = createImageIcon("/images/laundry.png");

		JComponent panel1 = registerNewclothe();
		tabbedPane.addTab(tabName1, icon, panel1, "새로운 옷 등록 하는 화면");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = makeLaundrySchedulePanel();
		tabbedPane.addTab(tabName2, icon, panel2, "세탁 일정을 확인하는 화면");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JComponent panel3 = updateLaundrySchedule();
		tabbedPane.addTab(tabName3, icon, panel3, "세탁한 옷 등록하는 화면");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_3);

		JComponent panel4 = listAllClothes();
		tabbedPane.addTab(tabName4, icon, panel4, "전체 의류 목");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_4);

		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	protected JComponent registerNewclothe() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();

		// Put constraints on different buttons
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("세탁물 별명: "), gbc);

		TextField clothesNickname = new TextField(6);
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(clothesNickname, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(new JLabel("세탁물 종류: "), gbc);

	    String[] clothesKind = { "상의", "하의" };
	    final JComboBox<String> cbKind = new JComboBox<String>(clothesKind);
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(cbKind, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(new JLabel("세탁주기: "), gbc);

		TextField clothesPeriod = new TextField(6);
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(clothesPeriod, gbc);

		gbc.gridx = 2;
		gbc.gridy = 2;
		panel.add(new JLabel("일"), gbc);

		JButton btnReg = new JButton("등록");
		btnReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "새로운 세탁물 등록 하기.");
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		panel.add(btnReg, gbc);

		return panel;
	}

	protected JComponent makeLaundrySchedulePanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();

		// Put constraints on different buttons
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("세탁예정일: "), gbc);

		TextField year = new TextField(6);
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(year, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		panel.add(new JLabel("년"), gbc);

		TextField month = new TextField(6);
		gbc.gridx = 3;
		gbc.gridy = 0;
		panel.add(month, gbc);

		gbc.gridx = 4;
		gbc.gridy = 0;
		panel.add(new JLabel("월"), gbc);

		TextField day = new TextField(6);
		gbc.gridx = 5;
		gbc.gridy = 0;
		panel.add(day, gbc);
		
		gbc.gridx = 6;
		gbc.gridy = 0;
		panel.add(new JLabel("일"), gbc);

		JButton btnSearch = new JButton("검색");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "세탁 예정물 검색.");
			}
		});
		gbc.gridx = 7;
		gbc.gridy = 0;
		panel.add(btnSearch, gbc);

		String[] test = { "111", "222" };
		JList<String> laundryList = new JList<>(test);
		laundryList.setSelectedIndex(0);
		laundryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 8;
		panel.add(laundryList, gbc);

		return panel;
	}

	protected JComponent updateLaundrySchedule() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("세탁물 별명: "), gbc);

	    String[] clothesKind = { "세탁물 별명" };
	    final JComboBox<String> cbKind = new JComboBox<String>(clothesKind);
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(cbKind, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(new JLabel("세탁 완료일: "), gbc);

		TextField doneLaundry = new TextField(6);
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(doneLaundry, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		panel.add(new JLabel("일"), gbc);

		JButton btnReg = new JButton("등록");
		btnReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "세탁 완료 등록 하기.");
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		panel.add(btnReg, gbc);

		return panel;		
	}

	protected JComponent listAllClothes() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("전체 의류 목록"), gbc);

		String[] test = { "111", "222" };
		JList<String> laundryList = new JList<>(test);
		laundryList.setSelectedIndex(0);
		laundryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(laundryList, gbc);

		return panel;
	}
	
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = LaundayApp.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

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