package data.shipsystems.scripts.ai;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.util.IntervalUtil;
<<<<<<< Updated upstream


/**
 * Sample ship system AI. 
 * 
 * THIS CODE IS NOT ACTUALLY USED, AND IS PROVIDED AS AN EXAMPLE ONLY.
 * 
 * To enable it, uncomment the relevant lines in fastmissileracks.system.
 * 
 * @author Alex Mosolov
 *
 * Copyright 2012 Fractal Softworks, LLC
 */
=======
import com.fs.starfarer.api.util.Misc;


//thanks convulsivelunge

>>>>>>> Stashed changes
public class pk_PiercingLightAI implements ShipSystemAIScript {

	private ShipAPI ship;
	private CombatEngineAPI engine;
	private ShipwideAIFlags flags;
	private ShipSystemAPI system;
<<<<<<< Updated upstream
	
	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);
	
=======

	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);

>>>>>>> Stashed changes
	public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
		this.ship = ship;
		this.flags = flags;
		this.engine = engine;
		this.system = system;
	}
<<<<<<< Updated upstream
	
	private float bestFractionEver = 0f;
	private float sinceLast = 0f;
	
	@SuppressWarnings("unchecked")
	public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
		tracker.advance(amount);
		
		sinceLast += amount;
		
		if (tracker.intervalElapsed()) {
			if (system.getCooldownRemaining() > 0) return;
			if (system.isOutOfAmmo()) return;
			if (system.isActive()) return;
			
			if (target == null) return;
			
			float maxCooldown = 0f;
			float totalCooldownSaved = 0f;
			
			float totalWeight = 0f;
			float totalReloadWeight = 0f;
			List weapons = ship.getAllWeapons();
			for (int i = 0; i < weapons.size(); i++) {
				WeaponAPI w = (WeaponAPI) (weapons.get(i));
				
				float weight = 0;
				WeaponSize size = w.getSize();
				if (size == WeaponSize.SMALL) weight = 1;
				else if (size == WeaponSize.MEDIUM) weight = 2;
				else if (size == WeaponSize.LARGE) weight = 4;
				
				totalWeight += weight;
				
				if (w.getType() != WeaponType.MISSILE) continue;
				if (w.getCooldown() < 2f) continue;
				
				maxCooldown += w.getCooldown();
				totalCooldownSaved += w.getCooldownRemaining();
				
				totalReloadWeight += weight;
			}
			
			
			if (maxCooldown <= 0 || totalCooldownSaved <= 0 || totalWeight <= 0f) return;
			
			float reloadSignificance = totalReloadWeight / totalWeight;
			float fluxLevel = ship.getFluxTracker().getFluxLevel();
			
			boolean targetIsVulnerable = target != null && target.getFluxTracker().isOverloadedOrVenting() && 
			 							(target.getFluxTracker().getOverloadTimeRemaining() > 5f || 
			 							target.getFluxTracker().getTimeToVent() > 5f);
			
			if (targetIsVulnerable) reloadSignificance *= 2f;
			
			float remainingFluxLevel = 1f - fluxLevel;
			
			float fluxFractionPerUse = system.getFluxPerUse() / ship.getFluxTracker().getMaxFlux();
			if (fluxFractionPerUse > remainingFluxLevel) return;
			
			float fluxLevelAfterUse = fluxLevel + fluxFractionPerUse;
			if (fluxLevelAfterUse > reloadSignificance || (fluxLevelAfterUse > 0.9f && fluxFractionPerUse > 0.025f)) return;
			
			if (!targetIsVulnerable && sinceLast < 10f) return;
			
			float fraction = totalCooldownSaved / maxCooldown;
			if (fraction > bestFractionEver) {
				bestFractionEver = fraction;
			}
			
			
			if (fraction >= 0.5) {
				ship.useSystem();
				sinceLast = 0f;
				return;
			}
		}
	}

=======

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
			float systemRange = 700;

			if (((hardFluxLevel <= 0.66f) && (fluxLevel <= 0.8f))) {
				if (target != null) {
					float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
					dist -= ship.getCollisionRadius() + target.getCollisionRadius();
					//angle = Misc.getAngleInDegrees(ship.getLocation(), target.getLocation());
					if (Misc.isInArc(angle, arc, ship.getLocation(), target.getLocation())) {

						if ((dist < systemRange && dist > 100) || dist > 1000) {
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

>>>>>>> Stashed changes
}
