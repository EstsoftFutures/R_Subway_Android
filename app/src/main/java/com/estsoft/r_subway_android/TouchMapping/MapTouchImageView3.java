package com.estsoft.r_subway_android.TouchMapping;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by estsoft on 2016-06-23.
 */
public class MapTouchImageView3 extends ImageView implements View.OnTouchListener {

    private static final String TAG = "MapTouchListener";
    private static final boolean D = false;


    //드래그 모드인지 핀티줌 모드인지 구분
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    //
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix1 = new Matrix();
    private Matrix savedMatrix2 = new Matrix();

    //
    private static final int WIDTH = 0;
    private static final int HEIGHT = 1;

    //
    private boolean isInit = false;


    //기본 이미지 배율
    private float defaultMagnification = 0.3f;

    //드래그시 좌표 저장
    int posX1 = 0, posY1 = 0, posX2 = 0, posY2 = 0;

    //드래그시 좌표 PointF(two floats)
    private PointF start = new PointF();
    private PointF mid = new PointF();

    //핀치시 두좌표간의 거리 저장
    float oldDist = 1f;
    float newDist = 1f;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if( isInit == false ) {
            init(); isInit = true;
        }
    }

    //context, attr, defStyleAttr 이 뭔지 궁금함
    public MapTouchImageView3(Context context, AttributeSet attrs, int defStyleAttr)  throws Exception  {
        super(context, attrs, defStyleAttr);
        setScaleType( ScaleType.MATRIX );
    }

    public MapTouchImageView3(Context context, AttributeSet attrs)  throws Exception  {
        this(context, attrs, 0);
    }

    public MapTouchImageView3(Context context) throws Exception {
        this(context, null);
    }

    protected void init() {
        setOnTouchListener( this );
        float[] value = new float[9];
        matrix.getValues( value );
        value[0] = value[4] = 1.0f;
        matrix.setValues( value );
        matrixTurning(matrix, this);


        setImageMatrix( matrix );
        setImagePit();
    }

    public void setImagePit() {

        //매트릭스 값
        float[] value = new float[9];
        this.matrix.getValues( value );

        //뷰 크기
        int width = this.getWidth();
        int height = this.getHeight();
        Log.d(TAG, "ViewSize = " + width + "/" + height);


        //이미지 크기
        Drawable drawable = this.getDrawable();
        if ( drawable == null ) return;

        // getIntrinsic : 이미지뷰에 있는 src 이미지의 실제 사이즈
        // drawable 디렉토리에 있을 경우 디바이스의 Density 비율에 따라 자동 확대
        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

        Log.d(TAG, "ImageSize = " + imageWidth + "/" + imageHeight);


        int scaleWidth = (int)(imageWidth * value[0]);
        int scaleHeight = (int)(imageHeight * value[4]);

        //이미지가 바깥으로 나가지 않도록
        value[2] = 0;
        value[5] = 0;
        if ( imageWidth > width || imageHeight > height ) {
            int target = WIDTH;
            if ( imageWidth < imageHeight ) target = HEIGHT;
            if ( target == WIDTH ) value[0] = value[4] = (float)width / imageWidth;
            if ( target == HEIGHT ) value[0] = value[4] = (float)height / imageHeight;


            // Default Magnification set
            value[0] = value[4] = defaultMagnification;
        }

        //가운데 위치
        scaleWidth = (int)(imageWidth * value[0]);
        scaleHeight = (int)(imageHeight * value[4]);
        if ( scaleWidth < width ) {
            value[2] = (float) width / 2 - (float)scaleWidth / 2;
        }
        if ( scaleHeight < height ) {
            value[5] = (float) height / 2 - (float)scaleHeight / 2;
        }

        matrix.setValues( value );
        setImageMatrix( matrix );

    }

    public void setDefaultMagnification ( float mag ) {
        defaultMagnification = mag;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 이미지 터치 전에 할 일
        beforeTouch( mode, matrix, event );

        ImageView imageView = (ImageView)v;

        switch ( event.getAction() & MotionEvent.ACTION_MASK ) {
            case MotionEvent.ACTION_DOWN :
                mode = DRAG;
                savedMatrix1.set(matrix);
                start.set( event.getX(), event.getY() );
//                posX1 = (int) event.getX();
//                posY1 = (int) event.getY();
                Log.d(TAG, "ACTION_DOWN");
//                Log.d(TAG, "MODE = DRAG");
//                Log.d(TAG, "POST : " + posX1 + " : " + posY1);
                break;

            case MotionEvent.ACTION_UP :
                Log.d(TAG, "ACTION_UP");
                break;     // 첫번째 손가락을 떼었을 경우

            case MotionEvent.ACTION_POINTER_UP :    // 두번째 손가락을 떼었을 경우
                mode = NONE;
                break;

            case MotionEvent.ACTION_POINTER_DOWN :
                //두번째 손가락 터치(손가락 2개를 인식하였기 때문에 핀치 줌으로 판별
                mode = ZOOM;
                newDist = spacing(event);
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix1.set(matrix);
                    midpoint( mid, event );
                }
                Log.d(TAG, "ACTION_POINTER_DOWN");
                Log.d(TAG, "MODE = ZOOM");
                Log.d(TAG, "NEW_DIST : " + newDist);
                Log.d(TAG, "OLD_DIST : " + oldDist);
                break;


            case MotionEvent.ACTION_MOVE :

                if ( mode == DRAG ) { //드래그 중
//                    posX2 = (int) event.getX();
//                    posY2 = (int) event.getY();
                    float distX = event.getX() - start.x;
                    float distY = event.getY() - start.y;


                    //matrix안의 X,Y로
                    if (Math.abs( distX ) > 20 || Math.abs( distY ) > 20 ) {
                        matrix.set(savedMatrix1);
                        matrix.postTranslate( distX, distY );

                        Log.d(TAG, "ACTION_MOVE");
                        Log.d(TAG, "MODE = DRAGGING");
                        Log.d(TAG, "POST : " + posX2 + " : " + posY2);
                    }

                } else if ( mode == ZOOM ) { //핀치 중

                    newDist = spacing( event );

                    if ( newDist - oldDist > 10 ) {

                        Log.d(TAG, "ACTION_MOVE");
                        Log.d(TAG, "ZOOM IN!");
                        Log.d(TAG, "MODE = PINCH_ZOOMING");
                        Log.d(TAG, "NEW_DIST : " + newDist);
                        Log.d(TAG, "OLD_DIST : " + oldDist);

//                        oldDist = newDist;

                        matrix.set(savedMatrix1);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);

                    } else if (oldDist - newDist > 10 ) {

                        Log.d(TAG, "ACTION_MOVE");
                        Log.d(TAG, "MODE = PINCH_ZOOMING");
                        Log.d(TAG, "ZOOM OUT!");
                        Log.d(TAG, "NEW_DIST : " + newDist);
                        Log.d(TAG, "OLD_DIST : " + oldDist);

//                        oldDist = newDist;

                        matrix.set(savedMatrix1);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);

                    }
                }
                break;

        }

        // 매트릭스 값 튜닝
        matrixTurning(matrix, imageView);

        imageView.setImageMatrix( matrix );

        Log.d("matrix", matrix.toString());

        // 이미지 터치 후에 할 일
        afterTouch( mode, matrix, event );


