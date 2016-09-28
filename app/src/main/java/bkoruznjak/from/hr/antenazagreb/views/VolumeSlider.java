package bkoruznjak.from.hr.antenazagreb.views;

/**
 * Created by bkoruznjak on 01/07/16.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import bkoruznjak.from.hr.antenazagreb.R;

public class VolumeSlider extends View {

    private final float SECTOR_MAX_ANGLE = (float) Math.PI;
    private final float SECTOR_MIN_ANGLE = 0F;
    private final int NUMBER_OF_SECTORS = 8;
    private final float SECTOR_ANGLE = SECTOR_MAX_ANGLE / NUMBER_OF_SECTORS;
    private final float[] SECTOR_ANGLES = {0.0F
            , SECTOR_ANGLE * 1
            , SECTOR_ANGLE * 2
            , SECTOR_ANGLE * 3
            , SECTOR_ANGLE * 4
            , SECTOR_ANGLE * 5
            , SECTOR_ANGLE * 6
            , SECTOR_ANGLE * 7
            , SECTOR_ANGLE * 8};
    private int mThumbX;
    private int mThumbY;
    private int mCircleCenterX;
    private int mCircleCenterY;
    private int mCircleRadius;
    private Drawable mThumbImage;
    private int mPadding;
    private int mThumbSize;
    private int mThumbColor;
    private int mBorderColor;
    private int mBorderThickness;
    private double mStartAngle;
    private double mAngle = mStartAngle;
    private boolean mIsThumbSelected = false;
    private Paint mPaint = new Paint();
    private Paint mVolumeTextPaint = new Paint();
    private OnSliderMovedListener mListener;
    private OnSectorChangedListener mSectorListener;
    //SECTORS
    private int numberOfSectorsPerPI = 8;
    private int sectorID = 8;
    private boolean[] sectorLocatorArray = {
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
    };
    public VolumeSlider(Context context) {
        this(context, null);
    }

    public VolumeSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public VolumeSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VolumeSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    // common initializer method
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VolumeSlider, defStyleAttr, 0);

        // read all available attributes
        float startAngle = a.getFloat(R.styleable.VolumeSlider_start_angle, (float) Math.PI / 2);
        float angle = a.getFloat(R.styleable.VolumeSlider_angle, (float) Math.PI / 2);
        int thumbSize = a.getDimensionPixelSize(R.styleable.VolumeSlider_thumb_size, 50);
        int thumbColor = a.getColor(R.styleable.VolumeSlider_thumb_color, Color.GRAY);
        int borderThickness = a.getDimensionPixelSize(R.styleable.VolumeSlider_border_thickness, 20);
        int borderColor = a.getColor(R.styleable.VolumeSlider_border_color, Color.RED);
        Drawable thumbImage = a.getDrawable(R.styleable.VolumeSlider_thumb_image);

        // save those to fields (really, do we need setters here..?)
        setStartAngle(startAngle);
        setAngle(angle);
        setBorderThickness(borderThickness);
        setBorderColor(borderColor);
        setThumbSize(thumbSize);
        setThumbImage(thumbImage);
        setThumbColor(thumbColor);

        // assign padding - check for version because of RTL layout compatibility
        int padding;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int all = getPaddingLeft() + getPaddingRight() + getPaddingBottom() + getPaddingTop() + getPaddingEnd() + getPaddingStart();
            padding = all / 6;
        } else {
            padding = (getPaddingLeft() + getPaddingRight() + getPaddingBottom() + getPaddingTop()) / 4;
        }
        setPadding(padding);

        a.recycle();
    }

    private void updateSectorPosition(int sector) {
        //retard safety check
        if (sectorLocatorArray.length < sector) {
            return;
        }
        for (int index = 0; index < sectorLocatorArray.length; index++) {
            sectorLocatorArray[index] = index == sector - 1;
        }
    }

    /* ***** Setters ***** */
    public void setStartAngle(double startAngle) {
        mStartAngle = startAngle;
    }

    public void setAngle(double angle) {
        mAngle = angle;
    }

    public void setThumbSize(int thumbSize) {
        mThumbSize = thumbSize;
    }

    public void setBorderThickness(int circleBorderThickness) {
        mBorderThickness = circleBorderThickness;
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
    }

    public void setThumbImage(Drawable drawable) {
        mThumbImage = drawable;
    }

    public void setThumbColor(int color) {
        mThumbColor = color;
    }

    public void setPadding(int padding) {
        mPadding = padding;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // use smaller dimension for calculations (depends on parent size)
        int smallerDim = w > h ? h : w;

        // find circle's rectangle points
        int largestCenteredSquareLeft = (w - smallerDim) / 2;
        int largestCenteredSquareTop = (h - smallerDim) / 2;
        int largestCenteredSquareRight = largestCenteredSquareLeft + smallerDim;
        int largestCenteredSquareBottom = largestCenteredSquareTop + smallerDim;

        // save circle coordinates and radius in fields
        mCircleCenterX = largestCenteredSquareRight / 2 + (w - largestCenteredSquareRight) / 2;
        mCircleCenterY = largestCenteredSquareBottom / 2 + (h - largestCenteredSquareBottom) / 2;
        mCircleRadius = smallerDim / 2 - mBorderThickness / 2 - mPadding;

        // works well for now, should we call something else here?
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // outer circle (ring)
        mPaint.setColor(mBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderThickness);
        mPaint.setAntiAlias(true);

        mVolumeTextPaint.setColor(Color.WHITE);
        mVolumeTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mVolumeTextPaint.setStrokeWidth(1.0f);
        mVolumeTextPaint.setAntiAlias(true);
        mVolumeTextPaint.setTextSize(25.0f);
        mVolumeTextPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mPaint);

        // find thumb position
        mThumbX = (int) (mCircleCenterX + mCircleRadius * Math.cos(mAngle));
        mThumbY = (int) (mCircleCenterY - mCircleRadius * Math.sin(mAngle));


        if (mThumbImage != null) {
            // draw png
            mThumbImage.setBounds(mThumbX - mThumbSize / 2, mThumbY - mThumbSize / 2, mThumbX + mThumbSize / 2, mThumbY + mThumbSize / 2);
            mThumbImage.draw(canvas);
            canvas.drawText("" + sectorID, mThumbX + 15f, mThumbY, mVolumeTextPaint);
        } else {
            // draw colored circle
            mPaint.setColor(mThumbColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mThumbX, mThumbY, mThumbSize, mPaint);
            canvas.drawText("" + sectorID, mThumbX, mThumbY + 15f, mVolumeTextPaint);
        }
    }

    public void updateSliderWithSectorID(int sectorID) {
        Log.d("bbb", "updating slider angle with sector id:" + sectorID);
        updateSectorPosition(sectorID);
        changeSector(sectorID);
        double sectorPICoef = Math.PI / numberOfSectorsPerPI;
        if (sectorID >= 1 && sectorID <= 8) {
            // od 0 do 3.14
            setAngle(sectorPICoef * sectorID);
        } else {
            // od -3.14 do 0
            setAngle(-sectorPICoef * ((numberOfSectorsPerPI * 2) - sectorID));
        }
        invalidate();
    }

    /**
     * Invoked when slider starts moving or is currently moving. This method calculates and sets position and angle of the thumb.
     * Method returns id of sector in which the thumbDialer is currently in
     *
     * @param touchX Where is the touch identifier now on X axis
     * @param touchY Where is the touch identifier now on Y axis
     */
    private void updateSliderState(int touchX, int touchY) {
        int distanceX = touchX - mCircleCenterX;
        int distanceY = mCircleCenterY - touchY;
        //noinspection SuspiciousNameCombination
        double c = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        mAngle = Math.acos(distanceX / c);
        boolean isTopSide = true;
        if (distanceY < 0) {
            mAngle = -mAngle;
            isTopSide = false;
        }

        if (isTopSide) {
            if (!sectorLocatorArray[0] && mAngle < SECTOR_ANGLES[1] && mAngle > SECTOR_ANGLES[0]) {
                updateSectorPosition(1);
                changeSector(1);
            } else if (!sectorLocatorArray[1] && mAngle < SECTOR_ANGLES[2] && mAngle > SECTOR_ANGLES[1]) {
                updateSectorPosition(2);
                changeSector(2);
            } else if (!sectorLocatorArray[2] && mAngle < SECTOR_ANGLES[3] && mAngle > SECTOR_ANGLES[2]) {
                updateSectorPosition(3);
                changeSector(3);
            } else if (!sectorLocatorArray[3] && mAngle < SECTOR_ANGLES[4] && mAngle > SECTOR_ANGLES[3]) {
                updateSectorPosition(4);
                changeSector(4);
            } else if (!sectorLocatorArray[4] && mAngle < SECTOR_ANGLES[5] && mAngle > SECTOR_ANGLES[4]) {
                updateSectorPosition(5);
                changeSector(5);
            } else if (!sectorLocatorArray[5] && mAngle < SECTOR_ANGLES[6] && mAngle > SECTOR_ANGLES[5]) {
                updateSectorPosition(6);
                changeSector(6);
            } else if (!sectorLocatorArray[6] && mAngle < SECTOR_ANGLES[7] && mAngle > SECTOR_ANGLES[6]) {
                updateSectorPosition(7);
                changeSector(7);
            } else if (!sectorLocatorArray[7] && mAngle < SECTOR_ANGLES[8] && mAngle > SECTOR_ANGLES[7]) {
                updateSectorPosition(8);
                changeSector(8);
            }
        } else {
            if (!sectorLocatorArray[15] && -mAngle < SECTOR_ANGLES[1] && -mAngle > SECTOR_ANGLES[0]) {
                updateSectorPosition(16);
                changeSector(16);
            } else if (!sectorLocatorArray[14] && -mAngle < SECTOR_ANGLES[2] && -mAngle > SECTOR_ANGLES[1]) {
                updateSectorPosition(15);
                changeSector(15);
            } else if (!sectorLocatorArray[13] && -mAngle < SECTOR_ANGLES[3] && -mAngle > SECTOR_ANGLES[2]) {
                updateSectorPosition(14);
                changeSector(14);
            } else if (!sectorLocatorArray[12] && -mAngle < SECTOR_ANGLES[4] && -mAngle > SECTOR_ANGLES[3]) {
                updateSectorPosition(13);
                changeSector(13);
            } else if (!sectorLocatorArray[11] && -mAngle < SECTOR_ANGLES[5] && -mAngle > SECTOR_ANGLES[4]) {
                updateSectorPosition(12);
                changeSector(12);
            } else if (!sectorLocatorArray[10] && -mAngle < SECTOR_ANGLES[6] && -mAngle > SECTOR_ANGLES[5]) {
                updateSectorPosition(11);
                changeSector(11);
            } else if (!sectorLocatorArray[9] && -mAngle < SECTOR_ANGLES[7] && -mAngle > SECTOR_ANGLES[6]) {
                updateSectorPosition(10);
                changeSector(10);
            } else if (!sectorLocatorArray[8] && -mAngle < SECTOR_ANGLES[8] && -mAngle > SECTOR_ANGLES[7]) {
                updateSectorPosition(9);
                changeSector(9);
            }
        }

        if (mListener != null) {
            // notify slider moved listener of the new position which should be in [0..1] range
            mListener.onSliderMoved((mAngle - mStartAngle) / (2 * Math.PI));
        }
    }

    public void changeSector(int sectorID) {
        this.sectorID = sectorID;
        if (mSectorListener != null) {
            mSectorListener.changeSector(sectorID);
        }
    }

    /**
     * Position setter. This method should be used to manually position the slider thumb.<br>
     * Note that counterclockwise {@link #mStartAngle} is used to determine the initial thumb position.
     *
     * @param pos Value between 0 and 1 used to calculate the angle. {@code Angle = StartingAngle + pos * 2 * Pi}<br>
     *            Note that angle will not be updated if the position parameter is not in the valid range [0..1]
     */
    public void setPosition(double pos) {
        if (pos >= 0 && pos <= 1) {
            mAngle = mStartAngle + pos * 2 * Math.PI;
        }
    }

    /**
     * Saves a new slider sector listner. Set {@link VolumeSlider.OnSectorChangedListener} to {@code null} to remove it.
     *
     * @param listener Instance of the slider sector listener, or null when removing it
     */
    public void setOnSectorChangedListener(OnSectorChangedListener listener) {
        mSectorListener = listener;
    }

    /**
     * Saves a new slider moved listner. Set {@link VolumeSlider.OnSliderMovedListener} to {@code null} to remove it.
     *
     * @param listener Instance of the slider moved listener, or null when removing it
     */
    public void setOnSliderMovedListener(OnSliderMovedListener listener) {
        mListener = listener;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // start moving the thumb (this is the first touch)
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (x < mThumbX + mThumbSize && x > mThumbX - mThumbSize && y < mThumbY + mThumbSize && y > mThumbY - mThumbSize) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mIsThumbSelected = true;
                    updateSliderState(x, y);
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // still moving the thumb (this is not the first touch)
                if (mIsThumbSelected) {
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    updateSliderState(x, y);
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                getParent().requestDisallowInterceptTouchEvent(false);
                // finished moving (this is the last touch)
                mIsThumbSelected = false;
                break;
            }
        }

        // redraw the whole component
        invalidate();
        return true;
    }

    /**
     * Listener interface used to detect when slider changes sector.
     */
    public interface OnSectorChangedListener {

        /**
         * This method is invoked when slider changes sector.
         *
         * @param sectorID Value between 1 and 16 representing the current sector where the slider
         *                 is located
         */
        void changeSector(int sectorID);
    }

    /**
     * Listener interface used to detect when slider moves around.
     */
    public interface OnSliderMovedListener {

        /**
         * This method is invoked when slider moves, providing position of the slider thumb.
         *
         * @param pos Value between 0 and 1 representing the current angle.<br>
         *            {@code pos = (Angle - StartingAngle) / (2 * Pi)}
         */
        void onSliderMoved(double pos);
    }
}