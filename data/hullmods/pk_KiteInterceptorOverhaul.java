package data.hullmods;

// import java.util.Iterator;
// import java.util.List;
import java.util.Collection;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class pk_KiteInterceptorOverhaul extends BaseHullMod {

	public static float DAMAGE_BONUS = 100f;
	public static float DAMAGE_REDUCTION = 25;
	public static float CR_PENALTY = 0.90f;
	public static float PENALTY = 10f;
	private static String KITE = "kite";
	private static String KGS = "pk_kitegroundsupport";
	private static String KIO = "pk_kiteinterceptoroverhaul";
	private static String KBO = "pk_kiteballisticoverhaul";
	private static String KMO = "pk_kitemissileoverhaul";
	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

		stats.getDamageToMissiles().modifyPercent(id, DAMAGE_BONUS);
		stats.getDamageToFighters().modifyPercent(id, DAMAGE_BONUS);

		// stats.getBallisticWeaponDamageMult().modifyMult(id, 1f - DAMAGE_REDUCTION * 0.01f);
		// stats.getMissileWeaponDamageMult().modifyMult(id, 1f - DAMAGE_REDUCTION * 0.01f);
		stats.getDamageToFrigates().modifyPercent(id, -DAMAGE_REDUCTION);
		stats.getDamageToDestroyers().modifyPercent(id, -DAMAGE_REDUCTION);
		stats.getDamageToCruisers().modifyPercent(id, -DAMAGE_REDUCTION);

		stats.getPeakCRDuration().modifyMult(id, CR_PENALTY);
		
	}


	@Override
    public boolean isApplicableToShip(ShipAPI ship) {
		//Collection<String> hm = ship.getVariant().getHullMods();
		if (!ship.getVariant().getHullSpec().getHullId().contains(KITE)) return false;

		Collection<String> mods = ship.getVariant().getHullMods();
		return ( 
				!mods.contains(KGS) &&
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
			mods.contains(KGS) ||
			mods.contains(KMO)
		) return "Already has a Kite Mod installed";
		return null;
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)Math.round(DAMAGE_BONUS) + "%";
		if (index == 1) return "" + (int)Math.round(DAMAGE_REDUCTION) + "%";
		if (index == 2) return "" + (int) PENALTY + "%";
		return null;
	}


}




