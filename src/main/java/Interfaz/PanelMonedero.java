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
	 * @throws UnsupportedLookAndFeelException 
	 */
	public PanelMonedero(Monedero mon) throws UnsupportedLookAndFeelException {
		
		this.monedero=mon;
		inicializar();

	}
	private void inicializar() throws UnsupportedLookAndFeelException {
		setLayout(null);
		setBounds(0, 121, 972, 504);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(Color.WHITE);
		JLabel lblShowBalance = new JLabel("BALANCE");
		lblShowBalance.setBounds(183, 76, 79, 14);
		add(lblShowBalance);

		lblBalance = new JLabel("CBC");
		lblBalance.setBounds(183, 93, 138, 31);
		add(lblBalance);

		JLabel lblNewLabel_1 = new JLabel("EQUIVALEN A:");
		lblNewLabel_1.setBounds(381, 76, 98, 14);
		add(lblNewLabel_1);

		lblEur = new JLabel("EUR");
		lblEur.setBounds(381, 93, 138, 31);
		add(lblEur);

		tfDireccion = new JTextField();
		tfDireccion.setEditable(false);
		tfDireccion.setBounds(381, 21, 245, 20);
		add(tfDireccion);
		tfDireccion.setColumns(10);

		JLabel lblDirreccion = new JLabel("DIRRECCION");
		lblDirreccion.setBounds(183, 23, 79, 14);
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
