package net.nukesfromthefuture.main;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapeArray;
import net.minecraft.util.math.shapes.VoxelShapeCube;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class Lib {
    public static boolean isObstructed(World world, double x, double y, double z, double a, double b, double c) {

        RayTraceResult pos = world.rayTraceBlocks(new Vector3d(x, y, z), new Vector3d(a, b, c), new BlockPos(x, y, z), world.getWorldBorder().getShape(), world.getBlockState(new BlockPos(x, y, z)));

        return pos != null;
    }
    public static String getShortNumber(long l) {

        if(l >= Math.pow(10, 18)) {
            double res = l / Math.pow(10, 18);
            res = Math.round(res * 100.0) / 100.0;
            return res + "E";
        }
        if(l >= Math.pow(10, 15)) {
            double res = l / Math.pow(10, 15);
            res = Math.round(res * 100.0) / 100.0;
            return res + "P";
        }
        if(l >= Math.pow(10, 12)) {
            double res = l / Math.pow(10, 12);
            res = Math.round(res * 100.0) / 100.0;
            return res + "T";
        }
        if(l >= Math.pow(10, 9)) {
            double res = l / Math.pow(10, 9);
            res = Math.round(res * 100.0) / 100.0;
            return res + "G";
        }
        if(l >= Math.pow(10, 6)) {
            double res = l / Math.pow(10, 6);
            res = Math.round(res * 100.0) / 100.0;
            return res + "M";
        }
        if(l >= Math.pow(10, 3)) {
            double res = l / Math.pow(10, 3);
            res = Math.round(res * 100.0) / 100.0;
            return res + "k";
        }

        return Long.toString(l);
    }
}
