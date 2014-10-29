package it.smartcommunitylab.bikesharing.rovereto.activity;

import android.os.Bundle;
import it.smartcommunitylab.bikesharing.rovereto.R;
import smartcampus.activity.MainActivity;

public class MainRoveretoActivity extends MainActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainClass = MainRoveretoActivity.class;
	}

	@Override
	protected int getAboutLayout() {
		return R.layout.fragment_about;
	}

}
