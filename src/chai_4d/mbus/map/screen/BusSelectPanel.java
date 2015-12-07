package chai_4d.mbus.map.screen;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import chai_4d.mbus.map.bean.MapDbBean;
import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.model.BusInfo;
import chai_4d.mbus.map.util.StringUtil;
import chai_4d.mbus.map.util.SwingUtil;

public class BusSelectPanel extends FormPanel
{
    private static final long serialVersionUID = -8643130899606137548L;

    private BusInfo busSelect = null;

    private JLabel lblBusNoTh = new JLabel("Bus No (TH)");
    private JComboBox txtBusNoTh = new JComboBox();

    private JLabel lblBusNoEn = new JLabel("Bus No (EN)");
    private JComboBox txtBusNoEn = new JComboBox();

    public BusSelectPanel(MainFrame mainFrame)
    {
        super(mainFrame);
        initialize();
    }

    private void initialize()
    {
        setLayout(new GridBagLayout());
        setBackground(MapConstants.controlPanel);
        setPreferredSize(new Dimension(300, 100));

        txtBusNoTh.setModel(new DefaultComboBoxModel(new Vector(MapDbBean.loadBusNo(1))));
        txtBusNoEn.setModel(new DefaultComboBoxModel(new Vector(MapDbBean.loadBusNo(2))));
        txtBusNoTh.insertItemAt("", 0);
        txtBusNoEn.insertItemAt("", 0);
        txtBusNoTh.setSelectedIndex(0);
        txtBusNoEn.setSelectedIndex(0);

        add(lblBusNoTh, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(
            txtBusNoTh,
            new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        add(lblBusNoEn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(
            txtBusNoEn,
            new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }

    public boolean validateForm()
    {
        if (StringUtil.isEmpty((String) txtBusNoTh.getSelectedItem()) && StringUtil.isEmpty((String) txtBusNoEn.getSelectedItem()))
        {
            SwingUtil.alertWarning("Please select Bus No (TH) or Bus No (EN).");
            txtBusNoTh.requestFocus();
            return false;
        }

        if (StringUtil.isEmpty((String) txtBusNoTh.getSelectedItem()) == false)
        {
            busSelect = MapDbBean.loadBusInfo(1, (String) txtBusNoTh.getSelectedItem());
        }
        else
        {
            busSelect = MapDbBean.loadBusInfo(2, (String) txtBusNoEn.getSelectedItem());
        }
        return true;
    }

    public BusInfo getBusSelect()
    {
        return busSelect;
    }
}
