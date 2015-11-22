package chai_4d.mbus.map.screen;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import chai_4d.mbus.map.constant.MapConstants;

public class OptionDialog extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 4257633619868006104L;

    private FormPanel form = null;

    private JButton butOK = new JButton("OK");
    private JButton butCancel = new JButton("Cancel");
    private boolean OK = false;

    public OptionDialog(Frame owner, String title, FormPanel form) throws HeadlessException
    {
        super(owner, title, true);
        this.form = form;
        initialize();
    }

    private void initialize()
    {
        butOK.setPreferredSize(new Dimension(80, 25));
        butCancel.setPreferredSize(new Dimension(80, 25));
        butOK.addActionListener(this);
        butCancel.addActionListener(this);

        JPanel control = new JPanel(new GridBagLayout());
        control.setBackground(MapConstants.controlPanel);
        control.add(butOK, new GridBagConstraints(
            0,
            0,
            1,
            1,
            0.0,
            0.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.NONE,
            new Insets(2, 10, 5, 10),
            0,
            0));
        control.add(butCancel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            2,
            10,
            5,
            10), 0, 0));

        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(MapConstants.controlPanel);
        contentPane.add(form, new GridBagConstraints(
            0,
            0,
            1,
            1,
            1.0,
            1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.BOTH,
            new Insets(2, 2, 2, 2),
            0,
            0));
        contentPane.add(new JSeparator(SwingConstants.HORIZONTAL), new GridBagConstraints(
            0,
            1,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL,
            new Insets(2, 2, 2, 2),
            0,
            0));
        contentPane.add(control, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
            2,
            2,
            2,
            2), 0, 0));

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(butOK))
        {
            OK = form.validateForm();
            if (OK)
            {
                dispose();
            }
        }
        else if (e.getSource().equals(butCancel))
        {
            dispose();
        }
    }

    public boolean isOK()
    {
        return OK;
    }
}
