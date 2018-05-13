package Interfaz;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Security;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Proveedor de seguridad
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 988, 664);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel1 = new JPanel();
		panel1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel1.setBackground(new Color(255, 228, 181));
		panel1.setBounds(0, 0, 972, 120);
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
			}
		});
		btnMon.setOpaque(false);
		btnMon.setContentAreaFilled(false);
		btnMon.setFocusPainted(false);
		btnMon.setBorderPainted(false);
		btnMon.setForeground(Color.BLACK);
		btnMon.setBounds(0, 0, 141, 120);
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
		btnNewButton.setBounds(139, 0, 50, 120);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setOpaque(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(true);
		panel.add(btnNewButton);

		btnMinar = new JButton("MINAR");
		btnMinar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		btnMinar.setOpaque(false);
		btnMinar.setContentAreaFilled(false);
		btnMinar.setFocusPainted(false);
		btnMinar.setBorderPainted(true);
		btnMinar.setForeground(Color.BLACK);
		panel1.add(btnMinar);

		btnSalir = new JButton("SALIR");
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
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(new Color(255, 228, 181));
		list.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(0, 0, 0)));
		list.setModel(model);

		panel2 = new JPanel();
		panel2.setBounds(0, 121, 972, 504);
		contentPane.add(panel2);
		panel2.setLayout(new BorderLayout(0, 0));
		list.setVisible(true);
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (model.getElement(list.getSelectedIndex()) instanceof String) {
					anadirMon();

				} else {
					Monedero m = (Monedero)model.getElement(list.getSelectedIndex());
					System.out.println(StringUtil.getStringDeclave(m.clavePublica));
					scrollPane.setVisible(false);
					new Thread(new Runnable() {

						@Override
						public void run() {
							PanelMonedero pm;
							try {
								panel2.removeAll();
								pm = new PanelMonedero((Monedero) model.getElement(list.getSelectedIndex()));
								pm.setBounds(0, 0,panel2.getWidth(), panel2.getHeight());
								panel2.add(pm);
								pm.repaint();
							} catch (UnsupportedLookAndFeelException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							

						}
					}).start();;

				}

			}
		});
		try {
			cargarMonederos();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPane, "Error al cargar los monederos", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarMonederos() throws IOException {
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		model.anadirElemento("+ anadir monedero");
		model.anadirElemento(model.size() - 1, new Monedero("Carlos"));
		FileInputStream input = new FileInputStream("config/config.properties");
		props.load(input);
		props.stringPropertyNames().forEach(c -> {
			if (c.contains("monedero")) {
				File fichero = new File(props.getProperty(c));
				ObjectInputStream ois;
				try {
					ois = new ObjectInputStream(new FileInputStream(fichero));
					Monedero mon = (Monedero) ois.readObject();
					model.anadirElemento(model.size() - 1, mon);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(contentPane, "Error al cargar el monedero " + props.getProperty(c),
							"Error", JOptionPane.ERROR_MESSAGE);

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
					oos = new ObjectOutputStream(
							new FileOutputStream(new File("monederos/" + textField.getText() + ".wallet")));
					oos.writeObject(mon);
					model.anadirElemento(model.size() - 1, mon);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(contentPane, "Error al crea el monedero", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		}

	}
}
