package br.com.esquadro.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.esquadro.resources.ResourcesImages;

public class Help extends JFrame {

	private static final long serialVersionUID = 4593163889948851947L;

	/**
	 * Create the panel.
	 */
	public Help() {
		setIconImage(ResourcesImages.informationIcon());
		setBackground(Color.WHITE);
		setType(Type.POPUP);
		setResizable(false);
		getContentPane().setBackground(Color.WHITE);
		setTitle("Ajuda");

		setPreferredSize(new Dimension(668, 400));
		setSize(new Dimension(637, 364));
		getContentPane().setLayout(null);

		final JPanel panel_1 = new JPanel();
		panel_1.setVisible(false);
		panel_1.setBounds(0, 0, 631, 275);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		panel_1.setBackground(Color.WHITE);

		JLabel lblParaConcederPermisso = new JLabel(
				"<html> Para conceder permissão ao projeto, basta clicar com o botão direito na pasta do mesmo, e acessar <b>Propriedades -> Segurança -> Editar</b>, se caso não houver a opção <b>Todos</b> como na imagem anterior, clique em <b>\"Adicionar\"</b> e escreva <b>\"todos\"</b>, em seguida, marque a opção <b>\"Controle Total\"</b> e por fim <b>\"Aplicar\"</b>.</html>");
		lblParaConcederPermisso.setBounds(10, 11, 611, 69);
		panel_1.add(lblParaConcederPermisso);

		JLabel lblumaTelaDe = new JLabel(
				"<html>Uma tela de carregamento irá aparecer, <u>aplicando as permissões em todas as pastas e arquivos do projeto</u>, após o término, é só seguir o procedimento anterior de instalação, que tudo vai dar certo!</html>");
		lblumaTelaDe.setBounds(464, 93, 134, 171);
		panel_1.add(lblumaTelaDe);

		JLabel label_1 = new JLabel("");
		label_1.setBounds(10, 91, 427, 184);
		panel_1.add(label_1);
		label_1.setIcon(ResourcesImages.tutorial2());
		final JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 631, 275);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblPodemAcontecerAlguns = new JLabel(
				"<html>Podem acontecer alguns erros durante a criação do projeto, tanto na fase de criação quanto na de instalação de depêndencias. A primeira coisa que você tem que se certificar é se a pasta selecionada é uma pasta a qual você tem <b>acesso e permissões de usuário.</b></html>");
		lblPodemAcontecerAlguns.setBounds(10, 11, 611, 69);
		panel.add(lblPodemAcontecerAlguns);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(20, 73, 325, 184);
		panel.add(lblNewLabel);
		lblNewLabel.setIcon(ResourcesImages.tutorial1());

		JLabel lblNewLabel_1 = new JLabel(
				"<html>Durante a fase de instalação de depêndencias, fique atento ao console pois <u>ele dirá qual problema está ocorrendo no caso de algum erro</u>. É nessa fase que problemas de permissão são comuns, então se estiver enfrentando este erro, aperte <b>\"Não\"</b> na hora de instalar as dependências, conceda as permissões, e em seguida acesse a pasta do projeto no CMD e execute <b>\"npm install\"</b>.</html>");
		lblNewLabel_1.setBounds(364, 73, 234, 171);
		panel.add(lblNewLabel_1);

		final JButton btnNewButton = new JButton("Próximo");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (panel.isVisible()) {
					panel.setVisible(false);
					panel_1.setVisible(true);
					btnNewButton.setText("Anterior");
				} else {
					panel.setVisible(true);
					panel_1.setVisible(false);
					btnNewButton.setText("Próximo");

				}
			}
		});
		btnNewButton.setBounds(281, 286, 89, 35);
		getContentPane().add(btnNewButton);

		setLocationRelativeTo(null);
	}
}
