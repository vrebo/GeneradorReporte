/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generador;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Adrián
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PDFHelper pdf = new PDFHelper();
        if (pdf.generarReporteGeneralPeliculasInventario()) {
            int opcion = JOptionPane.showConfirmDialog(null, "Desea abrir el documento?");
            if (opcion == JOptionPane.YES_OPTION) {
                PanelPDF panelPDF = new PanelPDF(pdf.getRuta());
                JFrame frame = new JFrame("Reporte");
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.add(panelPDF);
                frame.setVisible(true);
                frame.toFront();
            }
        }
    }

}
