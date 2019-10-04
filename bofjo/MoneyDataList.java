package bofjo;
import java.io.Serializable;
import java.util.ArrayList;

public class MoneyDataList implements Serializable{
	private double listSpendings;
	private ArrayList<MoneyData> list;
	
	public MoneyDataList() {
		list = new ArrayList<MoneyData>();
		listSpendings = 0;
	}
	public double getListSpendings() {
		return listSpendings;
	}
	
	public void add(MoneyData md) {
		list.add(md);
		listSpendings += md.getAmnt();
	}
	
	public void remove(MoneyData md) {
		listSpendings -= md.getAmnt();
		md.delete();
		list.remove(md);
	}
	public void remove(int i) {
		listSpendings -= list.get(i).getAmnt();
		list.get(i).delete();
		list.remove(list.get(i));
	}
	public MoneyData get(int i) {
		return list.get(i);
	}
	
	public int size() {
		return list.size();
	}
	
}
