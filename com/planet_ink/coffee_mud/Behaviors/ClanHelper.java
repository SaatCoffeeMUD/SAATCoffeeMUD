package com.planet_ink.coffee_mud.Behaviors;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

public class ClanHelper extends StdBehavior
{
	public String ID(){return "ClanHelper";}
	public Behavior newInstance()
	{
		return new ClanHelper();
	}
	/** this method defines how this thing responds
	 * to environmental changes.  It may handle any
	 * and every affect listed in the Affect class
	 * from the given Environmental source */
	public void affect(Environmental affecting, Affect affect)
	{
		super.affect(affecting,affect);
		MOB source=affect.source();
		if(!canFreelyBehaveNormal(affecting))
			return;
		MOB observer=(MOB)affecting;
		if(affect.target()==null)
			return;
		if(!(affect.target() instanceof MOB))
			return;
		MOB target=(MOB)affect.target();
		
		if((target==null)||(observer==null)) return;
		if((source!=observer)
		&&(Util.bset(affect.targetCode(),Affect.MASK_MALICIOUS))
		&&(target!=observer)
		&&(source!=target)
		&&(Sense.canBeSeenBy(source,observer))
		&&(Sense.canBeSeenBy(target,observer))
		&&(!BrotherHelper.isBrother(source,observer)))
		{
			if(observer.charStats().getMyRace().ID().equalsIgnoreCase(target.charStats().getMyRace().ID()))
			{
				boolean yep=Aggressive.startFight(observer,source,false);
				String reason="THAT`S MY FRIEND!! CHARGE!!";
				if((observer.charStats().getMyRace().ID().equals(target.charStats().getMyRace().ID()))
				&&(!observer.charStats().getMyRace().ID().equals(source.charStats().getMyRace().ID())))
					reason=observer.charStats().getMyRace().ID().toUpperCase()+"s UNITE! CHARGE!";
				if(yep)	ExternalPlay.quickSay(observer,null,reason,false,false);
			}
		}
	}
}
