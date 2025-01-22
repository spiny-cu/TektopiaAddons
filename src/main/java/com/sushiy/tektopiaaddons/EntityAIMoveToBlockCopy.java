package com.sushiy.tektopiaaddons;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.tangotek.tektopia.VillageManager;
import net.tangotek.tektopia.entities.EntityVillageNavigator;
import net.tangotek.tektopia.pathing.BasePathingNode;
import net.tangotek.tektopia.pathing.PathNavigateVillager2;
import net.tangotek.tektopia.structures.VillageStructure;

public abstract class EntityAIMoveToBlockCopy extends EntityAIBase {
    private static int STUCK_TIME = 40;
    protected EntityVillageNavigator navigator;
    private BlockPos walkPos;
    protected BlockPos destinationPos;
    private int pathUpdateTick = 20;
    private boolean arrived = false;
    private int stuckCheck;
    private Vec3d stuckPos;
    private boolean stuck;
    private int lastPathIndex;
    private Vec3d lastNodePos;

    public EntityAIMoveToBlockCopy(EntityVillageNavigator v) {
        this.stuckCheck = STUCK_TIME;
        this.stuckPos = Vec3d.ZERO;
        this.stuck = false;
        this.lastPathIndex = -1;
        this.navigator = v;
        this.setMutexBits(1);
    }

    protected abstract BlockPos getDestinationBlock();

    protected void onArrival() {
    }

    public boolean shouldExecute() {
        if (this.navigator.hasVillage() && this.navigator.getNavigator() instanceof PathNavigateVillager2 && this.canNavigate()) {
            this.destinationPos = this.getDestinationBlock();
            if (this.destinationPos != null) {
                this.stuck = false;
                this.stuckPos = new Vec3d((double)0.0F, (double)-400.0F, (double)0.0F);
                this.arrived = false;
                this.pathUpdateTick = 40;
                this.doMove();
                return !this.stuck;
            }
        }

        return false;
    }

    protected boolean isNearWalkPos() {
        return this.walkPos != null && this.walkPos.distanceSq(this.navigator.getPosition()) <= (double)1.0F;
    }

    protected boolean isNearDestination(double range) {
        return this.destinationPos.distanceSq(this.navigator.getPosition()) < range * range;
    }

    protected boolean canNavigate() {
        return this.navigator.onGround;
    }

    public void startExecuting() {
        this.updateMovementMode();
    }

    public boolean shouldContinueExecuting() {
        return !this.arrived && !this.stuck && this.navigator.canNavigate();
    }

    protected void updateFacing() {
        if (!this.arrived) {
            if (!this.navigator.getNavigator().noPath()) {
                Vec3d lookPos = this.navigator.getNavigator().getPath().getCurrentPos();
                this.navigator.faceLocation(lookPos.x, lookPos.z, 4.0F);
            }
        } else if (this.destinationPos != null) {
        }

    }

    protected abstract void updateMovementMode();

    public void updateTask() {
        --this.pathUpdateTick;
        if (this.pathUpdateTick <= 0 && !this.arrived) {
            this.pathUpdateTick = 40;
            this.navigator.updateMovement(this.arrived);
        }

        if (!this.arrived && this.isNearWalkPos()) {
            this.arrived = true;
            this.navigator.getNavigator().clearPath();
            this.onArrival();
        }

        this.updateFacing();
        if (!this.arrived) {
            if (this.navigator.getNavigator().noPath()) {
                this.doMove();
            } else {
                int pathIndex = this.navigator.getNavigator().getPath().getCurrentPathIndex();
                if (this.lastPathIndex != pathIndex) {
                    this.lastNodePos = this.navigator.getNavigator().getPath().getCurrentPos();
                    this.lastPathIndex = pathIndex;
                }
            }

            --this.stuckCheck;
            if (this.stuckCheck < 0) {
                this.stuckCheck = STUCK_TIME;
                if (!this.navigator.getNavigator().noPath()) {
                    this.stuck = this.navigator.getPositionVector().squareDistanceTo(this.stuckPos) < (double)1.0F;
                    this.stuckPos = this.navigator.getPositionVector();
                } else {
                    this.navigator.debugOut("has no path?");
                }
            }

            if (this.stuck) {
                if (this.attemptStuckFix() && this.lastPathIndex >= 0) {
                    this.navigator.getNavigator().clearPath();
                    this.doMove();
                } else {
                    this.onStuck();
                }
            }
        }

    }

