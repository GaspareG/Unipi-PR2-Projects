package TwistClient;

import java.awt.* ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.swing.* ;

import TwistUtility.TwistAuthInterface;
import TwistUtility.TwistInviteInterface;
import TwistUtility.TwistJsonConf;

public class TwistClient {

	// GUI Frame
	private JFrame homeFrame = null ;
	private JFrame gameFrame = null ;
	private JFrame creditsFrame = null ;
	private JFrame scoreBoardFrame = null ;
	
	// Configuration data
	private TwistJsonConf ClientConf ;
	
	// Address and Port
	private String ServerURL = null;

	// RMI Registry
	private Registry registry = null ;
	
	// Auth object
	private Integer authRMIPort = null ;
	private TwistAuthInterface auth = null ;
	
	// Keep-Alive auth thread
	private Integer refreshSec = null ;
	private String sessionToken = null ;
	private Thread keepAlive ;
	
	// Scoreboard Task Thread
	private Integer ScoreboardPort ;
	private Thread scoreBoardTask = null ;
	
	// Lista di inviti <username,token-invito>
	private HashMap<String,String> inviti ;
	private DefaultListModel<String> modelInviti;
	private JList<String> lsInviti;
	
	// Giocatore già in partita
	private Boolean gameInProgress = false ;
	
	// Callback per aggiungere inviti
	private TwistInviteInterface callBackInviti ;
	
