import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScraperMain extends JFrame implements MouseListener{
	//change the arrays to lists you lazy bastage.
	private static Document doc;
	private static String[] conditions = new String[20];
	private static Elements conditionsList;
	private static Elements airport;
	private static Elements latLong;
	private static String currentTempF;
	private static String currentTempC;
	private static String skyCondition;
	//Next six strings come from Condition array
	private static String humidity;
	private static String wind;
	private static String barometer;
	private static String dewpoint;
	private static String visibility;
	private static String windChill;
	//mouse variables
	private static int mouseX;
	private static int mouseY;
	//day of week vars
	private static Calendar calendar;
	private static int currentDay;
	//forecast vars
	private static Elements forcastSummaryList;
	private static Element[] forcastSummaryElements = new Element[20]; 
	
	//constructor
	private ScraperMain() throws IOException{
		doc = Jsoup.connect("http://forecast.weather.gov/MapClick.php?site=mhx&zmx=1&zmy=1&map_x=324&map_y=151&x=324&y=151").get();
		//current-conditions-detail div, get li elements
		ScraperMain.conditionsList = doc.select(".current-conditions-detail li"); 
		//get latitude and longitude
		ScraperMain.latLong = doc.select("div#current_conditions_station > p").eq(2);
		ScraperMain.airport = doc.select("div#current_conditions_station > p").eq(1);
		//get current temp
		ScraperMain.currentTempF = doc.select("p.myforecast-current-lrg").text();
		ScraperMain.currentTempC = doc.select("span.myforecast-current-sm").text();
		//get sky conditions
		ScraperMain.skyCondition = doc.select("p.myforecast-current").text();
		//get forecast summary list
		ScraperMain.forcastSummaryList = doc.select("div.one-ninth-first");
		for (int i = 0; i < forcastSummaryList.size(); i++){
			forcastSummaryElements[i] = forcastSummaryList.get(i);
			System.out.println(forcastSummaryElements[i].text());
		}
		
		//mouse const
		mouseX = 0;
		mouseY = 0;
		//day of week stuff
		calendar = Calendar.getInstance(TimeZone.getTimeZone("est"));
		currentDay = calendar.get(Calendar.DAY_OF_WEEK); 
		
		setTitle("Frisco Weather");
		setSize(420, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addMouseListener(this);
	}

	public static void main(String args[]) throws IOException {
		
		ScraperMain weatherScraper = new ScraperMain();

		//get current conditions
		for (int i = 0; i < conditionsList.size(); i++){
			conditions[i] = conditionsList.get(i).text();
		}
		humidity = conditions[0];
		wind = conditions[1];
		barometer = conditions[2];
		dewpoint = conditions[3];
		visibility = conditions[4];
		windChill = conditions[5];
		
		//System.out.println("forecast = " + forcastSummaryList.text());
		
		//print current location and latitude longitude
		//System.out.println(airport.text() + "\n" + latLong.text());
	}
	
	public void paint(Graphics g){
		Font f = new Font ("Serif", Font.BOLD, 18); //title font
		Font f2 = new Font ("new times roman", Font.BOLD, 12);
		
		g.setFont(f);
		g.fill3DRect(40, 40, 340, 22, true); // title block
		g.drawRect(40, 40, 340, 300); // entire black frame
		g.setColor(Color.white);
		
		int currentYPos = 60;
		g.drawString(airport.text(), 45, currentYPos);currentYPos += 15;
		g.setFont(f2);
		g.setColor(Color.black);
		g.drawString("Current Temperature : " + currentTempF + " " + currentTempC, 45, currentYPos);currentYPos += 15;
		g.drawString("Sky Conditions : " + skyCondition, 45, currentYPos);currentYPos += 15;
		for (int i = 0; i < conditions.length; i++) {
			if (conditions[i] != null)
				g.drawString(conditions[i], 45, currentYPos);
				currentYPos += 15;
		}	
		//draw forecast buttons section
		// This block needs to be a for loop.
		g.fill3DRect(70, 268, 90, 20, true);
		g.setColor(Color.white);
		g.drawString(getDay(currentDay+1), 72, 282);
		
		g.setColor(Color.black);
		g.fill3DRect(165, 268, 90, 20, true);
		g.setColor(Color.white);
		g.drawString(getDay(currentDay+2), 167, 282);
		
		g.setColor(Color.black);
		g.fill3DRect(260, 268, 90, 20, true);
		g.setColor(Color.white);
		g.drawString(getDay(currentDay+3), 262, 282);
		
		g.setColor(Color.black);
		g.fill3DRect(70, 290, 90, 20, true);
		g.setColor(Color.white);
		g.drawString(getDay(currentDay+4), 72, 304);
		
		g.setColor(Color.black);
		g.fill3DRect(165, 290, 90, 20, true);
		g.setColor(Color.white);
		g.drawString(getDay(currentDay+5), 167, 304);
		
		g.setColor(Color.black);
		g.fill3DRect(260, 290, 90, 20, true);
		g.setColor(Color.white);
		g.drawString(getDay(currentDay+6), 262, 304);
		
		g.setColor(Color.black);
		g.fill3DRect(165, 312, 90, 20, true);
		g.setColor(Color.white);
		g.drawString(getDay(currentDay+7), 167, 326);
	}
	
	private String getDay(int i) {
		if (i > 7)
			i = i - 7;
		switch (i){
		case 1:
			return "Sunday";
		case 2:
			return "Monday";
		case 3:
			return "Tuesday";
		case 4:
			return "Wednesday";
		case 5:
			return "Thursday";
		case 6:
			return "Friday";
		case 7:
			return "Saturday";
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e){
		System.out.println("Mouse Clicked");
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		mouseX = e.getX();
		mouseY = e.getY();
		System.out.println("Mouse Pressed @ " + mouseX + " - " + mouseY);
		//check for tomorrow button
		if (mouseX > 49 && mouseX < 141 && mouseY > 267 && mouseY < 287){
			JOptionPane.showMessageDialog(this, getDay(currentDay+1) + " button pressed");
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		System.out.println("Mouse Released");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Mouse Entered");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("Mouse Exited");
	}
	
}
