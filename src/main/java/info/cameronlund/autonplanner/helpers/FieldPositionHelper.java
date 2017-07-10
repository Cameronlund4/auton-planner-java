package info.cameronlund.autonplanner.helpers;

import info.cameronlund.autonplanner.starstruck.gameobjects.Cube;
import info.cameronlund.autonplanner.starstruck.gameobjects.Star;

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
}
