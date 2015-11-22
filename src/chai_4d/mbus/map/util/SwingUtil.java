package chai_4d.mbus.map.util;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import chai_4d.mbus.map.constant.MapConstants;

public class SwingUtil
{
    private SwingUtil()
    {
    }

    public static void alertWarning(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    public static JButton createImageButton(String path)
    {
        JButton button = new JButton(ImageUtil.createImageIcon(path));
        button.setSize(new Dimension(20, 20));
        button.setPreferredSize(new Dimension(20, 20));
        button.setBorder(null);
        button.setBackground(MapConstants.controlPanel);
        return button;
    }
}
