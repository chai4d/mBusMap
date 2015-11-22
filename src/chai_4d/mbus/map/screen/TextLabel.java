package chai_4d.mbus.map.screen;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JPanel;
import javax.swing.UIManager;

public class TextLabel extends JPanel
{
    private static final long serialVersionUID = -8438481510874668714L;

    private String text = null;

    public TextLabel(String text)
    {
        this.text = text;
    }

    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();

        Font font = UIManager.getFont("Label.font");
        g2d.setFont(font);

        float wrappingWidth = getWidth() - 10;
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
            TextLayout layout = lbm.nextLayout(wrappingWidth);
            y += layout.getAscent();
            layout.draw(g2d, x, y);
            y += layout.getDescent() + layout.getLeading();
        }
    }
}
