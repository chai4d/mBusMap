package chai_4d.mbus.map.status;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class StatusItem extends JPanel
{
    private static final long serialVersionUID = 9126302720448857667L;

    private JSeparator separator = null;
    private JLabel icon = null;
    private StatusLabel label = null;

    public StatusItem(String label)
    {
        initialize(label, false, null);
    }

    public StatusItem(String label, boolean separator)
    {
        initialize(label, separator, null);
    }

    public StatusItem(String label, boolean separator, Icon icon)
    {
        initialize(label, separator, icon);
    }

    private void initialize(String label, boolean separator, Icon icon)
    {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(100, 20));
        setOpaque(false);

        int x = 0;
        if (separator)
        {
            this.separator = new JSeparator(SwingConstants.VERTICAL);
            add(this.separator, new GridBagConstraints(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(
                2,
                2,
                2,
                2), 0, 0));
        }
        if (icon != null)
        {
            this.icon = new JLabel(icon);
            add(this.icon, new GridBagConstraints(
                x++,
                0,
                1,
                1,
                0.0,
                1.0,
                GridBagConstraints.WEST,
                GridBagConstraints.VERTICAL,
                new Insets(2, 2, 2, 2),
                0,
                0));
        }
        if (label != null)
        {
            this.label = new StatusLabel(label);
            add(this.label, new GridBagConstraints(
                x++,
                0,
                1,
                1,
                1.0,
                1.0,
                GridBagConstraints.WEST,
                GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2),
                0,
                0));
        }
    }

    public void setLabel(String label)
    {
        if (this.label != null)
        {
            this.label.setText(label);
        }
    }
}
