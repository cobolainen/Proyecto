package Interfaz;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
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
	private JComboBox comboDif;
	private Thread t;

	public PanelMinar() {
		inicializar();
		setVisible(true);
		inicializarHilo();
		if (BlockChainPrueba.minando==false) {
			pararMinar();
			
		}else {
			empezarMinar();
		}

	}

	public void inicializar() {

		setBounds(0, 121, 972, 504);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);

		JLabel lblMonedero = new JLabel("Monedero");
		lblMonedero.setBounds(299, 119, 119, 25);
		add(lblMonedero);
		tfMonedero = new JTextField();
		tfMonedero.setBounds(428, 119, 261, 25);
		add(tfMonedero);
		tfMonedero.setColumns(10);

		btnMinar = new JButton("Minar");
		btnMinar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				inicializarHilo();
				empezarMinar();
			}

			
		});
		btnMinar.setBounds(299, 246, 124, 25);
		add(btnMinar);

		btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pararMinar();
			}
		});
		btnParar.setBounds(521, 246, 124, 25);
		add(btnParar);

		JLabel lblDificultad = new JLabel("Dificultad");
		lblDificultad.setBounds(299, 188, 62, 14);
		add(lblDificultad);

		comboDif = new JComboBox();
		comboDif.setModel(
				new DefaultComboBoxModel(new String[] { "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" }));
		comboDif.setBounds(428, 185, 62, 25);
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
	public void empezarMinar() {
		BlockChainPrueba.minando = true;
		BlockChainPrueba.dificultad = Integer.parseInt(comboDif.getSelectedItem().toString());
		t.start();
		btnMinar.setEnabled(false);
		tfMonedero.setEnabled(false);
		btnParar.setEnabled(true);
		comboDif.setEnabled(false);
	}

	public void pararMinar() {
		BlockChainPrueba.minando = false;
		btnMinar.setEnabled(true);
		tfMonedero.setEnabled(true);
		btnParar.setEnabled(false);
		comboDif.setEnabled(true);
	}

	private void inicializarHilo() {
		t = new Thread(new Runnable() {

			public void run() {
				try {
					SystemTray st = SystemTray.getSystemTray();
					TrayIcon icon = new TrayIcon(ImageIO.read(new File("imagenes/image.png")).getScaledInstance(st.getTrayIconSize().width,st.getTrayIconSize().height, BufferedImage.SCALE_FAST ));
					st.add(icon);
					while (BlockChainPrueba.minando) {
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
						icon.displayMessage("Boque minado!!", "Has recibido 12 cbc", MessageType.NONE);

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
