package me.snay.boomerang;

import java.lang.reflect.*;

public class Brain {
    private final Boomerang boomerang;
    private Boomerang tracer;
    public float activationThreshold = 1;
    private float timeUntilActivation = 1;
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
            tracer.move(0.04F);
            for (Bonus bonus : this.bonuses) {
                for (Enemy enemy : this.enemies)
                {
                    if (enemy != null && enemy.getHitbox().overlaps(tracer.hitbox))
                    {
                        return false;
                    }
                }
                if (bonus != null && bonus.getHitbox().overlaps(tracer.hitbox)) {
                    bonusHit = true;
                }
            }
        }

        return bonusHit;
    }
}
