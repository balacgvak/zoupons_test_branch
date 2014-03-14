package com.us.zoupons.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class TransitionAnimation extends Animation{

	private final float mFromdegree,mTodegree,mCenterX,mCenterY,mDepthZ;
	private boolean mIsReversable;
	private Camera mCamera;
	public TransitionAnimation(float fromdegree,float todegree,float centerX,float centerY,float depthZ,boolean reversable) {
		mFromdegree =fromdegree;
		mTodegree = todegree;
		mCenterX = centerX;
		mCenterY =centerY;
		mDepthZ = depthZ;
		mIsReversable = reversable;
	}
	
	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		final float fromDegrees = mFromdegree;
		float mDegree = fromDegrees+((mTodegree-fromDegrees)*interpolatedTime);
		final Camera camera=mCamera;
		final Matrix matrix = t.getMatrix();
		camera.save();
		if (mIsReversable) {
        	camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
        } else {
        	camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(mDegree);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
	}
}