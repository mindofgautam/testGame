package com.example.game1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends Activity {
	private MySurfaceView mySurfaceView;
	private int height;
	private int width;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mySurfaceView = new MySurfaceView(this);
		setContentView(mySurfaceView);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;
		height = height - 100;
		width = width - 10;

	}

	@Override
	protected void onResume() {
		super.onResume();
		mySurfaceView.onResumeMySurfaceView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mySurfaceView.onPauseMySurfaceView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	class MySurfaceView extends SurfaceView implements Runnable,
			SensorEventListener {

		Thread thread = null;
		SurfaceHolder surfaceHolder;
		volatile boolean running = false;
		int xValue = 300;
		int yValue = 0;
		int speed = 1;
		int MaxSpeed = 0;
		int moveCounter = 0;

		private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		private SensorManager mSensorManager;
		private Sensor mAccelerometer;

		public MySurfaceView(Context context) {
			super(context);
			surfaceHolder = getHolder();
			mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			mAccelerometer = mSensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_NORMAL);
		}

		public void onResumeMySurfaceView() {
			running = true;
			thread = new Thread(this);
			thread.start();
		}

		public void onPauseMySurfaceView() {
			boolean retry = true;
			running = false;
			while (retry) {
				try {
					thread.join();
					retry = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {

			boolean rightFlag = true;
			boolean toptFlag = true;
			paint.setColor(Color.WHITE);
			while (running) {
				if (surfaceHolder.getSurface().isValid()) {
					Canvas canvas = surfaceHolder.lockCanvas();
					canvas.drawColor(Color.WHITE);
					if (moveCounter < 10) {
						if (xValue >= 0 && xValue <= width) {
							if (rightFlag == true) {
								xValue = xValue + MaxSpeed;
								if (xValue >= width) {
									rightFlag = false;
									moveCounter++;
									xValue = width;
								}
							}
							if (rightFlag == false) {
								xValue = xValue - MaxSpeed;
								if (xValue <= 0) {
									rightFlag = true;
									moveCounter++;
									xValue = 0;
								}
							}
						}

						if (yValue >= 0 && yValue <= height) {
							if (toptFlag == true) {
								yValue = yValue + MaxSpeed;
								if (yValue >= height) {
									toptFlag = false;
									moveCounter++;
									yValue = height;
								}
							}
							if (toptFlag == false) {
								yValue = yValue - MaxSpeed;
								if (yValue <= 0) {
									toptFlag = true;
									moveCounter++;
									yValue = 0;
								}
							}
						}

					}
					if (moveCounter >= 10 && moveCounter <= 20) {

						if (xValue >= 0 && xValue <= width) {
							if (rightFlag == false) {
								xValue = xValue + MaxSpeed;
								if (xValue >= width) {
									rightFlag = true;
									moveCounter++;
									xValue = width;
								}
							}
							if (rightFlag == true) {
								xValue = xValue - MaxSpeed;
								if (xValue <= 0) {
									rightFlag = false;
									moveCounter++;
									xValue = 0;
								}
							}
						}

						if (yValue >= 0 && yValue <= height) {
							if (toptFlag == false) {
								yValue = yValue + MaxSpeed;
								if (yValue >= height) {
									toptFlag = true;
									moveCounter++;
									yValue = height;
								}
							}
							if (toptFlag == true) {
								yValue = yValue - MaxSpeed;
								if (yValue <= 0) {
									toptFlag = false;
									moveCounter++;
									yValue = 0;
								}
							}
						}

					}
					paint.setColor(Color.BLACK);
					canvas.drawCircle(xValue, yValue, 20, paint);
					canvas.drawRect(280, 35, 320, 150, paint);
					canvas.drawRect(280, 35, 260, 55, paint);
					canvas.drawRect(320, 35, 340, 55, paint);
					paint.setStrokeWidth(15);
					if (moveCounter >= 21) {
						canvas.drawColor(Color.WHITE);
						canvas.drawCircle(300, 0, 20, paint);
						canvas.drawRect(280, 35, 320, 150, paint);
						canvas.drawRect(280, 35, 260, 55, paint);
						canvas.drawRect(280, 35, 320, 150, paint);
						canvas.drawRect(280, 35, 320, 150, paint);
						canvas.drawRect(280, 35, 260, 55, paint);
						canvas.drawRect(320, 35, 340, 55, paint);

					}
					surfaceHolder.unlockCanvasAndPost(canvas);
				
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			double x = event.values[0];
			double y = event.values[1];
			speed = (int) Math.sqrt((x * x) + (y * y));
			if (MaxSpeed < speed) {
				MaxSpeed = speed;
			}

		}
	}
}
