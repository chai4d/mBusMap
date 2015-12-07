package chai_4d.mbus.map.screen;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import chai_4d.mbus.map.bean.MapDbBean;
import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.model.PointInfo;
import chai_4d.mbus.map.util.StringUtil;
import chai_4d.mbus.map.util.SwingUtil;

public class RouteSelectPanel extends FormPanel implements ActionListener
{
    private static final long serialVersionUID = 8609470588116606782L;

    private PointInfo sourcePoint = null;
    private PointInfo destinationPoint = null;

    private JLabel lblSource = new JLabel("Source");
    private JComboBox txtSourceTh = new JComboBox();
    private JComboBox txtSourceEn = new JComboBox();

    private JLabel lblDestination = new JLabel("Destination");
    private JComboBox txtDestinationTh = new JComboBox();
    private JComboBox txtDestinationEn = new JComboBox();

    public RouteSelectPanel(MainFrame mainFrame)
    {
        super(mainFrame);
        initialize();
    }

    private void initialize()
    {
        setLayout(new GridBagLayout());
        setBackground(MapConstants.controlPanel);
        setPreferredSize(new Dimension(750, 100));

        txtSourceTh.setModel(new DefaultComboBoxModel(new Vector(MapDbBean.loadPointName(1))));
        txtSourceEn.setModel(new DefaultComboBoxModel(new Vector(MapDbBean.loadPointName(2))));
        txtSourceTh.insertItemAt("", 0);
        txtSourceEn.insertItemAt("", 0);
        txtSourceTh.setSelectedIndex(0);
        txtSourceEn.setSelectedIndex(0);

        txtDestinationTh.setModel(new DefaultComboBoxModel(new Vector(MapDbBean.loadPointName(1))));
        txtDestinationEn.setModel(new DefaultComboBoxModel(new Vector(MapDbBean.loadPointName(2))));
        txtDestinationTh.insertItemAt("", 0);
        txtDestinationEn.insertItemAt("", 0);
        txtDestinationTh.setSelectedIndex(0);
        txtDestinationEn.setSelectedIndex(0);

        txtSourceTh.setActionCommand("sourceTh");
        txtSourceTh.addActionListener(this);
        txtSourceEn.setActionCommand("sourceEn");
        txtSourceEn.addActionListener(this);
        txtDestinationTh.setActionCommand("destinationTh");
        txtDestinationTh.addActionListener(this);
        txtDestinationEn.setActionCommand("destinationEn");
        txtDestinationEn.addActionListener(this);

        add(lblSource, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(
            txtSourceTh,
            new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(
            txtSourceEn,
            new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        add(
            lblDestination,
            new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(
            txtDestinationTh,
            new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(
            txtDestinationEn,
            new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }

    private void autoSelectItem(JComboBox box1, JComboBox box2)
    {
        if (!StringUtil.isEmpty((String) box1.getSelectedItem()))
        {
            long id1 = StringUtil.getIdValue((String) box1.getSelectedItem());
            for (int i = 0; i < box2.getItemCount(); i++)
            {
                long id2 = StringUtil.getIdValue((String) box2.getItemAt(i));
                if (id1 == id2)
                {
                    box2.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public void actionPerformed(ActionEvent evt)
    {
        if ("sourceTh" == evt.getActionCommand())
        {
            autoSelectItem(txtSourceTh, txtSourceEn);
        }
        else if ("sourceEn" == evt.getActionCommand())
        {
            autoSelectItem(txtSourceEn, txtSourceTh);
        }
        else if ("destinationTh" == evt.getActionCommand())
        {
            autoSelectItem(txtDestinationTh, txtDestinationEn);
        }
        else if ("destinationEn" == evt.getActionCommand())
        {
            autoSelectItem(txtDestinationEn, txtDestinationTh);
        }
    }

    public boolean validateForm()
    {
        if (StringUtil.isEmpty((String) txtSourceTh.getSelectedItem()) && StringUtil.isEmpty((String) txtSourceEn.getSelectedItem()))
        {
            SwingUtil.alertWarning("Please select Source (TH) or Source (EN).");
            txtSourceTh.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty((String) txtDestinationTh.getSelectedItem()) && StringUtil.isEmpty((String) txtDestinationEn.getSelectedItem()))
        {
            SwingUtil.alertWarning("Please select Destination (TH) or Destination (EN).");
            txtDestinationTh.requestFocus();
            return false;
        }

        if (StringUtil.isEmpty((String) txtSourceTh.getSelectedItem()) == false)
        {
            sourcePoint = MapDbBean.loadPointInfoById(StringUtil.getIdValue((String) txtSourceTh.getSelectedItem()));
        }
        if (StringUtil.isEmpty((String) txtDestinationTh.getSelectedItem()) == false)
        {
            destinationPoint = MapDbBean.loadPointInfoById(StringUtil.getIdValue((String) txtDestinationTh.getSelectedItem()));
        }
        return true;
    }

    public PointInfo getSourcePoint()
    {
        return sourcePoint;
    }

    public PointInfo getDestinationPoint()
    {
        return destinationPoint;
    }
}
