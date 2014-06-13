package smartcampus.activity;

import java.util.ArrayList;

import smartcampus.model.Bike;
import smartcampus.model.Station;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class MainActivity extends ActionBarActivity
{

	private Button toMap, toStations;

	private ArrayList<Station> s;
	
	private ArrayList<Bike> bikes;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toMap = (Button) findViewById(R.id.btToMap);
		toStations = (Button) findViewById(R.id.btnStations);
		addListeners();

		s = new ArrayList<Station>();
		bikes = new ArrayList<Bike>();
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "STAZIONE FF.SS. - Piazzale Orsi", "38068", 45.890189, 11.034275)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "OSPEDALE - Corso Verona", "38068",45.882221, 11.040483)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "MUNICIPIO - Piazzetta Sichardt", "38068", 45.886525, 11.044749)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "MART - Corso Bettini", "38068", 45.893571, 11.043891)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "ZONA INDUSTRIALE - Viale Caproni", "38068", 45.866352, 11.019310)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "VIA PAOLI - Via Manzoni/Via Paoli", "38068", 45.892256,
		// 11.039370)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "MARCO - Via 2 Novembre", "38068", 45.840603, 11.009298)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "SACCO - Viale della Vittoria/Via Udine", "38068", 45.893120,
		// 11.038846)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "NORIGLIO - Via Chiesa San Martino", "38068", 45.883409,
		// 11.072827)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "BRIONE - Piazza della Pace", "38068", 45.904255, 11.044859)));
		// s.add(new Station(new CompleteAddress(Locale.ITALIAN, "Rovereto",
		// "PIAZZA ROSMINI", "38068", 45.891021, 11.038729)));

		s.add(new Station(new GeoPoint(45.890189, 11.034275),
				"STAZIONE FF.SS. - Piazzale Orsi", 12));
		s.add(new Station(new GeoPoint(45.882221, 11.040483),
				"OSPEDALE - Corso Verona", 12));
		s.add(new Station(new GeoPoint(45.886525, 11.044749),
				"MUNICIPIO - Piazzetta Sichardt", 6));
		s.add(new Station(new GeoPoint(45.893571, 11.043891),
				"MART - Corso Bettini", 6));
		s.add(new Station(new GeoPoint(45.866352, 11.019310),
				"ZONA INDUSTRIALE - Viale Caproni", 6));
		s.add(new Station(new GeoPoint(45.892256, 11.039370),
				"VIA PAOLI - Via Manzoni/Via Paoli", 12));
		s.add(new Station(new GeoPoint(45.840603, 11.009298),
				"SACCO - Viale della Vittoria/Via Udine", 6));
		s.add(new Station(new GeoPoint(45.893120, 11.038846),
				"SACCO - Viale della Vittoria/Via Udine", 12));
		s.add(new Station(new GeoPoint(45.883409, 11.072827),
				"NORIGLIO - Via Chiesa San Martino", 6));
		s.add(new Station(new GeoPoint(45.904255, 11.044859),
				"BRIONE - Piazza della Pace", 6));
		s.add(new Station(new GeoPoint(45.891021, 11.038729), "PIAZZA ROSMINI - via boh",
				6));

		bikes.add(new Bike(new GeoPoint(45.924255, 11.064859), "0"));
		s.get(0).setUsedSlots(11);
		s.get(0).addReport("segnalazione 1");
		s.get(0).addReport("segnalazione 2");
		s.get(0).addReport("segnalazione 3");
		s.get(1).setUsedSlots(12);
		s.get(2).setUsedSlots(6);
		s.get(3).setUsedSlots(5);
	}

	private void addListeners()
	{
		toMap.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent mapIntent = new Intent(getApplicationContext(),
						OsmMap.class);
				
		
				mapIntent.putParcelableArrayListExtra("bikes", bikes);
				mapIntent.putParcelableArrayListExtra("stations", s);
				
				
				startActivity(mapIntent);
			}
		});
		toStations.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent listIntent = new Intent(getApplicationContext(),
						StationsActivity.class);
				
				
				listIntent.putParcelableArrayListExtra("stations", s);
				
				startActivity(listIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}