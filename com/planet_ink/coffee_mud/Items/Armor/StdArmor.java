package com.planet_ink.coffee_mud.Items.Armor;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.Items.StdContainer;
import java.util.*;

public class StdArmor extends StdContainer implements Armor
{
	public String ID(){	return "StdArmor";}
	int sheath=0;
	public StdArmor()
	{
		super();

		setName("a shirt of armor");
		setDisplayText("a thick armored shirt sits here.");
		setDescription("Thick padded leather with strips of metal interwoven.");
		properWornBitmap=Item.ON_TORSO;
		wornLogicalAnd=false;
		baseEnvStats().setArmor(10);
		baseEnvStats().setAbility(0);
		baseGoldValue=150;
		setCapacity(0);
		setLidsNLocks(false,true,false,false);
		setUsesRemaining(100);
		recoverEnvStats();
	}
	public Environmental newInstance()
	{	return new StdArmor(); }

	public void setUsesRemaining(int newUses)
	{
		if(newUses==Integer.MAX_VALUE)
			newUses=100;
		super.setUsesRemaining(newUses);
	}

	private String armorHealth()
	{
		if(usesRemaining()>=100)
			return "";
		else
		if(usesRemaining()>=75)
		{
			switch(material()&EnvResource.MATERIAL_MASK)
			{
			case EnvResource.MATERIAL_CLOTH: return name()+" has a small tear ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_GLASS: return name()+" has a few hairline cracks ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_LEATHER: return name()+" is a bit scuffed ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_METAL:
			case EnvResource.MATERIAL_MITHRIL: return name()+" has some small dents ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_WOODEN: return name()+" has a few smell splinters ("+usesRemaining()+"%)";
			default: return name()+" is slightly damaged ("+usesRemaining()+"%)";
			}
		}
		else
		if(usesRemaining()>=50)
		{
			switch(material()&EnvResource.MATERIAL_MASK)
			{
			case EnvResource.MATERIAL_CLOTH:
			case EnvResource.MATERIAL_PAPER: return name()+" has a a few tears and rips ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_ROCK:
			case EnvResource.MATERIAL_PRECIOUS:
			case EnvResource.MATERIAL_GLASS: return name()+" is cracked ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_FLESH:
			case EnvResource.MATERIAL_LEATHER: return name()+" is torn ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_METAL:
			case EnvResource.MATERIAL_MITHRIL: return name()+" is dented ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_WOODEN: return name()+" is splintered ("+usesRemaining()+"%)";
			default: return name()+" is damaged ("+usesRemaining()+"%)";
			}
		}
		else
		if(usesRemaining()>=25)
		{
			switch(material()&EnvResource.MATERIAL_MASK)
			{
			case EnvResource.MATERIAL_PAPER:
			case EnvResource.MATERIAL_CLOTH: return name()+" has numerous tears and rips ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_ROCK:
			case EnvResource.MATERIAL_PRECIOUS:
			case EnvResource.MATERIAL_GLASS: return name()+" has numerous streaking cracks ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_FLESH:
			case EnvResource.MATERIAL_LEATHER: return name()+" is badly torn up ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_METAL:
			case EnvResource.MATERIAL_MITHRIL: return name()+" is badly dented and cracked ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_WOODEN: return name()+" is badly cracked and splintered ("+usesRemaining()+"%)";
			default: return name()+" is badly damaged ("+usesRemaining()+"%)";
			}
		}
		else
		{
			switch(material()&EnvResource.MATERIAL_MASK)
			{
			case EnvResource.MATERIAL_PAPER:
			case EnvResource.MATERIAL_CLOTH: return name()+" is a shredded mess ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_ROCK:
			case EnvResource.MATERIAL_PRECIOUS:
			case EnvResource.MATERIAL_GLASS: return name()+" is practically shardes ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_FLESH:
			case EnvResource.MATERIAL_LEATHER: return name()+" is badly shredded and ripped ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_METAL:
			case EnvResource.MATERIAL_MITHRIL: return name()+" is a crumpled mess ("+usesRemaining()+"%)";
			case EnvResource.MATERIAL_WOODEN: return name()+" is nothing but splinters ("+usesRemaining()+"%)";
			default: return name()+" is horribly damaged ("+usesRemaining()+"%)";
			}
		}
	}

