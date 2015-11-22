package chai_4d.mbus.map.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import chai_4d.mbus.map.constant.MapConstants;

public class BusPreview extends JPanel
{
    private static final long serialVersionUID = 6773451411640279914L;

    private ImageIcon thumbnail = null;
    private File file = null;

    public BusPreview()
    {
        initialize();
    }

    private void initialize()
    {
        setSize(new Dimension(100, 100));
        setPreferredSize(new Dimension(100, 100));
        setMinimumSize(new Dimension(100, 100));
        setBackground(MapConstants.controlPanel);
        setBorder(new LineBorder(Color.gray));
    }

    private void loadImage()
    {
        if (file == null)
        {
            thumbnail = null;
            return;
        }

        //Don't use createImageIcon (which is a wrapper for getResource)
        //because the image we're trying to load is probably not one
        //of this program's own resources.
        ImageIcon tmpIcon = new ImageIcon(file.getPath());
        if (tmpIcon != null)
        {
            if (tmpIcon.getIconWidth() > 90)
            {
                thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
            }
            else
            { //no need to miniaturize
                thumbnail = tmpIcon;
            }
        }
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (thumbnail == null)
        {
            loadImage();
        }
        if (thumbnail != null)
        {
            int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
            int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;

            if (y < 0)
            {
                y = 0;
            }
            if (x < 5)
            {
                x = 5;
            }
            thumbnail.paintIcon(this, g, x, y);
        }
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
        this.thumbnail = null;
        repaint();
    }
}
