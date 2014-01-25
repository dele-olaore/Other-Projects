package com.dexter.mtnodometertracker;

import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.StandardTitleBar;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.FlowFieldManager;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;

public class FormScreen extends MainScreen
{
	private App app;
	
	private TextField initFuelLvlText;
	private TextField quantityText;
	private TextField finalFuelLvlText;
	private TextField odometerText;
	private TextField fuelRateText;
	private TextField costText;
	private TextField locationText;
	
	ButtonField btnSave;
	
	public FormScreen(App app)
	{
		super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);
		
		this.app = app;
		
		// Set the displayed title of the screen
        StandardTitleBar myTitleBar = new StandardTitleBar()
        .addIcon("img/icon.png")
        .addTitle(App.APP_TITLE)
        .addClock()
        .addNotifications()
        .addSignalIndicator();
        
        myTitleBar.setPropertyValue(StandardTitleBar.PROPERTY_BATTERY_VISIBILITY,
        StandardTitleBar.BATTERY_VISIBLE_LOW_OR_CHARGING);
        setTitleBar(myTitleBar);
		
		initFuelLvlText = new TextField();
		quantityText = new TextField();
		finalFuelLvlText = new TextField();
		odometerText = new TextField();
		fuelRateText = new TextField();
		costText = new TextField();
		locationText = new TextField();
		
		GridFieldManager g1 = new GridFieldManager(7, 2, GridFieldManager.AUTO_SIZE);
		g1.add(new LabelField("Initial Fuel Level")
		{
			public int getPreferredWidth()
			{
				return 150;
			}
		});
		g1.add(initFuelLvlText);
		g1.add(new LabelField("Quantity")
		{
			public int getPreferredWidth()
			{
				return 150;
			}
		});
		g1.add(quantityText);
		g1.add(new LabelField("Final Fuel Level")
		{
			public int getPreferredWidth()
			{
				return 150;
			}
		});
		g1.add(finalFuelLvlText);
		g1.add(new LabelField("Odometer")
		{
			public int getPreferredWidth()
			{
				return 150;
			}
		});
		g1.add(odometerText);
		g1.add(new LabelField("Fuel Rate")
		{
			public int getPreferredWidth()
			{
				return 150;
			}
		});
		g1.add(fuelRateText);
		g1.add(new LabelField("Cost")
		{
			public int getPreferredWidth()
			{
				return 150;
			}
		});
		g1.add(costText);
		g1.add(new LabelField("Location")
		{
			public int getPreferredWidth()
			{
				return 150;
			}
		});
		g1.add(locationText);
		
		add(g1);
		
		FlowFieldManager flowStatus = new FlowFieldManager(FlowFieldManager.FIELD_HCENTER);
		
		btnSave = new ButtonField("Save Details", ButtonField.FIELD_HCENTER);
		btnSave.setCommand(new Command(new CommandHandler()
        {
			public void execute(ReadOnlyCommandMetadata metadata, Object context)
			{
				//String ret = doSave();
				
				//App.errorDialog(ret);
			}
		}));
		flowStatus.add(btnSave);
		
		setStatus(flowStatus);
	}
}
