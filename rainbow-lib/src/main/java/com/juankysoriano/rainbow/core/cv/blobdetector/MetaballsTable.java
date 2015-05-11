package com.juankysoriano.rainbow.core.cv.blobdetector;

//==================================================
//class MetaballsTable
//==================================================
public class MetaballsTable {

    // EdgeToCompute Array
    public static int edgeToCompute[] = {0, 3, 1, 2, 0, 3, 1, 2, 2, 1, 3, 0, 2, 1, 3, 0};

    // neightborVoxel Array
// bit 0 : X+1
// bit 1 : X-1
// bit 2 : Y+1
// bit 3 : Y-1
    public static byte neightborVoxel[] = {0, 10, 9, 3, 5, 15, 12, 6, 6, 12, 12, 5, 3, 9, 10, 0};

}
