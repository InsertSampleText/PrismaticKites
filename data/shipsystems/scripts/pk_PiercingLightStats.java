package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;

public class pk_PiercingLightStats extends BaseShipSystemScript {

	public static float SPEED_BONUS = 300;
	public static float DAMAGE_REDUCTION = .9f;
	public static float SHIELD_DAMAGE_REDUCTION = .66f;
	public static float EXPLOSION_DAMAGE_REDUCTION = .9f;
	public static float SHIELD_SIZE_BONUS = 120;
	public static float MASS_BONUS = 500f;

	public static final float MAX_TIME_MULT = 2f;
	// public static final float MIN_TIME_MULT = 0.1f;
	// public static final float DAM_MULT = 0.1f;

	private Float mass = null;

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		// CombatEntityAPI ship = (ShipAPI) stats.getEntity();
		// thanks PAGSM

		ShipAPI ship = null;
		boolean player = false;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
			player = ship == Global.getCombatEngine().getPlayerShip();
		}
		if (ship == null) {
			return;
		}
		if (mass == null) {
			mass = ship.getMass();
		}

		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			ship.setMass(mass);

		} else {
			stats.getMaxSpeed().modifyPercent(id, SPEED_BONUS * effectLevel);
			stats.getAcceleration().modifyPercent(id, SPEED_BONUS * 2 * effectLevel);

			stats.getTurnAcceleration().modifyMult(id, .25f);
			stats.getMaxTurnRate().modifyMult(id, .25f);

			stats.getHullDamageTakenMult().modifyMult(id, 1f - DAMAGE_REDUCTION * effectLevel);
			stats.getArmorDamageTakenMult().modifyMult(id, 1f - DAMAGE_REDUCTION * effectLevel);
			stats.getEmpDamageTakenMult().modifyMult(id, 1f - DAMAGE_REDUCTION * effectLevel);
			stats.getHighExplosiveDamageTakenMult().modifyMult(id, 1f - EXPLOSION_DAMAGE_REDUCTION * effectLevel);

			// stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_DAMAGE_REDUCTION * effectLevel);
			// stats.getShieldUnfoldRateMult().modifyPercent(id, 2000);

			ship.setMass(mass + MASS_BONUS);

			float shipTimeMult = 1f + (MAX_TIME_MULT - 1f) * effectLevel;
			stats.getTimeMult().modifyMult(id, shipTimeMult);
			if (player) {
				Global.getCombatEngine().getTimeMult().modifyMult(id, 1f / shipTimeMult);
			} else {
				Global.getCombatEngine().getTimeMult().unmodify(id);
			}

			for (WeaponAPI weapon : ship.getAllWeapons()){
				if (weapon.getSlot().isHidden()){
					weapon.setForceFireOneFrame(true);
				}
			}

		}

		if (state == ShipSystemStatsScript.State.ACTIVE){
			
		}

	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getDeceleration().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);

		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getEmpDamageTakenMult().unmodify(id);

		// stats.getShieldDamageTakenMult().unmodify(id);
		// stats.getShieldUnfoldRateMult().unmodify(id);

		Global.getCombatEngine().getTimeMult().unmodify(id);
		stats.getTimeMult().unmodify(id);
	}

	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("increased engine power", false);
		}
		if (index == 1) {
			return new StatusData("shield decompiled into #$@%#&*!@", false);
		}
		return null;
	}

}
