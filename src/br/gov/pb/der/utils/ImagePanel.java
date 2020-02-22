/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.utils;

import br.gov.pb.der.services.MessageService;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JDesktopPane;

/**
 *
 * @author Victor_2
 */
public class ImagePanel
        extends JDesktopPane {

    private Image img;

    public ImagePanel(String fileName) throws IOException {
        try {
            InputStream is = new FileInputStream(fileName);
            img = ImageIO.read(is);
        } catch (NullPointerException ex) {
            MessageService.errorMessage("Background não foi encontrado", "Erro", null);
        }
    }

    public ImagePanel(InputStream is) throws IOException {
//        this(new ImageIcon(img).getImage());
        try {
            ImageInputStream iis = (ImageInputStream) is;
            Image img = (Image) iis;
        } catch (NullPointerException ex) {
            MessageService.errorMessage("O arquivo de background não foi encontrado", "Erro", null);
        }
    }

    public ImagePanel(Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(img, 0, 0, this);
    }

//    public void paintComponent(Graphics g) {
//        g.drawImage(img, 0, 0, null);
//    }
}
