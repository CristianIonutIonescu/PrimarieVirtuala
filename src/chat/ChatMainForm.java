package chat;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import servers.ChatMessage;


public class ChatMainForm extends JFrame implements ActionListener, Client.MessageListner {

	private static final long serialVersionUID = 1L;
	private JScrollPane mScrollPane;
    private JPanel mPanel;
    private JTextField mNewMsg;
    private JTextArea mChatHistory;
    private JButton mSend;
    private JButton mRefreshList;
    @SuppressWarnings("rawtypes")
	private JList mList;
    private String[] mUserList = new String[]{};
    private boolean isWaitingForUserList = false;

    private JRadioButton broadCastRadio = new JRadioButton("bcast");
    private JRadioButton privateCastRadio = new JRadioButton("priv");

    private Client mClient;

    public ChatMainForm() throws HeadlessException {
        super("ChatMainForm");
        initUI();
        startClient();
        refreshUserList();
        pack();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        mPanel=new JPanel();
        GridBagLayout layout = new GridBagLayout();
        mPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        mScrollPane = new JScrollPane();


        mNewMsg=new JTextField();
        mChatHistory=new JTextArea();
        mChatHistory.setEditable(false);
        mSend=new JButton("Send");

        mScrollPane.getViewport().add(mChatHistory);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 360;
        gbc.ipadx = 450;
        gbc.gridx = 0;
        gbc.gridy = 0;
        mPanel.add(mScrollPane, gbc);

        gbc.ipady = 20;
        gbc.ipadx = 240;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mPanel.add(mNewMsg, gbc);

        gbc.ipady = 10;
        gbc.ipadx = 30;
        gbc.gridx = 1;
        gbc.gridy = 1;
        mPanel.add(mSend, gbc);
        mSend.addActionListener(this);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(broadCastRadio);
        group.add(privateCastRadio);
        broadCastRadio.setSelected(true);

        //Put the radio buttons in a column in a panel.
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 2;
        mPanel.add(broadCastRadio, gbc);
        gbc.gridx = 1;
        mPanel.add(privateCastRadio, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        mList= new JList(mUserList);
        mList.setSize(200, 400);
        mRefreshList = new JButton("Refresh");
        mRefreshList.addActionListener(this);
        mList.setSelectedIndex(0);
        mList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        mPanel.add(new JScrollPane(mList), gbc);
       
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 1;
        gbc.gridx = 2;
        mPanel.add(mRefreshList, gbc);
        
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
            	mClient.sendMessage(new ChatMessage("QUIT", ""));
            }
        });
        add(mPanel);
    }


    private void refreshUserList() {
        isWaitingForUserList = true;
        mClient.sendMessage(new ChatMessage("LIST", ""));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == mSend && !Objects.equals(mNewMsg.getText(), "")) {

            if(privateCastRadio.isSelected()) {
                mClient.sendMessage(new ChatMessage("MSG",mUserList[mList.getSelectedIndex()] + " " + mNewMsg.getText()));
            } else {
                mClient.sendMessage(new ChatMessage("BCAST", mNewMsg.getText()));
            }
            mNewMsg.setText("");
            
        } else if(e.getSource() == mRefreshList) {
        	refreshUserList();
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public void onMessageReceived(String message) {
        if(!isWaitingForUserList) {
            mChatHistory.setText(mChatHistory.getText() + '\n' + message);
            mScrollPane.scrollRectToVisible(mChatHistory.getBounds());
        } else {
            isWaitingForUserList = false;
            mUserList = message.split("\\n");
            mList.setListData(mUserList);
        }
    }



    private boolean startClient() {
        int portNumber = 1700;
        String serverAddress = "localhost";
        String userName = "User" + (int) (Math.random() * 100000);
        mClient = new Client(serverAddress, portNumber, userName, this,"User");
        return mClient.start();
    }
}
