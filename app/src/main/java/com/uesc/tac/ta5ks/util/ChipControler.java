package com.uesc.tac.ta5ks.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.uesc.tac.ta5ks.R;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;

/**
 * Created by levy on 12/05/18.
 */

public class ChipControler {
    public static void createChips(Context context, String title, int color, FlexboxLayout flexbox){
        ChipCloud chipCloud;

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.single)
                .checkedChipColor(Color.parseColor("#AAA5CF"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#efefef"))
                .uncheckedTextColor(Color.parseColor("#666666"))
                .useInsetPadding(true);

        //Create a new ChipCloud with a Context and ViewGroup:
        chipCloud = new ChipCloud(context, flexbox, config);

        //Add a single Chip:
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_tag);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC);
        chipCloud.addChip(title, drawable);
    }
}