    protected boolean attemptStuckFix() {
        return false;
    }

    protected void onStuck() {
        VillageManager.get(this.navigator.world).submitStuck(this.navigator.getPosition());
        Path path = this.navigator.getNavigator().getPath();
        if (path != null && this.navigator.hasVillage() && this.navigator.getNavigator() instanceof PathNavigateVillager2) {
            PathNavigateVillager2 var2 = (PathNavigateVillager2)this.navigator.getNavigator();
        }

        this.navigator.getNavigator().clearPath();
    }

    protected void onPathFailed(BlockPos pos) {
        VillageManager.get(this.navigator.world).submitStuck(this.navigator.getPosition());
        this.stuck = true;
    }

    public BlockPos getWalkPos() {
        return this.walkPos;
    }

    protected BlockPos findWalkPos() {
        BlockPos pos = this.destinationPos;
        BlockPos diff = this.navigator.getPosition().subtract(pos);
        EnumFacing facing = EnumFacing.getFacingFromVector((float)diff.getX(), 0.0F, (float)diff.getZ());
        BlockPos testPos = pos.offset(facing);
        if (this.isWalkable(testPos, this.navigator)) {
            return testPos;
        } else {
            testPos = pos.offset(facing).offset(facing.rotateY());
            if (this.isWalkable(testPos, this.navigator)) {
                return testPos;
            } else {
                testPos = pos.offset(facing).offset(facing.rotateYCCW());
                if (this.isWalkable(testPos, this.navigator)) {
                    return testPos;
                } else {
                    testPos = pos.offset(facing.rotateY());
                    if (this.isWalkable(testPos, this.navigator)) {
                        return testPos;
                    } else {
                        testPos = pos.offset(facing.rotateYCCW());
                        if (this.isWalkable(testPos, this.navigator)) {
                            return testPos;
                        } else {
                            testPos = pos.offset(facing.getOpposite());
                            if (this.isWalkable(testPos, this.navigator)) {
                                return testPos;
                            } else {
                                testPos = pos.offset(facing.getOpposite()).offset(facing.rotateY());
                                if (this.isWalkable(testPos, this.navigator)) {
                                    return testPos;
                                } else {
                                    testPos = pos.offset(facing.getOpposite()).offset(facing.rotateYCCW());
                                    if (this.isWalkable(testPos, this.navigator)) {
                                        return testPos;
                                    } else {
                                        return this.isWalkable(pos, this.navigator) ? pos : null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean isWalkable(BlockPos pos, EntityVillageNavigator nav) {
        if (nav.getVillage() != null) {
            BasePathingNode baseNode = nav.getVillage().getPathingGraph().getBaseNode(pos.getX(), pos.getY(), pos.getZ());
            if (baseNode != null) {
                if (!VillageStructure.isWoodDoor(nav.world, pos) && !VillageStructure.isGate(nav.world, pos)) {
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    protected void doMove() {
        this.arrived = false;
        this.stuckCheck = STUCK_TIME;
        this.walkPos = this.findWalkPos();
        if (this.walkPos == null) {
            this.stuck = true;
        } else if (!this.isNearWalkPos() && this.canNavigate()) {
            boolean pathFound = this.navigator.getNavigator().tryMoveToXYZ((double)this.walkPos.getX(), (double)this.walkPos.getY(), (double)this.walkPos.getZ(), (double)this.navigator.getAIMoveSpeed());
            if (pathFound) {
                this.navigator.getLookHelper().setLookPosition((double)this.walkPos.getX(), (double)this.walkPos.getY(), (double)this.walkPos.getZ(), 50.0F, (float)this.navigator.getVerticalFaceSpeed());
            } else {
                this.onPathFailed(this.walkPos);
            }
        }

    }

    protected boolean hasArrived() {
        return this.arrived;
    }

    protected void setArrived() {
        this.arrived = true;
    }

    public void resetTask() {
        super.resetTask();
        this.arrived = false;
        this.stuckCheck = STUCK_TIME;
        this.navigator.resetMovement();
    }
}
