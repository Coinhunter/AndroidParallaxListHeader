package com.springworks.parallaxview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

public class ParallaxImage {
	private ImageView imageView;
	private ListView listView;
	private Context context;
	
	// The operational mode of the scrolllist. 
	private int mode;
	
	// The width of the screen.
	private int displayWidth;
	
	// The current scroll value for the listView.
	private int currentScroll;
	
	public enum Modes {
		NONE,
		FOLD,
		BELOW
	}	
	
	public ParallaxImage(ImageView headerImage, ListView listView, Context context, Modes mode) {
		this.listView = listView;
		this.imageView = headerImage;
		this.context = context;
		this.mode = mode.ordinal();
		
		setDisplayWidth();
		setImageHeight();
		adjustHeaderImageMatrix(0);
		rescaleHeaderImage();
		setScrollListener();
	}

	private void setScrollListener() {
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView list, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (list != null) {
					if (list.getChildAt(0) != null) {
						if (currentScroll != list.getChildAt(0).getTop()) {
							int firstVisible = list.getFirstVisiblePosition();
							if (firstVisible == 0) {
								currentScroll = list.getChildAt(0).getTop();
								adjustHeaderImageMatrix(currentScroll);
							}
						}
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView list, int scrollState) {
			}			
		});
	}

	// Extracts the screen width and sets the display width variable.
	// The displayWidth is then used when scaling the image up and down.
	private void setDisplayWidth() {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		displayWidth = metrics.widthPixels;
	}
	
	// Once we scale the image up and down the old height of the imageview
	// does not scale with it, so we have to handle that initial rescale
	// ourselves.
	private void setImageHeight() {
		BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
		if (bd != null) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, (int) (bd.getIntrinsicHeight() * getHeaderImageScale()));
			imageView.setLayoutParams(layoutParams);
		}
	}

	// The scale is calculated as the quotient between
	// the display width and the width of the bitmap file.
	private float getHeaderImageScale() {
		BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
		float scale = 1;
		if (bd != null) {
			int bitmapWidth = bd.getBitmap().getWidth();
			scale = (float) displayWidth / (float) bitmapWidth;
		}
		return scale;
	}

	//Used to recalibrate sizes of imageviews that have loaded placeholders
	private void rescaleHeaderImage() {
		float scale = getHeaderImageScale();
		imageView.setScaleType(ScaleType.MATRIX);
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		imageView.setImageMatrix(matrix);
	}

	// Adjust the image matrix in such a way that the bitmap fills the entire image
	// horizontally and is centered vertically based on how much of the image that
	// is visible.
	public void adjustHeaderImageMatrix(int scroll) {
		float scale = getHeaderImageScale();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		
		if (this.mode == Modes.NONE.ordinal()) {
			//Do nothing. This image will behave as any other.
		} else if (this.mode == Modes.FOLD.ordinal()) {
			matrix.postTranslate(0, -scroll / 2);
		} else if (this.mode == Modes.BELOW.ordinal()) {
			matrix.postTranslate(0, -scroll);
		}

		imageView.setScaleType(ScaleType.MATRIX);
		imageView.setImageMatrix(matrix);
	}	
	
}
