import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class ChatDialog extends JDialog implements ActionListener{

	JFrame parentFrame;
	JEditorPane editor;
	JButton chat_sendButton;
	JTextField inputField;
	PrintWriter writer;
	Socket socket;

	public static String SEND_DIRECT_MESSAGE="5ae3846ad06a415b8810441bba46dbda";

	public ChatDialog(JFrame frame,Socket socket,String title){
		super(frame,title,Dialog.ModalityType.MODELESS);
		parentFrame=frame;
		try{
			writer = new PrintWriter(socket.getOutputStream(),true);	
		}catch(IOException exp){
			System.out.println(exp.getMessage());
		}
		

		setSize(350,350);
		setLocationRelativeTo(parentFrame);

		int bottomHeight=35;
		int bottomWidth=50;
		Dimension d=new Dimension(bottomWidth,bottomHeight);

		JPanel p=new JPanel(new BorderLayout());
		editor=new JEditorPane();
		editor.setEditable(false);
		editor.setFont(new Font("Arial",Font.PLAIN,14));
		editor.setText("Connected..");
		editor.setBackground(new Color(226,226,226));
		editor.setForeground(new Color(43,43,43));

		JScrollPane sp=new JScrollPane(editor);

		JPanel bottomPane=new JPanel(new GridBagLayout());
		chat_sendButton=new JButton("Send");
		chat_sendButton.addActionListener(this);
		chat_sendButton.setPreferredSize(d);

		inputField=new JTextField(100);
		inputField.setPreferredSize(d);
		inputField.addActionListener(this);

		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridx=0;	gbc.gridy=0;	gbc.gridheight=1;	gbc.gridwidth=1;	gbc.anchor=GridBagConstraints.CENTER;
		gbc.weightx=1.0;	gbc.weighty=1.0; 	
		gbc.fill=GridBagConstraints.BOTH;
		bottomPane.add(inputField,gbc);
		
		gbc.weightx=0;	gbc.weighty=0; 	
		gbc.gridx=1;	
		gbc.fill=GridBagConstraints.VERTICAL;
		bottomPane.add(chat_sendButton,gbc);

		p.add(sp,"Center");
		p.add(bottomPane,"South");
		add(p);

		setVisible(true);
	}

	public void write2Editor(String in){
		write2Editor(in,false);
	}

	public void write2Editor(String in,boolean nextLine){
		if(nextLine){
			editor.setText(editor.getText()+"\n"+in+"\n");
		}else{
			editor.setText(editor.getText()+"\n"+in);
		}
		inputField.requestFocus();
		editor.setCaretPosition(editor.getText().length());
	}

	public void actionPerformed(ActionEvent event){
		if(event.getSource()==chat_sendButton||event.getSource()==inputField){
			String text=inputField.getText();
			if(text.trim().length()==0){
				return;
			}

			writer.println(SEND_DIRECT_MESSAGE+" "+getTitle()+" "+text);
			write2Editor("Me: "+inputField.getText());
			inputField.setText("");
		}
	}

}