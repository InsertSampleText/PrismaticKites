package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

public class pk_PiercingLightStats extends BaseShipSystemScript {

	public static float SPEED_BONUS = 900;
	public static float DAMAGE_REDUCTION = .9f;

	public static float MASS_BONUS = 5000f;

	private Float mass = null;


	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		// CombatEntityAPI ship = (ShipAPI) stats.getEntity();

		
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

		} else {
			stats.getMaxSpeed().modifyPercent(id, SPEED_BONUS * effectLevel);
			stats.getAcceleration().modifyPercent(id, SPEED_BONUS * 2 * effectLevel);

			// stats.getHullDamageTakenMult().modifyMult(id, 1f - (1f - DAMAGE_REDUCTION) *
			// effectLevel);
			// stats.getArmorDamageTakenMult().modifyMult(id, 1f - (1f - DAMAGE_REDUCTION) *
			// effectLevel);
			// stats.getEmpDamageTakenMult().modifyMult(id, 1f - (1f - DAMAGE_REDUCTION) *
			// effectLevel);

			stats.getShieldDamageTakenMult().modifyMult(id, 1f - DAMAGE_REDUCTION * effectLevel);

			 ship.setMass(mass + MASS_BONUS);

		}

	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getAcceleration().unmodify(id);

		// stats.getHullDamageTakenMult().unmodify(id);
		// stats.getArmorDamageTakenMult().unmodify(id);
		// stats.getEmpDamageTakenMult().unmodify(id);

		stats.getShieldArcBonus().unmodify(id);
		stats.getShieldDamageTakenMult().unmodify(id);
		stats.getShieldTurnRateMult().unmodify(id);
		stats.getShieldUnfoldRateMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
	}

	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("increased engine power", false);
		}
		if (index == 1) {
			return new StatusData("shield absorbs 10x damage", false);
		}
		return null;
	}

}
