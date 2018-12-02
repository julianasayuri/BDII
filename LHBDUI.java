
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Calendar;
import javax.swing.*;

public class LHBDUI extends JFrame implements ActionListener{
	private Connection con;
	private Statement stmt;
	
    private JMenu arquivo;
    private JMenu cliente;
    private JMenu func;
    private JMenu comp;
    
    
    private JMenuBar barra;
    private DecoratedDesktopPane desktop;
    
    private JMenuItem menuCriar;
    private JMenuItem menuSair;
    private JMenuItem menuRemover;
    private JMenuItem associa;
    private JMenuItem menuConsultaA;
    private JMenuItem menuInsereC;
    private JMenuItem menuConsultaC;
    private JMenuItem menuInsereF;
    private JMenuItem menuConsultaF;
    private JMenuItem menuInsereCo;
    private JMenuItem menuConsultaCo;
    
	
	JanelaConsultaC janelaConsultaC;
    JanelaConsultaF janelaConsultaF;
    JanelaConsultaCo janelaConsultaCo;
    JanelaConsultaA janelaConsultaA;
	
    public LHBDUI() {
		super("Gerenciador de Banco de Dados da CyberNet");
		barra = new JMenuBar();
        arquivo = new JMenu("Arquivo");
        cliente = new JMenu("Cliente");
        func = new JMenu("Funcionário");
        comp = new JMenu("Computador");
       
        
        //menuCriar = new JMenuItem("Criar Tabelas"); OBSOLETO
        menuRemover = new JMenuItem("Remover");
        menuSair = new JMenuItem("Sair");
        associa = new JMenuItem("Associar Computador");
        menuConsultaA = new JMenuItem("Ver Computadores Associados");
        menuInsereC = new JMenuItem("Inserir");
        menuConsultaC = new JMenuItem("Consultar");
        menuInsereF = new JMenuItem("Inserir");
        menuConsultaF = new JMenuItem("Consultar");
        menuInsereCo = new JMenuItem("Inserir");
        menuConsultaCo = new JMenuItem("Consultar");
        
        //arquivo.add(menuCriar); OBSOLETO
        arquivo.add(menuRemover);
        arquivo.add(menuSair);
        cliente.add(menuInsereC);
        cliente.add(menuConsultaC);
        cliente.add(associa);
        cliente.add(menuConsultaA);
        func.add(menuInsereF);
        func.add(menuConsultaF);
        comp.add(menuInsereCo);
        comp.add(menuConsultaCo);
        
        desktop = new DecoratedDesktopPane();
        
        iniciaBD();

        barra.add(arquivo);
        barra.add(cliente);
        barra.add(func);
        barra.add(comp);
        
        menuSair.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent evento) {
				System.exit(0);
			}
		});
		
		//menuCriar.addActionListener(this); OBSOLETO
		menuInsereC.addActionListener(this);
		menuConsultaC.addActionListener(this);
		menuInsereF.addActionListener(this);
		menuConsultaF.addActionListener(this);
		menuInsereCo.addActionListener(this);
		menuConsultaCo.addActionListener(this);
		associa.addActionListener(this);
		menuConsultaA.addActionListener(this);
		menuRemover.addActionListener(this);
		
        setJMenuBar(barra);
        add(desktop);
        
        setSize(1280,720);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        janelaConsultaC = new JanelaConsultaC(desktop, con);  
		desktop.add(janelaConsultaC);  
		janelaConsultaC.setVisible(false);
		
		janelaConsultaF = new JanelaConsultaF(desktop, con);  
		desktop.add(janelaConsultaF);  
		janelaConsultaF.setVisible(false);
		
		janelaConsultaCo = new JanelaConsultaCo(desktop, con);  
		desktop.add(janelaConsultaCo);  
		janelaConsultaCo.setVisible(false);
		
		janelaConsultaA = new JanelaConsultaA(desktop, con);  
		desktop.add(janelaConsultaA);  
		janelaConsultaA.setVisible(false);  
    }
    
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuInsereC) {
			new JanelaCadastraCliente(desktop, con);
		} else if (e.getSource() == menuConsultaC) {
			janelaConsultaC.setVisible(true);
		} else if (e.getSource() == menuInsereF) {
			new JanelaCadastraFunc(desktop, con);
		} else if (e.getSource() == menuInsereCo) {
			new JanelaCadastraPC(desktop, con);
		} else if (e.getSource() == menuConsultaF) {
			janelaConsultaF.setVisible(true);
		} else if (e.getSource() == menuConsultaCo) {
			janelaConsultaCo.setVisible(true);
		} else if (e.getSource() == associa) {
			new JanelaAssocia(desktop, con);
		} else if (e.getSource() == menuRemover) {
			new JanelaRemove(desktop, con);
		} else if (e.getSource() == menuConsultaA) {
			janelaConsultaA.setVisible(true);
		}
	}
	
	void iniciaBD() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@200.145.158.211:1521:xe", "hcpavan", "151025088");
			stmt = con.createStatement();
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "O driver do banco de dados não foi encontrado.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Erro na iniciação do acesso ao banco de dados\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		new LHBDUI();
	}
}

