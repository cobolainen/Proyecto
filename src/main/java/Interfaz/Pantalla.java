package Interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Security;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import Constantes.Constantes;
import blockChain.BlockChainPrueba;
import blockChain.Monedero;
import blockChain.StringUtil;

import java.awt.BorderLayout;
import javax.swing.border.MatteBorder;

public class Pantalla extends JFrame {

	private JPanel contentPane;
	private Properties props = new Properties();
	private JButton btnMon;
	private JButton btnNewButton;
	private JList list;
	private JButton btnMinar;
	private JButton btnSalir;
	private ModeloLista model = new ModeloLista();
	private JScrollPane scrollPane;
	private JPanel panel2;
	private JScrollPane scrollPane_1;
	private Monedero monederoActual;
	private FileInputStream inputProps;
	private FileOutputStream outputProps;
	private String utimoMonedero = null;
	private PanelMinar panelMinar = new PanelMinar();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
						UIManager.setLookAndFeel(new com.jtattoo.plaf.mcwin.McWinLookAndFeel());
					} catch (UnsupportedLookAndFeelException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Pantalla frame = new Pantalla();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Pantalla() {
		setResizable(false);
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Proveedor de seguridad

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 988, 664);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {
					if (BlockChainPrueba.oos != null) {
						BlockChainPrueba.oos.close();
						BlockChainPrueba.minando=false;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		JPanel panel1 = new JPanel();
		panel1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel1.setBackground(new Color(255, 228, 181));
		panel1.setBounds(0, 0, 982, 120);
		contentPane.add(panel1);
		panel1.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel lblCobocoin = new JLabel("COBOCOIN");
		lblCobocoin.setHorizontalAlignment(SwingConstants.CENTER);
		lblCobocoin.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		lblCobocoin.setBackground(new Color(255, 228, 181));
		panel1.add(lblCobocoin);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 228, 181));
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		panel1.add(panel);
		panel.setLayout(null);

		btnMon = new JButton("MONEDERO");
		btnMon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						PanelMonedero pm;
						panel2.removeAll();
						pm = new PanelMonedero(monederoActual);
						pm.setBounds(0, 0, panel2.getWidth(), panel2.getHeight());
						panel2.add(pm);
						pm.repaint();

					}
				}).start();

			}
		});
		btnMon.setOpaque(false);
		btnMon.setContentAreaFilled(false);
		btnMon.setFocusPainted(false);
		btnMon.setBorderPainted(false);
		btnMon.setForeground(Color.BLACK);
		btnMon.setBounds(0, 0, 182, 120);
		panel.add(btnMon);

		btnNewButton = new JButton("\u25BC");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (scrollPane.isVisible()) {
					scrollPane.setVisible(false);
				} else {
					scrollPane.setVisible(true);
				}
			}
		});
		btnNewButton.setBounds(192, 0, 50, 120);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setOpaque(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(true);
		panel.add(btnNewButton);

		btnMinar = new JButton("MINAR");
		btnMinar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*new Thread(new Runnable() {

					@Override
					public void run() {*/
						
						panel2.removeAll();
						
						panelMinar.setBounds(0, 0, panel2.getWidth(), panel2.getHeight());
						panel2.add(panelMinar);
						panelMinar.repaint();

					/*}
				}).start();*/
				;

			}
		});
		btnMinar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		btnMinar.setOpaque(false);
		btnMinar.setContentAreaFilled(false);
		btnMinar.setFocusPainted(false);
		btnMinar.setBorderPainted(true);
		btnMinar.setForeground(Color.BLACK);
		panel1.add(btnMinar);

		btnSalir = new JButton("SALIR");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnSalir.setForeground(Color.BLACK);
		btnSalir.setOpaque(false);
		btnSalir.setFocusPainted(false);
		btnSalir.setContentAreaFilled(false);
		btnSalir.setBorderPainted(true);
		panel1.add(btnSalir);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(243, 119, 122, 149);
		contentPane.add(scrollPane);

		list = new JList();
		scrollPane.setViewportView(list);
		scrollPane.setVisible(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setPreferredSize (new Dimension(0,0));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(new Color(255, 228, 181));
		list.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(0, 0, 0)));
		list.setModel(model);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 119, 982, 522);
		contentPane.add(scrollPane_1);

		panel2 = new JPanel();
		scrollPane_1.setViewportView(panel2);
		panel2.setLayout(new BorderLayout(0, 0));
		list.setVisible(true);
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (model.getElement(list.getSelectedIndex()) instanceof String) {
					anadirMon();

				} else {
					Monedero m = (Monedero) model.getElement(list.getSelectedIndex());
					System.out.println(StringUtil.getStringDeclave(m.clavePublica));
					scrollPane.setVisible(false);
					/*new Thread(new Runnable() {

						@Override
						public void run() {*/
							PanelMonedero pm;
							monederoActual = (Monedero) model.getElement(list.getSelectedIndex());
							panel2.removeAll();
							pm = new PanelMonedero(monederoActual);
							pm.setBounds(0, 0, panel2.getWidth(), panel2.getHeight());
							panel2.add(pm);
							pm.repaint();

						/*}
					}).start();*/
					;

				}

			}
		});
		try {
			try {
				cargarBlockchain();
			} catch (ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(contentPane, "Error al cargar blockchain", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			cargarMonederos();

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(contentPane, "Error al cargar los monederos", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarMonederos() throws IOException {
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		model.anadirElemento("+ anadir monedero");
		inputProps = new FileInputStream("config/config.properties");
		props.load(inputProps);
		props.stringPropertyNames().forEach(clave -> {
			if (clave.contains("monedero")) {
				File fichero = new File(props.getProperty(clave));
				ObjectInputStream ois;
				try {
					ois = new ObjectInputStream(new FileInputStream(fichero));
					Monedero mon = (Monedero) ois.readObject();
					model.anadirElemento(model.size() - 1, mon);
					utimoMonedero = clave;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(contentPane, "Error al cargar el monedero " + props.getProperty(clave),
							"Error", JOptionPane.ERROR_MESSAGE);
					
					try {
						outputProps = new FileOutputStream("config/config.properties");
						props.remove(clave);
						props.store(outputProps, null);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}
		});

	}

	public void anadirMon() {
		Object[] options1 = { "Aceptar", "Cancelar" };
		JPanel panel = new JPanel();
		GridLayout gl = new GridLayout(1, 1);
		gl.setHgap(20);
		panel.setLayout(gl);
		panel.add(new JLabel("Introduce un nombre"));
		JTextField textField = new JTextField(15);
		panel.add(textField);
		int result = JOptionPane.showOptionDialog(null, panel, "Enviar fondos", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options1, null);
		if (result == JOptionPane.YES_OPTION) {
			if (textField.getText().replaceAll("\\s", "").equals("")) {
				JOptionPane.showMessageDialog(contentPane, "Introduce un nombre");
			} else {
				Monedero mon = new Monedero(textField.getText());
				ObjectOutputStream oos;
				try {
					File f = new File("monederos/" + textField.getText().toLowerCase() + ".wallet");
					if (f.exists()) {
						JOptionPane.showMessageDialog(contentPane, "Ya existe un moedero con ese nombre");
					} else {

						oos = new ObjectOutputStream(new FileOutputStream(f));
						oos.writeObject(mon);
						model.anadirElemento(model.size() - 1, mon);
						oos.close();
						outputProps = new FileOutputStream("config/config.properties");
						if (utimoMonedero == null) {
							props.put("monedero1", f.getPath());
							props.store(outputProps, null);
							utimoMonedero = "monedero1";
						} else {
							String num = utimoMonedero.replace("monedero", "");
							String nombreProp = "monedero" + (Integer.parseInt(num) + 1);
							props.put(nombreProp, f.getPath());
							props.store(outputProps, null);
							utimoMonedero = nombreProp;
						}

					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(contentPane, "Error al crea el monedero", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		}

	}

	private void cargarBlockchain() throws IOException, ClassNotFoundException {
		Properties prop = new Properties();
		FileInputStream input;
		try {
			input = new FileInputStream(Constantes.props);
			prop.load(input);
			String ruta = prop.getProperty("blockchain");
			if (ruta == null) {
				throw new FileNotFoundException();
			}
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(ruta)));
			BlockChainPrueba bl = (BlockChainPrueba) ois.readObject();
			BlockChainPrueba.blockchain = bl.blockchain;
			BlockChainPrueba.UTXOs = bl.UTXOs;
			BlockChainPrueba.transaccionesSinMinar = bl.transaccionesSinMinar;
			BlockChainPrueba.dificultad = bl.dificultad;
			BlockChainPrueba.transaccionMinima = bl.transaccionMinima;
		} catch (FileNotFoundException e1) {
			input = new FileInputStream(Constantes.props);
			prop.load(input);
			File file = new File(prop.getProperty("blockchain"));
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(new BlockChainPrueba());
			oos.close();
		}

	}
}
