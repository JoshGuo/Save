package bofjo;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class MoneyData implements Serializable{
	private double amnt;
	private String desc;
	private String category;
	private GregorianCalendar date;
	private static double totalSpent = 0.0;
	
	public MoneyData(double amnt, String desc, String category) {
		this.amnt = amnt;
		this.desc = desc;
		this.category = category;
		this.date = new GregorianCalendar();
		totalSpent += amnt;
	}
	public double getAmnt() {
		return amnt;
	}
	public String getDesc() {
		return desc;
	}
	public String getCategory() {
		return category;
	}
	public GregorianCalendar getCal() {
		return date;
	}
	public double getTotalSpent() {
		return totalSpent;
	}
	
	public void delete() {
		totalSpent -= amnt;
	}
	public String toString() {
		return String.format(desc + ": $%.2f", amnt);
	}
}