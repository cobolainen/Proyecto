package Interfaz;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Security;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import Constantes.Constantes;
import blockChain.BlockChainPrueba;
import blockChain.Monedero;

public class PantallaPrincipal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5164941281741302059L;
	private JPanel contentPane;
	Properties prop = new Properties();
	OutputStream output = null;
	InputStream input = null;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel(new com.jtattoo.plaf.mcwin.McWinLookAndFeel());
						// UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
					} catch (UnsupportedLookAndFeelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					PantallaPrincipal frame = new PantallaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public PantallaPrincipal() throws IOException {
		Constantes.props = new File("res/config/config.properties");
		setResizable(false);
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Proveedor de seguridad
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*try {
					BlockChainPrueba.oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				System.exit(0);
			}
		});
		setBounds(100, 100, 498, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnNewButton = new JButton("Nuevo Monedero\r\n");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.setFileFilter(new FileNameExtensionFilter(".wallet", "wallet"));
				int returnValue = jfc.showSaveDialog(contentPane);

				// int returnValue = jfc.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {

					File selectedFile = new File(jfc.getSelectedFile().getAbsolutePath() + ".wallet");
					try {
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedFile));
						Monedero mon = new Monedero(jfc.getSelectedFile().getName());
						oos.writeObject(mon);
						try {
							new PantallaMonedero(mon).setVisible(true);
							;
						} catch (UnsupportedLookAndFeelException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		btnNewButton.setBounds(174, 104, 130, 23);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Cargar Monedero");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.setFileFilter(new FileNameExtensionFilter(".wallet", "wallet"));

				int returnValue = jfc.showOpenDialog(null);
				// int returnValue = jfc.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					try {
						File selectedFile = jfc.getSelectedFile();
						ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile));
						Monedero mon = (Monedero) ois.readObject();
						new PantallaMonedero(mon).setVisible(true);
						;
					} catch (UnsupportedLookAndFeelException e) {

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		btnNewButton_1.setBounds(174, 37, 130, 23);
		contentPane.add(btnNewButton_1);

		JButton btnSalir = new JButton("Salir\r\n");
		btnSalir.setBounds(174, 228, 130, 23);
		contentPane.add(btnSalir);
		
		JButton btnMinar = new JButton("Minar");
		btnMinar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaMinar().setVisible(true);
			}
		});
		btnMinar.setBounds(174, 168, 130, 23);
		contentPane.add(btnMinar);
		cargarBlockchain();

	}

	private void cargarBlockchain() {
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
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			jfc.setFileFilter(new FileNameExtensionFilter(".blockchain", "blockchain"));

			int returnValue = jfc.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				try {
					File selectedFile = jfc.getSelectedFile();
					System.out.println(selectedFile.getAbsolutePath());
					if (selectedFile.exists()) {
						ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile));
						BlockChainPrueba bl = (BlockChainPrueba) ois.readObject();
						BlockChainPrueba.blockchain = bl.blockchain;
						BlockChainPrueba.UTXOs = bl.UTXOs;
						BlockChainPrueba.transaccionesSinMinar = bl.transaccionesSinMinar;
						BlockChainPrueba.dificultad = bl.dificultad;
						BlockChainPrueba.transaccionMinima = bl.transaccionMinima;
						Properties prop = new Properties();
						FileInputStream input = new FileInputStream(Constantes.props);
						prop.load(input);
						output = new FileOutputStream(Constantes.props);
						prop.setProperty("blockchain", selectedFile.getAbsolutePath());
						prop.store(output, null);
						BlockChainPrueba.oos = new ObjectOutputStream(
								new FileOutputStream(prop.getProperty("blockchain"), false));

						ois.close();
					} else {
						output = new FileOutputStream(Constantes.props);
						prop.setProperty("blockchain", selectedFile.getAbsolutePath()+ ".blockchain");
						prop.store(output, null);
						selectedFile = new File(selectedFile.getAbsolutePath() + ".blockchain");
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedFile));
						oos.writeObject(new BlockChainPrueba());
						oos.close();
					}
					output = new FileOutputStream(Constantes.props);
					prop.setProperty("blockchain", selectedFile.getAbsolutePath());
					prop.store(output, null);

				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ClassNotFoundException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
