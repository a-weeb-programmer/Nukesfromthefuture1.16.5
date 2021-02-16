package net.nukesfromthefuture.explosion;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class Advanced {
    public int posX;
    public int posY;
    public int posZ;
    public int lastposX = 0;
    public int lastposZ = 0;
    public int radius;
    public int radius2;
    public World worldObj;
    private int n = 1;
    private int nlimit;
    private int shell;
    private int leg;
    private int element;
    public float explosionCoefficient = 1.0F;
    public int type = 0;

    public void saveToNbt(CompoundNBT nbt, String name) {
        nbt.putInt(name + "posX", posX);
        nbt.putInt(name + "posY", posY);
        nbt.putInt(name + "posZ", posZ);
        nbt.putInt(name + "lastposX", lastposX);
        nbt.putInt(name + "lastposZ", lastposZ);
        nbt.putInt(name + "radius", radius);
        nbt.putInt(name + "radius2", radius2);
        nbt.putInt(name + "n", n);
        nbt.putInt(name + "nlimit", nlimit);
        nbt.putInt(name + "shell", shell);
        nbt.putInt(name + "leg", leg);
        nbt.putInt(name + "element", element);
        nbt.putFloat(name + "explosionCoefficient", explosionCoefficient);
        nbt.putInt(name + "type", type);
    }

    public void readFromNbt(CompoundNBT nbt, String name) {
        posX = nbt.getInt(name + "posX");
        posY = nbt.getInt(name + "posY");
        posZ = nbt.getInt(name + "posZ");
        lastposX = nbt.getInt(name + "lastposX");
        lastposZ = nbt.getInt(name + "lastposZ");
        radius = nbt.getInt(name + "radius");
        radius2 = nbt.getInt(name + "radius2");
        n = nbt.getInt(name + "n");
        nlimit = nbt.getInt(name + "nlimit");
        shell = nbt.getInt(name + "shell");
        leg = nbt.getInt(name + "leg");
        element = nbt.getInt(name + "element");
        explosionCoefficient = nbt.getFloat(name + "explosionCoefficient");
        type = nbt.getInt(name + "type");
    }

    public Advanced(int x, int y, int z, World world, int rad, float coefficient, int typ)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;

        this.worldObj = world;

        this.radius = rad;
        this.radius2 = this.radius * this.radius;

        this.explosionCoefficient = Math.min(Math.max((rad + coefficient * (y - 60))/(coefficient*rad), 1/coefficient),1.0f);	//scale the coefficient depending on detonation height
        this.type = typ;

        this.nlimit = this.radius2 * 4; //How many total columns should be broken (radius ^ 2 is one quadrant, there are 4 quadrants)
    }

    public boolean update()
    {
        if(!worldObj.isRemote) {
            switch (this.type) {
                case 0:
                    breakColumn(this.lastposX, this.lastposZ);
                    break;
                case 1:
                    vapor(this.lastposX, this.lastposZ);
                    break;
                case 2:
                    waste(this.lastposX, this.lastposZ);
                    break;
            }
            this.shell = (int) Math.floor((Math.sqrt(n) + 1) / 2); //crazy stuff I can't explain
            int shell2 = this.shell * 2;
            this.leg = (int) Math.floor((this.n - (shell2 - 1) * (shell2 - 1)) / shell2);
            this.element = (this.n - (shell2 - 1) * (shell2 - 1)) - shell2 * this.leg - this.shell + 1;
            this.lastposX = this.leg == 0 ? this.shell : this.leg == 1 ? -this.element : this.leg == 2 ? -this.shell : this.element;
            this.lastposZ = this.leg == 0 ? this.element : this.leg == 1 ? this.shell : this.leg == 2 ? -this.element : -this.shell;
            this.n++;
            return this.n > this.nlimit; //return whether we are done or not
        }
        return false;
    }

    private void breakColumn(int x, int z)
    {
        if(!worldObj.isRemote) {
            int dist = this.radius2 - (x * x + z * z); //we have two sides of the triangle (hypotenuse is radius, one leg is (x*x+z*z)) this calculates the third one
            if (dist > 0) //check if any blocks have to be broken here
            {
                dist = (int) Math.sqrt(dist); //calculate sphere height at this (x,z) coordinate
                for (int y = dist; y > -dist * this.explosionCoefficient; y--) //go from top to bottom to favor light updates
                {
                    if (y < 8) {//only spare blocks that are mostly below epicenter
                        y -= Generic.destruction(this.worldObj, this.posX + x, this.posY + y, this.posZ + z);//spare blocks below
                    } else {//don't spare blocks above epicenter
                        Generic.destruction(this.worldObj, this.posX + x, this.posY + y, this.posZ + z);
                    }
                }
            }
        }
    }

    private void vapor(int x, int z)
    {
        if(!worldObj.isRemote) {
            int dist = this.radius2 - (x * x + z * z);
            if (dist > 0) {
                dist = (int) Math.sqrt(dist);
                //int dist0 = (int)Math.sqrt(this.radius2*0.15f - (x * x + z * z));
                for (int y = dist; y > -dist * this.explosionCoefficient; y--) {
                    y -= Generic.vaporDest(this.worldObj, this.posX + x, this.posY + y, this.posZ + z);
				/*
				if(dist0>0){//skip blocks already in the destruction zone: we will
					if(y>=dist0 || y<=-dist0*this.explosionCoefficient){
						y-=ExplosionNukeGeneric.vaporDest(this.worldObj, this.posX + x, this.posY + y, this.posZ + z);
					}
				}else{
					y-=ExplosionNukeGeneric.vaporDest(this.worldObj, this.posX + x, this.posY + y, this.posZ + z);
				}*/
                }
            }
        }
    }

    private void waste(int x, int z)
    {
        if(!worldObj.isRemote) {
            int dist = this.radius2 - (x * x + z * z);
            if (dist > 0) {
                dist = (int) Math.sqrt(dist);
                for (int y = dist; y > -dist * this.explosionCoefficient; y--) {
                    if (radius >= 95)
                        Generic.wasteDest(this.worldObj, this.posX + x, this.posY + y, this.posZ + z);
                    else
                        Generic.wasteDestNoSchrab(this.worldObj, this.posX + x, this.posY + y, this.posZ + z);
                }
            }
        }
    }

	/*public static void mush(World world, double x, double y, double z)
	{
		 double d = (float)x + 0.5F;
		 double d1 = (float)y + 0.5F;
		 double d2 = (float)z + 0.5F;

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1, d2, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 15, d1, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1, d2 + 15, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 15, d1, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1, d2 - 15, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 25, d1, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1, d2 + 25, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 25, d1, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1, d2 - 25, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 10, d1, d2 + 10, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 10, d1, d2 - 10, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 10, d1, d2 + 10, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 10, d1, d2 - 10, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 15, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 30, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 45, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 60, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 75, d2, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 90, d2, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 15, d1 + 90, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 90, d2 + 15, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 15, d1 + 90, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 90, d2 - 15, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 40, d1 + 90, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 90, d2 + 40, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 40, d1 + 90, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 90, d2 - 40, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 30, d1 + 90, d2 + 30, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 30, d1 + 90, d2 - 30, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 30, d1 + 90, d2 + 30, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 30, d1 + 90, d2 - 30, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 105, d2, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 30, d1 + 105, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 105, d2 + 30, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 30, d1 + 105, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 105, d2 - 30, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 20, d1 + 105, d2 + 20, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 20, d1 + 105, d2 - 20, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 20, d1 + 105, d2 + 20, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 20, d1 + 105, d2 - 20, 0.0D, 0.0D, 0.0D, 100));

		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d + 10, d1 + 120, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 120, d2 + 10, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d - 10, d1 + 120, d2, 0.0D, 0.0D, 0.0D, 100));
		 Minecraft.getMinecraft().effectRenderer.addEffect(new NukeCloudFX(world, d, d1 + 120, d2 - 10, 0.0D, 0.0D, 0.0D, 100));
	}*/
}