class DecoratedDesktopPane extends JDesktopPane {
	Color fundo = new Color(30, 30, 30);
	Calendar data;
	int hor;
	int min;
	int seg;
	String hora = new String();
	private Image img;
	public DecoratedDesktopPane() {
		try {
			img = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource("logo.png"), "logo.png"));
		} catch (Exception ex) {}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(fundo);
        g.fillRect(0, 0, getWidth(), getHeight());
		Timer timer = new Timer (100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				data = Calendar.getInstance();
				hor = data.get(Calendar.HOUR_OF_DAY); 
				min = data.get(Calendar.MINUTE);
				seg = data.get(Calendar.SECOND);
				if(hor < 10) hora = "0" + hor + ":" + min + ":" + seg;
				if(min < 10) hora = hor + ":0" + min + ":" + seg;
				if(min < 10 && hor < 10) hora = "0" + hor + ":0" + min + ":" + seg;
				if(min > 10 && hor > 10) hora = hor + ":" + min + ":" + seg;
				getToolkit().sync();
				revalidate();
				repaint();
			}
		});
		timer.start();
		if (img != null) {
			Dimension dimension = this.getSize();
			int x = (int)(dimension.getWidth() - img.getWidth(this));
			int y = (int)(dimension.getHeight() - img.getHeight(this));
			g.drawImage(img, x, y, img.getWidth(this), img.getHeight(this), this);
			x = (int)(dimension.getWidth() - 150);
			g.setFont(new Font("Arial",1, 30));
			g.drawString(hora, x, 30);
		} else {
			g.drawString("Imagem nao encontrada", 50, 50);
		}
		
	}
}

class JanelaCadastraCliente extends JInternalFrame {
  PreparedStatement pStmt;
  JDesktopPane desktop;
  JButton bt1;
  JTextField cpf, nome, email, tel, rua, numero, cidade, uf, cep;

