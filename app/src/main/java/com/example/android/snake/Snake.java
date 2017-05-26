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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Snake: a simple game that everyone can enjoy.
 * 
 * This is an implementation of the classic Game "Snake", in which you control a
 * serpent roaming around the garden looking for apples. Be careful, though,
 * because when you catch one, not only will you become longer, but you'll move
 * faster. Running into yourself or the walls will end the game.
 * 
 */
public class Snake extends Activity {

	private SnakeView mSnakeView;

	private Button mStart;

	private static String ICICLE_KEY = "snake-view";

	/**
	 * Called when Activity is first created. Turns off the title bar, sets up
	 * the content views, and fires up the SnakeView.
	 * 
	 */
	public final static String ACTION_EXIT_APP = "cn.edu.zafu.leakcanary.exit";
	private static LocalBroadcastManager mLocalBroadcatManager;
	private BroadcastReceiver mExitReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ACTION_EXIT_APP)) {
				Log.d("TAG", "exit from broadcast");
				finish();
			}
		}
	};
	private void init() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_EXIT_APP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		getLocalBroadcastManager().registerReceiver(mExitReceiver, filter);
	}

	private  LocalBroadcastManager getLocalBroadcastManager() {
		if (mLocalBroadcatManager == null) {
			mLocalBroadcatManager = LocalBroadcastManager.getInstance(this);
		}
		return mLocalBroadcatManager;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.snake_layout);

		mSnakeView = (SnakeView) findViewById(R.id.snake);

		mSnakeView.setTextView((TextView) findViewById(R.id.text));
		mSnakeView.setStartButton((Button) findViewById(R.id.start));
		mSnakeView.setControlButton((Button) findViewById(R.id.left), (Button) findViewById(R.id.right),
				(Button) findViewById(R.id.top), (Button) findViewById(R.id.bottom));

		// 判断下数据是否有保存，没有的话，就重新开始游戏
		if (savedInstanceState == null) {
			// We were just launched -- set up a new game
			mSnakeView.setMode(SnakeView.READY);
		} else {
			// We are being restored
			Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
			if (map != null) {
				mSnakeView.restoreState(map);
			} else {
				mSnakeView.setMode(SnakeView.PAUSE);
			}
		}
		init();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Pause the game along with the activity
		mSnakeView.setMode(SnakeView.PAUSE);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Store the game state
		outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getRefWatcher().watch(this);
	}
}
