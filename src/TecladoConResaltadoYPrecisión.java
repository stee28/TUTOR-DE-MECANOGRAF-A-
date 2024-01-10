import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class TecladoConResaltadoYPrecisión extends JFrame {

    private JTextField textField;
    private JTextArea textArea;
    private JLabel pangramaLabel;
    private JLabel precisionLabel;
    private JLabel teclasProblemaLabel;

    private List<String> pangramas;
    private Random random;
    private int pulsacionesCorrectas;
    private int pulsacionesIncorrectas;
    private Set<Character> teclasProblema;

    public TecladoConResaltadoYPrecisión() {
        setTitle("Teclado Virtual con Resaltado, JTextArea y Monitoreo de Precisión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textField = new JTextField();
        add(textField, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        pangramaLabel = new JLabel();
        pangramaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pangramaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(pangramaLabel, BorderLayout.SOUTH);

        precisionLabel = new JLabel("Precisión: 100%");
        precisionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(precisionLabel, BorderLayout.SOUTH);

        teclasProblemaLabel = new JLabel("Teclas problema: ");
        teclasProblemaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(teclasProblemaLabel, BorderLayout.SOUTH);

        JPanel tecladoPanel = new JPanel(new GridLayout(4, 10, 5, 5));

        String teclas = "1234567890qwertyuiopasdfghjklzxcvbnm";
        for (char c : teclas.toCharArray()) {
            JButton boton = new JButton(String.valueOf(c));
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String textoActual = textField.getText() + c;
                    textField.setText(textoActual);
                    textArea.append(String.valueOf(c));

                    resaltarBoton(boton);
                    verificarPrecision();
                    actualizarTeclasProblema();
                    actualizarPangrama();
                }
            });
            tecladoPanel.add(boton);
        }

        add(tecladoPanel, BorderLayout.CENTER);

        pangramas = obtenerPangramas();
        random = new Random();
        pulsacionesCorrectas = 0;
        pulsacionesIncorrectas = 0;
        teclasProblema = new HashSet<>();

        actualizarPangrama();

        pack();
        setLocationRelativeTo(null);
    }

    private void resaltarBoton(JButton boton) {
        boton.setBackground(Color.YELLOW);

        Timer timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boton.setBackground(null);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void verificarPrecision() {
        String textoIngresado = textField.getText();
        String pangramaActual = pangramas.get(0);

        for (int i = 0; i < textoIngresado.length(); i++) {
            if (i >= pangramaActual.length() || textoIngresado.charAt(i) != pangramaActual.charAt(i)) {
                pulsacionesIncorrectas++;
                teclasProblema.add(textoIngresado.charAt(i));
                actualizarPrecision();
                return;
            }
        }

        pulsacionesCorrectas++;
        actualizarPrecision();
    }

    private void actualizarPrecision() {
        int totalPulsaciones = pulsacionesCorrectas + pulsacionesIncorrectas;
        int precision = totalPulsaciones == 0 ? 100 : (int) ((double) pulsacionesCorrectas / totalPulsaciones * 100);
        precisionLabel.setText("Precisión: " + precision + "%");
    }

    private void actualizarTeclasProblema() {
        StringBuilder teclasProblemaStr = new StringBuilder("Teclas problema: ");
        List<Character> teclasProblemaOrdenadas = new ArrayList<>(teclasProblema);
        Collections.sort(teclasProblemaOrdenadas);
        for (char c : teclasProblemaOrdenadas) {
            teclasProblemaStr.append(c).append(" ");
        }
        teclasProblemaLabel.setText(teclasProblemaStr.toString());
    }

    private void actualizarPangrama() {
        Collections.shuffle(pangramas);
        String pangramaActual = pangramas.get(0);
        pangramaLabel.setText("Pangrama: " + pangramaActual);

        pulsacionesCorrectas = 0;
        pulsacionesIncorrectas = 0;
        teclasProblema.clear();
        actualizarPrecision();
        actualizarTeclasProblema();
    }

    private List<String> obtenerPangramas() {
        List<String> pangramas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\STEEVEN MOREIRA\\Documents\\deber\\pangramas.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                pangramas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pangramas;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TecladoConResaltadoYPrecisión().setVisible(true);
            }
        });
    }
}
