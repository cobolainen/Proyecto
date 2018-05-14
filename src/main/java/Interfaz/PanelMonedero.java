package Interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import blockChain.BlockChainPrueba;
import blockChain.Bloque;
import blockChain.Monedero;
import blockChain.StringUtil;
import blockChain.Transaccion;

public class PanelMonedero extends JPanel {
	private Monedero monedero;
	private JLabel lblBalance;
	private JLabel lblEur;
	private JTextField tfDireccion;
	private JTable table;
	private DefaultTableModel tm;

	/**
	 * Create the panel.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 */
	public PanelMonedero(Monedero mon) {

		this.monedero = mon;

		inicializar();
		actualizar();

	}

	private void inicializar() {
		setLayout(null);
		setBounds(0, 121, 972, 504);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(Color.WHITE);
		JLabel lblShowBalance = new JLabel("BALANCE");
		lblShowBalance.setBounds(132, 76, 79, 14);
		add(lblShowBalance);

		lblBalance = new JLabel("CBC");
		lblBalance.setBounds(132, 93, 138, 31);
		add(lblBalance);

		JLabel lblNewLabel_1 = new JLabel("EQUIVALEN A:");
		lblNewLabel_1.setBounds(330, 76, 98, 14);
		add(lblNewLabel_1);

		lblEur = new JLabel("EUR");
		lblEur.setBounds(330, 93, 138, 31);
		add(lblEur);

		tfDireccion = new JTextField();
		tfDireccion.setEditable(false);
		tfDireccion.setBounds(330, 21, 245, 20);
		add(tfDireccion);
		tfDireccion.setColumns(10);

		JLabel lblDirreccion = new JLabel("DIRRECCION");
		lblDirreccion.setBounds(132, 23, 79, 14);
		add(lblDirreccion);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(39, 170, 837, 323);
		add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("ULTIMAS TRANSACCIONES");
		lblNewLabel_2.setBounds(29, 25, 160, 24);
		panel_1.add(lblNewLabel_2);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(29, 60, 751, 263);
		panel_1.add(scrollPane);

		table = new JTable();
		tm = new DefaultTableModel(new Object[][] {},
				new String[] { "Cantidad", "De", "Para", "Id Transaccion", "Fecha" });
		table.setModel(tm);
		table.getColumnModel().getColumn(0).setPreferredWidth(114);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(89);
		table.getColumnModel().getColumn(3).setPreferredWidth(107);
		table.getColumnModel().getColumn(4).setPreferredWidth(103);
		scrollPane.setViewportView(table);

		tfDireccion.setText(StringUtil.getStringDeclave(monedero.clavePublica));
		
		JLabel label = new JLabel("ACCIONES");
		label.setBounds(808, 21, 68, 14);
		add(label);
		
		JButton button = new JButton("ENVIAR");
		button.addActionListener(new ActionListener() {
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
						JOptionPane.showMessageDialog(null, "No deje ningún campo en blanco");
					} else {

						try {
							if (monedero.getBalance() < Float.parseFloat(textField2.getText())) {
								JOptionPane.showMessageDialog(null, "Fondos insuficientes");
							} else {
								Transaccion t = monedero.enviarFondos(StringUtil.getPublicKeyDeString(textField.getText()),
										Float.parseFloat(textField2.getText()));
								JOptionPane.showMessageDialog(null, "La transacción se realizó correctamente");
								BlockChainPrueba.transaccionesSinMinar.add(t);
								
							}

						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null, "Introduzca un valor correcto");
						} catch (InvalidKeySpecException e) {
							JOptionPane.showMessageDialog(null, "Ha ocurrido un error al enviar los fondos");
						} catch (NoSuchAlgorithmException e) {
							JOptionPane.showMessageDialog(null, "Ha ocurrido un error al enviar los fondos");
						} catch (NoSuchProviderException e) {
							JOptionPane.showMessageDialog(null, "Ha ocurrido un error al enviar los fondos");
						}
					}
				}

			}
		});
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button.setBackground(new Color(9, 241, 202));
		button.setBounds(665, 65, 89, 23);
		add(button);
		
		JButton button_1 = new JButton("RECARGAR");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizar();
			}
		});
		button_1.setForeground(Color.WHITE);
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_1.setFocusPainted(false);
		button_1.setBackground(new Color(9, 241, 202));
		button_1.setBounds(778, 65, 98, 23);
		add(button_1);
	}

	private void anadirTransaccion(Transaccion t) {
		String text;
		if (t.emisor.equals(monedero.clavePublica)) {
			text = "<html><font color=\"red\">Enviado &thinsp &thinsp &thinsp &thinsp</font>" + String.valueOf(t.valor)
					+ "  cbc</html>";
		} else {
			text = "<html><font color=\"green\">Recibido &thinsp &thinsp &thinsp</font>" + String.valueOf(t.valor)
					+ "  cbc</html>";
		}

		Object[] data = { text, StringUtil.getStringDeclave(t.emisor), StringUtil.getStringDeclave(t.receptor), t.id,
				new Date(t.timestamp).toLocaleString() };
		tm.addRow(data);
	}

	private void cargarLista() {
		DefaultTableModel dm = (DefaultTableModel) table.getModel();
		while (dm.getRowCount() > 0) {
			dm.removeRow(0);
		}
		for (Bloque bloque : BlockChainPrueba.blockchain) {
			for (Transaccion t : bloque.transacciones) {

				if (t.emisor.equals(monedero.clavePublica) || t.receptor.equals(monedero.clavePublica)) {
					anadirTransaccion(t);
				}

			}

		}

	}

	public void actualizar() {

		lblBalance.setText("CBC");
		lblEur.setText("EUR");
		float balance = monedero.getBalance();
		lblBalance.setText(lblBalance.getText() + "    " + String.valueOf(balance));
		lblEur.setText(lblEur.getText() + "    " + String.valueOf(balance * 89.54));
		cargarLista();

	}
}
