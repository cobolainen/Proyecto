package Interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;

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

	public PantallaMinar() {
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
		lblMonedero.setBounds(73, 40, 119, 25);
		contentPane.add(lblMonedero);
		tfMonedero = new JTextField();
		tfMonedero.setBounds(202, 40, 261, 25);
		contentPane.add(tfMonedero);
		tfMonedero.setColumns(10);

		btnMinar = new JButton("Minar");
		btnMinar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				minando = true;
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
								if (t != null) {
									if (t.esRecompensa) {
										b.anadirRecompensa(t);
									} else {
										b.anadirTransaccion(t);
									}
								}

								BlockChainPrueba.anadirBloque(b);
								BlockChainPrueba.darRecompensa(tfMonedero.getText());
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
			}
		});
		btnMinar.setBounds(68, 124, 124, 25);
		contentPane.add(btnMinar);

		btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				minando = false;
				btnMinar.setEnabled(true);
				tfMonedero.setEnabled(true);
				btnParar.setEnabled(false);
			}
		});
		btnParar.setBounds(303, 125, 124, 25);
		contentPane.add(btnParar);

	}
}
