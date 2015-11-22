package chai_4d.mbus.map.screen;

import javax.swing.JPanel;

public abstract class FormPanel extends JPanel
{
    private static final long serialVersionUID = 193802199168215195L;

    protected MainFrame mainFrame = null;

    public FormPanel(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
    }

    public abstract boolean validateForm();
}
