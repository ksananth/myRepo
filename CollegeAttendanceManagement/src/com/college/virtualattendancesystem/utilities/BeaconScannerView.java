package com.college.virtualattendancesystem.utilities;

import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.college.collegeAttendancemanagement.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class BeaconScannerView extends View {

	private Context mContext = null;
	private int circleRadius;
	private static final String TAG = "BeaconScannerView";
	private int mStartAngle = 45;
	private int mSweepAngle = 60;
	private Timer timer = null;
	private boolean isAlreadyDrawn = false;
	Bitmap bitmapBeacon = null;
	private int centerCircleY;
	private int centerCircleX;
	private Random random = new Random();
	private ConcurrentHashMap<Integer, BeaconCoord> beacons = new ConcurrentHashMap<Integer, BeaconScannerView.BeaconCoord>();
	private int beaconId = 0;
	Canvas canvas1 ;
	public BeaconScannerView(Context context) {
		super(context);
		init(context);
	}

	public BeaconScannerView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs);
		init(context);
	}

	public BeaconScannerView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public BeaconScannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	Paint circlePaint,linePaint,beaconPaint;
	RectF rect = null;
	Canvas bitmapCanvas = null;

	private void init(Context context) {
		mContext = context;
		timer = new Timer();
		circlePaint = new Paint();
		beaconPaint = new Paint();
		
		linePaint = new Paint();
		linePaint.setColor(mContext.getResources().getColor(
				R.color.divider));
		circlePaint.setColor(mContext.getResources().getColor(
				R.color.primary_light));
		beaconPaint.setColor(Color.BLACK);
		beaconPaint.setTextSize(40);
		beaconPaint.setTextAlign(Align.CENTER);
		bitmapBeacon = BitmapFactory.decodeResource(getResources(),
				R.drawable.beacon);
		bitmapBeacon = Bitmap.createScaledBitmap(bitmapBeacon, 128, 128, false);
		// setBackgroundColor(Color.BLACK);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//Log.i(TAG, "getX() - " + getX());
		//Log.i(TAG, "getY() - " + getY());
		//Log.i(TAG, "getHeight() - " + getHeight());
		//Log.i(TAG, "getWidth() - " + getWidth());
		if (rect == null) {
			rect = new RectF(0, 0, getWidth(), getHeight());
			linePaint.setShader(new LinearGradient(rect.left, rect.top,
					rect.right, rect.bottom, Color.parseColor("#727272"), Color
							.parseColor("#B6B6B6"), Shader.TileMode.CLAMP));
			centerCircleX = getWidth() / 2;
			centerCircleY = getHeight() / 2;
		}

		canvas.drawCircle(centerCircleX, centerCircleY, circleRadius,
				circlePaint);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, 20, linePaint);
		
		//Log.i(TAG, "mStartAngle - " + mStartAngle + ";; mSweepAngle - "
		//		+ mSweepAngle);
		canvas.drawArc(rect, mStartAngle, mSweepAngle, true, linePaint);
		for(Map.Entry<Integer, BeaconCoord> entry : beacons.entrySet()){
			//Log.i(TAG,"drawing beacon - "+entry.getKey() +" @ "+entry.getValue());
//			canvas1 = new Canvas(bitmapBeacon);
//			canvas1.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//			canvas1.drawText(entry.getKey()+"", 60, 60, beaconPaint);
//			canvas1.save();
			canvas.drawText(entry.getValue().name, entry.getValue().x, entry.getValue().y, beaconPaint);
			//canvas.drawBitmap(bitmapBeacon, entry.getValue().x, entry.getValue().y, linePaint);
			//canvas.drawCircle(entry.getValue().x, entry.getValue().y, 5,linePaint);
		}
		
		if (!isAlreadyDrawn) {
			timer.schedule(animateScanViewTask, 0, 10);
			isAlreadyDrawn = true;
		}
		
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		timer.cancel();
		bitmapBeacon.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int measuredWidth = measureWidth(widthMeasureSpec);
		if (circleRadius == 0) // No radius specified.
		{ // Lets see what we can make.
			// Check width size. Make radius half of available.
			circleRadius = measuredWidth / 2;
			int tempRadiusHeight = measureHeight(heightMeasureSpec) / 2;
			if (tempRadiusHeight < circleRadius)
				// Check height, if height is smaller than
				// width, then go half height as radius.
				circleRadius = tempRadiusHeight;
		}
		// RectF(float left, float top, float right, float bottom)
		// circleArc = new RectF(0, 0, circleDiameter, circleDiameter);
		int measuredHeight = measureHeight(heightMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredHeight);
		Log.d("onMeasure() ::",
				"measuredHeight =>" + String.valueOf(measuredHeight)
						+ "px measuredWidth => "
						+ String.valueOf(measuredWidth) + "px");
	}

	private int measureHeight(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 0;
		if (specMode == MeasureSpec.AT_MOST) {
			result = circleRadius * 2;
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	private int measureWidth(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 0;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	class AnimateScan implements Runnable {

		@Override
		public void run() {

		}

	}

	private TimerTask animateScanViewTask = new TimerTask() {

		@Override
		public void run() {
			mStartAngle = (mStartAngle + 1);
			if (mStartAngle >= 360) {
				mStartAngle = mStartAngle % 360;
			}
			
			postInvalidate();
		}
	};

	public boolean isPointInCircle(double x, double y) {
		if (isInRectangle(centerCircleX, centerCircleY, circleRadius, x, y)) {
			double dx = centerCircleX - x;
			double dy = centerCircleY - y;
			dx *= dx;
			dy *= dy;
			double distanceSquared = dx + dy;
			double radiusSquared = circleRadius * circleRadius;
			return distanceSquared <= radiusSquared - (bitmapBeacon.getWidth() * 2); //subtracting beacon image width
		}
		return false;
	}

	public boolean isInsideCircle(double x, double y) {
		double dx = centerCircleX - x;
		double dy = centerCircleY - y;
		double dist =  Math.sqrt(dx * dx + dy * dy);
		if(dist <= circleRadius){
			Log.d("isInsideCircle", "dist - "+dist+";; circleRadius - " +circleRadius);
			return true;
		}
		return false;
	}
	
	boolean isInRectangle(double centerX, double centerY, double radius,
			double x, double y) {
		return x >= centerX - radius && x <= centerX + radius
				&& y >= centerY - radius && y <= centerY + radius;
	}
	
	public int drawNewBeacon(String name){
//		while(true){
//			int x = random.nextInt(getWidth());
//			int y = random.nextInt(getHeight());
//			if(isInsideCircle(x, y)){
//				++beaconId;
//				beacons.put(beaconId, new BeaconCoord(x - bitmapBeacon.getWidth(),y - bitmapBeacon.getHeight()));
//				postInvalidate();
//				return beaconId;
//			}
//		}
		++beaconId;
		beacons.put(beaconId, generateNewCord(name));
		postInvalidate();
		return beaconId;
	}
	
	private BeaconCoord generateNewCord(String name){
		Log.i("NewCoord", "centerCircleX - "+centerCircleX+"; centerCircleY - "+centerCircleY+"; circleRadius - "+circleRadius);
		int angle = random.nextInt(360);
		int dist = random.nextInt(circleRadius - bitmapBeacon.getHeight()-30);
		int x = (int) (centerCircleX + (dist * Math.cos(Math.toRadians(angle))));
		int y = (int) (centerCircleY + (dist * Math.sin(Math.toRadians(angle))) );
		//Log.i("NewCoord", "X - "+x+"; Y - "+y);
		//Log.i("NewCoord", "----------------------------------------------");
		//int y = randInt(centerCircleY, centerCircleY+circleRadius - bitmapBeacon.getHeight());
		return new BeaconCoord(x, y,name);
	}
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public int drawNewBeacon1(int x1, int y1){
//		while(true){
//			
//			if(isInsideCircle(x, y)){
//				++beaconId;
//				beacons.put(beaconId, new BeaconCoord(x - bitmapBeacon.getWidth()*3,y - bitmapBeacon.getHeight()));
//				postInvalidate();
//				return beaconId;
//			}
//		}
		++beaconId;
//		beacons.put(beaconId, generateNewCord());
		postInvalidate();
		return beaconId;
	}
	
	public void removeBeacon(int id){
		beacons.remove(id);
		postInvalidate();
	}
	
	class BeaconCoord{
		int x;
		int y;
		String name;
		public BeaconCoord(int x, int y,String name) {
			super();
			this.x = x;
			this.y = y;
			this.name = name;
		}
		@Override
		public String toString() {
			return "[x=" + x + ", y=" + y + "]";
		}
		
		
	}
}

