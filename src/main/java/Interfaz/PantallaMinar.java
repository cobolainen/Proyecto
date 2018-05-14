package Interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Security;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import blockChain.BlockChainPrueba;
import blockChain.Bloque;
import blockChain.StringUtil;
import blockChain.Transaccion;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class PantallaMinar extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6036388649967771405L;
	private JPanel contentPane;
	Thread t;
	private JTextField tfMonedero;
	private JButton btnParar;
	private JButton btnMinar;
	private boolean minando;
	private JComboBox comboDif;

	public PantallaMinar() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Proveedor de seguridad
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				minando = false;
				btnMinar.setEnabled(true);
				tfMonedero.setEnabled(true);
				btnParar.setEnabled(false);

			}
		});
		setBounds(100, 100, 562, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblMonedero = new JLabel("Monedero");
		lblMonedero.setBounds(35, 51, 119, 25);
		contentPane.add(lblMonedero);
		tfMonedero = new JTextField();
		tfMonedero.setBounds(164, 51, 261, 25);
		contentPane.add(tfMonedero);
		tfMonedero.setColumns(10);

		btnMinar = new JButton("Minar");
		btnMinar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				minando = true;
				BlockChainPrueba.dificultad = Integer.parseInt(comboDif.getSelectedItem().toString());
				t = new Thread(new Runnable() {

					public void run() {
						try {
							while (minando) {
								Bloque b;
								if (BlockChainPrueba.transaccionesSinMinar.size() == 0) {
									b = new Bloque("0");
								} else {
									b = new Bloque(BlockChainPrueba.blockchain
											.get(BlockChainPrueba.blockchain.size() - 1).hash);
								}
								Transaccion t = BlockChainPrueba.transaccionesSinMinar.poll();
								anadirTransaccion(b,t);
								Transaccion t1 = BlockChainPrueba.transaccionesSinMinar.poll();
								anadirTransaccion(b,t1);
								Transaccion t2 = BlockChainPrueba.transaccionesSinMinar.poll();
								anadirTransaccion(b,t2);
								Transaccion t3 = BlockChainPrueba.transaccionesSinMinar.poll();
								anadirTransaccion(b,t3);
								Transaccion t4 = BlockChainPrueba.transaccionesSinMinar.poll();
								anadirTransaccion(b,t4);
								BlockChainPrueba.darRecompensa(tfMonedero.getText());
								BlockChainPrueba.anadirBloque(b);
								JOptionPane.showMessageDialog(contentPane, "Bloque minado!!. Has recibido 12 cbc");

							}
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(contentPane, "Error al actualizar la cadena", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}

					
				});
				t.start();
				btnMinar.setEnabled(false);
				tfMonedero.setEnabled(false);
				btnParar.setEnabled(true);
				comboDif.setEnabled(false);
			}
		});
		btnMinar.setBounds(35, 178, 124, 25);
		contentPane.add(btnMinar);

		btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				minando = false;
				btnMinar.setEnabled(true);
				tfMonedero.setEnabled(true);
				btnParar.setEnabled(false);
				comboDif.setEnabled(true);
			}
		});
		btnParar.setBounds(257, 178, 124, 25);
		contentPane.add(btnParar);
		
		JLabel lblDificultad = new JLabel("Dificultad");
		lblDificultad.setBounds(35, 120, 62, 14);
		contentPane.add(lblDificultad);
		
		comboDif = new JComboBox();
		comboDif.setModel(new DefaultComboBoxModel(new String[] {"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"}));
		comboDif.setBounds(164, 117, 62, 25);
		contentPane.add(comboDif);

	}
	private void anadirTransaccion(Bloque b, Transaccion t) {
		if (t != null) {
			if (t.esRecompensa) {
				b.anadirRecompensa(t);
				
			} else {
				b.anadirTransaccion(t);
			}
		}
	}
}
