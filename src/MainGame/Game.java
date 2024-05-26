package MainGame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.FileReader;


public class Game {
	private static Connection cn = Connect.getConnection();
	Player j1 = new Player("premier joueur");
	Player j2 = new Player("deuxieme joueur");
    private static Timer timer;
    private static long startTime1;
    private static long startTime2;
    private static long endTime1;
    private static long endTime2;
    private static int roundCounter=0;
    private static int count;
    private static int nombre_mots_p1;
    private static int nombre_mots_p2;
    private static JProgressBar progressBar1;
    private static JProgressBar progressBar2;
    private static ArrayList<Character> Chars1 = new ArrayList<>();
    private static ArrayList<Character> Chars2 = new ArrayList<>();
	private static JButton valider1 = new JButton("Valider");
	private static JButton valider2 = new JButton("Valider");
	private static JPanel NorthPan = new JPanel(){
        @Override
        public boolean isFocusable() {
            return true; 
        }
    };
	private static JFrame f=new JFrame("Game");
	private static JTextField txt1=new JTextField("");
	private static JTextField txt2=new JTextField("");
	private static String list = new String();
	
    public Main() {
    	
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800, 600);
    	f.setLayout(new BorderLayout());
    	f.setLocationRelativeTo(null);
        progressBar1 = new JProgressBar(0, 100);
        progressBar1.setStringPainted(true);
        progressBar2 = new JProgressBar(0, 100); 
        progressBar2.setStringPainted(true);
    	
    	
    	
    	f.add(NorthPan,BorderLayout.NORTH);
    	JPanel jp =new JPanel(new GridLayout(1,2)) ;

    	JPanel jp1=new JPanel();
    	JPanel jp2=new JPanel();    	    	   	
    	txt1.setPreferredSize(new Dimension(150,30));
    	txt1.setName("txt1");
    	txt2.setPreferredSize(new Dimension(150,30));
    	txt2.setName("txt2");
           addNamesToTable();
    
    	GenererChar();
        
   JInternalFrame Ji1 =createInternalFrame(j1.nom);
   
   startTimer(valider1,txt1,valider2,txt2,progressBar1);
   startTime1 = System.currentTimeMillis();
   JInternalFrame Ji2 =createInternalFrame(j2.nom);

   DisableEnable(txt2,valider2,false);
   
  
   valider1.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent e) {
		   stopTimer();
		   DisableEnable(txt1,valider1,false);
		   DisableEnable(txt2,valider2,true);
		   enableButtons(NorthPan,true);
			  
			  startTimer(valider2,txt2,valider1,txt1,progressBar2);
			  Chars2.clear();
			  int scoreRoundplayer1 = calculateScore(txt1);
			  updateScore(scoreRoundplayer1,0);

			  txt1.setText("");
			  endTime1 = System.currentTimeMillis();
			  startTime2 = System.currentTimeMillis();
              ajoutTemps(startTime1,endTime1,"1");
	   }
   });
   valider2.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent e) {
		   roundCounter++;
		   stopTimer();
		   DisableEnable(txt1,valider1,true);
		   DisableEnable(txt2,valider2,false);
		   enableButtons(NorthPan,true);
			 
			 startTimer(valider1,txt1,valider2,txt2,progressBar1);
           Chars1.clear();
           int scoreRoundplayer2 = calculateScore(txt2);
           updateScore(0,scoreRoundplayer2);
           txt2.setText("");
           endTime2 = System.currentTimeMillis();
           ajoutTemps(startTime2,endTime2,"2");
           
           if(roundCounter!=3) {
        	   startTime1 = System.currentTimeMillis();  		   
           }
		   
           showRecap();
	   }
   });
  

   jp1.add(txt1);
   restrictTextFieldInput(txt1,list,Chars1);
   TextTest(txt1,Chars1);
   
   jp1.add(valider1);
   jp1.add(progressBar1);
   jp2.add(txt2);
   restrictTextFieldInput(txt2,list,Chars2);
   TextTest(txt2,Chars2);
  
   
   jp2.add(valider2);
   jp2.add(progressBar2);
   Ji1.add(jp1);
   Ji2.add(jp2);
   jp.add(Ji1);
   jp.add(Ji2);
      
        f.add(jp,BorderLayout.CENTER);
        f.setVisible(true);
        
       
    }
    
    
    
    //Generer les bouttons (caracteres)
	  public static void GenererChar() {
		  ArrayList<Character> voy=new ArrayList<Character>();
	         voy.add('a');
	         voy.add('e');
	         voy.add('u');
	         voy.add('i');
	         voy.add('y');
	         voy.add('à');
	         voy.add('â');
	         voy.add('é');
	         voy.add('è');
	         voy.add('ê');
	         voy.add('ï');	         
	         voy.add('ô');

	         
	    	ArrayList<Character> cons=new ArrayList<Character>();
	    	String conss = new String("bcdfghjklmnpqrstvwxzç");
	    	  for (char c : conss.toCharArray()) {
	              cons.add(c);
	          }

	  
	  int test = 0;
	  //////////////test the characters///////////////////////////////
	while(test==0) {
  	list = new String(generateRandomString(15,"abcdefghijklmnopqrstuvwxyzàâçéèï"));
  	int compvoy =0;
  	int compcons =0;
    for(char c:voy) {        	  
  	 if(countOccurrences(list,c)>3) {
  		 compvoy++;
    	 }       	 
    }
    for(char c:cons) {        	  
   	 if(countOccurrences(list,c)>2) {
   		 compcons++;
     	 }       	 
     }
    
    if(compvoy==0&&compcons==0) {
  	  test=1;
    }
    
	}
	
    for (char c : list.toCharArray()) {
        JButton button = new JButton(String.valueOf(c));
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform actions when the button is clicked
                
                button.setEnabled(false);
                if(txt1.isEnabled()) {
                	Chars1.add(button.getText().charAt(0));
                }else {
                	Chars2.add(button.getText().charAt(0));
                }
                
                String string = String.valueOf(c);
                if(valider1.isEnabled()) {
                	String or1 =txt1.getText();
                	txt1.setText(or1+string);
                }else {
                String or2 =txt2.getText();
                	txt2.setText(or2+string);
                }
                
            }
        });
        NorthPan.add(button);    
    }
    
	}


