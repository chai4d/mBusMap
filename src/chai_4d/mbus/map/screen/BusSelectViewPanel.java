package chai_4d.mbus.map.screen;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import chai_4d.mbus.map.bean.MapDbBean;
import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.model.BusInfo;
import chai_4d.mbus.map.util.StringUtil;
import chai_4d.mbus.map.util.SwingUtil;

public class BusSelectViewPanel extends FormPanel implements ActionListener
{
    private static final long serialVersionUID = -7183516506764032238L;

    private static final int MAX_SELECTED = 5;

    private BusInfo busSelect = null;

    private JLabel lblBusNoTh = new JLabel("Bus No (TH)");
    private JComboBox txtBusNoTh = new JComboBox();

    private JLabel lblBusNoEn = new JLabel("Bus No (EN)");
    private JComboBox txtBusNoEn = new JComboBox();

    private JButton butAdd = new JButton("Add");
    private JButton butRemove = new JButton("Remove");

    private JList lstBusNo = new JList(new DefaultListModel());
    private JLabel lblWarning = new JLabel("Max selection is " + MAX_SELECTED + " items.");

    public BusSelectViewPanel(MainFrame mainFrame)
    {
        super(mainFrame);
        initialize();
    }

    private void initialize()
    {
        setLayout(new GridBagLayout());
        setBackground(MapConstants.controlPanel);
        setPreferredSize(new Dimension(300, 250));

        txtBusNoTh.setModel(new DefaultComboBoxModel(new Vector(MapDbBean.loadBusNo(1))));
        txtBusNoEn.setModel(new DefaultComboBoxModel(new Vector(MapDbBean.loadBusNo(2))));
        txtBusNoTh.insertItemAt("", 0);
        txtBusNoEn.insertItemAt("", 0);
        txtBusNoTh.setSelectedIndex(0);
        txtBusNoEn.setSelectedIndex(0);

        add(lblBusNoTh, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(txtBusNoTh, new GridBagConstraints(
            1,
            0,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 5, 5),
            0,
            0));

        add(lblBusNoEn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(txtBusNoEn, new GridBagConstraints(
            1,
            1,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 5, 5),
            0,
            0));

        butAdd.setPreferredSize(new Dimension(60, 25));
        butRemove.setPreferredSize(new Dimension(80, 25));
        butAdd.addActionListener(this);
        butRemove.addActionListener(this);

        JPanel pnlButton = new JPanel(new GridBagLayout());
        pnlButton.setBackground(MapConstants.controlPanel);
        pnlButton.add(butAdd, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            2,
            10,
            5,
            10), 0, 0));
        pnlButton.add(butRemove, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            2,
            10,
            5,
            10), 0, 0));

        add(pnlButton, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        lstBusNo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstBusNo.setCellRenderer(new BusNoRenderer(10));
        JScrollPane sclBusNo = new JScrollPane(lstBusNo);
        sclBusNo.setSize(new Dimension(100, 100));
        sclBusNo.setPreferredSize(new Dimension(100, 100));
        sclBusNo.setMinimumSize(new Dimension(100, 100));
        add(sclBusNo, new GridBagConstraints(
            0,
            3,
            2,
            1,
            1.0,
            0.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 2, 5),
            0,
            0));

        add(
            lblWarning,
            new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 5, 5, 5), 0, 0));
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(butAdd))
        {
            if (StringUtil.isEmpty((String) txtBusNoTh.getSelectedItem()) && StringUtil.isEmpty((String) txtBusNoEn.getSelectedItem()))
            {
                SwingUtil.alertWarning("Please select Bus No (TH) or Bus No (EN).");
                txtBusNoTh.requestFocus();
                return;
            }
            else if (lstBusNo.getModel().getSize() >= MAX_SELECTED)
            {
                SwingUtil.alertWarning("Your Bus No is equals max selection.");
                return;
            }

            BusInfo tmpBus = null;
            if (StringUtil.isEmpty((String) txtBusNoTh.getSelectedItem()) == false)
            {
                tmpBus = MapDbBean.loadBusInfo(1, (String) txtBusNoTh.getSelectedItem());
            }
            else
            {
                tmpBus = MapDbBean.loadBusInfo(2, (String) txtBusNoEn.getSelectedItem());
            }

            if (tmpBus != null)
            {
                DefaultListModel model = (DefaultListModel) lstBusNo.getModel();
                for (int i = 0; i < model.getSize(); i++)
                {
                    String[] item = (String[]) model.get(i);
                    if (item[0].equals(tmpBus.getBusNoTh()) || item[1].equals(tmpBus.getBusNoEn()))
                    {
                        SwingUtil.alertWarning("Your Bus No is selected.");
                        return;
                    }
                }
                model.addElement(new String[] { tmpBus.getBusNoTh(), tmpBus.getBusNoEn() });
                txtBusNoTh.setSelectedIndex(0);
                txtBusNoEn.setSelectedIndex(0);
            }
        }
        else if (e.getSource().equals(butRemove))
        {
            int index = lstBusNo.getSelectedIndex();
            if (index >= 0)
            {
                DefaultListModel model = (DefaultListModel) lstBusNo.getModel();
                model.remove(index);
            }
            else
            {
                SwingUtil.alertWarning("Please select Bus No before remove.");
            }
        }
    }

    public boolean validateForm()
    {
        if (lstBusNo.getModel().getSize() == 0)
        {
            SwingUtil.alertWarning("Please select Bus No (TH) or Bus No (EN).");
            txtBusNoTh.requestFocus();
            return false;
        }

        List<String> busNoTh = new ArrayList<String>();
        List<String> busNoEn = new ArrayList<String>();
        DefaultListModel model = (DefaultListModel) lstBusNo.getModel();
        for (int i = 0; i < model.getSize(); i++)
        {
            String[] item = (String[]) model.get(i);
            busNoTh.add(item[0]);
            busNoEn.add(item[1]);
        }
        busSelect = new BusInfo();
        MapDbBean.loadBusLine(busNoTh, busNoEn, busSelect.getBusLine());
        return true;
    }

    public BusInfo getBusSelect()
    {
        return busSelect;
    }
}
