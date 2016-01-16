package presentation;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRendererTypesBiens extends DefaultTableCellRenderer {
	private ArrayList<Color> listeCouleurs ;
	private ArrayList<Color> couleursRetenus;
	
	public CellRendererTypesBiens(){
		super();
		this.listeCouleurs = new ArrayList<Color>();
		this.listeCouleurs.add(Color.cyan);
		this.listeCouleurs.add(Color.green);
		this.listeCouleurs.add(Color.yellow);
		this.listeCouleurs.add(Color.red);
		this.listeCouleurs.add(Color.pink);
		this.listeCouleurs.add(Color.orange);
		this.listeCouleurs.add(Color.magenta);
		this.listeCouleurs.add(new Color(198, 95, 251));
		this.listeCouleurs.add(new Color(244, 104, 147));
		this.listeCouleurs.add(new Color(147, 195, 201));

		this.couleursRetenus = new ArrayList<Color>();
		this.setHorizontalAlignment( SwingConstants.CENTER );
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		Component component = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		if (value.equals("") || value == null) {
			Color clr = Color.yellow;
			component.setBackground(clr);
		} 
		else if (value instanceof String || value instanceof Boolean) {
			Color clr;
			switch(row){
			case 0 : clr=new Color(198, 224, 180);
			component.setBackground(clr);
			this.couleursRetenus.add(clr);
			break;
			case 1 : clr=new Color(248, 203, 173);
			component.setBackground(clr);
			this.couleursRetenus.add(clr);
			break;
			case 2 : clr=new Color(255, 230, 153);
			component.setBackground(clr);
			this.couleursRetenus.add(clr);
			break;
			case 3 : clr=new Color(250, 172, 207);
			component.setBackground(clr);
			this.couleursRetenus.add(clr);
			break;
			case 4 : clr=new Color(189, 215, 238);
			component.setBackground(clr);
			this.couleursRetenus.add(clr);
			break;
			case 5 : clr=new Color(172, 185, 202);
			component.setBackground(clr);
			this.couleursRetenus.add(clr);
			break;
			case 6 : clr=new Color(219, 219, 219);
			component.setBackground(clr);
			this.couleursRetenus.add(clr);
			break;
			default:
				if (row - 7 < this.listeCouleurs.size()) {
					component.setBackground(listeCouleurs.get(row - 7));
					this.couleursRetenus.add(listeCouleurs.get(row - 7));
				}
				else{
					clr = new Color(255, 255, 255);
					component.setBackground(clr);
					this.couleursRetenus.add(clr);
				}
				/*
				if (couleurs.size()>= row - 7) {
					Random rand = new Random();
					float r = rand.nextFloat();
					float g = rand.nextFloat();
					float b = rand.nextFloat();
					clr = new Color(r, g, b);
					couleurs.add(clr);
					component.setBackground(couleurs.get(couleurs.size()-1));
				}
				else{
					clr=couleurs.get(row-7);
					component.setBackground(clr);
				}
				*/
		}
		} else {
			Color clr = new Color(255, 255, 255);
			component.setBackground(clr);
			this.couleursRetenus.add(clr);
		}
		return component;
	}

}