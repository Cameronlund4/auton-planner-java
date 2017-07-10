package info.cameronlund.autonplanner.helpers;

import info.cameronlund.autonplanner.implementations.itz.gameobjects.Cone;
import info.cameronlund.autonplanner.implementations.itz.gameobjects.MobileGoal;
import info.cameronlund.autonplanner.implementations.starstruck.gameobjects.Cube;
import info.cameronlund.autonplanner.implementations.starstruck.gameobjects.Star;

public class FieldPositionHelper {
    public static void setStarPositions(Star[] stars) {
        for (Star star : stars)
            star.setOnField(true);
        // Pre load
        stars[0].setRestingReturn(515, 630); // Pre load, put on start tile

        // Bottom, by the wall
        stars[1].setRestingReturn(57, 630); // Bottom left corner

        stars[2].setRestingReturn(286, 630); // Bottom middle left
        stars[3].setRestingReturn(343, 630); // Bottom middle center
        stars[4].setRestingReturn(401, 630); // Bottom middle right

        stars[5].setRestingReturn(630, 630); // Bottom right corner

        // On the fence (y of 343)
        stars[6].setRestingReturn(47, 343); // Left group left
        stars[7].setRestingReturn(114, 343); // Left group middle
        stars[8].setRestingReturn(182, 343); // Left group right

        stars[9].setRestingReturn(286 - 20, 343); // Middle group left
        stars[10].setRestingReturn(286 + 32, 343); // Middle group middle left
        stars[11].setRestingReturn(401 - 32, 343); // Middle group middle right
        stars[12].setRestingReturn(401 + 20, 343); // Middle group right

        stars[13].setRestingReturn(505, 343); // Right group left
        stars[14].setRestingReturn(572, 343); // Right group middle
        stars[15].setRestingReturn(640, 343); // Right group right

        // ---------- Only skills existent ----------
        // Game loads
        stars[16].setRestingReturn(515, 630); // Game load, put on start tile
        stars[16].setOnField(false);
        stars[17].setRestingReturn(515, 630); // Game load, put on start tile
        stars[17].setOnField(false);
        stars[18].setRestingReturn(515, 630); // Game load, put on start tile
        stars[18].setOnField(false);

        // In front of fence
        stars[19].setRestingReturn(39, 395); // Middle group left

        stars[20].setRestingReturn(286, 395); // Bottom middle left
        stars[21].setRestingReturn(343, 395); // Bottom middle center
        stars[22].setRestingReturn(401, 395); // Bottom middle right

        stars[23].setRestingReturn(647, 395); // Middle group right

        for (int i = 16; i < 24; i++)
            stars[i].setSkillsOnly(true);
    }

    public static void setCubePositions(Cube[] cubes) {
        for (Cube cube : cubes)
            cube.setOnField(true);

        // Middle
        cubes[0].setRestingReturn(343, 515); // Middle of white line

        // ---------- Only skills existent ----------
        // Other starting tile
        cubes[1].setRestingReturn(172, 630); // Other starting tile

        // Game loads
        cubes[2].setRestingReturn(515, 630); // Game load, put on start tile
        cubes[2].setOnField(false);
        cubes[3].setRestingReturn(515, 630); // Game load, put on start tile
        cubes[3].setOnField(false);

        for (int i = 1; i < 4; i++)
            cubes[i].setSkillsOnly(true);
    }

    public static void setConePositions(Cone[] cones) {
        for (Cone cone : cones)
            cone.setOnField(true);

        // Game loads
        // Game loader: 675, 285
        for (int i = 0; i < 12; i++) {
            cones[i].setRestingReturn(675, 285);
            cones[i].setOnField(false);
        }
        cones[0].setOnField(true); // One cone on the loader

        // Pre load
        cones[12].setRestingReturn(570, 570); // Pre load, put in mid of the cone

        // Okay, let's make our lives easy
        // Cones are always spaced in increments of 60
        // They're also all aligned in rows on the field
        // So, let's just generate them in rows top-down

        /*
        ** The top half section
        ** (455, 20), 50 between close rows, 55 between far rows
        */
        generateConeRowLong(cones, 13, 456, 20, 3); // 3
        generateConeRowShort(cones, 16, 620, 20, 2); // 2
        generateConeRowShort(cones, 18, 570, 70, 3); // 3
        //generateConeRowShort(cones, 21, 455, 120, 5); // 5
        generateConeRowLong(cones, 21, 456, 120, 3); // 3
        generateConeRowShort(cones, 24, 620, 120, 2); // 2
        generateConeRowShort(cones, 26, 570, 175, 3, 1); // 2
        generateConeRowShort(cones, 28, 570, 230, 3, 1); // 2
        generateConeRowLong(cones, 30, 400, 235, 2); // 2
        generateConeRowLong(cones, 32, 342, 290, 3, 1); // 2
        //generateConeRowLong(cones, 28, 400, 230, 6, 2, 4); // 4
        //generateConeRowShort(cones, 32, 456, 285, 3, 1); // 2

        /*
        ** Bottom half section
        ** (20, 345), 50 between close rows, 55 between far rows
        */
        generateConeRowShort(cones, 34, 19, 350, 3, 1); // 2
        generateConeRowLong(cones, 36, 285, 350, 3, 1); //2
        generateConeRowShort(cones, 38, 19, 405, 3, 1); // 2
        generateConeRowLong(cones, 40, 173+56, 405, 3, 1); // 2

        generateConeRowShort(cones, 42, 19, 460, 3, 1); // 2
        generateConeRowLong(cones, 44, 173+56, 460, 2); // 2

        generateConeRowShort(cones, 46, 19, 515, 3, 1); // 2
        generateConeRowShort(cones, 48, 19, 570, 3); // 3
        generateConeRowLong(cones, 51, 173, 570, 4); // 4
        generateConeRowShort(cones, 55, 19, 620, 3); // 3
        generateConeRowShort(cones, 58, 19, 670, 3); // 3
        generateConeRowLong(cones, 61, 173, 670, 4); // 4
    }

    private static void generateConeRowShort(final Cone[] cones, int startCone, final int startX, final int y,
                                             final int count, final int... skips) {
        generateConeRow(cones, startCone, startX, y, count, 49, skips);
    }

    private static void generateConeRowLong(final Cone[] cones, int startCone, final int startX, final int y,
                                            final int count, final int... skips) {
        generateConeRow(cones, startCone, startX, y, count, 57, skips);
    }

    private static void generateConeRow(final Cone[] cones, int startCone, final int startX, final int y,
                                        final int count, final int multiplier, final int... skips) {
        drawLoop:
        for (int i = 0; i < count; i++) {
            for (int skip : skips) // If we don't want to draw at this loc, continue
                if (i == skip)
                    continue drawLoop;
            cones[startCone++].setRestingReturn(startX + (i * multiplier), y);
        }
    }

    public static void setMogoPositions(MobileGoal[] mogos) {
        // TODO Implement
    }
}
