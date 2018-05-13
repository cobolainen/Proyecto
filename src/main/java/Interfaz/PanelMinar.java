package Interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import blockChain.BlockChainPrueba;
import blockChain.Bloque;
import blockChain.Transaccion;

public class PanelMinar extends JPanel {

	private JTextField tfMonedero;
	private JButton btnParar;
	private JButton btnMinar;
	private boolean minando;
	private JComboBox comboDif;
	private Thread t;

	public PanelMinar() {
		inicializar();
		setVisible(true);

	}

	public void inicializar() {

		setBounds(100, 100, 562, 455);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);

		JLabel lblMonedero = new JLabel("Monedero");
		lblMonedero.setBounds(117, 53, 119, 25);
		add(lblMonedero);
		tfMonedero = new JTextField();
		tfMonedero.setBounds(246, 53, 261, 25);
		add(tfMonedero);
		tfMonedero.setColumns(10);

		btnMinar = new JButton("Minar");
		btnMinar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				inicializarHilo();
				empezarMinar();
			}

			public void empezarMinar() {
				minando = true;
				BlockChainPrueba.dificultad = Integer.parseInt(comboDif.getSelectedItem().toString());
				t.start();
				btnMinar.setEnabled(false);
				tfMonedero.setEnabled(false);
				btnParar.setEnabled(true);
				comboDif.setEnabled(false);
			}
		});
		btnMinar.setBounds(117, 180, 124, 25);
		add(btnMinar);

		btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pararMinar();
			}
		});
		btnParar.setBounds(339, 180, 124, 25);
		add(btnParar);

		JLabel lblDificultad = new JLabel("Dificultad");
		lblDificultad.setBounds(117, 122, 62, 14);
		add(lblDificultad);

		comboDif = new JComboBox();
		comboDif.setModel(
				new DefaultComboBoxModel(new String[] { "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" }));
		comboDif.setBounds(246, 119, 62, 25);
		add(comboDif);
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

	public void pararMinar() {
		minando = false;
		btnMinar.setEnabled(true);
		tfMonedero.setEnabled(true);
		btnParar.setEnabled(false);
		comboDif.setEnabled(true);
	}

	private void inicializarHilo() {
		t = new Thread(new Runnable() {

			public void run() {
				try {
					while (minando) {
						Bloque b;
						if (BlockChainPrueba.transaccionesSinMinar.size() == 0) {
							b = new Bloque("0");
						} else {
							b = new Bloque(
									BlockChainPrueba.blockchain.get(BlockChainPrueba.blockchain.size() - 1).hash);
						}
						Transaccion t = BlockChainPrueba.transaccionesSinMinar.poll();
						anadirTransaccion(b, t);
						Transaccion t1 = BlockChainPrueba.transaccionesSinMinar.poll();
						anadirTransaccion(b, t1);
						Transaccion t2 = BlockChainPrueba.transaccionesSinMinar.poll();
						anadirTransaccion(b, t2);
						Transaccion t3 = BlockChainPrueba.transaccionesSinMinar.poll();
						anadirTransaccion(b, t3);
						Transaccion t4 = BlockChainPrueba.transaccionesSinMinar.poll();
						anadirTransaccion(b, t4);

						BlockChainPrueba.anadirBloque(b);
						BlockChainPrueba.darRecompensa(tfMonedero.getText());

					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error al actualizar la cadena", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});
	}

}
