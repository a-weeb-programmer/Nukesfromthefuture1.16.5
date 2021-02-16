package net.nukesfromthefuture.explosion;

import cofh.api.energy.IEnergyProvider;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WoodType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.command.arguments.EnchantmentArgument;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.nukesfromthefuture.entity.MK3Explosion;
import net.nukesfromthefuture.interfaces.IConsumer;
import net.nukesfromthefuture.main.Lib;
import net.nukesfromthefuture.main.NffDamageSource;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import org.lwjgl.system.CallbackI;

import java.util.List;
import java.util.Random;

public class Generic {
    private final static Random random = new Random();

    public static void detonateTestBomb(World world, int x, int y, int z, int bombStartStrength) {
        int r = bombStartStrength;
        int r2 = r * r;
        int r22 = r2 / 2;
        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (r22 >= 25) {
                        if (ZZ < r22 + world.rand.nextInt(r22 / 25)) {
                            if (Y >= y)
                                destruction(world, X, Y, Z);
                        }
                    } else {
                        if (ZZ < r22) {
                            if (Y >= y)
                                destruction(world, X, Y, Z);
                        }
                    }
                }
            }
        }

        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy * 50;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (ZZ < r22/* +world.rand.nextInt(r22) */) {
                        if (Y < y)
                            destruction(world, X, Y, Z);
                    }
                }
            }
        }
    }

    public static void empBlast(World world, int x, int y, int z, int bombStartStrength) {
        int r = bombStartStrength;
        int r2 = r * r;
        int r22 = r2 / 2;
        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (ZZ < r22) {
                        emp(world, X, Y, Z);
                    }
                }
            }
        }
    }

    public static void dealDamage(World world, int x, int y, int z, int bombStartStrength) {
        float f = bombStartStrength;
        int i;
        int j;
        int k;
        double d5;
        double d6;
        double d7;
        double wat = bombStartStrength/** 2 */
                ;

        // bombStartStrength *= 2.0F;
        i = MathHelper.floor(x - wat - 1.0D);
        j = MathHelper.floor(x + wat + 1.0D);
        k = MathHelper.floor(y - wat - 1.0D);
        int i2 = MathHelper.floor(y + wat + 1.0D);
        int l = MathHelper.floor(z - wat - 1.0D);
        int j2 = MathHelper.floor(z + wat + 1.0D);
        List list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(i, k, l, j, i2, j2));

        for (int i1 = 0; i1 < list.size(); ++i1) {
            Entity entity = (Entity) list.get(i1);
            double d4 = entity.getDistance(entity) / bombStartStrength;

            if (d4 <= 1.0D) {
                d5 = entity.getPosX() - x;
                d6 = entity.getPosY() + entity.getEyeHeight() - y;
                d7 = entity.getPosZ() - z;
                double d9 = MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
                if(!Lib.isObstructed(world, x, y, z, entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ()))
                    if (d9 < wat && !(entity instanceof OcelotEntity)) {
                        d5 /= d9;
                        d6 /= d9;
                        d7 /= d9;
                        // double d10 = (double)world.getBlockDensity(vec3,
                        // entity.boundingBox);
                        // if(d10 > 0) isOccupied = true;
                        double d11 = (1.0D - d4);// * d10;
                        if (!(entity instanceof ServerPlayerEntity) || (entity instanceof ServerPlayerEntity
                                && ((ServerPlayerEntity) entity).interactionManager.getGameType() != GameType.CREATIVE)) {
                            // entity.attackEntityFrom(DamageSource.generic,
                            // ((int)((d11 * d11 + d11) / 2.0D * 8.0D *
                            // bombStartStrength + 1.0D)));
                            double damage = entity.getDistance(entity) / bombStartStrength * 250;
                            entity.attackEntityFrom(NffDamageSource.nuke_blast, (float)damage);
                            entity.setFire(5);
                            if(entity.isLiving()) {
                                double d8 = ProtectionEnchantment.getBlastDamageReduction((LivingEntity) entity, (int) d11);
                                entity.lastTickPosX += d5 * d8 * 0.2D;
                                entity.lastTickPosY += d6 * d8 * 0.2D;
                                entity.lastTickPosZ += d7 * d8 * 0.2D;
                            }
                        }
                    }
            }
        }

        bombStartStrength = (int) f;
    }

    public static void succ(World world, int x, int y, int z, int radius) {
        int i;
        int j;
        int k;
        double d5;
        double d6;
        double d7;
        double wat = radius/** 2 */
                ;

        // bombStartStrength *= 2.0F;
        i = MathHelper.floor(x - wat - 1.0D);
        j = MathHelper.floor(x + wat + 1.0D);
        k = MathHelper.floor(y - wat - 1.0D);
        int i2 = MathHelper.floor(y + wat + 1.0D);
        int l = MathHelper.floor(z - wat - 1.0D);
        int j2 = MathHelper.floor(z + wat + 1.0D);
        List list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(i, k, l, j, i2, j2));

        for (int i1 = 0; i1 < list.size(); ++i1) {
            Entity entity = (Entity) list.get(i1);



            double d4 = entity.getDistance(entity) / radius;

            if (d4 <= 1.0D) {
                d5 = entity.getPosX() - x;
                d6 = entity.getPosY() + entity.getEyeHeight() - y;
                d7 = entity.getPosZ() - z;
                double d9 = MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
                if (d9 < wat && !(entity instanceof PlayerEntity)) {
                    d5 /= d9;
                    d6 /= d9;
                    d7 /= d9;

                    if (!(entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.isCreativeMode)) {
                        double d8 = 0.125 + (random.nextDouble() * 0.25);
                        entity.lastTickPosX -= d5 * d8;
                        entity.lastTickPosY -= d6 * d8;
                        entity.lastTickPosZ -= d7 * d8;
                    }
                }
            }
        }
    }

    public static boolean dedify(World world, int x, int y, int z, int radius) {
        int i;
        int j;
        int k;
        double d5;
        double d6;
        double d7;
        double wat = radius/** 2 */
                ;

        // bombStartStrength *= 2.0F;
        i = MathHelper.floor(x - wat - 1.0D);
        j = MathHelper.floor(x + wat + 1.0D);
        k = MathHelper.floor(y - wat - 1.0D);
        int i2 = MathHelper.floor(y + wat + 1.0D);
        int l = MathHelper.floor(z - wat - 1.0D);
        int j2 = MathHelper.floor(z + wat + 1.0D);
        List list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(i, k, l, j, i2, j2));

        for (int i1 = 0; i1 < list.size(); ++i1) {
            Entity entity = (Entity) list.get(i1);
            double d4 = entity.getDistance(entity) / radius;

            if (d4 <= 1.0D) {
                d5 = entity.getPosX() - x;
                d6 = entity.getPosY() + entity.getEyeHeight() - y;
                d7 = entity.getPosZ() - z;
                double d9 = MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
                if (d9 < wat && !(entity instanceof PlayerEntity)) {
                    d5 /= d9;
                    d6 /= d9;
                    d7 /= d9;
                    // double d10 = (double)world.getBlockDensity(vec3,
                    // entity.boundingBox);
                    // if(d10 > 0) isOccupied = true;



                    if (!(entity instanceof ServerPlayerEntity
                            && ((ServerPlayerEntity) entity).interactionManager.getGameType() == GameType.CREATIVE)) {
                        entity.attackEntityFrom(NffDamageSource.nuke_blast, 1000F);
                    }


                }
            }
        }

        return false;
    }



    public static void vapor(World world, int x, int y, int z, int bombStartStrength) {
        int r = bombStartStrength * 2;
        int r2 = r * r;
        int r22 = r2 / 2;
        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (ZZ < r22)
                        vaporDest(world, X, Y, Z);
                }
            }
        }
    }

    public static int destruction(World world, int x, int y, int z) {
        int rand;
            BlockState b = world.getBlockState(new BlockPos(x,y,z));
            if (b.getExplosionResistance(world, new BlockPos(x, y, z), null)>=200f) {	//500 is the resistance of liquids
                //blocks to be spared
                int protection = (int)(b.getExplosionResistance(world, new BlockPos(x, y, z), null)/300f);


            }else{//oherwise, kill the block!
                world.removeBlock(new BlockPos(x, y, z), false);

            }if(b == Blocks.BEDROCK.getDefaultState()){
                for(int i = 1; i < 6; i++) {
                    world.removeBlock(new BlockPos(x, y + i, z), false);
                }

            }if(b == Blocks.OBSIDIAN.getDefaultState()){
                world.removeBlock(new BlockPos(x, y, z), false);
            }

        return 0;
    }

    public static int vaporDest(World world, int x, int y, int z) {
        FluidState fluid = world.getFluidState(new BlockPos(x, y, z));
        BlockState b = world.getBlockState(new BlockPos(x, y, z));
        if (b.getExplosionResistance(world, new BlockPos(x, y, z), null) < 0.5f //most light things
                || b == Blocks.COBWEB.getDefaultState()
                || fluid != Fluids.EMPTY.getDefaultState()
                || b == Blocks.WATER.getDefaultState() || b == Blocks.LAVA.getDefaultState()) {
            world.removeBlock(new BlockPos(x, y, z), false);
            world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
            return 0;
        } else if (b.getExplosionResistance(world, new BlockPos(x, y, z), null) <= 3.0f && !b.isOpaqueCube(world, new BlockPos(x, y, z))) {
            if (b != Blocks.CHEST.getDefaultState() && b != Blocks.FARMLAND.getDefaultState()) {
                //destroy all medium resistance blocks that aren't chests or farmland
                world.removeBlock(new BlockPos(x, y, z), false);
                return 0;
            }
        }

        if (b.isFlammable(world, new BlockPos(x, y, z), Direction.UP)
                && world.getBlockState(new BlockPos(x, y + 1, z)) == Blocks.AIR.getDefaultState()) {
            world.setBlockState(new BlockPos(x, y + 1, z), Blocks.FIRE.getDefaultState(), 0, 2);
        }
        return (int) (b.getExplosionResistance(world, new BlockPos(x, y, z), null) / 300f);

    }

    public static void waste(World world, int x, int y, int z, int radius) {
        int r = radius;
        int r2 = r * r;
        int r22 = r2 / 2;
        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (ZZ < r22 + world.rand.nextInt(r22 / 5)) {
                        if (world.getBlockState(new BlockPos(X, Y, Z)) != Blocks.AIR.getDefaultState())
                            wasteDest(world, X, Y, Z);
                    }
                }
            }
        }
    }

    public static void wasteDest(World world, int x, int y, int z) {
            int rand;
            BlockState b = world.getBlockState(new BlockPos(x,y,z));
            if (b == Blocks.OAK_DOOR.getDefaultState() || b == Blocks.IRON_DOOR.getDefaultState()) {
                world.removeBlock(new BlockPos(x, y, z),false);
            }

            else if (b == Blocks.GRASS_BLOCK.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());
            }

            else if (b == Blocks.MYCELIUM.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());
            }

            else if (b == Blocks.SAND.getDefaultState()) {
                rand = random.nextInt(20);
                if (rand == 1) {
                    world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());
                }
                if (rand == 1) {
                    world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());
                }
            }

            else if (b == Blocks.CLAY.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Blocks.TERRACOTTA.getDefaultState());
            }

            else if (b == Blocks.MOSSY_COBBLESTONE.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Blocks.COAL_ORE.getDefaultState());
            }

            else if (b == Blocks.COAL_ORE.getDefaultState()) {
                rand = random.nextInt(10);
                if (rand == 1 || rand == 2 || rand == 3) {
                    world.setBlockState(new BlockPos(x, y, z), Blocks.DIAMOND_ORE.getDefaultState());
                }
                if (rand == 9) {
                    world.setBlockState(new BlockPos(x, y, z), Blocks.EMERALD_ORE.getDefaultState());
                }
            }else if(b == Nukesfromthefuture.egonium_ore.getDefaultState()){
                rand = random.nextInt(10);
                if(rand == 2 || rand == 4){
                    world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.Deathinum_ore.getDefaultState());
                }
            }

            else if (b == Blocks.OAK_LOG.getDefaultState() || b == Blocks.SPRUCE_LOG.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste_wood.getDefaultState());
            }

            else if (b == Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()) {
                    world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());

            }

            else if (b == Blocks.RED_MUSHROOM_BLOCK.getDefaultState()) {

                    world.removeBlock(new BlockPos(x, y, z), false);

            }

            else if (b.getMaterial() == Material.WOOD && b.isOpaqueCube(world, new BlockPos(x, y, z)) && b != Nukesfromthefuture.waste.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());
            }

    }

    public static void wasteNoSchrab(World world, int x, int y, int z, int radius) {
        int r = radius;
        int r2 = r * r;
        int r22 = r2 / 2;
        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (ZZ < r22 + world.rand.nextInt(r22 / 5)) {
                        if (world.getBlockState(new BlockPos(X, Y, Z)) != Blocks.AIR.getDefaultState())
                            wasteDestNoSchrab(world, X, Y, Z);
                    }
                }
            }
        }
    }

    public static void wasteDestNoSchrab(World world, int x, int y, int z) {
            int rand;

            if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.GLASS.getDefaultState()
                    || world.getBlockState(new BlockPos(x, y, z)) == Blocks.OAK_DOOR.getDefaultState() || world.getBlockState(new BlockPos(x, y, z)) == Blocks.IRON_DOOR.getDefaultState()
                    || world.getBlockState(new BlockPos(x, y, z)) == Blocks.OAK_LEAVES.getDefaultState() || world.getBlockState(new BlockPos(x, y, z)) == Blocks.DARK_OAK_LEAVES.getDefaultState()) {
                world.removeBlock(new BlockPos(x, y, z), false);
            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.GRASS_BLOCK.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());
            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.MYCELIUM.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());
            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.SAND.getDefaultState()) {
                rand = random.nextInt(20);
                if (rand == 1) {
                    world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());
                }

            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.CLAY.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Blocks.TERRACOTTA.getDefaultState());
            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.MOSSY_COBBLESTONE.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Blocks.COAL_ORE.getDefaultState());
            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.COAL_ORE.getDefaultState()) {
                rand = random.nextInt(30);
                if (rand == 1 || rand == 2 || rand == 3) {
                    world.setBlockState(new BlockPos(x, y, z), Blocks.DIAMOND_ORE.getDefaultState());
                }
                if (rand == 29) {
                    world.setBlockState(new BlockPos(x, y, z), Blocks.EMERALD_ORE.getDefaultState());
                }
            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.OAK_LOG.getDefaultState() || world.getBlockState(new BlockPos(x, y, z)) == Blocks.SPRUCE_LOG.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste_wood.getDefaultState());
            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.OAK_PLANKS.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.waste.getDefaultState());
            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()) {
                    world.removeBlock(new BlockPos(x, y, z), false);
            }

            else if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.RED_MUSHROOM_BLOCK.getDefaultState()) {
                    world.removeBlock(new BlockPos(x, y, z), false);
            }

    }

    public static void emp(World world, int x, int y, int z) {

            BlockState b = world.getBlockState(new BlockPos(x,y,z));
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));


            if (te != null && te instanceof IConsumer) {

                ((IConsumer)te).setPower(0);


            }
            if (te != null && te instanceof IEnergyProvider) {

                ((IEnergyProvider)te).extractEnergy(Direction.UP, ((IEnergyProvider)te).getEnergyStored(Direction.UP), false);
                ((IEnergyProvider)te).extractEnergy(Direction.DOWN, ((IEnergyProvider)te).getEnergyStored(Direction.DOWN), false);
                ((IEnergyProvider)te).extractEnergy(Direction.NORTH, ((IEnergyProvider)te).getEnergyStored(Direction.NORTH), false);
                ((IEnergyProvider)te).extractEnergy(Direction.SOUTH, ((IEnergyProvider)te).getEnergyStored(Direction.SOUTH), false);
                ((IEnergyProvider)te).extractEnergy(Direction.EAST, ((IEnergyProvider)te).getEnergyStored(Direction.EAST), false);
                ((IEnergyProvider)te).extractEnergy(Direction.WEST, ((IEnergyProvider)te).getEnergyStored(Direction.WEST), false);


            }



    }

    public static void solinium(World world, int x, int y, int z) {
            BlockState b = world.getBlockState(new BlockPos(x,y,z));
            Material m = b.getMaterial();

            if(b == Blocks.GRASS_BLOCK.getDefaultState() || b == Blocks.MYCELIUM.getDefaultState() || b == Nukesfromthefuture.waste.getDefaultState()) {
                world.setBlockState(new BlockPos(x, y, z), Blocks.DIRT.getDefaultState());
                return;
            }

            if(m == Material.CACTUS || m == Material.CORAL || m == Material.LEAVES || m == Material.PLANTS || m == Material.SPONGE || m == Material.GOURD || m == Material.WOOD) {
                world.removeBlock(new BlockPos(x, y, z), false);
            }
        }

}
