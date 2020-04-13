package com.planet_ink.coffee_mud.WebMacros.grinder;

import com.planet_ink.coffee_web.interfaces.*;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.core.collections.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.ColorLibrary.Color;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.Clan.MemberRecord;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

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
public class GrinderPlanes
{
	public String name()
	{
		return "GrinderPlanes";
	}

	public String standardField(final PlanarAbility.PlanarVar var, final String httpVal)
	{
		if((httpVal != null)&&(httpVal.trim().length()>0))
		{
			if(CMath.isNumber(httpVal.trim()))
				return var.toString().toLowerCase()+"="+httpVal+" ";
			return var.toString().toLowerCase()+"=\""+CMStrings.replaceAll(httpVal,"\"","\\\"")+"\" ";
		}
		return "";
	}
	
	public String runMacro(final HTTPRequest httpReq, final String parm)
	{
		final String last=httpReq.getUrlParameter("PLANE");
		if(last==null)
			return " @break@";
		if(last.length()>0)
		{
			if(!httpReq.getRequestObjects().containsKey("SYSTEM_PLANE_CACHE_"+last.toUpperCase()))
			{
				final PlanarAbility planeSet = (PlanarAbility)CMClass.getAbilityPrototype("StdPlanarAbility");
				planeSet.setMiscText(last);
				httpReq.getRequestObjects().put("SYSTEM_PLANE_CACHE_"+last.toUpperCase(), planeSet);
			}
			final PlanarAbility planeObj = (PlanarAbility)httpReq.getRequestObjects().get("SYSTEM_PLANE_CACHE_"+last.toUpperCase());
			final Map<String,String> planarData = planeObj.getPlaneVars();
			if(planarData != null)
			{
				final StringBuilder finalStr = new StringBuilder("");
				for(final PlanarAbility.PlanarVar var: PlanarAbility.PlanarVar.values())
				{
					final String key=var.name().toUpperCase().trim();
					String httpVal = httpReq.getUrlParameter(key);
					if(httpVal == null)
						httpVal = planarData.get(key);
					if(httpVal == null)
						httpVal="";
					switch(var)
					{
					case ABSORB:
						finalStr.append(standardField(var,httpVal));
						break;
					case ADJSIZE:
					{
						final String weight=httpReq.getUrlParameter(key+"_WEIGHT");
						final String height=httpReq.getUrlParameter(key+"_HEIGHT");
						if(((weight != null)&&(weight.trim().length()>0))
						||((height != null)&&(height.trim().length()>0)))
						{
							finalStr.append(var.toString().toLowerCase()).append("=\"");
							if((weight != null)&&(weight.trim().length()>0))
							{
								if(CMath.isInteger(weight))
									finalStr.append("WEIGHT="+weight).append(" ");
								else
									return var.toString()+":WEIGHT is not valid.";
							}
							if((height != null)&&(height.trim().length()>0))
							{
								if(CMath.isInteger(height))
									finalStr.append("HEIGHT="+height).append(" ");
								else
									return var.toString()+":HEIGHT is not valid.";
							}
							finalStr.append("\" ");
						}
						break;
					}
					case ADJSTAT:
						finalStr.append(standardField(var,httpVal));
						break;
					case ADJUST:
						finalStr.append(standardField(var,httpVal));
						break;
					case ALIGNMENT:
						if(CMath.isInteger(httpVal))
							finalStr.append(standardField(var,httpVal));
						else
						if(httpVal.length()>0)
							return var.toString()+" is not valid.";
						break;
					case AREABLURBS:
					{
						Map<String,String> parsed = CMParms.parseEQParms(httpVal);
						if(httpReq.isUrlParameter(key+"_1"))
						{
							parsed.clear();
							int i=1;
							while(httpReq.isUrlParameter(key+"_"+i))
							{
								String chg=httpReq.getUrlParameter(key+"_"+i);
								String to=httpReq.getUrlParameter(key+"_V"+i);
								if(chg.length()>0)
									parsed.put(chg, to);
								i++;
							}
						}
						if(parsed.size()>0)
						{
							finalStr.append(var.toString().toLowerCase()).append("=\"");
							for(final String p : parsed.keySet())
							{
								finalStr.append(p).append("=\\\"");
								final String val = parsed.get(p);
								finalStr.append(CMStrings.replaceAll(val, "\"", "\\\\\""));
								finalStr.append("\\\" ");
							}
							finalStr.append("\" ");
						}
						break;
					}
					case ATMOSPHERE:
					{
						
						finalStr.append(standardField(var,httpVal));
						break;
					}
					case BEHAVAFFID:
					{
						if(httpReq.isUrlParameter(key+"_1"))
						{
							List<Pair<String,String>> parsed = new ArrayList<Pair<String,String>>();
							int i=1;
							while(httpReq.isUrlParameter(key+"_"+i))
							{
								String chg=httpReq.getUrlParameter(key+"_"+i);
								String cp=httpReq.getUrlParameter(key+"_S"+i);
								String to=httpReq.getUrlParameter(key+"_V"+i);
								if(chg.length()>0)
									parsed.add(new Pair<String,String>(chg, (cp.equalsIgnoreCase("on")?"*":"")+to));
								i++;
							}
							if(parsed.size()>0)
							{
								finalStr.append(var.toString().toLowerCase()).append("=\"");
								for(final Pair<String,String> p : parsed)
								{
									finalStr.append(p.first);
									if(p.second.trim().length()>0)
										finalStr.append("(").append(p.second).append(")");
									finalStr.append(" ");
								}
								finalStr.append("\" ");
							}
						}
						break;
					}
					case BEHAVE:
					{
						if(httpReq.isUrlParameter(key+"_1"))
						{
							List<Pair<String,String>> parsed = new ArrayList<Pair<String,String>>();
							int i=1;
							while(httpReq.isUrlParameter(key+"_"+i))
							{
								String chg=httpReq.getUrlParameter(key+"_"+i);
								String to=httpReq.getUrlParameter(key+"_V"+i);
								if(chg.length()>0)
									parsed.add(new Pair<String,String>(chg,to));
								i++;
							}
							if(parsed.size()>0)
							{
								finalStr.append(var.toString().toLowerCase()).append("=\"");
								for(final Pair<String,String> p : parsed)
								{
									finalStr.append(p.first);
									if(p.second.trim().length()>0)
										finalStr.append("(").append(p.second).append(")");
									finalStr.append(" ");
								}
								finalStr.append("\" ");
							}
						}
						break;
					}
					case BONUSDAMAGESTAT:
						finalStr.append(standardField(var,httpVal));
						break;
					case CATEGORY:
						finalStr.append(standardField(var,httpVal));
						break;
					case DESCRIPTION:
					{
						httpVal=CMStrings.replaceAll(httpVal,"\n", "%0D");
						httpVal=CMStrings.replaceAll(httpVal,"\r", "");
						finalStr.append(standardField(var,httpVal));
						break;
					}
					case ELITE:
						if(CMath.isInteger(httpVal))
							finalStr.append(standardField(var,httpVal));
						else
						if(httpVal.length()>0)
							return var.toString()+" is not valid.";
						break;
					case ENABLE:
					{
						if(httpReq.isUrlParameter(key+"_1"))
						{
							List<Pair<String,String>> parsed = new ArrayList<Pair<String,String>>();
							int i=1;
							while(httpReq.isUrlParameter(key+"_"+i))
							{
								String chg=httpReq.getUrlParameter(key+"_"+i);
								String to=httpReq.getUrlParameter(key+"_V"+i);
								if(chg.length()>0)
									parsed.add(new Pair<String,String>(chg,to));
								i++;
							}
							if(parsed.size()>0)
							{
								finalStr.append(var.toString().toLowerCase()).append("=\"");
								for(final Pair<String,String> p : parsed)
								{
									if(p.first.equalsIgnoreCase("number"))
									{
										int x=p.second.indexOf('/');
										if((x<0)
										||(!CMath.isInteger(p.second.substring(0,x)))
										||(!CMath.isInteger(p.second.substring(x+1))))
											return var.toString()+":+number is not valid.";
									}
									finalStr.append(p.first);
									if(p.second.trim().length()>0)
										finalStr.append("(").append(p.second).append(")");
									finalStr.append(" ");
								}
								finalStr.append("\" ");
							}
						}
						break;
					}
					case FACTIONS:
					{
						if(httpReq.isUrlParameter(key+"_1"))
						{
							List<Pair<String,String>> parsed = new ArrayList<Pair<String,String>>();
							int i=1;
							while(httpReq.isUrlParameter(key+"_"+i))
							{
								String chg=httpReq.getUrlParameter(key+"_"+i);
								String to=httpReq.getUrlParameter(key+"_V"+i);
								if(chg.length()>0)
									parsed.add(new Pair<String,String>(chg,to));
								i++;
							}
							if(parsed.size()>0)
							{
								finalStr.append(var.toString().toLowerCase()).append("=\"");
								for(final Pair<String,String> p : parsed)
								{
									if(CMath.isInteger(p.second))
									{
										finalStr.append(p.first.toLowerCase()+"("+p.second+") ");
									}
									else
										return var.toString()+":"+p.first+" has invalid number.";
										
								}
								finalStr.append("\" ");
							}
						}
						break;
					}
					case FATIGUERATE:
						if(CMath.isInteger(httpVal.trim()))
							finalStr.append(standardField(var,httpVal));
						else
						if(httpVal.length()>0)
							return var.toString()+" is not valid.";
						break;
					case HOURS:
						if(CMath.isInteger(httpVal))
							finalStr.append(standardField(var,httpVal));
						else
						if(httpVal.length()>0)
							return var.toString()+" is not valid.";
						break;
					case ID:
						break;
					case LEVELADJ:
						if(CMath.isInteger(httpVal)||CMath.isMathExpression(httpVal))
							finalStr.append(standardField(var,httpVal));
						else
						if(httpVal.length()>0)
							return var.toString()+" is not valid.";
						break;
					case LIKE:
					{
						finalStr.append(standardField(var,httpVal));
						break;
					}
					case MIXRACE:
					{
						finalStr.append(standardField(var,httpVal));
						break;
					}
					case MOBCOPY:
						if(CMath.isInteger(httpVal))
							finalStr.append(standardField(var,httpVal));
						else
						if(httpVal.length()>0)
							return var.toString()+" is not valid.";
						break;
					case MOBRESIST:
						finalStr.append(standardField(var,httpVal));
						break;
					case PREFIX:
						finalStr.append(standardField(var,httpVal));
						break;
					case PROMOTIONS:
					{
						if(httpReq.isUrlParameter(key+"_1"))
						{
							List<Pair<String,String>> parsed = new ArrayList<Pair<String,String>>();
							int i=1;
							while(httpReq.isUrlParameter(key+"_"+i))
							{
								String chg=httpReq.getUrlParameter(key+"_"+i);
								String to=httpReq.getUrlParameter(key+"_V"+i);
								if(chg.length()>0)
								{
									if(!CMath.isInteger(to))
										return var.toString()+":"+chg+" pct is not valid.";
									parsed.add(new Pair<String,String>(chg,to));
								}
								i++;
							}
							if(parsed.size()>0)
							{
								finalStr.append(var.toString().toLowerCase()).append("=\"");
								for(final Pair<String,String> p : parsed)
								{
									finalStr.append(p.first);
									if(p.second.trim().length()>0)
										finalStr.append("(").append(p.second).append(")");
									if(p != parsed.get(parsed.size()-1))
										finalStr.append(",");
								}
								finalStr.append("\" ");
							}
						}
						break;
					}
					case RECOVERRATE:
						if(CMath.isInteger(httpVal))
							finalStr.append(standardField(var,httpVal));
						else
						if(httpVal.length()>0)
							return var.toString()+" is not valid.";
						break;
					case AEFFECT:
					case REFFECT:
					{
						if(httpReq.isUrlParameter(key+"_1"))
						{
							List<Pair<String,String>> parsed = new ArrayList<Pair<String,String>>();
							int i=1;
							while(httpReq.isUrlParameter(key+"_"+i))
							{
								String chg=httpReq.getUrlParameter(key+"_"+i);
								String to=httpReq.getUrlParameter(key+"_V"+i);
								if(chg.length()>0)
									parsed.add(new Pair<String,String>(chg,to));
								i++;
							}
							if(parsed.size()>0)
							{
								finalStr.append(var.toString().toLowerCase()).append("=\"");
								for(final Pair<String,String> p : parsed)
								{
									finalStr.append(p.first);
									if(p.second.trim().length()>0)
										finalStr.append("(").append(p.second).append(")");
									finalStr.append(" ");
								}
								finalStr.append("\" ");
							}
						}
						break;
					}
					case REQWEAPONS:
					{
						finalStr.append(standardField(var,httpVal));
						break;
					}
					case ROOMADJS:
					{
						if(httpReq.isUrlParameter(key+"_UP")
						&&httpReq.isUrlParameter(key+"_CHANCE")
						&&((httpVal != null)&&(httpVal.trim().length()>0)))
						{
							final String chance = httpReq.getUrlParameter(key+"_CHANCE");
							if(chance.length()>0)
							{
								if(!CMath.isInteger(chance.trim()))
									httpVal=chance+" "+httpVal;
								else
									return var.toString()+":chance is not valid.";
							}
							if("on".equalsIgnoreCase(httpReq.getUrlParameter(key+"_UP")))
								httpVal = "UP "+httpVal;
							finalStr.append(var.toString().toLowerCase()+"=\"").append(httpVal).append("\" ");
						}
						break;
					}
					case ROOMCOLOR:
					{
						if(httpVal.trim().length()>0)
						{
							httpVal += ("on".equalsIgnoreCase(httpReq.getUrlParameter(key+"_UP"))?"UP ":"");
							finalStr.append(standardField(var,httpVal));
						}
						break;
					}
					case SETSTAT:
						finalStr.append(standardField(var,httpVal));
						break;
					case SPECFLAGS:
					{
						List<String> selected = new ArrayList<String>(2);
						if(httpReq.isUrlParameter("VOTEFUNCS"))
						{
							int x=1;
							while(httpReq.getUrlParameter("VOTEFUNCS"+x)!=null)
							{
								selected.add(httpReq.getUrlParameter("VOTEFUNCS"+x));
								x++;
							}
						}
						if(selected.size()>0)
						{
							finalStr.append(var.toString().toLowerCase()).append("=\"");
							for(final String s : selected)
								finalStr.append(s).append(" ");
							finalStr.append("\" ");
						}
						break;
					}
					case TRANSITIONAL:
					{
						finalStr.append(standardField(var,httpVal));
						break;
					}
					case WEAPONMAXRANGE:
						if(CMath.isInteger(httpVal))
							finalStr.append(standardField(var,httpVal));
						else
						if(httpVal.length()>0)
							return var.toString()+" is not valid.";
						break;
					default:
						break;
					
					}
				}
			}
		}
		return "";
	}
}