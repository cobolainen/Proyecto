package Interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import blockChain.BlockChainPrueba;
import blockChain.Bloque;
import blockChain.Monedero;
import blockChain.StringUtil;
import blockChain.Transaccion;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PantallaMonedero extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8101138307326484731L;

	Monedero monedero;

	private JPanel contentPane;
	private JLabel lblBalance;
	private JLabel lblEur;
	private JTextField tfDireccion;
	private JTable table;
	private DefaultTableModel tm;

	/**
	 * Create the frame.
	 * 
	 * @param mon
	 * @throws UnsupportedLookAndFeelException
	 */
	public PantallaMonedero(Monedero mon) throws UnsupportedLookAndFeelException {

		this.monedero = mon;
		inicializar();
		actualizar();

	}

	private void inicializar() throws UnsupportedLookAndFeelException {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 816, 648);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 800, 224);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblShowBalance = new JLabel("BALANCE");
		lblShowBalance.setBounds(28, 81, 79, 14);
		panel.add(lblShowBalance);

		lblBalance = new JLabel("CBC");
		lblBalance.setBounds(28, 98, 138, 31);
		panel.add(lblBalance);

		JLabel lblNewLabel_1 = new JLabel("EQUIVALEN A:");
		lblNewLabel_1.setBounds(226, 81, 98, 14);
		panel.add(lblNewLabel_1);

		lblEur = new JLabel("EUR");
		lblEur.setBounds(226, 98, 138, 31);
		panel.add(lblEur);

		JLabel lblAcciones = new JLabel("ACCIONES");
		lblAcciones.setBounds(637, 58, 68, 14);
		panel.add(lblAcciones);

		JButton btnNewButton = new JButton("ENVIAR");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] options1 = { "Enviar", "Cancelar" };
				JPanel panel = new JPanel();
				GridLayout gl = new GridLayout(2, 1);
				gl.setHgap(20);
				panel.setLayout(gl);
				panel.add(new JLabel("Introduce id del monedero destino"));
				JTextField textField = new JTextField(15);
				panel.add(textField);
				panel.add(new JLabel("Introduce cantidad"));
				JTextField textField2 = new JTextField(5);
				panel.add(textField2);
				int result = JOptionPane.showOptionDialog(null, panel, "Enviar fondos", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options1, null);
				if (result == JOptionPane.YES_OPTION) {
					if (textField.getText().replaceAll("\\s", "").equals("")
							|| textField2.getText().replaceAll("\\s", "").equals("")) {
						JOptionPane.showMessageDialog(contentPane, "No deje ningún campo en blanco");
					} else {

						try {
							if (monedero.getBalance() < Float.parseFloat(textField2.getText())) {
								JOptionPane.showMessageDialog(contentPane, "Fondos insuficientes");
							} else {
								Transaccion t = monedero.enviarFondos(StringUtil.getPublicKeyDeString(textField.getText()),
										Float.parseFloat(textField2.getText()));
								JOptionPane.showMessageDialog(contentPane, "La transacción se realizó correctamente");
								Bloque b = new Bloque(BlockChainPrueba.blockchain.get(BlockChainPrueba.blockchain.size()-1).hash);
								b.anadirTransaccion(t);
								try {
									BlockChainPrueba.anadirBloque(b);
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}

						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(contentPane, "Introduzca un valor correcto");
						} catch (InvalidKeySpecException e) {
							JOptionPane.showMessageDialog(contentPane, "Ha ocurrido un error al enviar los fondos");
						} catch (NoSuchAlgorithmException e) {
							JOptionPane.showMessageDialog(contentPane, "Ha ocurrido un error al enviar los fondos");
						} catch (NoSuchProviderException e) {
							JOptionPane.showMessageDialog(contentPane, "Ha ocurrido un error al enviar los fondos");
						}
					}
				}

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBounds(494, 102, 89, 23);
		btnNewButton.setBackground(new Color(9, 241, 202));
		panel.add(btnNewButton);

		JButton btnRecibir = new JButton("RECARGAR");
		btnRecibir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actualizar();
			}
		});
		btnRecibir.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRecibir.setForeground(Color.WHITE);
		btnRecibir.setBounds(607, 102, 98, 23);
		btnRecibir.setBackground(new Color(9, 241, 202));
		btnRecibir.setFocusPainted(false);
		panel.add(btnRecibir);

		tfDireccion = new JTextField();
		tfDireccion.setEditable(false);
		tfDireccion.setBounds(226, 26, 245, 20);
		panel.add(tfDireccion);
		tfDireccion.setColumns(10);

		JLabel lblDirreccion = new JLabel("DIRRECCION");
		lblDirreccion.setBounds(28, 28, 79, 14);
		panel.add(lblDirreccion);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(0, 224, 800, 386);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("ULTIMAS TRANSACCIONES");
		lblNewLabel_2.setBounds(29, 25, 160, 24);
		panel_1.add(lblNewLabel_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(29, 60, 730, 299);
		panel_1.add(scrollPane);
		
		table = new JTable();
		tm = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Cantidad", "De", "Para", "Id Transaccion", "Fecha"
				});
		table.setModel(tm);
		table.getColumnModel().getColumn(0).setPreferredWidth(114);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(89);
		table.getColumnModel().getColumn(3).setPreferredWidth(107);
		table.getColumnModel().getColumn(4).setPreferredWidth(103);
		scrollPane.setViewportView(table);
		
		
		tfDireccion.setText(StringUtil.getStringDeclave(monedero.clavePublica));
		UIManager.setLookAndFeel(new com.jtattoo.plaf.mcwin.McWinLookAndFeel());
	}

	public void actualizar() {
		
		lblBalance.setText("CBC");
		lblEur.setText("EUR");
		float balance = monedero.getBalance();
		lblBalance.setText(lblBalance.getText() + "    " + String.valueOf(balance));
		lblEur.setText(lblEur.getText() + "    " + String.valueOf(balance * 89.54));
		cargarLista();
		
		
	}
	private void anadirTransaccion(Transaccion t) {
		String text;
		if (t.emisor.equals(monedero.clavePublica)) {
			text = "<html><font color=\"red\">Enviado &thinsp &thinsp &thinsp &thinsp</font>"+String.valueOf(t.valor)+"  cbc</html>";
		}else {
			text = "<html><font color=\"green\">Recibido &thinsp &thinsp &thinsp</font>"+String.valueOf(t.valor)+"  cbc</html>";
		}
		
		Object [] data = {text, StringUtil.getStringDeclave(t.emisor), StringUtil.getStringDeclave(t.receptor), t.id, new Date(t.timestamp).toLocaleString()};
		tm.addRow(data);
	}
	private void cargarLista() {
		DefaultTableModel dm = (DefaultTableModel)table.getModel();
		while(dm.getRowCount() > 0)
		{
		    dm.removeRow(0);
		}
		for (Bloque bloque : BlockChainPrueba.blockchain) {
			for (Transaccion t : bloque.transacciones) {
				if (t.emisor.equals(monedero.clavePublica)||t.receptor.equals(monedero.clavePublica)) {
					anadirTransaccion(t);
				}
				
			}
			
		}
		
		
		
		
		
		
		
	}
}
