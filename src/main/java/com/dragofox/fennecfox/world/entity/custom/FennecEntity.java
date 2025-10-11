package com.dragofox.fennecfox.world.entity.custom;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class FennecEntity extends Animal implements GeoEntity {

    public static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(FennecEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(FennecEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_TRUSTED_ID_0 = SynchedEntityData.defineId(FennecEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    public static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_TRUSTED_ID_1 = SynchedEntityData.defineId(FennecEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    public static final EntityDataAccessor<Boolean> POUNCING = SynchedEntityData.defineId(FennecEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FennecEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    void clearStates() {
        this.setSleeping(false);
        this.setSitting(false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.20D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.1D));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new FennecSleepGoal(this));
        this.goalSelector.addGoal(8, new PerchAndSearchGoal());

        super.registerGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("Walk/Run/Idle", 24,  state -> {
            if (state.isMoving())
                return state.setAndContinue(FennecEntity.this.isSprinting() ? DefaultAnimations.RUN : DefaultAnimations.WALK);
            if (this.isSleeping())
                return state.setAndContinue(RawAnimation.begin().thenLoop("misc.sleep"));
            if (this.isSitting())
                return state.setAndContinue(RawAnimation.begin().thenLoop("misc.sit"));
            return state.setAndContinue(DefaultAnimations.IDLE);
        }));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING, false);
        builder.define(SITTING, false);
        builder.define(DATA_TRUSTED_ID_0, Optional.empty());
        builder.define(DATA_TRUSTED_ID_1, Optional.empty());
        builder.define(POUNCING, false);
    }


    @Nullable
    @Override
    protected  SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.FOX_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.FOX_DEATH; }

    @Nullable
    @Override
    protected  SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return SoundEvents.FOX_SLEEP;
        } else {
            if (!this.level().isBrightOutside() && this.random.nextFloat() < 0.1F) {
                List<Player> list = this.level()
                        .getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(16.0, 16.0, 16.0), EntitySelector.NO_SPECTATORS);
                if (list.isEmpty()) {
                    return SoundEvents.FOX_SCREECH;
                }
            }

            return SoundEvents.FOX_AMBIENT;
        }
    }

    boolean trusts(LivingEntity entity) {
        return this.getTrustedEntities().anyMatch((EntityReference<LivingEntity> livingEntityEntityReference) -> livingEntityEntityReference.matches(entity));
    }

    @Override
    public boolean isSleeping() {return this.entityData.get(SLEEPING);}

    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }


    public boolean isSitting() {return  this.entityData.get(SITTING);};

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
    }

    public boolean isPouncing() {
        return this.entityData.get(POUNCING);
    }

    public void setIsPouncing(boolean isPouncing) { this.entityData.set(POUNCING, isPouncing); }

    Stream<EntityReference<LivingEntity>> getTrustedEntities() {
        return Stream.concat(((Optional)this.entityData.get(DATA_TRUSTED_ID_0)).stream(), ((Optional)this.entityData.get(DATA_TRUSTED_ID_1)).stream());
    }

    void addTrustedEntity(LivingEntity entity) {
        this.addTrustedEntity(new EntityReference(entity));
    }

    private void addTrustedEntity(EntityReference<LivingEntity> entityReference) {
        if (((Optional)this.entityData.get(DATA_TRUSTED_ID_0)).isPresent()) {
            this.entityData.set(DATA_TRUSTED_ID_1, Optional.of(entityReference));
        } else {
            this.entityData.set(DATA_TRUSTED_ID_0, Optional.of(entityReference));
        }
    }
    
    class FennecSleepGoal extends FennecBehaviorGoal {
        private final FennecEntity fennec;
        private static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(140);
        private int countdown;

        public FennecSleepGoal(FennecEntity fennec) {
            this.fennec = fennec;
            this.countdown = fennec.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return fennec.xxa == 0.0F && fennec.yya == 0.0F && fennec.zza == 0.0F && (this.canSleep() || fennec.isSleeping());
        }

        @Override
        public boolean canContinueToUse() {
            return this.canSleep();
        }

        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            } else {
                return fennec.level().isBrightOutside() && this.hasShelter() && !this.alertable() && !fennec.isInPowderSnow;
            }
        }

        @Override
        public void start() {
            this.fennec.getNavigation().stop();
            this.fennec.getMoveControl().setWantedPosition(this.fennec.getX(), this.fennec.getY(), this.fennec.getZ(), (double) 0.0F);
            this.fennec.setSleeping(true);
            this.fennec.setSitting(false);
        }

        @Override
        public void stop() {
            this.countdown = fennec.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            fennec.clearStates();

        }
    }

    public class FennecAlertableEntitesSelector implements TargetingConditions.Selector {
        public boolean test(LivingEntity livingEntity, ServerLevel serverLevel) {
            if (livingEntity instanceof FennecEntity) {
                return false;
            } else if (!(livingEntity instanceof Chicken) && !(livingEntity instanceof Rabbit) && !(livingEntity instanceof Monster)) {
                if (livingEntity instanceof TamableAnimal) {
                    return !((TamableAnimal)livingEntity).isTame();
                } else {
                    if (livingEntity instanceof  Player) {
                        Player player = (Player)livingEntity;
                        if (player.isSpectator() || player.isCreative()) {
                            return false;
                        }
                    }

                    return FennecEntity.this.trusts(livingEntity) ? false : !livingEntity.isSleeping() && !livingEntity.isDiscrete();
                }
            } else {
                return true;
            }
        }
    }

    abstract class FennecBehaviorGoal extends Goal {
        private final TargetingConditions alertableTargeting;

        FennecBehaviorGoal() {
            TargetingConditions target = TargetingConditions.forCombat().range((double) 12.0F).ignoreLineOfSight();
            FennecEntity fennec = FennecEntity.this;
            Objects.requireNonNull(fennec);
            this.alertableTargeting = target.selector(fennec.new FennecAlertableEntitesSelector());
        }

        protected boolean hasShelter() {
            BlockPos blockPos = BlockPos.containing(FennecEntity.this.getX(), FennecEntity.this.getBoundingBox().maxY, FennecEntity.this.getZ());
            return !FennecEntity.this.level().canSeeSky(blockPos) && FennecEntity.this.getWalkTargetValue(blockPos) >= 0.0F;
        }

        protected boolean alertable() {
            return !getServerLevel(FennecEntity.this.level()).getNearbyEntities(LivingEntity.class, this.alertableTargeting, FennecEntity.this, FennecEntity.this.getBoundingBox().inflate((double) 12.0F, (double) 6.0F, (double) 12.0F)).isEmpty();
        }
    }

    class PerchAndSearchGoal extends FennecBehaviorGoal {
        private double relX;
        private double relZ;
        private int lookTime;
        private int looksRemaining;

        public PerchAndSearchGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return FennecEntity.this.getLastHurtByMob() == null && FennecEntity.this.getRandom().nextFloat() < 0.02F && !FennecEntity.this.isSleeping() && FennecEntity.this.getTarget() == null && FennecEntity.this.getNavigation().isDone() && !this.alertable() && !FennecEntity.this.isPouncing() && !FennecEntity.this.isCrouching();
        }

        public boolean canContinueToUse() { return this.looksRemaining > 0; }

        public void start() {
            this.resetLook();
            this.looksRemaining = 2 + FennecEntity.this.getRandom().nextInt(3);
            FennecEntity.this.setSitting(true);
            FennecEntity.this.getNavigation().stop();
        }

        @Override
        public void stop() {
            FennecEntity.this.setSitting(false);
        }

        @Override
        public void tick() {
            --this.lookTime;
            if (this.lookTime <= 0) {
                --this.looksRemaining;
                this.resetLook();
            }

            FennecEntity.this.getLookControl().setLookAt(FennecEntity.this.getX() + this.relX, FennecEntity.this.getEyeY(), FennecEntity.this.getZ() + this.relZ, (float) FennecEntity.this.getMaxHeadYRot(), (float) FennecEntity.this.getMaxHeadXRot());
        }

        private void resetLook() {
            double d0 = (Math.PI * 2D) * FennecEntity.this.getRandom().nextDouble();
            this.relX = Math.cos(d0);
            this.relZ = Math.sin(d0);
            this.lookTime = this.adjustedTickDelay(80 + FennecEntity.this.getRandom().nextInt(20));
        }
    }

    class FennecBreedGoal extends BreedGoal {
        public FennecBreedGoal(double speedModifier) { super(FennecEntity.this, speedModifier);}

        public void start() {
            ((FennecEntity)this.animal).clearStates();
            ((FennecEntity)this.partner).clearStates();
            super.start();
        }

        @Override
        protected void breed() {
            super.breed();
            ServerLevel serverLevel = this.level;
            FennecEntity fennec = (FennecEntity) this.animal.getBreedOffspring(serverLevel, this.partner);
            BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(this.animal, this.partner, fennec);
            boolean cancelled = ((BabyEntitySpawnEvent) NeoForge.EVENT_BUS.post(event)).isCanceled();
            fennec = (FennecEntity) event.getChild();
            if (cancelled) {
                this.animal.setAge(6000);
                this.partner.setAge(6000);
                this.animal.resetLove();
                this.partner.resetLove();
            } else {
                if (fennec != null) {
                    ServerPlayer serverplayer = this.animal.getLoveCause();
                    ServerPlayer serverplayer1 = this.partner.getLoveCause();
                    ServerPlayer serverplayer2 = serverplayer;

                    if (serverplayer != null) {
                        fennec.addTrustedEntity(serverplayer);
                    } else {
                        serverplayer2 = serverplayer1;
                    }

                    if (serverplayer1 != null && serverplayer != serverplayer1) {
                        fennec.addTrustedEntity(serverplayer1);
                    }

                    if (serverplayer2 != null) {
                        serverplayer2.awardStat(Stats.ANIMALS_BRED);
                        CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer2, this.animal, this.partner, fennec);
                    }

                    this.animal.setAge(6000);
                    this.partner.setAge(6000);
                    this.animal.resetLove();
                    this.partner.resetLove();
                    fennec.setAge(-24000);
                    serverLevel.addFreshEntityWithPassengers(fennec);
                    this.level.broadcastEntityEvent(this.animal, (byte) 18);
                    if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                        this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), this.animal.getRandom().nextInt(7) + 1));
                    }
                }
            }
        }
    }




}
