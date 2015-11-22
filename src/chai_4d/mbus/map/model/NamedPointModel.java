package chai_4d.mbus.map.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.util.SwingUtil;

public class NamedPointModel extends AbstractTableModel
{
    private static final long serialVersionUID = 8815834738331033698L;

    private String[] columnNames = { "Name (TH)", "Name (EN)" };
    private List<PointName> data = null;

    public String getColumnName(int col)
    {
        return columnNames[col];
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }

    public int getRowCount()
    {
        int count = 0;
        for (int i = 0; data != null && i < data.size(); i++)
        {
            PointName pointName = data.get(i);
            if (pointName.getMode() != Mode.DELETE)
            {
                count++;
            }
        }
        return count;
    }

    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    public PointName getRow(int row)
    {
        int count = 0;
        for (int i = 0; data != null && i < data.size(); i++)
        {
            PointName pointName = data.get(i);
            if (pointName.getMode() != Mode.DELETE)
            {
                count++;
            }
            if ((count - 1) == row)
            {
                return pointName;
            }
        }
        return null;
    }

    public Object getValueAt(int row, int col)
    {
        PointName aRow = getRow(row);
        if (aRow != null)
        {
            switch (col)
            {
                case 0:
                    return aRow.getNameTh();
                case 1:
                    return aRow.getNameEn();
            }
        }
        return null;
    }

    public void setValueAt(Object value, int row, int col)
    {
        if (value == null || ((String) value).trim().equals(""))
        {
            SwingUtil.alertWarning("Can't be empty value.");
            return;
        }

        PointName aRow = getRow(row);
        if (aRow != null)
        {
            switch (col)
            {
                case 0:
                    aRow.setNameTh((String) value);
                    break;
                case 1:
                    aRow.setNameEn((String) value);
                    break;
            }
        }
        fireTableCellUpdated(row, col);
    }

    public List<PointName> getData()
    {
        return data;
    }

    public void setData(List<PointName> data)
    {
        this.data = data;
        fireTableDataChanged();
    }
}