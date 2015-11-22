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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import chai_4d.mbus.map.constant.MapConstants;

public class HelpDialog extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 4257633619868006104L;

    public HelpDialog(Frame owner, String title) throws HeadlessException
    {
        super(owner, title, true);
        initialize();
    }

    private void initialize()
    {
        JLabel programName = new JLabel("m-Bus Map Screen");
        programName.setFont(programName.getFont().deriveFont(18f));

        String desc = "This is program for setting Bus Routes on Bangkok Map.";
        desc += " Bus is including BTS, MRT, BRT and Airport Link.";
        desc += " The information will be used for searching the Bus Number";
        desc += " according to where are you start and where will you go.";
        TextLabel programDesc = new TextLabel(desc);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(MapConstants.controlPanel);
        content.setPreferredSize(new Dimension(340, 200));
        content.add(programName, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
            0,
            5,
            15,
            5), 0, 0));
        content.add(new JLabel("Version : 1.0"), new GridBagConstraints(
            0,
            1,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL,
            new Insets(2, 5, 2, 5),
            0,
            0));
        content.add(new JLabel("Author : Chai_4D"), new GridBagConstraints(
            0,
            2,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL,
            new Insets(2, 5, 12, 5),
            0,
            0));
        content.add(programDesc, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
            2,
            5,
            12,
            5), 0, 0));
        content.add(new JLabel("Copyright @ 2008, All rights reserved."), new GridBagConstraints(
            0,
            4,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL,
            new Insets(2, 5, 2, 5),
            0,
            0));

        JButton butOK = new JButton("OK");
        butOK.setPreferredSize(new Dimension(80, 25));
        butOK.addActionListener(this);

        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(MapConstants.controlPanel);
        contentPane.add(content, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
            2,
            2,
            2,
            2), 0, 0));
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
        contentPane.add(butOK, new GridBagConstraints(
            0,
            2,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.EAST,
            GridBagConstraints.NONE,
            new Insets(5, 10, 7, 7),
            0,
            0));

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        //setResizable(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        dispose();
    }
}
