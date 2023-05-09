package net.sylvesternotcute.fennecmod.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.sylvesternotcute.fennecmod.FennecMod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class FennecEntity extends AnimalEntity implements IAnimatable {
    static final Predicate<ItemEntity> PICKABLE_DROP_FILTER = (item) -> {return !item.cannotPickup() && item.isAlive(); };
    private AnimationFactory factory = new AnimationFactory(this);

    public FennecEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 9.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0F)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 5.0D);
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new WanderAroundPointOfInterestGoal(this, 0.75f, false));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(4, new TemptGoal(this, 1.2, Ingredient.ofItems(new ItemConvertible[]{Items.CARROT_ON_A_STICK}), false));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(9, new PickupItemGoal());
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    private <E extends  IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fennec.walk", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fennec.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_FOX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_FOX_HURT;
    }

    protected SoundEvent getDeathSound() {
        return  SoundEvents.ENTITY_FOX_DEATH;
    }

    public boolean canEquip(ItemStack stack){
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(stack);
        if (!this.getEquippedStack(equipmentSlot).isEmpty()) {
            return false;
        } else {
            FennecMod.LOGGER.info("testing can equip");
            return equipmentSlot == EquipmentSlot.MAINHAND && super.canEquip(stack);
        }

    }

    public boolean canPickupItem(ItemStack stack) {
//        Item item = stack.getItem();
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        return itemStack.isEmpty();
    }

//    boolean wantToPickupItem {}

    class PickupItemGoal extends Goal {
        public PickupItemGoal() {this.setControls(EnumSet.of(Control.MOVE)); }

        public boolean canStart() {
            if (!FennecEntity.this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()) {
                return false;
            } else if (FennecEntity.this.getTarget() == null && FennecEntity.this.getAttacker() == null) {
                if (FennecEntity.this.getRandom().nextInt(toGoalTicks(10)) != 0) {
                    return false;
                } else {
                    List<ItemEntity> list = FennecEntity.this.world.getEntitiesByClass(ItemEntity.class, FennecEntity.this.getBoundingBox().expand(8.0, 8.0, 8.0), FennecEntity.PICKABLE_DROP_FILTER);
                    return !list.isEmpty() && FennecEntity.this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty();
                }
            } else {
                return false;
            }
        }

        public void tick() {
            List<ItemEntity> list = FennecEntity.this.world.getEntitiesByClass(ItemEntity.class, FennecEntity.this.getBoundingBox().expand(8.0, 8.0, 8.0), FennecEntity.PICKABLE_DROP_FILTER);
            ItemStack itemStack =FennecEntity.this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (itemStack.isEmpty() && !list.isEmpty()) {
                FennecEntity.this.getNavigation().startMovingTo((Entity) list.get(0), 1.2);
            }
        }

        @Override
        public void start() {
            List<ItemEntity> list = FennecEntity.this.world.getEntitiesByClass(ItemEntity.class, FennecEntity.this.getBoundingBox().expand(8.0, 8.0, 8.0), FennecEntity.PICKABLE_DROP_FILTER);
            if (!list.isEmpty()) {
                FennecEntity.this.getNavigation().startMovingTo((Entity) list.get(0), 1.2);
            }
        }
    }

//    @Override
//    protected void playStepSound(BlockPos pos, BlockState state) {
//        this.playSound(SoundEvents.ENTITY_FOX_SPIT, 0.15f, 1.0f);
//    }
}
