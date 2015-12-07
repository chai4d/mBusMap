package chai_4d.mbus.map.screen;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import chai_4d.mbus.map.constant.MapConstants;

public class BusPathCalculator extends JDialog implements ActionListener, PropertyChangeListener
{
    private static final long serialVersionUID = 4774492044000418424L;

    private JButton butStart = new JButton("Start");
    private JProgressBar prgTaskProgress = new JProgressBar(0, 100);
    private JTextArea txtTaskOutput = new JTextArea(10, 30);
    private JButton butClose = new JButton("Close");
    private Task task;

    class Task extends SwingWorker<Void, Void>
    {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground()
        {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100)
            {
                //Sleep for up to one second.
                try
                {
                    Thread.sleep(random.nextInt(1000));
                }
                catch (InterruptedException ignore)
                {
                }
                //Make random progress.
                progress += random.nextInt(5);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done()
        {
            Toolkit.getDefaultToolkit().beep();
            butStart.setEnabled(true);
            butClose.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            txtTaskOutput.append("Done!\n\n");
        }
    }

    public BusPathCalculator(Frame owner, String title)
    {
        super(owner, title, true);
        initialize();
    }

    private void initialize()
    {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(MapConstants.controlPanel);
        contentPane.setPreferredSize(new Dimension(400, 300));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        butStart.setActionCommand("start");
        butStart.addActionListener(this);

        prgTaskProgress.setValue(0);
        prgTaskProgress.setStringPainted(true);
        prgTaskProgress.setPreferredSize(new Dimension(300, 20));

        JPanel panelProgress = new JPanel();
        panelProgress.setBackground(MapConstants.controlPanel);
        panelProgress.add(butStart);
        panelProgress.add(prgTaskProgress);

        txtTaskOutput.setMargin(new Insets(5, 5, 5, 5));
        txtTaskOutput.setEditable(false);

        JSeparator line = new JSeparator(SwingConstants.HORIZONTAL);
        line.setPreferredSize(new Dimension(400, 1));

        butClose.setActionCommand("close");
        butClose.addActionListener(this);

        JPanel panelClose = new JPanel();
        panelClose.setBackground(MapConstants.controlPanel);
        panelClose.add(line);
        panelClose.add(butClose);

        contentPane.add(panelProgress, BorderLayout.PAGE_START);
        contentPane.add(new JScrollPane(txtTaskOutput), BorderLayout.CENTER);
        contentPane.add(panelClose, BorderLayout.PAGE_END);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
    }

    public boolean validateForm()
    {
        return true;
    }

    public void actionPerformed(ActionEvent evt)
    {
        if ("start" == evt.getActionCommand())
        {
            butStart.setEnabled(false);
            butClose.setEnabled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Instances of javax.swing.SwingWorker are not reusuable, so
            // we create new instances as needed.
            task = new Task();
            task.addPropertyChangeListener(this);
            task.execute();
        }
        else if ("close" == evt.getActionCommand())
        {
            dispose();
        }
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if ("progress" == evt.getPropertyName())
        {
            int progress = (Integer) evt.getNewValue();
            prgTaskProgress.setValue(progress);
            txtTaskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
        }
    }
}
