package chai_4d.mbus.map.status;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class StatusLabel extends JComponent
{
    private static final long serialVersionUID = -3087110095480538513L;

    private static final String TEXT_ENDING = "...";

    private String text = null;

    public StatusLabel(String text)
    {
        this.text = text;
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Font font = UIManager.getFont("Label.font");
        g2d.setFont(font);

        FontMetrics fm = g2d.getFontMetrics();
        int endingWidth = fm.stringWidth(TEXT_ENDING);

        float wrappingWidth = getWidth() - endingWidth;
        if (text == null || text.trim().equals("") || wrappingWidth <= 0)
        {
            g2d.dispose();
            return;
        }

        AttributedString attribString = new AttributedString(text);
        attribString.addAttribute(TextAttribute.FONT, font, 0, text.length());

        AttributedCharacterIterator attribCharIterator = attribString.getIterator();

        FontRenderContext frc = new FontRenderContext(null, false, false);
        LineBreakMeasurer lbm = new LineBreakMeasurer(attribCharIterator, frc);

        int x = 0, y = 0; // Left and top margins

        while (lbm.getPosition() < text.length())
        {
            TextLayout layout = lbm.nextLayout(wrappingWidth + (endingWidth / 2));
            y += layout.getAscent();
            layout.draw(g2d, x, y);
            //y += layout.getDescent() + layout.getLeading();

            if (lbm.getPosition() < text.length())
            {
                g2d.drawString(TEXT_ENDING, x + layout.getVisibleAdvance(), y);
            }
            break;
        }
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        String old = this.text;
        this.text = text;
        firePropertyChange("text", old, text);
        if (text == null || old == null || !text.equals(old))
        {
            revalidate();
            repaint();
        }
    }
}