//Generer des JInternalFrame
    private static JInternalFrame createInternalFrame(String title) {
        JInternalFrame internalFrame = new JInternalFrame(title, true, true, true, true);
        internalFrame.setSize(200, 150);
        internalFrame.setLocation(50, 50);



        internalFrame.setVisible(true);
        return internalFrame;
    }
   
    //Generation d'une chaine des caractères aléatoires
    public static String generateRandomString(int length, String characters) {
        if (length <= 0 || characters.isEmpty()) {
            throw new IllegalArgumentException("Paramètres non valides");
        }

        Random random = new Random();
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
    //Desactiver ou activer le champ de joueur
    private static void DisableEnable(JTextField txt,JButton b, boolean enabled) {
        txt.setEnabled(enabled);
        b.setEnabled(enabled);
       
    }
    //Control sur l'etat des bouttons des lettres aléatoires
    private static void enableButtons(JPanel panel, boolean enabled) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                ((JButton) component).setEnabled(enabled);
            }
        }
    }
    //Control sur le temps alloué
    private static void startTimer(JButton Valider1,JTextField Txt1,JButton Valider2,JTextField Txt2,JProgressBar progressBar) {
    	if(roundCounter==3) {
    		if(valider2.isEnabled()) {
    			showRecap();
    		}
    		stopTimer();
            DisableEnable(txt1,valider1,false);
            DisableEnable(txt2,valider2,false);
           
    	}else {
        count = 0;
        progressBar.setValue(0);

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (count <= 100) {
                    
                    progressBar.setValue(count);
                    count++;
                } else {
                  
                        
                    stopTimer();
                    DisableEnable(Txt1,Valider1,false);
                    DisableEnable(Txt2,Valider2,true);
                    Txt1.setText("");
                    String str1 = Txt1.getName();
                    String player =new String("") ;
                    ajoutTemps(0,4*60,player+str1.charAt(str1.length()-1));
               
                	if(progressBar==progressBar2) {
                		enableButtons(NorthPan,true);
                		startTimer(valider1,txt1,valider2,txt2,progressBar1);
                		
                	}else {
                		enableButtons(NorthPan,true);
                		startTimer(valider2,txt2,valider1,txt1,progressBar2);
                		roundCounter++;
                		}
                		
                    
                }
                
            }
        }, 0, 2400);
    }
    	}
    private static void stopTimer() {
        // Stop the timer
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }


    }
    //Control sur le text à taper par les joueurs
    private static void restrictTextFieldInput(JTextField textField, String allowedLetters,ArrayList<Character> Chars) {
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char typedChar = e.getKeyChar();
                

                if (allowedLetters.indexOf(typedChar) == -1||Chars.contains(typedChar)) {
                  
                    e.consume();

                    
                }else {
                	
                
                   Component[] components = NorthPan.getComponents();
                    int count =0;
                    for (Component component : components) {
                         if (component instanceof JButton) {                	
                             JButton button = (JButton) component;
                             
                             if(button.getText().charAt(0)==typedChar && button.isEnabled()==false) {
                                
	                                 count++;
                                                               	
                             }
                            	 
                             }
                         }
                    if(count==countOccurrences(list,typedChar)) {
                    	Chars.add(typedChar);
                    }
                }

                

                }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
              
            }}
               );
    }
    //Mise à jour d'etat de boutton
    private static void updateButtonState(JPanel panel, char typedChar,Boolean state) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.isEnabled()==!state && button.getText().charAt(0) == typedChar) {
                    button.setEnabled(state);
                    break; 
                }
            }
        }
    }

   //Calcul d'occurence de char dans un String
    private static int countOccurrences(String input, char target) {
        int count = 0;

        for (char c : input.toCharArray()) {
            if (c == target) {
                count++;
            }
        }

        return count;
    }
    //Control sur le clavier pour Le mot taper
    private static void TextTest(JTextField txt,ArrayList<Character> Chars) {
    	   txt.addKeyListener(new KeyListener() {

    		   @Override
    		   public void keyTyped(KeyEvent e) { }

    		   @Override
    		   public void keyPressed(KeyEvent e) {
    		   if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) {
    	            String str =txt.getText();
    	            	
    			   if (!(str == null || str.isEmpty())){
    				   char but = str.charAt(str.length()-1);
    	           	
    				   updateButtonState(NorthPan,but,true);
    				   txt.setText(str.substring(0, str.length() - 1));
    				
    	  
    				   int index=0;
    			        int count = 0;

    			        for (int value : Chars) {	            
    			            	if(value != but) {
    			                count++;
    			            }else {
    			            	index=count;
    			            	break;
    			            }
    			        }
    			        Chars.remove(index); 
    		        }
    			  
    	  
    		        
    		      
    		   }else {
    	           
    			   char typedChar = e.getKeyChar();
    	           updateButtonState(NorthPan, typedChar,false);
    	       }
    		   if(e.getKeyCode() ==KeyEvent.VK_BACK_SPACE) {
    			   String str =txt.getText();
    			   char but = str.charAt(str.length()-1);
    	          	
    			   updateButtonState(NorthPan,but,true);
    			   int index=0;
			        int count = 0;

			        for (int value : Chars) {	            
			            	if(value != but) {
			                count++;
			            }else {
			            	index=count;
			            	break;
			            }
			        }
			        Chars.remove(index);
    		   }
    		   }

    		   @Override
    		   public void keyReleased(KeyEvent e) { }

    		   });
    }
    //Test si le mot existe dans le dictionnaire
    private static boolean isWordInDictionary(String word) {
        try (BufferedReader reader = new BufferedReader(new FileReader("PATH_TO_YOUR_WORDS_FILE.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                if (line.trim().equalsIgnoreCase(word)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    //Mise à jour de score aprés chaque tour
    public void updateScore(int Score_p1,int Score_p2) {
    	try {
    		PreparedStatement ps=cn.prepareStatement("update game set Score_p1=Score_p1+?,Score_p2=Score_p2+? where GameID=(select max(GameID) from game)");
    		
    		
    		ps.setInt(1,Score_p1);
    		ps.setInt(2, Score_p2);
    		ps.executeUpdate();
    		
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    //calculer les points à ajouter après round
    public static int calculateScore(JTextField txt) {
    	int score;
    	String str = txt.getText();
    	String name = txt.getName();
    	char player = name.charAt(name.length()-1);
    	
    	if(isWordInDictionary(str)) {
    		score =str.length();
    		if(player=='1') {
    			nombre_mots_p1 ++;
    		}else {
    			nombre_mots_p2 ++;
    		}
    	}else {
    		score=0;
    	}
    	
    	return score;
    	
    }
    //Ajout des noms de joueurs a la table
    public void addNamesToTable() {
    	try {
    		PreparedStatement ps=cn.prepareStatement("insert into game(Player1,Player2) values(?,?)");
    		ps.setString(1,j1.nom);
    		ps.setString(2, j2.nom);
    		ps.executeUpdate();
    		
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    //Affichage de recap après le jeu
    public static void showRecap() {
    	 if(roundCounter==3) {
        	 
        	 JFrame recap = new JFrame("Recap");
        	 recap.setLayout(new BorderLayout());
        	 recap.setSize(500,500);
        	 recap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	 recap.setLocationRelativeTo(null);
        	 JPanel pan = new JPanel(new GridLayout(1,2));
        	 JPanel pan1 = new JPanel(new GridLayout(4,1));
        	 JPanel pan2 = new JPanel(new GridLayout(4,1));
        	 pan.add(pan1);
        	 pan.add(pan2);
        	 
        	 JLabel pl1 = new JLabel();

        	 JLabel Score_pl1 = new JLabel();
        	 JLabel temps_p1 = new JLabel();
        	 JLabel Mots1 = new JLabel("     Nombre de Mots Correctes : "+nombre_mots_p1);
        	 pan1.add(pl1);
        	 pan1.add(Score_pl1);
        	 pan1.add(temps_p1);
        	 pan1.add(Mots1);
        	 JLabel pl2 = new JLabel();

        	 JLabel Score_pl2 = new JLabel();
        	 JLabel temps_p2 = new JLabel();
        	 JLabel Mots2 = new JLabel("     Nombre de Mots Correctes : "+nombre_mots_p2);

        	 pan2.add(pl2);
        	 pan2.add(Score_pl2);
        	 pan2.add(temps_p2);
        	 pan2.add(Mots2);
        	 JPanel SouthPan = new JPanel(new FlowLayout(FlowLayout.CENTER));
        	 JLabel gagnant = new JLabel();
        	 SouthPan.add(gagnant);
        	 
        	 recap.add(SouthPan,BorderLayout.SOUTH);
        	 recap.add(pan,BorderLayout.CENTER);
        	 recap.setVisible(true);
         	try {
        		PreparedStatement ps=cn.prepareStatement("SELECT * from game where GameID=(select max(GameID) from game)");
        		
        		ResultSet res = ps.executeQuery();
        		res.next();
        		String player1 = res.getString(2);
        		String player2 = res.getString(3);
        		int score_p1 = res.getInt(4);
        		int score_p2 = res.getInt(5);
        		int tmps_p1 = res.getInt(6);
        		int tmps_p2 = res.getInt(7);
        		pl1.setText("     Joueur 1 : "+player1);
        		pl2.setText("     Joueur 2 : "+player2);
        		Score_pl1.setText("     Score : "+score_p1);
        		Score_pl2.setText("Score : "+score_p2);
        		temps_p1.setText("     Temps Utilisé : "+tmps_p1+" secondes");
        		temps_p2.setText("     Temps Utilisé : "+tmps_p2+" secondes");
        	if(score_p1	>score_p2) {
        		gagnant.setText("****************************  Gagnant   :   " +player1+"   ****************************");
        	}else if(score_p1	<score_p2) {
        		gagnant.setText("****************************  Gagnant   :   " +player2+"   ****************************");
        	}else if(nombre_mots_p1>nombre_mots_p2) {
        		gagnant.setText("****************************  Gagnant   :   " +player1+"   ****************************");

        	}else if(nombre_mots_p1<nombre_mots_p2) {
        		gagnant.setText("****************************  Gagnant   :   " +player2+"   ****************************");

        	}else if(tmps_p1<tmps_p2) {
        		gagnant.setText("****************************  Gagnant   :   " +player1+"   ****************************");
        	}else if(tmps_p1>tmps_p2) {
        		gagnant.setText("****************************  Gagnant   :   " +player2+"   ****************************");

        	}else {
        		gagnant.setText("****************************   Match Null!!   ****************************");

        	}
        	;
        	}catch(Exception ex) {
        		ex.printStackTrace();
        	}

         }
    }
    //Ajout de temps utilisé ala base de donnees
    public static void ajoutTemps(long start,long end,String player) {
    	long tps=(end - start)/1000;
    	
    	String str = "temps_p"+player;
    	String statement = "Update game  set "+str+" = "+str+" +"+tps+" where GameID=(select max(GameID) from game)";
    	try {
    		PreparedStatement ps =cn.prepareStatement(statement);
    		
    	
    		ps.executeUpdate();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
public static void main(String [] args) {
     Main g = new Game();
}
}
