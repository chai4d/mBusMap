package chai_4d.mbus.map.screen;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class ImagePreview extends JComponent implements PropertyChangeListener
{
    private static final long serialVersionUID = 5338095931346035287L;

    private ImageIcon thumbnail = null;
    private File file = null;

    public ImagePreview(JFileChooser fc)
    {
        setPreferredSize(new Dimension(100, 50));
        fc.addPropertyChangeListener(this);
    }

    public void loadImage()
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

    public void propertyChange(PropertyChangeEvent e)
    {
        boolean update = false;
        String prop = e.getPropertyName();

        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) //If the directory changed, don't show an image.
        {
            file = null;
            update = true;
        }
        else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) //If a file became selected, find out which one.
        {
            file = (File) e.getNewValue();
            update = true;
        }

        //Update the preview accordingly.
        if (update)
        {
            thumbnail = null;
            if (isShowing())
            {
                loadImage();
                repaint();
            }
        }
    }

    protected void paintComponent(Graphics g)
    {
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
}