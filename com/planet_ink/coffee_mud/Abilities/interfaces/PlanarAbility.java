package com.planet_ink.coffee_mud.Abilities.interfaces;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.core.CMath.CompiledFormula;
import com.planet_ink.coffee_mud.core.collections.*;
import com.planet_ink.coffee_mud.Abilities.Misc.Amputation;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

import java.lang.ref.WeakReference;
import java.util.*;

/*
   Copyright 2020-2020 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
/**
 * This interface denotes an ability governs the behavior of another
 * plane of existence, how its data is stored and retreived, how
 * the mobs and items and rooms appear and behave inside.
 */
public interface PlanarAbility extends Ability
{
	
	/**
	 * Get the name of the current plane
	 * @return the planarName
	 */
	public String getPlanarName();

	/**
	 * Get the current level of this plane
	 * @return the planarLevel
	 */
	public int getPlanarLevel();

	/**
	 * Get the return room when leaving/entering this plane
	 * @return the oldRoom
	 */
	public Room getOldRoom();

	/**
	 * Set the return room when leaving/entering this plane
	 * @param oldRoom the oldRoom to set
	 */
	public void setOldRoom(Room oldRoom);

	/**
	 * The chosen-from-a-list prefix to use on mobs in this plane
	 * @return the planarPrefix
	 */
	public String getPlanarPrefix();

	/**
	 * The xtra difficulty level of this plane
	 * @return the hardBumpLevel
	 */
	public int getHardBumpLevel();

	/**
	 * Modify the xtra difficulty level of this plane
	 * @param hardBumpLevel the hardBumpLevel to set
	 */
	public void setHardBumpLevel(int hardBumpLevel);

	/**
	 * Get the key/pair definitions for this plane
	 * @return the planeVars
	 */
	public Map<String, String> getPlaneVars();

	/**
	 * Get the pct change and name of available
	 * mob promotions for this plane
	 * @return the promotions
	 */
	public PairList<Integer, String> getPromotions();

	/**
	 * Get the categories that apply to this plane
	 * @return the categories
	 */
	public List<String> getCategories();

	/**
	 * Get the list of behaviors and parms for this plane
	 * @return the behavList
	 */
	public PairList<String, String> getBehavList();

	/**
	 * Get the list of room effects and args for this plane
	 * @return the reffectList
	 */
	public PairList<String, String> getReffectList();

	/**
	 * Get the list of faction ids and values for this plane
	 * @return the factionList
	 */
	public PairList<String, String> getFactionList();

	/**
	 * Get the CharStat STAT_* ID of the stat that gives bonus
	 * damage on this plane
	 * @return the bonusDmgStat
	 */
	public int getBonusDmgStat();

	/**
	 * Get the seq of required weapon flags for hurting things
	 * in this plane.
	 * @return the reqWeapons
	 */
	public Set<String> getReqWeapons();

	/**
	 * Get the number of extra recover ticks for players on this plane
	 * @return the recoverRate
	 */
	public int getRecoverRate();

	/**
	 * Get the extra fatigue ticks for players on this plane
	 * @return the fatigueRate
	 */
	public int getFatigueRate();

	/**
	 * Get the special attribute flags for this plane
	 * @return the specFlags
	 */
	public Set<PlanarSpecFlag> getSpecFlags();

	/**
	 * Get the mob/item level adjustment formula for this plane.
	 * @x1 = base areas median level, @x2 = specific mob/item level
	 * @x2 = the plane traveling players level
	 * @return the levelFormula
	 */
	public CompiledFormula getLevelFormula();

	/**
	 * Get the bonus ability list for this plane.
	 * Clearly, it's complicated.
	 * @return the enableList
	 */
	public Pair<Pair<Integer, Integer>, List<Pair<String, String>>> getEnableList();

	/**
	 * Get the definition for the given plane
	 * @param planeName the name of the plane to get definitions for
	 * @return the definitions map
	 */
	public Map<String,String> getPlanarVars(String planeName);

	/**
	 * The definitions variables for the attributes of each plane
	 * 
	 * @author Bo Zimmerman
	 *
	 */
	public static enum PlanarVar
	{
		ID,
		TRANSITIONAL,
		ALIGNMENT,
		PREFIX,
		LEVELADJ,
		MOBRESIST,
		SETSTAT,
		BEHAVAFFID,
		ADJSTAT,
		ADJSIZE,
		ADJUST,
		MOBCOPY,
		BEHAVE,
		ENABLE,
		WEAPONMAXRANGE,
		BONUSDAMAGESTAT,
		REQWEAPONS,
		ATMOSPHERE,
		AREABLURBS,
		ABSORB,
		HOURS,
		RECOVERRATE,
		FATIGUERATE,
		REFFECT,
		AEFFECT,
		SPECFLAGS,
		MIXRACE,
		ELITE,
		ROOMCOLOR,
		ROOMADJS,
		FACTIONS,
		CATEGORY,
		PROMOTIONS,
		LIKE,
		DESCRIPTION
	}

	/**
	 * The special attribute flags for planes
	 * 
	 * @author Bo Zimmerman
	 *
	 */
	public static enum PlanarSpecFlag
	{
		NOINFRAVISION,
		BADMUNDANEARMOR,
		ALLBREATHE
	}

}
