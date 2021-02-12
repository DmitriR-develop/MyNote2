package com.dmitri.mynote2;

import java.util.Random;

public class PictureIndexConverter {

    private static Random rnd = new Random();
    private static Object syncObj = new Object();

    private static int[] picIndex = {R.drawable.im1,
            R.drawable.im2,
            R.drawable.im3,
            R.drawable.im4,
            R.drawable.im5};

    public static int randomPictureIndex() {
        synchronized (syncObj) {
            return rnd.nextInt(picIndex.length);
        }
    }

    public static int getPictureByIndex(int index) {
        if (index < 0 || index >= picIndex.length) {
            index = 0;
        }
        return picIndex[index];
    }

    public static int getIndexByPicture(int picture) {
        for (int i = 0; i < picIndex.length; i++) {
            if (picIndex[i] == picture) {
                return i;
            }
        }
        return 0;
    }
}
