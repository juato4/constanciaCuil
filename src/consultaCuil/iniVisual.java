package consultaCuil;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Modelo.trabajo;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class iniVisual extends JFrame {

	private JPanel contentPane;
	private static JTextField textField;
	private static JButton btnNewButton;
	private static JLabel lblNombre,lblDni,lblCuil;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					iniVisual frame = new iniVisual();
					iniciar();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void iniciar() {
				
				btnNewButton.addActionListener(new ActionListener() {
					
					trabajo t = new trabajo();
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						t.bucar(textField.getText());
						lblNombre.setText(null);
						lblNombre.setText(t.getNombre());
						lblDni.setText(null);
						lblDni.setText(textField.getText());
						lblCuil.setText(t.getCuil());
					}
				});
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public iniVisual() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 243);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("INGRESAR DNI");
		lblNewLabel.setBounds(43, 11, 88, 14);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(141, 8, 110, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnNewButton = new JButton("BUSCAR");
		btnNewButton.setBounds(287, 7, 89, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("NOMBRE:");
		lblNewLabel_1.setBounds(43, 67, 57, 14);
		contentPane.add(lblNewLabel_1);
		
		lblNombre = new JLabel("");
		lblNombre.setBounds(110, 67, 179, 14);
		contentPane.add(lblNombre);
		
		JLabel lblNewLabel_2 = new JLabel("DNI:");
		lblNewLabel_2.setBounds(43, 107, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		lblDni = new JLabel("");
		lblDni.setBounds(110, 107, 215, 14);
		contentPane.add(lblDni);
		
		JLabel lblNewLabel_3 = new JLabel("CUIL");
		lblNewLabel_3.setBounds(43, 146, 46, 14);
		contentPane.add(lblNewLabel_3);
		
		lblCuil = new JLabel("");
		lblCuil.setBounds(110, 146, 179, 14);
		contentPane.add(lblCuil);
	}
}
