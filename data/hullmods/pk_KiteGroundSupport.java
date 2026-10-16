package data.hullmods;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

@SuppressWarnings("unchecked")
public class pk_KiteGroundSupport extends BaseHullMod {

	private static Map supp_bonus = new HashMap();
	static {
		supp_bonus.put(HullSize.FRIGATE, 50f);
		supp_bonus.put(HullSize.DESTROYER, 75f);
	}

	//public static final float GROUND_BONUS = 50f;
	private static String KITE = "kite";
	private static String KGS = "pk_kitegroundsupport";
	private static String KIO = "pk_kiteinterceptoroverhaul";
	private static String KBO = "pk_kiteballisticoverhaul";
	private static String KMO = "pk_kitemissileoverhaul";
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getDynamic().getMod(Stats.FLEET_GROUND_SUPPORT).modifyFlat(id, (Float) supp_bonus.get(hullSize));
	}

	@Override
    public boolean isApplicableToShip(ShipAPI ship) {
		
		if (!ship.getVariant().getHullSpec().getHullId().contains(KITE)) return false;
		if (ship.getVariant().getHullSize() == HullSize.CAPITAL_SHIP) return false;
		if (ship.getVariant().getHullSize() == HullSize.CRUISER) return false;

		Collection<String> mods = ship.getVariant().getHullMods();
		return ( 
				
				!mods.contains(KIO) &&
				!mods.contains(KBO) &&
				!mods.contains(KMO)
				);

		//return true;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		Collection<String> mods = ship.getVariant().getHullMods();
		String id = ship.getVariant().getHullSpec().getHullId();
		if (!id.contains(KITE)) return "Not a Kite";	
		if (
			mods.contains(KBO) ||
			mods.contains(KIO) ||
			mods.contains(KMO)
		) return "Already has a Kite Mod installed";

		if (ship.getVariant().getHullSize() == HullSize.CAPITAL_SHIP) return "Cannot be installed on Capitals";
		if (ship.getVariant().getHullSize() == HullSize.CRUISER) return "Cannot be installed on Cruisers";
		return null;
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) supp_bonus.get(HullSize.FRIGATE)).intValue();
		if (index == 1) return "" + ((Float) supp_bonus.get(HullSize.DESTROYER)).intValue();
		return null;
	}


}