//        return false; // getAction()에서 ACTION_DOWN 만 서치됨.
        return true;
    }

    private float spacing( MotionEvent event ) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt( x * x + y * y );
    }

    private void midpoint(PointF point, MotionEvent event) {
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        point.set(x, y);
    }

    private void matrixTurning( Matrix matrix, ImageView imageView ) {

        // 매트릭스 값
        float[] value = new float[9];
        matrix.getValues(value);
        float[] savedValue = new float[9];
        savedMatrix1.getValues(savedValue);

        //뷰 크기
        int width = imageView.getWidth();
        int height = imageView.getHeight();

        //이미지 크기
        Drawable drawable = imageView.getDrawable();
        if ( drawable == null ) return;

        // getIntrinsic : 이미지뷰에 있는 src 이미지의 실제 사이즈
        // drawable 디렉토리에 있을 경우 디바이스의 Density 비율에 따라 자동 확대
        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();
        int scaleWidth = (int)(imageWidth * value[0]);
        int scaleHeight = (int)(imageHeight * value[4]);

        // 이미지가 밖으로 나가지 않도록
        if (value[2] < width - scaleWidth) value[2] = width - scaleWidth;
        if (value[5] < height - scaleHeight) value[5] = height - scaleHeight;
        if (value[2] > 0) value[2] = 0;
        if (value[5] > 0) value[5] = 0;

        // 10배 이상 확대하지 않도록
        if (value[0] > 10 || value[4] > 10) {
            value[0] = savedValue[0];
            value[4] = savedValue[4];
            value[2] = savedValue[2];
            value[5] = savedValue[5];
        }

        // 화면보다 작게 축소하지 않도록
        if ( imageWidth > width || imageHeight > height ) {
            if (scaleWidth < width && scaleHeight < height) {
                int target = WIDTH;
                if (imageWidth < imageHeight) target = HEIGHT;
                if (target == WIDTH) value[0] = value[4] = (float) width / imageWidth;
                if (target == HEIGHT) value[0] = value[4] = (float) height / imageHeight;

                scaleWidth = (int) (imageWidth * value[0]);
                scaleHeight = (int) (imageHeight * value[4]);

                if (scaleWidth > width) value[0] = value[4] = (float) width / imageWidth;
                if (scaleHeight > height) value[0] = value[4] = (float) height / imageHeight;
            }
        }
        // 원래부터 작은 것을 본래보다 작게 하지 않도록
        else {
            if (value[0] < 1) value[0] = 1;
            if (value[4] < 1) value[4] = 1;
        }

        // 그리고 가운데 위치하도록
        scaleWidth = (int)(imageWidth * value[0]);
        scaleHeight = (int)(imageHeight * value[4]);
        if ( scaleWidth < width ) {
            value[2] = (float) width / 2 - (float)scaleWidth / 2;
        }
        if ( scaleHeight < height ) {
            value[5] = (float) height / 2 - (float)scaleHeight / 2;
        }

        matrix.setValues( value );
        savedMatrix2.set( matrix );
    }


    //Override 용 함수들
    public void afterTouch ( int mode, Matrix matrix, MotionEvent event ) {}
    public void beforeTouch ( int mode,Matrix matrix, MotionEvent event ) {}


}
