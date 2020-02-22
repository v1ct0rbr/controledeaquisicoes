/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.utils;

import javax.swing.JInternalFrame;

/**
 *
 * @author victo
 */
public class MyJInternalFrame extends JInternalFrame {

    protected int frameIndex;

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

}
