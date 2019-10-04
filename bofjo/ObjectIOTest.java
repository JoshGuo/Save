package bofjo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;

public class ObjectIOTest {

	public static void main(String[] args) {
		File monthData = new File(GregorianCalendar.DAY_OF_MONTH + "_" + new GregorianCalendar().getTime().toString().substring(26) + "_data.dat");// GregorianCalendar.MONTH + "." + GregorianCalendar.YEAR +"data.dat"
		MoneyDataList values = new MoneyDataList();
		MoneyData a = new MoneyData(10,"Jasmine", "Food");
		MoneyData b = new MoneyData(60,";)", "Fun");
		MoneyData c = new MoneyData(80,"Supreme Box Logo", "Clothes");
		MoneyData d = new MoneyData(5,"Spotify/Hulu", "Subscription");
		MoneyData g = new MoneyData(10,"Amazon Prime", "Subscription");
		MoneyData e = new MoneyData(10,"2am Uber", "Transportation");
		MoneyData h = new MoneyData(15,"Stuff", "Other");
		
		values.add(a);
		values.add(b);
		values.add(c);
		values.add(d);
		values.add(e);
		values.add(g);
		values.add(h);
		
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(monthData));){	
			output.writeObject(values);
			output.close();
		}catch (IOException ex) {System.out.println("duck");}
		
		//System.out.println(GregorianCalendar.month);
	}

}
