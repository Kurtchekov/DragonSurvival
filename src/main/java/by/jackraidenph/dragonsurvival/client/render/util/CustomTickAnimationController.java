package by.jackraidenph.dragonsurvival.client.render.util;

import by.jackraidenph.dragonsurvival.common.entity.DragonEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

import java.util.HashMap;
import java.util.List;

public class CustomTickAnimationController extends AnimationController
{
	public CustomTickAnimationController(IAnimatable animatable, String name, int transitionLength, IAnimationPredicate<DragonEntity> predicate)
	{
		super(animatable, name, transitionLength, predicate);
		markNeedsReload();
	}
	
	public double speed = 1;
	public double lastTick = 0;
	
	@Override
	public void process(double tick, AnimationEvent event, List modelRendererList, HashMap boneSnapshotCollection, MolangParser parser, boolean crashWhenCantFindBone)
	{
		double tickDif = tick - lastTick;
		lastTick = tick;
		super.process(tick + (tickDif * (speed - 1.0)), event, modelRendererList, boneSnapshotCollection, parser, crashWhenCantFindBone);
	}
}

