package Interfaz;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.tools.FileObject;

import blockChain.BlockChainPrueba;
import blockChain.Bloque;
import blockChain.Monedero;
import blockChain.OutputTransaccion;
import blockChain.StringUtil;
import blockChain.Transaccion;

public class PantallaPrincipal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5164941281741302059L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaPrincipal frame = new PantallaPrincipal();
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
	public PantallaPrincipal() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Proveedor de seguridad
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(new com.jtattoo.plaf.mcwin.McWinLookAndFeel());
			// UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
						Monedero mon = new Monedero();
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
		btnNewButton.setBounds(175, 140, 130, 23);
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
		btnNewButton_1.setBounds(175, 73, 130, 23);
		contentPane.add(btnNewButton_1);

		JButton btnSalir = new JButton("Salir\r\n");
		btnSalir.setBounds(175, 208, 130, 23);
		contentPane.add(btnSalir);
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setFileFilter(new FileNameExtensionFilter(".blockchain", "blockchain"));

		int returnValue = jfc.showOpenDialog(null);
		// int returnValue = jfc.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			try {
				File selectedFile = jfc.getSelectedFile();
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile));
				BlockChainPrueba bl = (BlockChainPrueba) ois.readObject();
				BlockChainPrueba.blockchain = bl.blockchain;
				BlockChainPrueba.UTXOs = bl.UTXOs;
				BlockChainPrueba.transaccionesSinMinar = bl.transaccionesSinMinar;
				BlockChainPrueba.dificultad = bl.dificultad;
				BlockChainPrueba.transaccionMinima = bl.transaccionMinima;

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
}
