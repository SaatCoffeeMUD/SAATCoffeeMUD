package com.planet_ink.coffee_mud.Items.Weapons;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

public class GenBow extends StdBow
{
	public String ID(){	return "GenBow";}
	protected String	readableText="";
	public GenBow()
	{
		super();

		setName("a generic short bow");
		setDisplayText("a generic short bow sits here.");
		setDescription("");
		recoverEnvStats();
	}
	public Environmental newInstance()
	{
		return new GenBow();
	}
	public boolean isGeneric(){return true;}


	public String text()
	{
		return Generic.getPropertiesStr(this,false);
	}
	public String readableText(){return readableText;}
	public void setReadableText(String text){readableText=text;}

	public void setMiscText(String newText)
	{
		miscText="";
		Generic.setPropertiesStr(this,newText,false);
		recoverEnvStats();
	}
}

