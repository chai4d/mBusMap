package chai_4d.mbus.map.screen;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Insets;

import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

public class BusNoRenderer extends JTextPane implements ListCellRenderer
{
    private static final long serialVersionUID = 6936256711670117184L;

    public BusNoRenderer(int tabColumn)
    {
        setMargin(new Insets(0, 0, 0, 0));

        FontMetrics fm = getFontMetrics(getFont());
        int width = fm.charWidth('w') * tabColumn;

        TabStop[] tabs = new TabStop[1];
        tabs[0] = new TabStop(width, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        TabSet tabSet = new TabSet(tabs);

        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setTabSet(attributes, tabSet);
        getStyledDocument().setParagraphAttributes(0, 0, attributes, false);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        String[] item = (String[]) value;
        setText(item[0] + "\t" + item[1]);
        setBackground(isSelected ? list.getSelectionBackground() : null);
        setForeground(isSelected ? list.getSelectionForeground() : null);
        return this;
    }
}