	private void createCreditsFrame()
	{
		if(creditsFrame != null)
		{
			creditsFrame.setVisible(false);
			creditsFrame.dispose();
			creditsFrame = null ;
		}

		creditsFrame = new JFrame(TwistClientStatic.sNameTitle + " - " + TwistClientStatic.sCredits);
		creditsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		creditsFrame.setLayout(null);
        
        JLabel lTitle  = new JLabel(TwistClientStatic.sNameTitle);
        JButton bClose = new JButton(TwistClientStatic.sClose);
        JLabel lCredits = new JLabel(TwistClientStatic.sCreditsText);

        lTitle.setVerticalAlignment(JTextField.CENTER); 
        lTitle.setHorizontalAlignment(JTextField.CENTER);
        lCredits.setVerticalAlignment(JTextField.TOP);
        lTitle.setFont(new Font("Arial",Font.PLAIN,55)); 
        lCredits.setFont(new Font("Arial",Font.PLAIN,20)); 
        lCredits.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        lTitle.setBounds(0, 0, 480, 120);
        bClose.setBounds(350,400,100,30);
        lCredits.setBounds(20,100,440,280);
       
        bClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				creditsFrame.dispose();
			}
        });
        
        creditsFrame.add(lTitle);
        creditsFrame.add(bClose);
        creditsFrame.add(lCredits);
        
		creditsFrame.setSize(480, 480);
		creditsFrame.setLocation(0,500);
		creditsFrame.setVisible(true);
        		
	}
	
	private void createScoreboardFrame()
	{
		if(scoreBoardFrame != null)
		{
			scoreBoardFrame.setVisible(false);
			scoreBoardFrame.dispose();
			scoreBoardFrame = null ;
		}

		scoreBoardFrame = new JFrame(TwistClientStatic.sNameTitle + " - " + TwistClientStatic.sScoreBoard);
		scoreBoardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		scoreBoardFrame.setLayout(null);
        
        JLabel   lTitle  = new JLabel(TwistClientStatic.sNameTitle);
        
        JButton bClose = new JButton("Close");
        
        JLabel lCredits = new JLabel("<html>Loading...</html>");

        lTitle.setVerticalAlignment(JTextField.CENTER); 
        lTitle.setHorizontalAlignment(JTextField.CENTER);
        lCredits.setVerticalAlignment(JTextField.TOP);
        lTitle.setFont(new Font("Arial",Font.PLAIN,55)); 
        lCredits.setFont(new Font("Arial",Font.PLAIN,20)); 
        lCredits.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        lTitle.setBounds(0, 0, 480, 120);
        bClose.setBounds(350,400,100,30);
        lCredits.setBounds(20,100,440,280);
        
        bClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				scoreBoardFrame.dispose();
			}
        });
        
        scoreBoardFrame.add(lTitle);
        scoreBoardFrame.add(bClose);
        scoreBoardFrame.add(lCredits);
        
		scoreBoardFrame.setSize(480, 480);
		scoreBoardFrame.setLocation(500,500);
		scoreBoardFrame.setVisible(true);
     
		// Avvio il task di caricamento classifica
		if( scoreBoardTask != null ) 
		{
			scoreBoardTask.interrupt();
			try {
				scoreBoardTask.join();
			} catch (InterruptedException e) {
				
			}
		}
		
		TwistScoreBoardTask task = new TwistScoreBoardTask(lCredits,ServerURL,ScoreboardPort);
		scoreBoardTask = new Thread(task);
		scoreBoardTask.start();
		
	}
	
	private void createGameFrame() throws RemoteException
	{
		if(gameFrame != null)
		{
			gameFrame.setVisible(false);
			gameFrame.dispose();
			gameFrame = null ;
		}
		
		gameFrame = new JFrame("TextTwist - Game Room");
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setLayout(null);
        
        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(gameFrame, 
                    "Are you sure to close this window?", "Really Closing?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                	try 
                	{
                		keepAlive.interrupt();
                		keepAlive.join();
                		auth.logout(sessionToken);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (RemoteException e) {
						e.printStackTrace();
					}

                    System.exit(0);
                }
                else
                {
                    gameFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        
        JLabel   lTitle  = new JLabel("TextTwist");
        JLabel   lInviti = new JLabel("Inviti");
        JLabel   lUsername = new JLabel(auth.getCurrentUser(sessionToken));
   
        JButton	 bScoreboard = new JButton("Scoreboard");
        JButton	 bCredits    = new JButton("Credits");
        JButton	 bAccept     = new JButton("Accept");
        JButton	 bDecline    = new JButton("Decline");
        JButton	 bNewInvite    = new JButton("Crea Partita");
                 
        JPanel   pGioco  = new JPanel();
        JPanel   pInviti = new JPanel();
        
        lsInviti = new JList<String>(modelInviti);
        
        pGioco.setLayout(null);
        pInviti.setLayout(null);
       
          pGioco.setBorder(BorderFactory.createLineBorder(Color.BLACK));
         pInviti.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lsInviti.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lsInviti.setLayoutOrientation(JList.VERTICAL);
          lTitle.setFont(new Font("Arial",Font.PLAIN,55)); 
       lUsername.setFont(new Font("Arial",Font.PLAIN,25)); 
         lInviti.setFont(new Font("Arial",Font.PLAIN,25)); 
         lInviti.setHorizontalAlignment(JTextField.CENTER);
         lInviti.setVerticalAlignment(JTextField.CENTER);
         lUsername.setVerticalAlignment(JTextField.CENTER);
         lUsername.setHorizontalAlignment(JTextField.CENTER);
         
             lTitle.setBounds(20,20,300,60);
          lUsername.setBounds(480, 20, 140, 30);
            lInviti.setBounds(0,10,140,20);
            bAccept.setBounds(20, 170,100,30);
            bDecline.setBounds(20,210,100,30);
            bNewInvite.setBounds(480, 60, 140, 30);
             pGioco.setBounds(20,100,440,330);
            pInviti.setBounds(480, 100, 140, 250);
           lsInviti.setBounds(10,40,120,120);
        bScoreboard.setBounds(490,360,120,30);
           bCredits.setBounds(490,400,120,30);

        gameFrame.add(lUsername);
        gameFrame.add(lTitle);
        gameFrame.add(bScoreboard);
        gameFrame.add(bCredits);
        gameFrame.add(bNewInvite);
        gameFrame.add(lInviti);
        gameFrame.add(pGioco);
        gameFrame.add(pInviti);

        pInviti.add(lInviti);
        pInviti.add(bDecline);
        pInviti.add(bAccept);
        pInviti.add(lsInviti);
        
        bCredits.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createCreditsFrame();
			}
        });
        
        bScoreboard.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createScoreboardFrame();
			}
        });

        bAccept.addActionListener(new ActionListener(){
        	@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO: Accetta
			}
        });
        bDecline.addActionListener(new ActionListener(){
        	@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO: Rifiuta
			}
        });
        
        bNewInvite.addActionListener(new ActionListener(){
        	@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO: Crea Invito
        		if( gameInProgress )
        		{
        			JOptionPane.showMessageDialog(gameFrame,
        				    "Game In Progress",
        				    "Error",
        				    JOptionPane.ERROR_MESSAGE);        			
        		}
        		else
        		{        		
		        	String s = (String)JOptionPane.showInputDialog(
		                    gameFrame,
		                    "Inserisci la lista degli utenti da invitare, separata da virgola",
		                    "New Game",
		                    JOptionPane.QUESTION_MESSAGE,
		                    null,
		                    null,
		        			"user1,user2,user3");
		        	gameInProgress = true ;
		        	
		        	// TODO invio notifica
        		}
        		
			}
        });
        
        gameFrame.setBackground(Color.WHITE);
        gameFrame.setSize(640, 480);
        gameFrame.setLocation(500, 0);
        gameFrame.setVisible(true);
        
	}
	
    private void createHome() 
    {
    
        homeFrame = new JFrame(TwistClientStatic.sNameTitle + " - " + TwistClientStatic.sHomePage );
        homeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        homeFrame.setLayout(null);
        
        JLabel            lTitle = new JLabel(TwistClientStatic.sNameTitle);
        JLabel         lUsername = new JLabel(TwistClientStatic.sUsername);
        JLabel         lPassword = new JLabel(TwistClientStatic.sPassword);
        JLabel          lCredits = new JLabel(TwistClientStatic.sDevelopedBy);
        JLabel            lError = new JLabel();
        JTextField     fUsername = new JTextField();
        JPasswordField fPassword = new JPasswordField();
        JButton           bLogin = new JButton(TwistClientStatic.sLogIn);
        JButton          bSignup = new JButton(TwistClientStatic.sSignUp);
        
          lTitle.setVerticalAlignment(JTextField.CENTER);
          lTitle.setFont(new Font("Arial",Font.PLAIN,55));   
          lTitle.setHorizontalAlignment(JTextField.CENTER);     
          lError.setHorizontalAlignment(JTextField.CENTER);
        lCredits.setHorizontalAlignment(JTextField.RIGHT);
        
        int height = 30 ;
        int width = 220 ;
        int left = (480-width)/2 ;
        int top = 120;
        
           lTitle.setBounds(0,0,500,top);
           lError.setBounds(0, top, 500, height);
        lUsername.setBounds(left           ,top+1*height   ,width     ,height);
        fUsername.setBounds(left           ,top+2*height   ,width     ,height);
        lPassword.setBounds(left           ,top+3*height   ,width     ,height);
        fPassword.setBounds(left           ,top+4*height   ,width     ,height);
           bLogin.setBounds(left+5         ,top+5*height+10,width/2-20,height);
          bSignup.setBounds(left+width/2+10,top+5*height+10,width/2-20,height);
        lCredits.setBounds(0,410, 460,30);

        homeFrame.add(lTitle);
        homeFrame.add(lError);
        homeFrame.add(lUsername);
        homeFrame.add(fUsername);
        homeFrame.add(lPassword);
        homeFrame.add(fPassword);
        homeFrame.add(bLogin);
        homeFrame.add(bSignup);
        homeFrame.add(lCredits);

        bLogin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String username = fUsername.getText();
				String password = new String(fPassword.getPassword());
				if(username == null || username.length() == 0 )
				{
					lError.setText(TwistClientStatic.sError + ": " + TwistClientStatic.sNoUsername);
					lError.setForeground(TwistClientStatic.sErrorColor);
				}
				else if(password == null || password.length() == 0 )
				{
					lError.setText(TwistClientStatic.sError + ": " + TwistClientStatic.sNoPassword);
					lError.setForeground(TwistClientStatic.sErrorColor);
				}
				else
				{

					try {
					
						// Invio la mia callback
						TwistInviteInterface cStub= (TwistInviteInterface) UnicastRemoteObject.exportObject(callBackInviti, 0);
						registry.rebind(TwistInviteInterface.REMOTE_OBJECT_NAME + "_" + username, cStub);
						
						// Tento il login tramite RMI
						sessionToken = auth.login(username, password);
						// Se il login avviene correttamente, avvio il thread di keep-alive
						if( sessionToken != null ) 
						{
							if( keepAlive != null && !keepAlive.isInterrupted() )
							{
								keepAlive.interrupt();
								keepAlive.join();
							}
							System.out.println("[Client] ok login, avvio keep-alive thread");
							TwistAuthKeepAlive ka = new TwistAuthKeepAlive(auth,refreshSec,sessionToken);
							
							keepAlive = new Thread(ka);
							keepAlive.start();
					
							homeFrame.dispose();
							createGameFrame();	
						}
						else
						{
							lError.setText(TwistClientStatic.sError + ": " + TwistClientStatic.sInvalidLogin);
							lError.setForeground(TwistClientStatic.sErrorColor);
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				
				}
			}
        });
        
        bSignup.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String username = fUsername.getText();
				String password = new String(fPassword.getPassword());
				if(username == null || username.length() == 0 )
				{
					lError.setText(TwistClientStatic.sError + ": " + TwistClientStatic.sNoUsername);
					lError.setForeground(TwistClientStatic.sErrorColor);
				}
				else if(username.length() < TwistClientStatic.sMinInputSize )
				{
					lError.setText(TwistClientStatic.sWarning + ": " + TwistClientStatic.sUsernameShort);
					lError.setForeground(TwistClientStatic.sWarningColor);
				}
				else if(password == null || password.length() == 0 )
				{
					lError.setText(TwistClientStatic.sError + ": " + TwistClientStatic.sNoPassword);
					lError.setForeground(TwistClientStatic.sErrorColor);
				}
				else if(password.length() < TwistClientStatic.sMinInputSize )
				{
					lError.setText(TwistClientStatic.sWarning + ": " + TwistClientStatic.sPasswordShort);
					lError.setForeground(TwistClientStatic.sWarningColor);
				}
				else
				{
					
					try {
						boolean okLogin = auth.signUp(username, password);
						// Se il sign-up avviene correttamente mostro conferma
						if( okLogin ) 
						{
							lError.setText(TwistClientStatic.sValidSignUp);
							lError.setForeground(TwistClientStatic.sOkColor);
						}
						else
						{
							lError.setText(TwistClientStatic.sError + ": " + TwistClientStatic.sInvalidSignUp);
							lError.setForeground(TwistClientStatic.sErrorColor);
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					} 			
				}
			}
        });
        
        homeFrame.setSize(480, 480);
        homeFrame.setVisible(true);
    }

    public TwistClient()
    {
    	// Carica i file di configurazione JSON
    	ClientConf = new TwistJsonConf(TwistClientStatic.sConfFileName);
    	
    	// Carica i dati dal json
    	ServerURL = ClientConf.getString("ServerURL");
    	authRMIPort = ClientConf.getLong("AuthRMIPort").intValue();
    	refreshSec = ClientConf.getLong("refreshSec").intValue();
    	ScoreboardPort = ClientConf.getLong("ScoreboardPort").intValue();
    	
    	// Inviti
    	inviti = new HashMap<String,String>();
        modelInviti = new DefaultListModel<>();
        modelInviti.addElement("ciao prova");
                
    	// Carica tramite RMI l'interfaccia per l'autenticazione
		try {
	    	
			callBackInviti = new TwistInvite();
	    	callBackInviti.setLista(inviti);
	        callBackInviti.setPanel(modelInviti);
			registry = LocateRegistry.getRegistry(ServerURL, authRMIPort);		
			auth = (TwistAuthInterface) registry.lookup(TwistAuthInterface.REMOTE_OBJECT_NAME);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}		
		
    	// Carico la Home
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createHome();
            }
        });
        
    }
    
    public static void main(String[] args) 
    {
    	new TwistClient();
    }
}
