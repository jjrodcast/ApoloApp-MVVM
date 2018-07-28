/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.view.decorator

import android.content.Context
import android.graphics.Rect
import android.support.annotation.IntegerRes
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View

class MarginDecorator(val context: Context, @IntegerRes val offset: Int) : RecyclerView.ItemDecoration() {

    private var mItemOffset: Int

    init {
        val itemOffsetDp = context.resources.getInteger(offset)
        mItemOffset = convertDpToPixel(itemOffsetDp, context.resources.displayMetrics)
    }

    private fun convertDpToPixel(offsetDpValue: Int, metrics: DisplayMetrics): Int {
        return offsetDpValue * (metrics.densityDpi / 160)
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect?.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }


}