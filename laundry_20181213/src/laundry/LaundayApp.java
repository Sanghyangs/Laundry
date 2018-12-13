package laundry;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

public class LaundayApp extends JPanel {
	private static final long serialVersionUID = 1L;

	private String m_tabName1 = "의류 등록하기";
	private String m_tabName2 = "세탁 일정 보기";
	private String m_tabName3 = "세탁 결과등록";
	private String m_tabName4 = "의류 목록 검색";

	public LaundayApp() {
		super(new GridLayout(1, 1));

		Properties properties = new Properties();
		String strPropertyPath = "db.properties";
        InputStream inputStream = getClass().getResourceAsStream(strPropertyPath);
        if (inputStream == null) {
        	String strMsg = "DB 설정 파일이 없습니다.\r\n프로그램을 종료합니다.";
        	JOptionPane.showMessageDialog(m_mainFrame, strMsg);
			System.exit(-1);
        }
        try {
			properties.load(inputStream);
	        inputStream.close();
		} catch (Exception e) {
        	String strMsg = "오류가 발생하였습니다. 설정을 확인하여야 합니다.\r\n오류메시지는 다음과 같습니다.\r\n(" + e.getLocalizedMessage() + ")";
        	JOptionPane.showMessageDialog(m_mainFrame, strMsg);

        	JOptionPane.showMessageDialog(m_mainFrame, "프로그램을 종료합니다.");
			System.exit(-1);
		}
        
        String strHost = (String) properties.get("DB.HOST");
        String strPort = (String) properties.get("DB.PORT");
        int port = Integer.parseInt(strPort);
        String strDbName = (String) properties.get("DB.NAME");
        String strDbUser = (String) properties.get("DB.USER");
        String strUserPwd = (String) properties.get("DB.PASSWORD");

		if (DataStore.connectDB(strHost, port, strDbName, strDbUser, strUserPwd) == false) {
        	JOptionPane.showMessageDialog(m_mainFrame, "프로그램을 종료합니다.");
			System.exit(-1);
		}
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int tabSelected = tabbedPane.getSelectedIndex();
		        switch (tabSelected) {
		        case 0:
		        	resetNewClothePanel();
		        	break;
		        	
		        case 1:
		        	resetMakeLaundrySchedulePanel();
		        	break;
		        	
		        case 2:
		        	resetUpdateLaundrySchedulePanel();
		        	break;
		        	
		        default:
		        	resetListAllClothePanel();
		        	break;
		        }
			}
		});
		ImageIcon icon = null;

		JComponent panel1 = registerNewClothe();
		tabbedPane.addTab(m_tabName1, icon, panel1, "새로운 옷 등록 하는 화면");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = makeLaundrySchedulePanel();
		tabbedPane.addTab(m_tabName2, icon, panel2, "세탁 일정을 확인하는 화면");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JComponent panel3 = updateLaundrySchedule();
		tabbedPane.addTab(m_tabName3, icon, panel3, "세탁한 옷 등록하는 화면");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		JComponent panel4 = listAllClothes();
		tabbedPane.addTab(m_tabName4, icon, panel4, "전체 의류 목록");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	JTextField m_clothesNickname;
	JComboBox<String> m_cbKind;
	JTextField m_clothesPeriod;
	
	private void resetNewClothePanel() {
		m_clothesNickname.setText("");
		m_cbKind.setSelectedIndex(0);
		m_clothesPeriod.setText("");
	}

	protected JComponent registerNewClothe() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();
		
		int xCoord = 0, yCoord = 0;

		gbc.weightx = 1.0;
		//gbc.weighty = 1.0;

		///
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("세탁물 별명: "), gbc);

		m_clothesNickname = new JTextField(6);
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_clothesNickname, gbc);

		///
		xCoord = 0;
		yCoord++;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("세탁물 종류: "), gbc);

	    String[] clothesKind = { "상의", "하의" };
	    m_cbKind = new JComboBox<String>(clothesKind);
	    m_cbKind.addFocusListener(new FocusAdapter() {
	    	   @Override
	    	   public void focusGained(FocusEvent e) {
	    		   m_cbKind.setPopupVisible(true);
	    		   //m_cbKind.showPopup();
	    	   }
	    	});
	    //m_cbKind.setOpaque(true);
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_cbKind, gbc);

		///
		xCoord = 0;
		yCoord++;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("세탁주기: "), gbc);

		m_clothesPeriod = new JTextField(6);
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_clothesPeriod, gbc);

		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("일"), gbc);

		JButton btnReg = new JButton("등록");
		btnReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strNickName = m_clothesNickname.getText().trim();
				if (strNickName.length() < 1) {
					m_clothesNickname.setText("");
					m_clothesNickname.requestFocus();
					return;
				}
				String strKind = (String) m_cbKind.getSelectedItem();
				String strPeriod = m_clothesPeriod.getText().trim();
				if (strNickName.length() < 1) {
					m_clothesPeriod.setText("");
					m_clothesPeriod.requestFocus();
					return;
				}
				int period = 0;
				try {
					period = Integer.parseInt(strPeriod);
					if (period < 0) {
						m_clothesPeriod.setText("");
						m_clothesPeriod.requestFocus();
						return;
					}
				} catch (NumberFormatException nfe) {
					m_clothesPeriod.setText("");
					m_clothesPeriod.requestFocus();
					return;
				}
				
				boolean bSuccess = DataStore.insertClothes(strNickName, strKind, period);
				if (bSuccess == true) {
					JOptionPane.showMessageDialog(m_mainFrame, "세탁물을 등록하였습니다.");
				} else {
					JOptionPane.showMessageDialog(m_mainFrame, "세탁물 등록 과정에서 오류가 발생하였습니다.");
				}
				
				m_clothesNickname.setText("");
				m_cbKind.setSelectedIndex(0);
				m_clothesPeriod.setText("");
			}
		});
		
		///
		xCoord = 0;
		yCoord++;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		panel.add(btnReg, gbc);

		return panel;
	}

	JTable m_mstpScheduleTable;
	DefaultTableModel m_mstpScheduleTableEntry;
	
	private void resetMakeLaundrySchedulePanel() {
    	Calendar calendar = Calendar.getInstance();

		m_mlspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
		m_mlspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
		m_mlspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
		
		m_mstpScheduleTableEntry.setRowCount(0);
	}
	
	private JScrollPane makeScheduleTablePane() {
		m_mstpScheduleTableEntry = new DefaultTableModel();      
		m_mstpScheduleTableEntry.addColumn("별명");
		m_mstpScheduleTableEntry.addColumn("종류");
		m_mstpScheduleTableEntry.addColumn("세탁주기");
		m_mstpScheduleTableEntry.addColumn("마지막세탁일");
		m_mstpScheduleTableEntry.addColumn("다음세탁일");
		
		m_mstpScheduleTable = new JTable(m_mstpScheduleTableEntry);
		m_mstpScheduleTable.getTableHeader().setReorderingAllowed(false);
		m_mstpScheduleTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

	    JScrollPane jsp = new JScrollPane(m_mstpScheduleTable);
	    setJspSize(jsp);
	    jsp.setVisible(true);
		
	    return jsp;
	}
	
	JTextField m_mlspYear, m_mlspMonth, m_mlspDay;
	protected JComponent makeLaundrySchedulePanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();

		int xCoord = 0, yCoord = 0;

		gbc.weightx = 1.0;
		//gbc.weighty = 1.0;

		///
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("세탁예정일: "), gbc);

    	Calendar calendar = Calendar.getInstance();

		m_mlspYear = new JTextField(6);
		m_mlspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_mlspYear, gbc);
		
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("년"), gbc);

		m_mlspMonth = new JTextField(6);
		m_mlspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_mlspMonth, gbc);

		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("월"), gbc);

		m_mlspDay = new JTextField(6);
		m_mlspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_mlspDay, gbc);
		
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("일"), gbc);

		JButton btnSearch = new JButton("검색");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int year, month, day;				
				String strYear = m_mlspYear.getText().trim();
				if (strYear.length() < 1) {
					m_mlspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
					return;
				}
				try {
					year = Integer.parseInt(strYear);
					if (year < 0) {
						m_mlspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
						return;
					}
				} catch (NumberFormatException nfe) {
					m_mlspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
					return;
				}
				
				String strMonth = m_mlspMonth.getText().trim();
				if (strMonth.length() < 1) {
					m_mlspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
					return;
				}
				try {
					month = Integer.parseInt(strMonth);
					if (month < 0) {
						m_mlspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
						return;
					}
				} catch (NumberFormatException nfe) {
					m_mlspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
					return;
				}
				
				String strDay = m_mlspDay.getText().trim();
				if (strDay.length() < 1) {
					m_mlspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
					return;
				}
				try {
					day = Integer.parseInt(strDay);
					if (day < 0) {
						m_mlspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
						return;
					}
				} catch (NumberFormatException nfe) {
					m_mlspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
					return;
				}

				int rowCount = DataStore.fillScheduleTableEntry(m_mstpScheduleTableEntry, year, month, day);
				if (rowCount > 0) {
					JOptionPane.showMessageDialog(m_mainFrame, "세탁할 옷이 " + rowCount + "벌 있습니다.");
				} else {
					JOptionPane.showMessageDialog(m_mainFrame, "세탁할 옷이 없습니다.");
				}
			}
		});
		
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(btnSearch, gbc);

		///
		xCoord = 0;
		yCoord++;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 8;
		panel.add(makeScheduleTablePane(), gbc);

		return panel;
	}

	JTextField m_ulspYear, m_ulspMonth, m_ulspDay;
	JComboBox<String> m_ulspCbNickNameList;
	
	private void resetUpdateLaundrySchedulePanel() {
		ArrayList<String> strNickName = DataStore.getNickNameList(); 

	    for (int index = 0; index < strNickName.size(); index++) {
	    	m_ulspCbNickNameList.addItem(strNickName.get(index));
	    }

    	Calendar calendar = Calendar.getInstance();

		m_ulspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
		m_ulspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
		m_ulspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));		
	}
	
	protected JComponent updateLaundrySchedule() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();
		
		ArrayList<String> strNickName = DataStore.getNickNameList(); 
		
		int xCoord = 0, yCoord = 0;

		gbc.weightx = 1.0;
		//gbc.weighty = 1.0;

		///
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("세탁물 별명: "), gbc);

	    String[] nickName = {  };
	    m_ulspCbNickNameList = new JComboBox<String>(nickName);
	    for (int index = 0; index < strNickName.size(); index++) {
	    	m_ulspCbNickNameList.addItem(strNickName.get(index));
	    }
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_ulspCbNickNameList, gbc);

		///		
		xCoord = 0;
		yCoord++;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("세탁완료일: "), gbc);
    	Calendar calendar = Calendar.getInstance();

		m_ulspYear = new JTextField(6);
		m_ulspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_ulspYear, gbc);
		
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("년"), gbc);

		m_ulspMonth = new JTextField(6);
		m_ulspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_ulspMonth, gbc);

		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("월"), gbc);

		m_ulspDay = new JTextField(6);
		m_ulspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_ulspDay, gbc);
		
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("일"), gbc);

		JButton btnReg = new JButton("등록");
		btnReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strNickName = (String) m_ulspCbNickNameList.getSelectedItem();
				int year, month, day;
				String strYear = m_ulspYear.getText().trim();
				if (strYear.length() < 1) {
					m_ulspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
					return;
				}
				try {
					year = Integer.parseInt(strYear);
					if (year < 0) {
						m_ulspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
						return;
					}
				} catch (NumberFormatException nfe) {
					m_ulspYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
					return;
				}
				
				String strMonth = m_ulspMonth.getText().trim();
				if (strMonth.length() < 1) {
					m_ulspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
					return;
				}
				try {
					month = Integer.parseInt(strMonth);
					if (month < 0) {
						m_ulspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
						return;
					}
				} catch (NumberFormatException nfe) {
					m_ulspMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
					return;
				}
				
				String strDay = m_ulspDay.getText().trim();
				if (strDay.length() < 1) {
					m_ulspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
					return;
				}
				try {
					day = Integer.parseInt(strDay);
					if (day < 0) {
						m_ulspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
						return;
					}
				} catch (NumberFormatException nfe) {
					m_ulspDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
					return;
				}
				
				if (DataStore.updateLaundry(strNickName, year, month, day) == true) {
					JOptionPane.showMessageDialog(m_mainFrame, "세탁 일정을 update 하였습니다.");
				} else {
					JOptionPane.showMessageDialog(m_mainFrame, "세탁 일정을 update 하는 과정에서 오류가 발생하였습니다.");
				}
			}
		});
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		panel.add(btnReg, gbc);

		return panel;		
	}

	JTable m_mctpClothesTable = null;
	DefaultTableModel m_mctpClothesTableEntry = null;
	
	private void resetListAllClothePanel() {
	    m_lacCbKind.setSelectedIndex(0);
		m_mctpClothesTableEntry.setRowCount(0);
	}
	
	private JScrollPane makeClothesTablePane() {
		m_mctpClothesTableEntry = new DefaultTableModel();      
		m_mctpClothesTable = new JTable(m_mctpClothesTableEntry);
		m_mctpClothesTable.getTableHeader().setReorderingAllowed(false);
		m_mctpClothesTableEntry.addColumn("별명");
		m_mctpClothesTableEntry.addColumn("종류");
		m_mctpClothesTableEntry.addColumn("등록일");
		m_mctpClothesTableEntry.addColumn("세탁주기");
		m_mctpClothesTableEntry.addColumn("마지막세탁일");
		m_mctpClothesTableEntry.addColumn("다음세탁일");
		m_mctpClothesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		m_mctpClothesTable.setBounds(10, 0, 457, 103);   

	    JScrollPane jsp = new JScrollPane(m_mctpClothesTable);
	    setJspSize(jsp);
	    jsp.setVisible(true);
		
	    return jsp;
	}
	
	JComboBox<String> m_lacCbKind;
	
	protected JComponent listAllClothes() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();
		
		int xCoord = 0, yCoord = 0;

		gbc.weightx = 1.0;
		//gbc.weighty = 1.0;

		///
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(new JLabel("세탁물 검색: "), gbc);

	    String[] clothesKind = { "상의", "하의", "전체" };
	    m_lacCbKind = new JComboBox<String>(clothesKind);
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		panel.add(m_lacCbKind, gbc);

		JButton btnSearch = new JButton("검색");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strCbKind = (String) m_lacCbKind.getSelectedItem();				
				int rowCount = DataStore.listClothes(m_mctpClothesTableEntry, strCbKind);
				if (rowCount > 0) {
					JOptionPane.showMessageDialog(m_mainFrame, "등록된 옷이 " + rowCount + "벌 있습니다.");
				} else {
					JOptionPane.showMessageDialog(m_mainFrame, "등록된 옷이 없습니다.");
				}
			}
		});
		
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(btnSearch, gbc);

		JButton btnResetDB = new JButton("데이터 초기화");
		btnResetDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataStore.flushTable();
				
				m_mctpClothesTableEntry.setRowCount(0);
				
				JOptionPane.showMessageDialog(m_mainFrame, "등록된 의류 정보를 모두 삭제하였습니다.");
			}
		});
		
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(btnResetDB, gbc);
		
		///
		xCoord = 0;
		yCoord++;
		gbc.gridx = xCoord++;
		gbc.gridy = yCoord;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 4;
		panel.add(makeClothesTablePane(), gbc);

		return panel;
	}

	private int width = 500;
	private int height = 300;
	
	private void setJspSize(JScrollPane jsp) {
	    jsp.setPreferredSize(new Dimension(width, height));
	    jsp.setMinimumSize(new Dimension(width, height));
	    jsp.setMaximumSize(new Dimension(width, height));
	    
	    JScrollBar vertical = jsp.getVerticalScrollBar();
	    vertical.setValue(vertical.getMaximum());
	}
	
	static JFrame m_mainFrame = null;
	public static void main(String[] args) {
		m_mainFrame = new JFrame("세탁 관리 프로그램");
		m_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m_mainFrame.add(new LaundayApp(), BorderLayout.CENTER);

		m_mainFrame.setSize(500, 400);
		m_mainFrame.setVisible(true);
	}
}