	public boolean okAffect(Environmental myHost, Affect affect)
	{
		if(!super.okAffect(myHost,affect))
			return false;
		if((affect.amITarget(this))
		&&(envStats().height()>0)
		&&(affect.source().envStats().height()>0)
		&&(affect.targetMinor()==Affect.TYP_WEAR))
		{
			int devianceAllowed=200;
			if(((rawProperLocationBitmap()&Item.ON_TORSO)>0)
			||((rawProperLocationBitmap()&Item.ON_LEGS)>0)
			||((rawProperLocationBitmap()&Item.ON_WAIST)>0)
			||((rawProperLocationBitmap()&Item.ON_ARMS)>0)
			||((rawProperLocationBitmap()&Item.ON_HANDS)>0)
			||((rawProperLocationBitmap()&Item.ON_FEET)>0))
				devianceAllowed=20;
			if(affect.source().envStats().height()<(envStats().height()-devianceAllowed))
			{
				affect.source().tell(name()+" doesn't fit you -- it's too big.");
				return false;
			}
			if(affect.source().envStats().height()>(envStats().height()+devianceAllowed))
			{
				affect.source().tell(name()+" doesn't fit you -- it's too small.");
				return false;
			}
		}
		return true;
	}
	public void affect(Environmental myHost, Affect affect)
	{
		super.affect(myHost,affect);
		// lets do some damage!
		if((affect.amITarget(this))
		&&(affect.targetMinor()==Affect.TYP_EXAMINESOMETHING)
		&&(subjectToWearAndTear())
		&&(usesRemaining()<100)
		&&(Sense.canBeSeenBy(this,affect.source())))
			affect.source().tell(armorHealth());
		else
		if((!amWearingAt(Item.INVENTORY))
		&&(owner()!=null)
		&&(owner() instanceof MOB)
		&&(affect.amITarget(owner()))
		&&((!Sense.isABonusItems(this))||(Dice.rollPercentage()>envStats().level()*2))
		&&(subjectToWearAndTear())
		&&(Dice.rollPercentage()>(((MOB)owner()).charStats().getStat(CharStats.DEXTERITY))))
		{
			int weaponType=-1;
			if((Util.bset(affect.targetCode(),Affect.MASK_HURT))
			&&((affect.targetCode()-Affect.MASK_HURT)>0))
			{
				if((affect.tool()!=null)
				&&(affect.tool() instanceof Weapon))
				   weaponType=((Weapon)affect.tool()).weaponType();
				else
				switch(affect.sourceMinor())
				{
				case Affect.TYP_FIRE:
					weaponType=Weapon.TYPE_BURNING;
					break;
				case Affect.TYP_WATER:
					weaponType=Weapon.TYPE_FROSTING;
					break;
				case Affect.TYP_ACID:
					weaponType=Weapon.TYPE_MELTING;
					break;
				case Affect.TYP_COLD:
					weaponType=Weapon.TYPE_FROSTING;
					break;
				case Affect.TYP_GAS:
					weaponType=Weapon.TYPE_GASSING;
					break;
				case Affect.TYP_ELECTRIC:
					weaponType=Weapon.TYPE_STRIKING;
					break;
				}
			}
			if(weaponType>=0)
			{
				switch(material()&EnvResource.MATERIAL_MASK)
				{
				case EnvResource.MATERIAL_CLOTH:
				case EnvResource.MATERIAL_PAPER:
					switch(weaponType)
					{
					case Weapon.TYPE_BURSTING:
					case Weapon.TYPE_FROSTING:
					case Weapon.TYPE_GASSING:
						break;
					case Weapon.TYPE_STRIKING:
						if(Dice.rollPercentage()<25)
							setUsesRemaining(usesRemaining()-Dice.roll(1,5,0));
						break;
					case Weapon.TYPE_MELTING:
					case Weapon.TYPE_BURNING:
						if(Dice.rollPercentage()<25)
							setUsesRemaining(usesRemaining()-Dice.roll(1,15,0));
						break;
					case Weapon.TYPE_NATURAL:
						if(Dice.rollPercentage()==1)
							setUsesRemaining(usesRemaining()-1);
						break;
					case Weapon.TYPE_PIERCING:
					case Weapon.TYPE_SHOOT:
					case Weapon.TYPE_SLASHING:
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-2);
						break;
					}
					break;
				case EnvResource.MATERIAL_GLASS:
					switch(weaponType)
					{
					case Weapon.TYPE_BURSTING:
					case Weapon.TYPE_GASSING:
					case Weapon.TYPE_MELTING:
					case Weapon.TYPE_BURNING:
						break;
					case Weapon.TYPE_STRIKING:
					case Weapon.TYPE_FROSTING:
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-Dice.roll(1,20,0));
						break;
					case Weapon.TYPE_BASHING:
					case Weapon.TYPE_NATURAL:
						if(Dice.rollPercentage()<10)
							setUsesRemaining(usesRemaining()-Dice.roll(1,10,0));
						break;
					case Weapon.TYPE_PIERCING:
					case Weapon.TYPE_SHOOT:
					case Weapon.TYPE_SLASHING:
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-Dice.roll(1,15,0));
						break;
					}
					break;
				case EnvResource.MATERIAL_LEATHER:
					switch(weaponType)
					{
					case Weapon.TYPE_BURSTING:
					case Weapon.TYPE_FROSTING:
					case Weapon.TYPE_GASSING:
						break;
					case Weapon.TYPE_STRIKING:
						if(Dice.rollPercentage()<25)
							setUsesRemaining(usesRemaining()-Dice.roll(1,5,0));
						break;
					case Weapon.TYPE_MELTING:
					case Weapon.TYPE_BURNING:
						if(Dice.rollPercentage()<25)
							setUsesRemaining(usesRemaining()-Dice.roll(1,15,0));
						break;
					case Weapon.TYPE_BASHING:
					case Weapon.TYPE_NATURAL:
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-1);
						break;
					case Weapon.TYPE_PIERCING:
					case Weapon.TYPE_SHOOT:
						if(Dice.rollPercentage()<10)
							setUsesRemaining(usesRemaining()-Dice.roll(1,4,0));
						break;
					case Weapon.TYPE_SLASHING:
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-1);
						break;
					}
					break;
				case EnvResource.MATERIAL_MITHRIL:
					if(Dice.rollPercentage()==1)
						setUsesRemaining(usesRemaining()-1);
					break;
				case EnvResource.MATERIAL_METAL:
					switch(weaponType)
					{
					case Weapon.TYPE_BURSTING:
					case Weapon.TYPE_FROSTING:
					case Weapon.TYPE_GASSING:
						break;
					case Weapon.TYPE_MELTING:
						if(Dice.rollPercentage()<25)
							setUsesRemaining(usesRemaining()-Dice.roll(1,15,0));
						break;
					case Weapon.TYPE_BURNING:
						if(Dice.rollPercentage()==1)
							setUsesRemaining(usesRemaining()-1);
						break;
					case Weapon.TYPE_BASHING:
						if((rawWornCode()==Armor.ON_HEAD)
						&&(Dice.rollPercentage()==1)
						&&(Dice.rollPercentage()==1)
						&&((affect.targetCode()-Affect.MASK_HURT)>10))
						{
							Ability A=CMClass.getAbility("Disease_Tinnitus");
							if((A!=null)&&(owner().fetchAffect(A.ID())==null))
								A.invoke((MOB)owner(),owner(),true);
						}
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-2);
						break;
					case Weapon.TYPE_STRIKING:
					case Weapon.TYPE_NATURAL:
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-2);
						break;
					case Weapon.TYPE_PIERCING:
					case Weapon.TYPE_SHOOT:
					case Weapon.TYPE_SLASHING:
						if(Dice.rollPercentage()<2)
							setUsesRemaining(usesRemaining()-1);
						break;
					}
					break;
				case EnvResource.MATERIAL_ROCK:
				case EnvResource.MATERIAL_PRECIOUS:
					switch(weaponType)
					{
					case Weapon.TYPE_BURSTING:
					case Weapon.TYPE_FROSTING:
					case Weapon.TYPE_GASSING:
						break;
					case Weapon.TYPE_MELTING:
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-Dice.roll(1,5,0));
						break;
					case Weapon.TYPE_BURNING:
						if(Dice.rollPercentage()==1)
							setUsesRemaining(usesRemaining()-1);
						break;
					case Weapon.TYPE_BASHING:
					case Weapon.TYPE_STRIKING:
					case Weapon.TYPE_NATURAL:
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-2);
						break;
					case Weapon.TYPE_PIERCING:
					case Weapon.TYPE_SHOOT:
					case Weapon.TYPE_SLASHING:
						if(Dice.rollPercentage()<2)
							setUsesRemaining(usesRemaining()-1);
						break;
					}
					break;
				case EnvResource.MATERIAL_WOODEN:
					switch(weaponType)
					{
					case Weapon.TYPE_BURSTING:
					case Weapon.TYPE_FROSTING:
					case Weapon.TYPE_GASSING:
						break;
					case Weapon.TYPE_STRIKING:
						if(Dice.rollPercentage()<20)
							setUsesRemaining(usesRemaining()-1);
						break;
					case Weapon.TYPE_MELTING:
					case Weapon.TYPE_BURNING:
						if(Dice.rollPercentage()<20)
							setUsesRemaining(usesRemaining()-Dice.roll(1,5,0));
						break;
					case Weapon.TYPE_BASHING:
					case Weapon.TYPE_NATURAL:
						if(Dice.rollPercentage()<5)
							setUsesRemaining(usesRemaining()-2);
						break;
					case Weapon.TYPE_PIERCING:
					case Weapon.TYPE_SHOOT:
					case Weapon.TYPE_SLASHING:
						if(Dice.rollPercentage()<2)
							setUsesRemaining(usesRemaining()-1);
						break;
					}
					break;
				default:
					if(Dice.rollPercentage()==1)
						setUsesRemaining(usesRemaining()-1);
					break;
				}
			}

			if((usesRemaining()<=0)
			&&(owner()!=null)
			&&(owner() instanceof MOB))
			{
				MOB owner=(MOB)owner();
				setUsesRemaining(100);
				affect.addTrailerMsg(new FullMsg(((MOB)owner()),null,null,Affect.MSG_OK_VISUAL,name()+" is destroyed!!",Affect.NO_EFFECT,null,Affect.MSG_OK_VISUAL,name()+" being worn by <S-NAME> is destroyed!"));
				unWear();
				destroy();
				owner.recoverEnvStats();
				owner.recoverCharStats();
				owner.recoverMaxState();
				owner.location().recoverRoomStats();
			}
		}
	}

	public void recoverEnvStats()
	{
		super.recoverEnvStats();
		if((baseEnvStats().height()==0)
		   &&(!amWearingAt(Item.INVENTORY))
		   &&(owner() instanceof MOB))
			baseEnvStats().setHeight(((MOB)owner()).baseEnvStats().height());
	}

	public void affectEnvStats(Environmental affected, EnvStats affectableStats)
	{
		super.affectEnvStats(affected,affectableStats);
		if((!this.amWearingAt(Item.INVENTORY))&&((!this.amWearingAt(Item.HELD))||(this instanceof Shield)))
		{
			affectableStats.setArmor(affectableStats.armor()-envStats().armor());
			if(this.amWearingAt(Item.ON_TORSO))
				affectableStats.setArmor(affectableStats.armor()-(envStats().ability()*10));
			else
			if((this.amWearingAt(Item.ON_HEAD))||(this.amWearingAt(Item.HELD)))
				affectableStats.setArmor(affectableStats.armor()-(envStats().ability()*5));
			else
				affectableStats.setArmor(affectableStats.armor()-envStats().ability());
		}
	}
	public void affectCharStats(MOB affected, CharStats affectableStats)
	{
		super.affectCharStats(affected,affectableStats);
		if(!amWearingAt(Item.INVENTORY))
		switch(material()&EnvResource.MATERIAL_MASK)
		{
		case EnvResource.MATERIAL_METAL:
			affectableStats.setStat(CharStats.SAVE_ELECTRIC,affectableStats.getStat(CharStats.SAVE_ELECTRIC)-2);
			break;
		case EnvResource.MATERIAL_LEATHER:
			affectableStats.setStat(CharStats.SAVE_ACID,affectableStats.getStat(CharStats.SAVE_ACID)+2);
			break;
		case EnvResource.MATERIAL_MITHRIL:
			affectableStats.setStat(CharStats.SAVE_MAGIC,affectableStats.getStat(CharStats.SAVE_MAGIC)+2);
			break;
		case EnvResource.MATERIAL_CLOTH:
		case EnvResource.MATERIAL_PAPER:
			affectableStats.setStat(CharStats.SAVE_FIRE,affectableStats.getStat(CharStats.SAVE_FIRE)-2);
			break;
		case EnvResource.MATERIAL_GLASS:
		case EnvResource.MATERIAL_ROCK:
		case EnvResource.MATERIAL_PRECIOUS:
		case EnvResource.MATERIAL_VEGETATION:
		case EnvResource.MATERIAL_FLESH:
			affectableStats.setStat(CharStats.SAVE_FIRE,affectableStats.getStat(CharStats.SAVE_ACID)+2);
			break;
		}
	}
	public int value()
	{
		if(usesRemaining()<1000)
			return (int)Math.round(Util.mul(super.value(),Util.div(usesRemaining(),100)));
		else
			return super.value();
	}
	public boolean subjectToWearAndTear()
	{
		if((usesRemaining()<=1000)&&(usesRemaining()>=0))
			return true;
		return false;
	}

	public String secretIdentity()
	{
		String id=super.secretIdentity();
		if(envStats().ability()>0)
			id=name()+" +"+envStats().ability()+((id.length()>0)?"\n\r":"")+id;
		else
		if(envStats().ability()<0)
			id=name()+" "+envStats().ability()+((id.length()>0)?"\n\r":"")+id;
		return id+"\n\rProtection: "+envStats().armor();
	}
}
