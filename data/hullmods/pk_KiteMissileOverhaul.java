package data.hullmods;

import java.util.Collection;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class pk_KiteMissileOverhaul extends BaseHullMod {

	public static float AMMO_BONUS = 75f;
	public static float ROF_MULT = 75f;
	public static float BALLISTIC_ROF_DEBUFF = 50f;

	public static float CR_PENALTY = 0.60f;
	public static float PENALTY = 40f;
	private static String KITE = "kite";
	private static String KGS = "pk_kitegroundsupport";
	private static String KIO = "pk_kiteinterceptoroverhaul";
	private static String KBO = "pk_kiteballisticoverhaul";
	private static String KMO = "pk_kitemissileoverhaul";
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getMissileAmmoBonus().modifyPercent(id, AMMO_BONUS);
		stats.getMissileRoFMult().modifyMult(id, 1f + (ROF_MULT * 0.01f));
		stats.getPeakCRDuration().modifyMult(id, CR_PENALTY);
		stats.getBallisticRoFMult().modifyMult(id, 1f - (BALLISTIC_ROF_DEBUFF * 0.01f));
	}

	@Override
    public boolean isApplicableToShip(ShipAPI ship) {
		//Collection<String> hm = ship.getVariant().getHullMods();
		if (!ship.getVariant().getHullSpec().getHullId().contains(KITE)) return false;

		Collection<String> mods = ship.getVariant().getHullMods();
		return ( 
				!mods.contains(KGS) &&
				!mods.contains(KIO) &&
				!mods.contains(KBO)

				);

		//return true;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		Collection<String> mods = ship.getVariant().getHullMods();
		String id = ship.getVariant().getHullSpec().getHullId();
		if (!id.contains(KITE)) return "Not a Kite";	
		if (
			mods.contains(KGS) ||
			mods.contains(KIO) ||
			mods.contains(KBO)
		) return "Already has a Kite Mod installed";
		return null;
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) AMMO_BONUS + "%";
		if (index == 1) return "" + (int) ROF_MULT + "%";
		if (index == 2) return "" + (int) (1 - CR_PENALTY * 0.01) + "%";
		return null;
	}
	


}


