package com.mmdev.meowmayo.utils;

import java.lang.Math;

import net.minecraft.client.Minecraft;

public class SupplyUtils {
    public enum CrateLocation {
        TRIANGLE(-67.5, 77, -122.5, "Triangle"),
        X(-142.5, 77, -151, "X"),
        EQUALS(-65.5, 76, -87.5, "Equals"),
        SLASH(-113.5, 77, -68.5, "Slash"),
        SHOP(-81, 76, -143, "Shop"),
        XCANNON(-143, 76, -125, "xCannon"),
        SQUARE(-143, 76, -80, "Square");

        private final double x, y, z;
        private final String name;

        CrateLocation(double x, double y, double z, String name) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.name = name;
        }

        public double[] getLocation() {
            return new double[]{x, y, z};
        }

        public String getName() {
            return name;
        }
    }

    public enum PreLocation {
        TRIANGLE(-94, 79, -106),
        X(-106, 79, -113),
        SLASH(-98, 79, -99),
        EQUALS(-106, 79, -99),
        SHOP(-98, 79, -113),
        XCANNON(-110, 79, -106);

        private final double x, y, z;

        PreLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double[] getLocation() {
            return new double[]{x, y, z};
        }
    }

    public enum SecondLocation {
        SQUARE(-138, 79, -90),
        SHOP(-87, 79, -127),
        XCANNON(-130, 78, -114);

        private final double x, y, z;

        SecondLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double[] getLocation() {
            return new double[]{x, y, z};
        }
    }

    private static final double grav = 0.05;
    private static final double eVel = 1.67;

    public static class PearlResult {
        public double[] location;
        public int timing;

        public PearlResult(double[] location, int timing) {
            this.location = location;
            this.timing = timing;
        }

        public double[] getLocation() { return location; }
        public int getTiming() { return timing; }
    }

    public static PearlResult calculatePearl(double[] target) {
        final double playerX = Minecraft.getMinecraft().thePlayer.posX;
        final double playerY = Minecraft.getMinecraft().thePlayer.posY;
        final double playerZ = Minecraft.getMinecraft().thePlayer.posZ;

        final double offX = target[0] - playerX;
        final double offY = target[1] - (playerY + 1.62);
        final double offZ = target[2] - playerZ;
        final double offHor = Math.hypot(offX, offZ);

        final double vSq = eVel * eVel;
        final double term1 = (grav * offHor * offHor) / (2 * vSq);
        final double discrim = vSq - grav * (term1 - offY);

        if (discrim < 0) {
            return new PearlResult(new double[]{0.0, 9.0, 0.0}, 0);
        }

        final double sqrtDiscrim = Math.sqrt(discrim);
        final double atanFactor = grav * offHor;
        final double angle1 = Math.toDegrees(Math.atan((vSq + sqrtDiscrim) / atanFactor));
        final double angle2 = Math.toDegrees(Math.atan((vSq - sqrtDiscrim) / atanFactor));

        double angle = angle1 >= 45.0 ? angle1 : angle2 >= 45.0 ? angle2 : -1.0;

        if (angle < 0.0) {
            return new PearlResult(new double[]{0.0, 10.0, 0.0}, 0);
        }

        double dragAng = 1.0;
        if (offHor < 45 && offHor > 36) {
            dragAng = 0.982;
        } else if (offHor < 10) {
            dragAng = 1.0;
        } else if (offHor < 40 && offHor >= 28) {
            dragAng = 1.033 + (((offHor - 28) / (12)) * (-0.033));
        } else if (offHor < 28) {
            dragAng = 1.026 + (((offHor - 10) / (18)) * (-0.017));
        } else {
            dragAng = 1.0 + (((offHor - 40) / (15)) * (-0.12));
        }
        angle *= dragAng;

        final double pitch = -angle;
        final double radP = Math.toRadians(pitch);
        final double radY = -Math.atan2(offX, offZ);

        final double vY = eVel * Math.sin(Math.toRadians(angle));
        final double flightTimeFactor = Math.pow(1.0015, Math.max(offHor / 15, 1)) * 0.8;
        double fT = (vY + Math.sqrt(vY * vY + 2 * grav * (playerY + 1.62 - target[1]))) / grav;
        int timing = (int) (Math.floor((fT / Math.pow(0.992, fT)) * flightTimeFactor)) - 2;

        final double cosRadP = Math.cos(radP);
        final double fX = cosRadP * Math.sin(radY);
        final double fY = -Math.sin(radP);
        final double fZ = cosRadP * Math.cos(radY);

        final double targetX = playerX - fX * 10;
        final double targetY = playerY + fY * 10;
        final double targetZ = playerZ + fZ * 10;

        return new PearlResult(new double[]{targetX, targetY, targetZ}, timing);
    }

    public static PreLocation getPreLocation(String missing) {
        double[] pos = {
                Minecraft.getMinecraft().thePlayer.posX,
                Minecraft.getMinecraft().thePlayer.posY,
                Minecraft.getMinecraft().thePlayer.posZ
        };

        if (MathUtils.get3dDistance(pos, new double[]{-73, 79, -135}) <= 5.5) {
            return PreLocation.SHOP;
        }
        if (MathUtils.get3dDistance(pos, new double[]{-87, 79, -127}) <= 4) {
            return PreLocation.SHOP;
        }
        if (MathUtils.get3dDistance(pos, new double[]{-67, 77, -122}) <= 5.5) {
            return PreLocation.TRIANGLE;
        }
        if (MathUtils.get3dDistance(pos, new double[]{-133, 77, -137}) <= 3.5) {
            return PreLocation.X;
        }
        if (
                MathUtils.get3dDistance(pos, new double[]{-134, 78, -128}) <= 4.5
                        || MathUtils.get3dDistance(pos, new double[]{-131, 79, -111}) <= 6
                        || MathUtils.get3dDistance(pos, new double[]{-135, 76, -122}) <= 3.5
        ) {
            return PreLocation.XCANNON;
        }
        if (
                MathUtils.get3dDistance(pos, new double[]{-112, 77, -69}) <= 3.5
                        || MathUtils.get3dDistance(pos, new double[]{-117, 78, -85}) <= 4.5
        ) {
            return PreLocation.SLASH;
        }
        if (
                MathUtils.get3dDistance(pos, new double[]{-65, 76, -87}) <= 3.5
                        || MathUtils.get3dDistance(pos, new double[]{-79, 79, -89}) <= 3.5
        ) {
            return PreLocation.EQUALS;
        }
        if (MathUtils.get3dDistance(pos, new double[]{-140, 77, -88}) <= 4.5) {
            switch (missing) {
                case "slash": return PreLocation.SLASH;
                case "equals": return PreLocation.EQUALS;
                case "triangle": return PreLocation.TRIANGLE;
                case "x": return PreLocation.X;
                case "shop": return PreLocation.SHOP;
                default: return PreLocation.XCANNON;
            }
        }
        return null;
    }

    public static SecondLocation getSecondLocation() {
        double[] pos = {
                Minecraft.getMinecraft().thePlayer.posX,
                Minecraft.getMinecraft().thePlayer.posY,
                Minecraft.getMinecraft().thePlayer.posZ
        };

        if (MathUtils.get3dDistance(pos, new double[]{-67, 77, -122}) <= 5.5) {
            return SecondLocation.SHOP;
        }
        if (MathUtils.get3dDistance(pos, new double[]{-133, 77, -137}) <= 3.5) {
            return SecondLocation.XCANNON;
        }
        if (MathUtils.get3dDistance(pos, new double[]{-112, 77, -69}) <= 3.5) {
            return SecondLocation.SQUARE;
        }
        if (MathUtils.get3dDistance(pos, new double[]{-65, 76, -87}) <= 3.5) {
            return SecondLocation.SHOP;
        }
        return null;
    }

    public static int getSupplyTime(int tali, String tier) {
        double amount = 1.0;
        switch (tali) {
            case 1:
                amount = 0.9;
                break;
            case 2:
                amount = 0.8;
                break;
            case 3:
                amount = 0.7;
                break;
        }

        switch (tier) {
            case "Basic":
                return MathUtils.roundUpToFive(60 * amount);
            case "Hot":
                return MathUtils.roundUpToFive(80 * amount);
            case "Burning":
                return MathUtils.roundUpToFive(100 * amount);
            case "Fiery":
                return MathUtils.roundUpToFive(120 * amount);
            case "Infernal":
                return MathUtils.roundUpToFive(140 * amount);
            default:
                return MathUtils.roundUpToFive(140 * amount);
        }
    }
}