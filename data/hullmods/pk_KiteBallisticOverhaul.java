package data.hullmods;

// import java.util.Iterator;
// import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class pk_KiteBallisticOverhaul extends BaseHullMod {

	public static float TURRET_SPEED_BONUS = 75f;
	public static float RECOIL_BONUS = 30f;
	public static float TURRET_HEALTH_BONUS = 100f;
	public static float TURRET_ROF_BONUS = 100f;
	public static float MISSILE_RANGE_DEBUFF = 20f;
	//public static float MISSILE_SPEED_DEBUFF = 10f;
	//public static float MISSILE_AMMO_DEBUFF = 10f;
	public static float MISSILE_ROF_DEBUFF = 50f;


	public static float CR_PENALTY = 0.80f;
	public static float PENALTY = 20f;
	//thanks Emergent Threats
	private static String KITE = "kite";
	private static String KGS = "pk_kitegroundsupport";
	private static String KIO = "pk_kiteinterceptoroverhaul";
	private static String KBO = "pk_kiteballisticoverhaul";
	private static String KMO = "pk_kitemissileoverhaul";

	public static Map mag = new HashMap();
	static {
		mag.put(HullSize.FIGHTER, 0f);
		mag.put(HullSize.FRIGATE, 10f);
		mag.put(HullSize.DESTROYER, 20f);
		mag.put(HullSize.CRUISER, 40f);
		mag.put(HullSize.CAPITAL_SHIP, 60f);
	}

	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		stats.getWeaponTurnRateBonus().modifyPercent(id, TURRET_SPEED_BONUS);
		
		stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilDecayMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));

		stats.getWeaponHealthBonus().modifyPercent(id, TURRET_HEALTH_BONUS);
		stats.getBallisticRoFMult().modifyMult(id, 1f + TURRET_ROF_BONUS * 0.01f);

		stats.getBallisticWeaponRangeBonus().modifyPercent(id, (Float) mag.get(hullSize));

		stats.getPeakCRDuration().modifyMult(id, CR_PENALTY);

		stats.getMissileWeaponRangeBonus().modifyPercent(id, -MISSILE_RANGE_DEBUFF);
		//stats.getMissileMaxSpeedBonus().modifyPercent(id, -MISSILE_SPEED_DEBUFF);
		stats.getMissileRoFMult().modifyMult(id, (float) Math.round(MISSILE_ROF_DEBUFF * 0.01f));
	
		
	}


	@Override
    public boolean isApplicableToShip(ShipAPI ship) {
		//Collection<String> hm = ship.getVariant().getHullMods();
		if (!ship.getVariant().getHullSpec().getHullId().contains(KITE)) return false;

		Collection<String> mods = ship.getVariant().getHullMods();
		return ( 
				!mods.contains(KGS) &&
				!mods.contains(KIO) &&
				!mods.contains(KMO)

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
			mods.contains(KMO)
		) return "Already has a Kite Mod installed";
		return null;
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)Math.round(TURRET_SPEED_BONUS) + "%";
		if (index == 1) return "" + (int)Math.round(RECOIL_BONUS) + "%";
		if (index == 2) return "" + (int)Math.round(TURRET_HEALTH_BONUS) + "%";
		if (index == 3) return "" + (int)Math.round(TURRET_ROF_BONUS) + "%";
		if (index == 4) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue() + "%";
		if (index == 5) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue() + "%";
		if (index == 6) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
		if (index == 7) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
		if (index == 8) return "" + (int) PENALTY + "%";
		if (index == 9) return "" + (int)Math.round(MISSILE_RANGE_DEBUFF) + "%";
		if (index == 10) return "" + (int)Math.round(MISSILE_ROF_DEBUFF) + "%";
		return null;
	}


}




