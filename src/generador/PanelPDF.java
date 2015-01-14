/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generador;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Adrián
 */
public class PanelPDF extends JPanel {

    private JButton jbAnterior;
    private JButton jbSiguiente;
    private JButton jbRestablecer;
    private final String ruta;
    private PagePanel panel;
    private PDFFile file;
    private int indice;
    private JLabel jlPaginaActual;
    private JLabel jlTotalPaginas;

    public PanelPDF(String ruta) {
        this.ruta = ruta;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(getBackground(), 15));
        addComponentes();
    }

    private void addComponentes() {
        JPanel panelNorte = new JPanel();
        panel = new PagePanel();
        JPanel panelSur = new JPanel();
        panel.setSize(600, 800);
        indice = 1;
        try {
            RandomAccessFile raf = new RandomAccessFile(ruta, "r");
            FileChannel fc = raf.getChannel();
            ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            file = new PDFFile(bb);
            PDFPage pagina = file.getPage(indice);
            panel.showPage(pagina);
            repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, "Advertencia", JOptionPane.ERROR_MESSAGE);
        }
        jlPaginaActual = new JLabel(Integer.toString(indice));
        jlTotalPaginas = new JLabel(Integer.toString(file.getNumPages()));
        panelNorte.add(new JLabel("Página: "));
        panelNorte.add(jlPaginaActual);
        panelNorte.add(new JLabel("\\"));
        panelNorte.add(jlTotalPaginas);
        jbAnterior = new JButton("Anterior");
        jbSiguiente = new JButton("Siguiente");
        jbRestablecer = new JButton("Restablecer");
        panelSur.add(jbAnterior);
        panelSur.add(jbSiguiente);
        panelSur.add(jbRestablecer);
        panel.setClip(null);
        panel.useZoomTool(true);
        add(panelNorte, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
        addEventos();
    }

    private void addEventos() {
        jbAnterior.addActionListener((ActionEvent e) -> {
            if (indice > 1) {
                restablecer.run();
                indice = indice - 1;
                PDFPage pagina = file.getPage(indice);
                panel.showPage(pagina);
                jlPaginaActual.setText(Integer.toString(indice));
            }
        });
        jbSiguiente.addActionListener((ActionEvent e) -> {
            if (indice < file.getNumPages()) {
                restablecer.run();
                indice = indice + 1;
                PDFPage pagina = file.getPage(indice);
                panel.showPage(pagina);
                jlPaginaActual.setText(Integer.toString(indice));
            }
        });
        jbRestablecer.addActionListener((ActionEvent e) -> {
            restablecer.run();
        });
    }

    Runnable restablecer = () -> {
        try {
            panel.setClip(null);
            panel.useZoomTool(true);
        } catch (NullPointerException e) {
            System.out.println(e + "\n" + e.getMessage() + "Huebos");
        }
    };

}
