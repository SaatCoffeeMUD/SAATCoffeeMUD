package com.planet_ink.coffee_mud.Items;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;


public class SmallChest extends LockableContainer
{
	public String ID(){	return "SmallChest";}
	public SmallChest()
	{
		super();
		setName("a small chest");
		setDisplayText("a small wooden chest sits here.");
		setDescription("It\\`s of solid wood construction with metal bracings.  The lid has a key hole.");
		capacity=50;
		material=EnvResource.RESOURCE_OAK;
		baseGoldValue=15;
		baseEnvStats().setWeight(25);
		recoverEnvStats();
	}

	public Environmental newInstance()
	{
		return new SmallChest();
	}

}
