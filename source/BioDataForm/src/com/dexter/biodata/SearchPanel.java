package com.dexter.biodata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

import javax.persistence.Query;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PatternPredicate;
import org.jdesktop.swingx.decorator.ShadingColorHighlighter;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.renderer.IconValues;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.table.ColumnFactory;

import com.dexter.biodata.model.BioData;
import com.dexter.biodata.model.Sector;
import com.dexter.biodata.model.State;

public class SearchPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private BioDataForm form;
	
	private JTextField txtFirstname, txtLastname, txtNIS;
	private JComboBox cbState, cbSector;
	
	private JXDatePicker stPicker, edPicker;
	
	private JButton searchBtn;
	
	private JXTable resultTable;
	
	private DefaultTableModel resultTableModel;
	Vector<BioData> rows = null;
	
	public SearchPanel(final BioDataForm form)
	{
		this.form = form;
		
		txtFirstname = new JTextField();
		txtLastname = new JTextField();
		txtNIS = new JTextField();
		
		Vector v2 = new Vector();
		v2.addElement("");
		if(form.getStates() != null)
		{
			for(State e : form.getStates())
			{
				v2.addElement(e.getName());
			}
		}
		cbState = new JComboBox(v2);
		
		Vector v = new Vector();
		v.addElement("");
		if(form.getSectors() != null)
		{
			for(Sector e : form.getSectors())
			{
				v.addElement(e.getName() + " (" + e.getType().getName() + ")");
			}
		}
		cbSector = new JComboBox(v);
		
		stPicker = new JXDatePicker();
		edPicker = new JXDatePicker();
		
		searchBtn = new JButton("Search");
		
		resultTableModel = new DefaultTableModel();
		resultTableModel.addColumn("ID Code");
		resultTableModel.addColumn("Sur name");
		resultTableModel.addColumn("First name");
		resultTableModel.addColumn("NIS");
		resultTableModel.addColumn("Photo");
		resultTableModel.addColumn("Finger print");
		resultTableModel.addColumn("Rank");
		resultTableModel.addColumn("Modified date");
		resultTableModel.addTableModelListener(new TableModelListener()
		{
			@Override
			public void tableChanged(final TableModelEvent e)
			{
				EventQueue.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						resultTable.setRowHeight(e.getFirstRow(), 50);
					}
				});
			}
		});
		
		resultTable = new JXTable(resultTableModel);
		resultTable.setEditable(false);
		resultTable.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 2)
				{
					if(rows != null)
					{
						JXTable target = (JXTable)e.getSource();
						int row = target.getSelectedRow();
						//int column = target.getSelectedColumn();
						
						form.setData(rows.get(row));
						form.setFields();
					}
				}
			}
		});
		
		resultTable.setDefaultRenderer(resultTable.getColumnClass(4), new ImageRenderer());
		resultTable.setDefaultRenderer(resultTable.getColumnClass(5), new ImageRenderer());
		
		Highlighter simpleStriping = HighlighterFactory.createSimpleStriping();
		PatternPredicate patternPredicate = new PatternPredicate("ˆM", 1);
		ColorHighlighter magenta = new ColorHighlighter(patternPredicate, null, Color.MAGENTA, null, Color.MAGENTA);
		Highlighter shading = new ShadingColorHighlighter(new HighlightPredicate.ColumnHighlightPredicate(1));
		
		resultTable.setHighlighters(simpleStriping, magenta, shading);
		
		JPanel p1 = new JPanel(new GridLayout(4, 1));
		p1.add(new JLabel("Start date", JLabel.CENTER));
		p1.add(stPicker);
		p1.add(new JLabel("End date", JLabel.CENTER));
		p1.add(edPicker);
		
		JPanel p2 = new JPanel(new GridLayout(4, 1));
		p2.add(new JLabel("Sur name", JLabel.CENTER));
		p2.add(txtLastname);
		p2.add(new JLabel("First name", JLabel.CENTER));
		p2.add(txtFirstname);
		
		JPanel p3 = new JPanel(new GridLayout(4, 1));
		p3.add(new JLabel("NIS", JLabel.CENTER));
		p3.add(txtNIS);
		p3.add(new JLabel());
		p3.add(new JLabel());
		
		JPanel p4 = new JPanel(new GridLayout(4, 1));
		p4.add(new JLabel("State", JLabel.CENTER));
		p4.add(cbState);
		p4.add(new JLabel("Sector", JLabel.CENTER));
		p4.add(cbSector);
		
		JPanel pbtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pbtn.add(searchBtn);
		
		JPanel ps = new JPanel(new GridLayout(1, 4));
		ps.add(p1);
		ps.add(p2);
		ps.add(p3);
		ps.add(p4);
		
		JPanel ptop = new JPanel(new BorderLayout());
		ptop.add(ps, BorderLayout.NORTH);
		ptop.add(pbtn, BorderLayout.SOUTH);
		
		setLayout(new BorderLayout());
		
		add(ptop, BorderLayout.NORTH);
		
		JXPanel centerPanel = new JXPanel(new BorderLayout());
		centerPanel.add(new JScrollPane(resultTable));
		
		add(centerPanel, BorderLayout.CENTER);
		
		searchBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				search();
			}
		});
	}
	
	private void search()
	{
		rows = null;
		
		while(resultTableModel.getRowCount() > 0)
			resultTableModel.removeRow(0);
		
		String fname = txtFirstname.getText();
		String lname = txtLastname.getText();
		String nis = txtNIS.getText();
		State st = null;
		Sector sec = null;
		if(cbState.getSelectedIndex() >= 1)
		{
			st = form.getStates().get(cbState.getSelectedIndex()-1);
		}
		if(cbSector.getSelectedIndex() >= 1)
		{
			sec = form.getSectors().get(cbSector.getSelectedIndex()-1);
		}
		Date start_dt = stPicker.getDate();
		Date end_dt = edPicker.getDate();
		
		StringBuilder str = new StringBuilder("Select e from BioData e where e.id > 0");
		
		if(fname != null && fname.trim().length() > 0)
		{
			str.append(" and e.firstname like :fname");
		}
		if(lname != null && lname.trim().length() > 0)
		{
			str.append(" and e.lastname like :lname");
		}
		if(nis != null && nis.trim().length() > 0)
		{
			str.append(" and e.nis like :nis");
		}
		if(st != null)
		{
			str.append(" and e.state = :st");
		}
		if(sec != null)
		{
			str.append(" and e.sector = :sec");
		}
		if(start_dt != null && end_dt != null)
		{
			if(end_dt.after(start_dt))
			{
				str.append(" and e.crt_dt between :start_dt and :end_dt");
			}
		}
		
		Query q = form.getDbUtil().createQuery(str.toString());
		if(fname != null && fname.trim().length() > 0)
			q.setParameter("fname", "%"+fname+"%");
		if(lname != null && lname.trim().length() > 0)
			q.setParameter("lname", "%"+lname+"%");
		if(nis != null && nis.trim().length() > 0)
			q.setParameter("nis", "%"+nis+"%");
		if(st != null)
			q.setParameter("st", st);
		if(sec != null)
			q.setParameter("sec", sec);
		if(start_dt != null && end_dt != null)
		{
			if(end_dt.after(start_dt))
			{
				q.setParameter("start_dt", start_dt);
				q.setParameter("end_dt", end_dt);
			}
		}
		
		Object result = form.getDbUtil().search(q);
		if(result != null)
		{
			rows = (Vector<BioData>)result;
			
			for(BioData e : rows)
			{
				ImageIcon photo = null, fingerprint = null;
				
				if(e.getPhoto() != null)
					photo = new ImageIcon(e.getPhoto());
				if(e.getFingerprint() != null)
					fingerprint = new ImageIcon(e.getFingerprint());
				
				Object[] rowData = new Object[]
				{
					e.getIdentificationcode(),
					e.getLastname(),
					e.getFirstname(),
					e.getNis(),
					(photo != null) ? photo : "N/A",
					(fingerprint != null) ? fingerprint : "N/A",
					e.getRank(),
					e.getCrt_dt()
				};
				resultTableModel.addRow(rowData);
			}
			
			JOptionPane.showMessageDialog(form, rows.size() + " record(s) found.", "Search", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(form, "Search Error, no returned values.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public class ImageRenderer extends JLabel implements TableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public ImageRenderer()
		{
			setOpaque(false); //MUST do this for background to show up.
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			setIcon(null);
			setText(null);
			setHorizontalAlignment(JLabel.LEFT);
			if(column == 4)
			{
				if(rows.get(row).getPhoto() != null)
				{
					setHorizontalAlignment(JLabel.CENTER);
					ImageIcon img = new ImageIcon(rows.get(row).getPhoto());
					img.setImage(img.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
					setIcon(img);
				}
				else
				{
					setText("N/A");
				}
			}
			else if(column == 5)
			{
				if(rows.get(row).getFingerprint() != null)
				{
					setHorizontalAlignment(JLabel.CENTER);
					ImageIcon img = new ImageIcon(rows.get(row).getFingerprint());
					img.setImage(img.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
					setIcon(img);
				}
				else
				{
					setText("N/A");
				}
			}
			else
			{
				setText(value.toString());
			}
			//setToolTipText("Image"); //Discussed in the following section
			return this;
		}
	}
}
