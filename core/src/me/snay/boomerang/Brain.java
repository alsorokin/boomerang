package me.snay.boomerang;

import com.badlogic.gdx.math.Circle;

import java.lang.reflect.*;

public class Brain {
    /**
     * How much time (in seconds) should pass between consecutive brain activations.
     */
    public float activationThreshold = 1F;

    /**
     * Inflate boomerang hitbox by this factor to intentionally make AI less accurate.
     * Also has a side effect of AI being more afraid of enemies.
     */
    public float hitboxInflationFactor = 1.5F;

    private final Boomerang boomerang;
    private Boomerang tracer;
    private float timeUntilActivation = 1F;
    private final Bonus[] bonuses;
    private final Enemy[] enemies;

    public Brain (Boomerang boomerang, Field field, Bonus[] bonuses, Enemy[] enemies) {
        this.boomerang = boomerang;
        this.bonuses = bonuses;
        this.enemies = enemies;

        Boomerang tracer = null;
        // reflection magicks
        try
        {
            Class<? extends Boomerang> boomerangClass = boomerang.getClass();
            Constructor<? extends Boomerang> ctor = boomerangClass.getConstructor(Field.class);
            tracer = (Boomerang)ctor.newInstance(field);
        }
        catch (Exception e)
        {
            // Something went wrong. Oh well.
        }
        this.tracer = tracer;
    }

    public void think(float deltaTime)
    {
        if (boomerang.isTossed) return;

        timeUntilActivation -= deltaTime;
        if (timeUntilActivation <= 0)
        {
            timeUntilActivation = activationThreshold;
            if (shouldToss()) boomerang.toss();
        }
    }

    private boolean shouldToss()
    {
        tracer.setPosition(boomerang.getX(), boomerang.getY());
        tracer.setTimeTravelledX(boomerang.getTimeTravelledX());
        tracer.setTimeTravelledY(boomerang.getTimeTravelledY());
        tracer.toss();

        boolean bonusHit = false;
        while (tracer.isTossed)
        {
            Circle inflatedHitbox = new Circle();
            inflatedHitbox.x = tracer.hitbox.x;
            inflatedHitbox.y = tracer.hitbox.y;
            inflatedHitbox.radius = tracer.hitbox.radius * hitboxInflationFactor;

            tracer.move(0.04F);
            for (Bonus bonus : this.bonuses) {
                for (Enemy enemy : this.enemies)
                {
                    if (enemy != null && enemy.getHitbox().overlaps(inflatedHitbox))
                    {
                        return false;
                    }
                }
                if (bonus != null && bonus.getHitbox().overlaps(inflatedHitbox)) {
                    bonusHit = true;
                }
            }
        }

        return bonusHit;
    }
}