  public JanelaCadastraCliente(JDesktopPane d, Connection con) {
    super("Insere na tabela Cliente", true, true, false, true); //resizable, closable, maximizable, iconifiable
    desktop = d;
    try {
	  pStmt = con.prepareStatement("INSERT INTO CLIENTES VALUES (?, T_PESSOA (?, ?, ?), T_ENDERECO(?, ?, ?, ?, ?))");

      setLayout(new FlowLayout());
      add(new JLabel("CPF: "));
      add(cpf = new JTextField(11));
      add(new JLabel("Nome: "));
      add(nome = new JTextField(20));
	  add(new JLabel("Telefone: "));
      add(tel = new JTextField(12));
	  add(new JLabel("E-mail: "));
      add(email = new JTextField(20));
      add(new JLabel("Rua: "));
      add(rua = new JTextField(20));
	  add(new JLabel("Numero: "));
      add(numero = new JTextField(10));
	  add(new JLabel("Cidade: "));
      add(cidade = new JTextField(25));
	  add(new JLabel("Estado (UF): "));
      add(uf = new JTextField(5));
	  add(new JLabel("CEP: "));
      add(cep = new JTextField(10));
      
      add(bt1 = new JButton("Insere"));
      setPreferredSize(new Dimension(500, 200));
      pack();
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setVisible(true);
      desktop.add(this);

      bt1.addActionListener(new ActionListener() {    //classe interna listener sem nome
        public void actionPerformed(ActionEvent e) {
          try {
            pStmt.setLong(1, Long.parseLong(cpf.getText()));
            pStmt.setString(2, nome.getText());
			pStmt.setLong(3, Long.parseLong(tel.getText()));
			pStmt.setString(4, email.getText());
			pStmt.setString(5, rua.getText());
			pStmt.setInt(6, Integer.parseInt(numero.getText()));
			pStmt.setString(7, cidade.getText());
            pStmt.setString(8, uf.getText());
			pStmt.setInt(9, Integer.parseInt(cep.getText()));
            cpf.setText("");
            nome.setText("");
            tel.setText("");
			email.setText("");
			rua.setText("");
			numero.setText("");
			cidade.setText("");
			uf.setText("");
			cep.setText("");
            pStmt.executeUpdate();
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
          }
        }
      });

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void finalize() {
    try {
      pStmt.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}

class JanelaCadastraFunc extends JInternalFrame {
  PreparedStatement pStmt;
  JDesktopPane desktop;
  JButton bt1;
  JTextField carteira, nome, endereco, sal, func;

  public JanelaCadastraFunc(JDesktopPane d, Connection con) {
    super("Insere na tabela Funcionario", true, true, false, true); //resizable, closable, maximizable, iconifiable
    desktop = d;
    try {
      pStmt = con.prepareStatement("INSERT INTO FUNCIONARIO VALUES (?, ?, ?, ?, ?)");

      setLayout(new FlowLayout());
      add(new JLabel("Carteira: "));
      add(carteira = new JTextField(20));
      add(new JLabel("Nome: "));
      add(nome = new JTextField(20));
      add(new JLabel("Endereço: "));
      add(endereco = new JTextField(20));
      add(new JLabel("Salário: "));
      add(sal = new JTextField(10));
      add(new JLabel("Função Principal: "));
      add(func = new JTextField(20));
      add(bt1 = new JButton("Insere"));
      setPreferredSize(new Dimension(580, 125));
      pack();
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setVisible(true);
      desktop.add(this);

      bt1.addActionListener(new ActionListener() {    //classe interna listener sem nome
        public void actionPerformed(ActionEvent e) {
          try {
            pStmt.setInt(1, Integer.parseInt(carteira.getText()));
            pStmt.setString(2, nome.getText());
            pStmt.setString(3, endereco.getText());
            pStmt.setDouble(4, Double.parseDouble(sal.getText()));
            pStmt.setString(5, func.getText());
            carteira.setText("");
            nome.setText("");
            endereco.setText("");
            sal.setText("");
            func.setText("");
            pStmt.executeUpdate();
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
          }
        }
      });

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void finalize() {
    try {
      pStmt.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}

class JanelaCadastraPC extends JInternalFrame {
  PreparedStatement pStmt;
  JDesktopPane desktop;
  JButton bt1;
  JTextField nome;
  JComboBox tipo;

  public JanelaCadastraPC(JDesktopPane d, Connection con) {
    super("Insere na tabela Computador", true, true, false, true); //resizable, closable, maximizable, iconifiable
    desktop = d;
    try {
      pStmt = con.prepareStatement("INSERT INTO COMPUTADOR VALUES (?, ?)");

      setLayout(new FlowLayout());
      add(new JLabel("Nome de Rede: "));
      add(nome = new JTextField(20));
      add(new JLabel("Tipo: "));
      JComboBox<String> tipo = new JComboBox<String>();
	  tipo.addItem("Comum");
      tipo.addItem("Gamer");
      add(tipo);
      add(bt1 = new JButton("Inserir"));
      pack();
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setVisible(true);
      desktop.add(this);

      bt1.addActionListener(new ActionListener() {    //classe interna listener sem nome
        public void actionPerformed(ActionEvent e) {
          try {
            pStmt.setString(1, nome.getText());
            pStmt.setInt(2, tipo.getSelectedIndex());
            nome.setText("");
            tipo.setSelectedIndex(0);
            pStmt.executeUpdate();
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
          }
        }
      });

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void finalize() {
    try {
      pStmt.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}

class JanelaAssocia extends JInternalFrame {
  PreparedStatement pStmt;
  Statement st;
  ResultSet rs;
  JDesktopPane desktop;
  JButton bt1;
  JTextField tempo, valor;
  JComboBox cpf, nome, carteira;
  Calendar data;
	int hor;
	int min;
	int seg;
	String hora = new String();

  public JanelaAssocia(JDesktopPane d, Connection con) {
    super("Associa Cliente ao Computador", false, true, false, true); //resizable, closable, maximizable, iconifiable
    desktop = d;
    try {
		st = con.createStatement();
		
      pStmt = con.prepareStatement("INSERT INTO ASSOCIA VALUES (?, ?, ?, ?, ?)");

      setLayout(new FlowLayout());
      add(new JLabel("CPF: "));
	  JComboBox<String> cpf = new JComboBox<String>();
      add(cpf);
      rs = st.executeQuery("SELECT * FROM CLIENTE");
      while(rs.next())  { 
		cpf.addItem(rs.getString("CPF"));  
      } 
      add(new JLabel("Carteira Funcionario: "));
	  JComboBox<String> carteira = new JComboBox<String>();
      add(carteira);
      rs = st.executeQuery("SELECT * FROM FUNCIONARIO");
      while(rs.next())  { 
		carteira.addItem(rs.getString("CARTEIRA"));  
      } 
      add(new JLabel("Computador: "));
	  JComboBox<String> nome = new JComboBox<String>();
      add(nome);
      rs = st.executeQuery("SELECT * FROM COMPUTADOR");
      while(rs.next())  { 
		nome.addItem(rs.getString("NOME"));  
      } 
      add(new JLabel("Tempo (Minutos): "));
      add(tempo = new JTextField(10));
      add(new JLabel("Valor: "));
      add(valor = new JTextField(10));
      valor.setEditable(false);
      add(bt1 = new JButton("Criar"));
      pack();
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setVisible(true);
      desktop.add(this);

      bt1.addActionListener(new ActionListener() {    //classe interna listener sem nome
        public void actionPerformed(ActionEvent e) {
			data = Calendar.getInstance();
			hor = data.get(Calendar.HOUR_OF_DAY); 
			min = data.get(Calendar.MINUTE);
			seg = data.get(Calendar.SECOND);
			hora = hor + ":" + min + ":" + seg;
          try {
            pStmt.setInt(1, Integer.parseInt(cpf.getSelectedItem().toString()));
            pStmt.setInt(2, Integer.parseInt(carteira.getSelectedItem().toString()));
            pStmt.setString(3, nome.getSelectedItem().toString());
            pStmt.setString(4, hora);
            pStmt.setInt(5, Integer.parseInt(tempo.getText()));
            pStmt.executeUpdate();
            Double t = Double.parseDouble(tempo.getText());
            t = (t/60)*2;
            String resultado = String.format("%.2f", t);
            valor.setText("R$ " + resultado);
            desktop.add(new Temporizador(cpf.getSelectedItem().toString(), Integer.parseInt(tempo.getText())));
            tempo.setText("");
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
          }
          
        }
      });

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void finalize() {
    try {
      pStmt.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}

class JanelaRemove extends JInternalFrame {
  PreparedStatement pStmt;
  JDesktopPane desktop;
  JButton bt1;
  JTextField termo;
  JComboBox tab;
  String s;

  public JanelaRemove(JDesktopPane d, Connection con) {
    super("Remover", false, true, false, true); //resizable, closable, maximizable, iconifiable
    desktop = d;
    try {
      

      setLayout(new FlowLayout());
      add(new JLabel("Tabela: "));
	  JComboBox<String> tab = new JComboBox<String>();
      add(tab);
      tab.addItem("CLIENTE");
      tab.addItem("FUNCIONARIO");
      tab.addItem("COMPUTADOR");
      tab.addItem("ASSOCIA");
      add(new JLabel("Chave: "));
      add(termo = new JTextField(20));
      s = new String();
      add(bt1 = new JButton("Remover"));
      pack();
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setVisible(true);
      desktop.add(this);

      bt1.addActionListener(new ActionListener() {    //classe interna listener sem nome
        public void actionPerformed(ActionEvent e) {
          try {
            if(tab.getSelectedItem().toString() == "CLIENTE") {
				pStmt = con.prepareStatement("DELETE FROM CLIENTE WHERE CPF = ?");
			}
            if(tab.getSelectedItem().toString() == "FUNCIONARIO") {
				pStmt = con.prepareStatement("DELETE FROM FUNCIONARIO WHERE CARTEIRA = ?");
			}
            if(tab.getSelectedItem().toString() == "COMPUTADOR") {
				pStmt = con.prepareStatement("DELETE FROM COMPUTADOR WHERE NOME = ?");
			}
            if(tab.getSelectedItem().toString() == "ASSOCIA") {
				pStmt = con.prepareStatement("DELETE FROM ASSOCIA WHERE CPF = ?");
			}
            pStmt.setString(1, termo.getText());
            pStmt.executeUpdate();
            termo.setText("");
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
          }
          
        }
      });

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void finalize() {
    try {
      pStmt.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}

class JanelaConsultaC extends JInternalFrame implements ActionListener {
  PreparedStatement pStmt;
  JDesktopPane desktop;
  JButton bt1;
  JTextField tf1;
  JTextArea ta1;

  public JanelaConsultaC(JDesktopPane d, Connection con) {
    super("Consulta na tabela Cliente", false, true, false, true); //resizable, closable, maximizable, iconifiable
    desktop = d;
    try {
      pStmt = con.prepareStatement("SELECT * FROM CLIENTES c WHERE c.CPF = ?");

      JPanel l1 = new JPanel();
      l1.add(new JLabel("CPF: "));
      l1.add(tf1 = new JTextField(30));
      l1.add(bt1 = new JButton("Pesquisa"));
      add(l1, BorderLayout.NORTH);
      l1 = new JPanel();
      JScrollPane scrollPane = new JScrollPane(ta1 = new JTextArea(30, 60));
      l1.add(scrollPane);
      add(l1, BorderLayout.CENTER);

      bt1.addActionListener(this);
      pack();
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void actionPerformed(ActionEvent e) {
    try {
      ta1.setText("");
      pStmt.setString(1, tf1.getText());
      ResultSet rs = pStmt.executeQuery();
      while (rs.next()) {
        String s = rs.getString(1);
        String n = rs.getString(2);
        String d = rs.getString(3);
        String f = rs.getString(4);
        ta1.append(s + "   " + n + "   " + d + "   " + f + "\n");
      }
      tf1.selectAll();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void finalize() {
    try {
      pStmt.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}

class JanelaConsultaF extends JInternalFrame implements ActionListener {
  PreparedStatement pStmt;
  JDesktopPane desktop;
  JButton bt1;
  JTextField tf1;
  JTextArea ta1;

  public JanelaConsultaF(JDesktopPane d, Connection con) {
    super("Consulta na tabela Funcionário", false, true, false, true); //resizable, closable, maximizable, iconifiable
    desktop = d;
    try {
      pStmt = con.prepareStatement("SELECT * FROM FUNCIONARIO WHERE CARTEIRA = ?");

      JPanel l1 = new JPanel();
      l1.add(new JLabel("Carteira: "));
      l1.add(tf1 = new JTextField(30));
      l1.add(bt1 = new JButton("Pesquisa"));
      add(l1, BorderLayout.NORTH);
      l1 = new JPanel();
      JScrollPane scrollPane = new JScrollPane(ta1 = new JTextArea(30, 30));
      l1.add(scrollPane);
      add(l1, BorderLayout.CENTER);

      bt1.addActionListener(this);
      pack();
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void actionPerformed(ActionEvent e) {
    try {
      ta1.setText("");
      pStmt.setString(1, tf1.getText());
      ResultSet rs = pStmt.executeQuery();
      while (rs.next()) {
        String s = rs.getString(1);
        String n = rs.getString(2);
        String d = rs.getString(3);
        String f = rs.getString(4);
        String z = rs.getString(5);
        ta1.append(s + "   " + n + "   " + d + "   " + f + "   " + z + "\n");
      }
      tf1.selectAll();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void finalize() {
    try {
      pStmt.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}

class JanelaConsultaCo extends JInternalFrame implements ActionListener {
  PreparedStatement pStmt;
  JDesktopPane desktop;
  JButton bt1;
  JComboBox cb1;
  JTextArea ta1;
  ResultSet rs;

  public JanelaConsultaCo(JDesktopPane d, Connection con) {
    super("Consulta na tabela Computador", false, true, false, true); //resizable, closable, maximizable, iconifiable
    desktop = d;
    cb1  = new JComboBox();
    
    try {
      pStmt = con.prepareStatement("SELECT * FROM COMPUTADOR");

      JPanel l1 = new JPanel();
      l1.add(bt1 = new JButton("Atualizar"));
      add(l1, BorderLayout.NORTH);
      l1 = new JPanel();
      JScrollPane scrollPane = new JScrollPane(ta1 = new JTextArea(30, 30));
      l1.add(scrollPane);
      add(l1, BorderLayout.CENTER);

      bt1.addActionListener(this);
      pack();
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void actionPerformed(ActionEvent e) {
	  ta1.setText("");
    try {
      ResultSet rs = pStmt.executeQuery();
      while (rs.next()) {
        String s = rs.getString(1);
        String n = rs.getString(2);
        if(n.equals("0")) ta1.append(s + "   Comum\n");
        if(n.equals("1")) ta1.append(s + "   Gamer\n");
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void finalize() {
    try {
      pStmt.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}

class JanelaConsultaA extends JInternalFrame implements ActionListener {
  PreparedStatement pStmt;
  JDesktopPane desktop;
  JButton bt1;
  JComboBox cb1;
  JTextArea ta1;
  ResultSet rs;

  public JanelaConsultaA(JDesktopPane d, Connection con) {
    super("Consulta na tabela Associa", false, true, false, true); //resizable, closable, maximizable, iconifiable
    desktop = d;
    cb1  = new JComboBox();
    
    try {
      pStmt = con.prepareStatement("SELECT * FROM ASSOCIA");

      JPanel l1 = new JPanel();
      l1.add(bt1 = new JButton("Atualizar"));
      add(l1, BorderLayout.NORTH);
      l1 = new JPanel();
      JScrollPane scrollPane = new JScrollPane(ta1 = new JTextArea(30, 30));
      l1.add(scrollPane);
      add(l1, BorderLayout.CENTER);

      bt1.addActionListener(this);
      pack();
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void actionPerformed(ActionEvent e) {
	  ta1.setText("");
    try {
      ResultSet rs = pStmt.executeQuery();
      while (rs.next()) {
        String s = rs.getString(1);
        String n = rs.getString(2);
        String t = rs.getString(3);
        String u = rs.getString(4);
        String i = rs.getString(5);
        ta1.append(s + "   " + n +"   "+ t +"   "+ u +"   "+ i + "\n");
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void finalize() {
    try {
      pStmt.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}

class Temporizador extends JInternalFrame {
	JLabel nome, tempo;
	Timer timer;
	public Temporizador (String n, int t) {
		super(" ", true, false, false, true);
		setLayout(new FlowLayout());
		add(nome = new JLabel(n));
		add(tempo = new JLabel("00:00"));
		pack();
		setVisible(true);

		timer = new Timer(1000, new ActionListener() {
			int min = t - 1;
			int seg = 60;
			public void actionPerformed (ActionEvent ae) {
				tempo.setText(min + ":" + seg);
				if(min < 0) {
					JOptionPane.showMessageDialog(null,"Tempo de " + n + " Acabou!");
					dispose();
					timer.stop();
				}
				if(seg == 0) {
					seg = 60;
					min--;
				}
				if(seg > 0) seg--;
			}
		});
		timer.start();
	}
}
