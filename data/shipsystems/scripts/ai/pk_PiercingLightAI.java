package data.shipsystems.scripts.ai;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;


//thanks convulsivelunge

public class pk_PiercingLightAI implements ShipSystemAIScript {

	private ShipAPI ship;
	private CombatEngineAPI engine;
	private ShipwideAIFlags flags;
	private ShipSystemAPI system;

	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);

	public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
		this.ship = ship;
		this.flags = flags;
		this.engine = engine;
		this.system = system;
	}

	private float bestFractionEver = 0f;
	private float sinceLast = 0f;

	@SuppressWarnings("unchecked")
	public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
		if (ship == null)
			return;
		tracker.advance(amount);

		sinceLast += amount;

		if (tracker.intervalElapsed()) {
			// if (system.getCooldownRemaining() > 0) return;
			// if (system.isActive()) return;
			if (!isSystemUsable())
				return;

			if (target == null)
				return;
			if (ship.getFluxTracker().isOverloadedOrVenting())
				return;

			float hardFluxLevel = ship.getHardFluxLevel();
			float fluxLevel = ship.getFluxLevel();
			float arc = 15f;
			float angle = ship.getFacing();
			float systemRange = 1000;

			if (((hardFluxLevel <= 0.66f) && (fluxLevel <= 0.8f))) {
				if (target != null) {
					float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
					dist -= ship.getCollisionRadius() + target.getCollisionRadius();
					//angle = Misc.getAngleInDegrees(ship.getLocation(), target.getLocation());
					if (Misc.isInArc(angle, arc, ship.getLocation(), target.getLocation())) {

						if ((dist < systemRange && dist > 100) || dist > 1500) {
							ship.useSystem();
						}

					}

				}
			}

		}
	}

	public boolean isSystemUsable() {
		if (system.getCooldownRemaining() > 0)
			return false;
		if (system.isOutOfAmmo())
			return false;
		if (system.isActive())
			return false;
		return true;
	}

}
