package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * ShakeListener
 * Built on code from
 * http://www.javacodegeeks.com/2014/04/android-shake-to-refresh-tutorial.html
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

import static android.hardware.SensorManager.*;

public class ShakeListener implements SensorEventListener {
	private static final int FORCE_THRESHOLD = 500;
	private static final int TIME_THRESHOLD = 100;
	private static final int SHAKE_TIMEOUT = 500;
	private static final int SHAKE_DURATION = 1000;
	private static final int SHAKE_COUNT = 0;
	private SensorManager sManager;
	private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
	private long mLastTime;
	private OnShakeListener mShakeListener;
	private Context mContext;
	private int mShakeCount = 4;
	private long mLastShake;
	private long mLastForce;
	private Sensor sensor;


	@Override
	public void onSensorChanged(SensorEvent event) {

		long now = System.currentTimeMillis();

		if ((now - mLastForce) > SHAKE_TIMEOUT) {
			mShakeCount = 0;
		}
		if ((now - mLastTime) > TIME_THRESHOLD) {
			long diff = now - mLastTime;
			float speed = Math.abs(event.values[0]
					+ event.values[1] + event.values[2] - mLastX - mLastY - mLastZ)
					/ diff * 10000;
			if (speed > FORCE_THRESHOLD) {
				if ((++mShakeCount >= SHAKE_COUNT)
						&& (now - mLastShake > SHAKE_DURATION)) {
					mLastShake = now;
					mShakeCount = 0;
					if (mShakeListener != null) {
						mShakeListener.onShake();
					}
				}
				mLastForce = now;
			}
			mLastTime = now;
			mLastX = event.values[0];
			mLastY = event.values[1];
			mLastZ = event.values[2];
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//N/A
	}

	public interface OnShakeListener {
		public void onShake();
	}

	public ShakeListener(Context context) {
		this.mContext = context;
		sManager = (SensorManager)  context.getSystemService(Context.SENSOR_SERVICE);
		sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		resume();
	}

	public void setOnShakeListener(OnShakeListener listener) {
		mShakeListener = listener;
	}

	public void resume() {
			sManager = (SensorManager)  mContext.getSystemService(Context.SENSOR_SERVICE);
			sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sManager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_UI );

	}

	public void pause() {
		if (sManager != null) {
			sManager.unregisterListener(this,sensor);
			sManager = null;
		}
	}


}
