/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.snake;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * TileView: a View-variant designed for handling arrays of "icons" or other
 * drawables. 首先tile是有瓦的意思。用一个个tile拼接起来的就是地图。tileview就是用来呈现地图的类
 */
public class TileView extends View {

	/**
	 * Parameters controlling the size of the tiles and their range within view.
	 * Width/Height are in pixels, and Drawables will be scaled to fit to these
	 * dimensions. X/Y Tile Counts are the number of tiles that will be drawn.
	 */

	protected static int mTileSize; // 地图tile的大小。其实就是点的宽和高（是一样的值）

	protected static int mXTileCount;// 地图上x轴能够容纳的tile的数量。下面类似
	protected static int mYTileCount;

	private static int mXOffset;// 地图的起始坐标
	private static int mYOffset;

	/**
	 * A hash that maps integer handles specified by the subclasser to the
	 * drawable that will be used for that reference
	 */
	private Bitmap[] mTileArray;
	// 地图上tile对应的图片数组。每一种tile都对应一个bitmap。比如mTileArray[1]就是草地的bitmap。可以类推。

	/**
	 * A two-dimensional array of integers in which the number represents the
	 * index of the tile that should be drawn at that locations
	 */
	private int[][] mTileGrid;
	// 地图上的tile的数组。比如int[1][1]=0说明这个点是草地。int[1][2]=1说明这个点是苹果
	// 其实思想就是这么简单。方式可以有各种各样

	private final Paint mPaint = new Paint();// 画笔。画图需要笔来画。应该很容易理解。各种笔。黑色。红色。

	public TileView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 这里用到的TypeArray。不懂的童鞋要去google下。是google弄出来的一种样式数组，其实就像定义一个控件的属性的集合。
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);

		mTileSize = a.getInt(R.styleable.TileView_tileSize, 12);

		a.recycle();
	}

	public TileView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);

		mTileSize = a.getInt(R.styleable.TileView_tileSize, 12);

		a.recycle();
	}

	/**
	 * Rests the internal array of Bitmaps used for drawing tiles, and sets the
	 * maximum index of tiles to be inserted
	 * 
	 * @param tilecount
	 */

	public void resetTiles(int tilecount) {
		mTileArray = new Bitmap[tilecount];
	}

	// 个人认为。这个函数是比较有意思的。这个是view的一个回调函数。最开始初始话的时候view的大小都是0。当进行layout之后。每个view都确定了大小。这样就开始回调这个函数。
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mXTileCount = (int) Math.floor(w / mTileSize);
		mYTileCount = (int) Math.floor(h / mTileSize);

		mXOffset = ((w - (mTileSize * mXTileCount)) / 2);
		mYOffset = ((h - (mTileSize * mYTileCount)) / 2);

		mTileGrid = new int[mXTileCount][mYTileCount];
		clearTiles();
	}

	/**
	 * Function to set the specified Drawable as the tile for a particular
	 * integer key.
	 * 
	 * @param key
	 * @param tile
	 *            这函数就是根据Key代表tile种类。来加载地图tile的图片（这里是一个drawable。要变成bitmap）
	 */
	public void loadTile(int key, Drawable tile) {
		Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, mTileSize, mTileSize);
		tile.draw(canvas);

		mTileArray[key] = bitmap;
	}

	/**
	 * Resets all tiles to 0 (empty)
	 * 
	 */
	public void clearTiles() {
		for (int x = 0; x < mXTileCount; x++) {
			for (int y = 0; y < mYTileCount; y++) {
				setTile(0, x, y);
			}
		}
	}

	/**
	 * Used to indicate that a particular tile (set with loadTile and referenced
	 * by an integer) should be drawn at the given x/y coordinates during the
	 * next invalidate/draw cycle.
	 * 
	 * @param tileindex
	 * @param x
	 * @param y
	 *  这边就是设置每一个tile（地图上的点）对应的是哪一种图片。这里的tileindex就是代表了mTileArray[]中的index
	 */
	public void setTile(int tileindex, int x, int y) {
		mTileGrid[x][y] = tileindex;
	}

	// 这个函数就是画出地图了。遍历地图的点，然后把每个tile的坐标都计算出来，然后一个个的tile都draw到canvas上哈
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int x = 0; x < mXTileCount; x += 1) {
			for (int y = 0; y < mYTileCount; y += 1) {
				if (mTileGrid[x][y] > 0) {
					canvas.drawBitmap(mTileArray[mTileGrid[x][y]], mXOffset + x * mTileSize, mYOffset + y * mTileSize, mPaint);
				}
			}
		}

	}
}
