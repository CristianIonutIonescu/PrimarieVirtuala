package chat;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
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
    private JButton mSaveContent;
    @SuppressWarnings("rawtypes")
	private JList mList;
    private String[] mUserList = new String[]{};
    private ArrayList<String> mChatConversation = new ArrayList<String>();
    private boolean isWaitingForUserList = false;

    private Client mClient;
    private final SimpleDateFormat sdf;
    private final String ChatSubject;

    public ChatMainForm(String username,String chatSubject) throws HeadlessException {
        super("ChatMainForm");
        sdf = new SimpleDateFormat("yyyy-MM-dd'T' HH-mm-ss");
        initUI();
        startClient(username);
        refreshUserList();
        pack();
        ChatSubject = chatSubject;
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
        
    	mSaveContent = new JButton("SAVE");
    	gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 1;
        gbc.gridx = 3;
        mPanel.add(mSaveContent, gbc);
        mSaveContent.addActionListener(this);
        
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
    
    private void saveConversation() throws IOException{
    	 String fileName = sdf.format(new Date()) +"_" + ChatSubject + ".txt";
    	 
    	 Writer writer = null;

    	 try {
    	     writer = new BufferedWriter(new OutputStreamWriter(
    	           new FileOutputStream(fileName), "utf-8"));
    	     for(int i = 1 ;i<mChatConversation.size();i++){
    	    	 writer.write(mChatConversation.get(i));
    	    	 ((BufferedWriter) writer).newLine();
    	     }
    	 } catch (IOException ex) {
    	 } finally {
    	    try {writer.close();} catch (Exception ex) {/*ignore*/}
    	 }
    	 System.out.println(fileName);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == mSend && !Objects.equals(mNewMsg.getText(), "")) {

            mClient.sendMessage(new ChatMessage("BCAST", mNewMsg.getText()));
            mNewMsg.setText("");
            
        } else if(e.getSource() == mRefreshList) {
        	refreshUserList();
        } else if(e.getSource() == (JButton)mSaveContent){
				try {
					saveConversation();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public void onMessageReceived(String message) {
        if(!isWaitingForUserList) {
            mChatHistory.setText(mChatHistory.getText() + '\n' + message);
            mChatConversation.add(message + "\n");
            mScrollPane.scrollRectToVisible(mChatHistory.getBounds());
        } else {
            isWaitingForUserList = false;
            mUserList = message.split("\\n");
            mList.setListData(mUserList);
        }
    }



    private boolean startClient(String clientName) {
        int portNumber = 1700;
        String serverAddress = "localhost";
        mClient = new Client(serverAddress, portNumber, clientName, this,"User");
        return mClient.start();
    }
}
