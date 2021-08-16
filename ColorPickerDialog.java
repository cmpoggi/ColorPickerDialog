package com.cmpoggiapps.testapp; //change this to your app package

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class ColorPickerDialog {
    @SuppressWarnings("FieldCanBeLocal")
    private final String defAccept = "Ok";
    @SuppressWarnings("FieldCanBeLocal")
    private final String defCancel = "Cancel";
    @SuppressWarnings("FieldCanBeLocal")
    private final String defMore = "More";

    private final Context mContext;
    private AlertDialog cpDialog;
    //textos de los botones / titulo del dialogo
    private final String strTitle;
    private final String strAccept;
    private final String strMore;
    private final String strCancel;

    private final onColorSelectedListener mListener;
    private int selectedColor;
    private final int initialColor;
    private final int borderColor = 0xFF606060;

    //parametros del gridPicker
    private int gridColWidthDp;
    private int gridItemCornerRadiusDp;
    private int gridItemBorderDp;
    private int gridColumns;
    private int horSpacing;
    private int verSpacing;
    private boolean oneTouchSelect = false;
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Integer> gridColorList = new ArrayList<>();

    private final int[] defaultColorList = {
        //gray
        0xFF212121, 0xFF424242, 0xFF616161, 0xFF757575, 0xFF9E9E9E, 0xFFBDBDBD, 0xFFEEEEEE, 0xFFF5F5F5,
        //red
        0xFFB71C1C, 0xFFC62828, 0xFFD32F2F, 0xFFE53935, 0xFFF44336, 0xFFEF5350, 0xFFE57373, 0xFFEF9A9A,
        //deep orange
        0xFFBF360C, 0xFFD84315, 0xFFE64A19, 0xFFF4511E, 0xFFFF5722, 0xFFFF7043, 0xFFFF8A65, 0xFFFFAB91,
        //orange
        0xFFE65100, 0xFFEF6C00, 0xFFF57C00, 0xFFFB8C00, 0xFFFF9800, 0xFFFFA726, 0xFFFFB74D, 0xFFFFCC80,
        //amber
        0xFFFF6F00, 0xFFFF8F00, 0xFFFFA000, 0xFFFFB300, 0xFFFFC107, 0xFFFFCA28, 0xFFFFD54F, 0xFFFFD54F,
        //yellow
        0xFFF57F17, 0xFFF9A825, 0xFFFBC02D, 0xFFFDD835, 0xFFFFEB3B, 0xFFFFEE58, 0xFFFFF176, 0xFFFFF59D,
        //lime
        0xFF827717, 0xFF9E9D24, 0xFFAFB42B, 0xFFC0CA33, 0xFFCDDC39, 0xFFD4E157, 0xFFDCE775, 0xFFE6EE9C,
        //green
        0xFF1B5E20, 0xFF2E7D32, 0xFF388E3C, 0xFF43A047, 0xFF4CAF50, 0xFF66BB6A, 0xFF81C784, 0xFFA5D6A7,
        //teal
        0xFF004D40, 0xFF00695C, 0xFF00796B, 0xFF00897B, 0xFF009688, 0xFF26A69A, 0xFF4DB6AC, 0xFF80CBC4,
        //cyan
        0xFF006064, 0xFF00838F, 0xFF0097A7, 0xFF00ACC1, 0xFF00BCD4, 0xFF26C6DA, 0xFF4DD0E1, 0xFF80DEEA,
        //light blue
        0xFF01579B, 0xFF0277BD, 0xFF0288D1, 0xFF039BE5, 0xFF03A9F4, 0xFF29B6F6, 0xFF4FC3F7, 0xFF81D4FA,
        //blue
        0xFF0D47A1, 0xFF1565C0, 0xFF1976D2, 0xFF1E88E5, 0xFF2196F3, 0xFF42A5F5, 0xFF64B5F6, 0xFF90CAF9,
        //indigp
        0xFF1A237E, 0xFF283593, 0xFF303F9F, 0xFF3949AB, 0xFF3F51B5, 0xFF5C6BC0, 0xFF7986CB, 0xFF9FA8DA,
        //purple
        0xFF4A148C, 0xFF6A1B9A, 0xFF7B1FA2, 0xFF8E24AA, 0xFF9C27B0, 0xFFAB47BC, 0xFFBA68C8, 0xFFCE93D8,
        //pink
        0xFF880E4F, 0xFFAD1457, 0xFFC2185B, 0xFFD81B60, 0xFFE91E63, 0xFFEC407A, 0xFFF06292, 0xFFF48FB1,
        //brown
        0xFF3E2723, 0xFF4E342E, 0xFF5D4037, 0xFF6D4C41, 0xFF795548, 0xFF8D6E63, 0xFFA1887F, 0xFFBCAAA4,
        //blue grey
        0xFF263238, 0xFF37474F, 0xFF455A64, 0xFF546E7A, 0xFF607D8B, 0xFF78909C, 0xFF90A4AE, 0xFFB0BEC5
    };

    /* ***************************************************************************** */
    /* ******************************* Constructores ******************************* */
    /* ***************************************************************************** */
    public ColorPickerDialog(Context context, int initColor, String title,
                             onColorSelectedListener listener) {
        this.mContext = context;
        this.strTitle = title;
        this.strAccept = defAccept;
        this.strCancel = defCancel;
        this.strMore = defMore;
        this.initialColor = initColor;
        this.selectedColor = initColor;
        this.mListener = listener;
        initColorPicker();
    }

    public ColorPickerDialog(Context context, int initColor, String title, String accept,
                             String cancel, onColorSelectedListener listener) {
        this.mContext = context;
        this.strTitle = title;
        this.strAccept = accept;
        this.strMore = "";
        this.strCancel = cancel;
        this.initialColor = initColor;
        this.selectedColor = initColor;
        this.mListener = listener;
        initColorPicker();
    }

    public ColorPickerDialog(Context context, int initColor, String title, String accept,
                             String cancel, String more, onColorSelectedListener listener) {
        this.mContext = context;
        this.strTitle = title;
        this.strAccept = accept;
        this.strMore = more;
        this.strCancel = cancel;
        this.initialColor = initColor;
        this.selectedColor = initColor;
        this.mListener = listener;
        initColorPicker();
    }

    private void initColorPicker() {
        this.gridColWidthDp = 45;
        this.gridItemCornerRadiusDp = 10;
        this.gridItemBorderDp = 1;
        this.horSpacing = 1;
        this.verSpacing = 2;
        this.gridColumns = GridView.AUTO_FIT; //GridView.AUTO_FIT (-1) //8
    }

    public void setGridItemCornerRadiusDp(int radiusDp) { this.gridItemCornerRadiusDp = radiusDp; }
    public void setGridColumns(int nColumns) { this.gridColumns = nColumns; }
    public void setGridColWidthDp(int colWidthDp) { this.gridColWidthDp = colWidthDp; }
    public void setGridColorList(int[] colorArray) {
        this.gridColorList.clear();
        if (colorArray.length > 0) for (int color : colorArray) this.gridColorList.add(color);
        else for (int color : defaultColorList) this.gridColorList.add(color);
    }
    public void setGridColorList(String[] colorArray) {
        this.gridColorList.clear();
        if (colorArray.length > 0) for (String color : colorArray)
            this.gridColorList.add(Color.parseColor(color));
        else for (int color : defaultColorList) this.gridColorList.add(color);
    }
    public void setOneTouchSelect(boolean oneTouch) { this.oneTouchSelect = oneTouch; }

    /* ***************************************************************************** */
    /* ******************************* GridPickerView ****************************** */
    /* ***************************************************************************** */
    private class GridPickerView extends GridView {
        ArrayAdapter<Integer> arrayAdapter;

        //Constructor por defecto
        public GridPickerView(Context context) {
            super(context);
            initGridView();
        }

        void initGridView () {
            // Specify the GridView layout parameters
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));

            // Define column width and number of columns
            this.setNumColumns(gridColumns);

            //Should be same as TextView width and height
            if (gridColumns==GridView.AUTO_FIT) {
                this.setColumnWidth(dpToPx(gridColWidthDp));
            }

            // Define addition settings of GridView for design purpose
            this.setHorizontalSpacing(horSpacing);
            this.setVerticalSpacing(verSpacing);
            this.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            this.setBackgroundColor(Color.TRANSPARENT);
            this.setPadding(0, 0, 0, 0);
            this.setGravity(Gravity.CENTER);

            // Create an ArrayAdapter using colors list
            for (int color : defaultColorList) gridColorList.add(color);

            arrayAdapter = new ArrayAdapter<Integer>(mContext,
                    android.R.layout.simple_list_item_1, gridColorList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Cast the current view as a TextView
                    TextView view = (TextView) super.getView(position, convertView, parent);

                    // Get the current color from list
                    int currentColor = gridColorList.get(position);

                    //border for textview
                    GradientDrawable gd = new GradientDrawable();
                    // Changes this drawbale to use a single color instead of a gradient
                    gd.setColor(currentColor);
                    if (gridItemCornerRadiusDp >0) gd.setCornerRadius(dpToPx(gridItemCornerRadiusDp));
                    if (currentColor == selectedColor)
                        gd.setStroke(dpToPx(gridItemBorderDp +3), borderColor);
                    else gd.setStroke(dpToPx(gridItemBorderDp), borderColor);

                    // Set the background color of TextView as current color
                    view.setBackground(gd);

                    // Set empty text in TextView
                    view.setText("");
                    view.setPadding(0, 0, 0, 0);

                    // Set the layout parameters for TextView widget
                    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT);
                    view.setLayoutParams(lp);

                    // Get the TextView LayoutParams
                    LayoutParams params = (LayoutParams) view.getLayoutParams();

                    // Set the TextView width and height in pixels
                    // Should be same as GridView column width
                    int columnWidth;
                    if (gridColumns !=GridView.AUTO_FIT){
                        int offset=dpToPx(1);
                        if (gridItemBorderDp >0) offset += dpToPx(gridItemBorderDp);
                        if (horSpacing>0) offset += dpToPx(horSpacing);
                        columnWidth = (getColumnWidth() - offset);
                        //columnWidth = (gridView.getColumnWidth() - offset);
                    }
                    else columnWidth = dpToPx(gridColWidthDp);
                    params.width = columnWidth;
                    params.height = columnWidth;
                    // Set the TextView layout parameters
                    view.setLayoutParams(params);
                    view.requestLayout();
                    // Return the TextView as current view
                    return view;
                }
            };
            // Specify the GridView data source
            this.setAdapter(arrayAdapter);

            //establecer el color al tocar una casilla
            this.setOnItemClickListener((parent, view1, position, id) -> {
                // Get the pickedColor from AdapterView
                if (oneTouchSelect) setSelectedColor((int)parent.getItemAtPosition(position));
                else {
                    selectedColor = (int) parent.getItemAtPosition(position);
                    arrayAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /* ***************************************************************************** */
    /* ******************************* CirclePickerView **************************** */
    /* ***************************************************************************** */
    private class CirclePickerView extends View {
        //bordes
        private Paint mBorderPaint; //ring brush border
        private Paint mCursorPaint; //cursor brush border
        private Paint mLinePaint; // divider brush
        //pinceles
        private Paint mPaint; //gradient color ring brush
        private Paint mCenterPaint; //Intermediate Circle Brush
        private Paint mRectPaint; //gradient square brush
        //coordenadas
        private float rectLeft; // gradient square left x coordinate
        private float rectTop; //gradient square right x coordinate
        private float rectRight; //y coordinate on the gradient block
        private float rectBottom; //y coordinate under the gradient box
        //gradientes
        private int[] mCircleColors; //gradient color circle color
        private int[] mRectColors; //gradation block color
        //medidas
        private final int mHeight;//View high
        private final int mWidth;//View wide
        private float r;// color circle radius (paint middle)
        private float centerRadius;// center circle radius
        //banderas
        private boolean downInCircle = true; // press on the gradient ring
        private boolean downInRect; // press on the gradient box
        private boolean highlightCenter; //highlight
        private boolean highlightCenterLittle; //light

        private float curCircleX;
        private float curCircleY;
        private float curRectX;
        private float curRectY;
        private float rectOffset;
        private Shader rectShader;
        private RectF rectF;

        public CirclePickerView(Context context, int height, int width) {
            super(context);
            this.mHeight = height;
            this.mWidth = width;
            setMinimumHeight(height);
            setMinimumWidth(width);
            initCirclePicker();
        }

        void initCirclePicker() {
            mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStrokeWidth(dpToPx(1));
            mBorderPaint.setStyle(Paint.Style.STROKE);

            mCursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCursorPaint.setColor(Color.WHITE);
            mCursorPaint.setStrokeWidth(dpToPx(3));
            mCursorPaint.setStyle(Paint.Style.STROKE);

            // Gradient color ring parameters
            mCircleColors = new int[] {0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                    0xFFFFFF00, 0xFFFF0000};
            Shader shader = new SweepGradient(0, 0, mCircleColors, null);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(shader);
            mPaint.setStyle(Paint.Style.STROKE);
            int circleStroke = (int) (mWidth * 0.15f);
            mPaint.setStrokeWidth(circleStroke);
            //radio externo (0.9f = 90% del dialogo)
            float viewPercent = 0.85f;
            r = ((float) (mWidth / 2) * viewPercent) - (mPaint.getStrokeWidth() * 0.5f);

            // Center circle parameters
            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setColor(initialColor);
            mCenterPaint.setStrokeWidth(dpToPx(3));
            float innerPercent = 0.9f;
            centerRadius = (r - (mPaint.getStrokeWidth() / 2)) * innerPercent;

            // border parameters
            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setColor(borderColor);
            mLinePaint.setStrokeWidth(dpToPx(1));

            // black and white gradient parameters
            mRectColors = new int[] {0xFF000000, mCenterPaint.getColor(), 0xFFFFFFFF};
            mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mRectPaint.setStrokeWidth(dpToPx(3));
            rectLeft = -r - mPaint.getStrokeWidth() * 0.5f;
            rectTop = r + mPaint.getStrokeWidth() * 0.5f + mLinePaint.getStrokeMiter() * 0.5f
                    + (mPaint.getStrokeWidth() * 0.1f);
            rectRight = r + mPaint.getStrokeWidth() * 0.5f;
            int rectStroke = (int) mPaint.getStrokeWidth();
            rectBottom = rectTop + rectStroke; //altura de la barra

            //gradient square gradient image
            rectShader = updateShader();
            //color ring
            rectF = new RectF(-r, -r, r, r);
            rectOffset = mLinePaint.getStrokeWidth() / 2;
            curRectX = ((rectRight + rectLeft) / 2) - rectOffset;
            curRectY = ((rectBottom + rectTop) / 2) - rectOffset;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            /*
            canvas.translate(0,0);
            float off = mLinePaint.getStrokeWidth();
            canvas.drawLine(0+off, 0+off, mWidth-off, 0+off, mLinePaint); //top
            canvas.drawLine(0+off, mHeight-off, mWidth-off, mHeight-off, mLinePaint); //bottom
            canvas.drawLine(0+off, 0+off,0+off, mHeight-off, mLinePaint); //left
            canvas.drawLine(mWidth-off, 0+off, mWidth-off, mHeight-off, mLinePaint); //right
            */

            //Mobile Center
            //canvas.translate((float) (mWidth/2) - (float) (dpToPx(circleStroke)/2),(float) (mHeight/2) - dpToPx(circleStroke));
            //canvas.translate((float) (mWidth/2),(float) (mHeight/2) - dpToPx(circleStroke));
            canvas.translate((float) (mWidth/2),(float) (mHeight/2) - (mPaint.getStrokeWidth()/2));

            //Draw the center circle
            canvas.drawCircle(0, 0, centerRadius,  mCenterPaint);
            // Whether to display the small circle outside the center circle
            if (highlightCenter || highlightCenterLittle) {
                int c = mCenterPaint.getColor();
                mCenterPaint.setStyle(Paint.Style.STROKE);
                if(highlightCenter) {
                    mCenterPaint.setAlpha(0xFF);
                }
                else if(highlightCenterLittle) {
                    mCenterPaint.setAlpha(0x90);
                }
                canvas.drawCircle(0, 0,centerRadius + mCenterPaint.getStrokeWidth(),
                        mCenterPaint);
                mCenterPaint.setStyle(Paint.Style.FILL);
                mCenterPaint.setColor(c);
            }

            //draw color ring
            canvas.drawOval(rectF, mPaint);

            //draw color border
            canvas.drawCircle(0,0, r + (mPaint.getStrokeWidth()/2), mBorderPaint);
            canvas.drawCircle(0,0, r - (mPaint.getStrokeWidth()/2), mBorderPaint);

            // draw black and white gradient block
            if(downInCircle) {
                mRectColors[1] = mCenterPaint.getColor();
            }

            //gradient square gradient image
            rectShader = updateShader();
            mRectPaint.setShader(rectShader);

            canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, mRectPaint);
            float offset = mLinePaint.getStrokeWidth() / 2;
            canvas.drawLine(rectLeft - offset, rectTop - offset * 2,
                    rectLeft - offset, rectBottom + offset * 2, mLinePaint);//left
            canvas.drawLine(rectLeft - offset * 2, rectTop - offset,
                    rectRight + offset * 2, rectTop - offset, mLinePaint); //
            canvas.drawLine(rectRight + offset, rectTop - offset * 2,
                    rectRight + offset, rectBottom + offset * 2, mLinePaint);//right
            canvas.drawLine(rectLeft - offset * 2, rectBottom + offset,
                    rectRight + offset * 2, rectBottom + offset, mLinePaint);//

            //dibujar cursor paleta circular
            if (inColorCircle(curCircleX, curCircleY,r + mPaint.getStrokeWidth() / 2,
                    r - mPaint.getStrokeWidth() / 2)) {
                canvas.drawCircle(curCircleX, curCircleY, dpToPx(8), mCursorPaint);
                canvas.drawCircle(curCircleX, curCircleY, dpToPx(9), mBorderPaint);
            }

            //dibujar cursor paleta rectangular
            if (inRect(curRectX, curRectY)) {
                canvas.drawCircle(curRectX, curRectY, dpToPx(8), mCursorPaint);
                canvas.drawCircle(curRectX, curRectY, dpToPx(9), mBorderPaint);
            }
            super.onDraw(canvas);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX() - (float) mWidth / 2;
            float y = event.getY() - (float) (mHeight/2) + (mPaint.getStrokeWidth()/2);

            boolean inCircle = inColorCircle(x, y,r + mPaint.getStrokeWidth() / 2,
                    r - mPaint.getStrokeWidth() / 2);
            boolean inCenter = inCenter(x, y, centerRadius);
            boolean inRect = inRect(x, y);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downInCircle = inCircle;
                    downInRect = inRect;
                    highlightCenter = inCenter;

                case MotionEvent.ACTION_MOVE:
                    //down is pressed in the gradient color circle, and move is also in the
                    //gradient color circle.
                    if(downInCircle && inCircle) {
                        curRectX = (rectRight + rectLeft) / 2 - rectOffset;
                        curRectY = (rectBottom + rectTop) / 2 - rectOffset;
                        curCircleX = x;
                        curCircleY = y;
                        float angle = (float) Math.atan2(y, x);
                        float unit = (float) (angle / (2 * Math.PI));
                        if (unit < 0) unit += 1;
                        mCenterPaint.setColor(getCircleColor(mCircleColors, unit));
                        selectedColor = getCircleColor(mCircleColors, unit);
                    }
                    //down is in the gradient box, and move is also in the gradient box
                    else if(downInRect && inRect) {
                        curRectX = x;
                        //centramos verticalmente el cursor en la barra
                        curRectY = (rectBottom + rectTop) / 2 - rectOffset;
                        mCenterPaint.setColor(getRectColor(mRectColors, x));
                        selectedColor = getRectColor(mRectColors, x);
                    }
                    //Click on the center circle, the current movement is in the center circle
                    if ((highlightCenter && inCenter) || (highlightCenterLittle && inCenter)) {
                        highlightCenter = true;
                        highlightCenterLittle = false;
                    }
                    //Click on the center circle, currently moving out of the center circle
                    else if(highlightCenter || highlightCenterLittle) {
                        highlightCenter = false;
                        highlightCenterLittle = true;
                    }
                    else {
                        highlightCenter = false;
                        highlightCenterLittle = false;
                    }
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    //Click on the center circle, and the current start is in the center circle
                    if(highlightCenter && inCenter) {
                        if(mListener != null) {
                            mListener.onColorSelected(mCenterPaint.getColor());
                            cpDialog.dismiss();
                        }
                    }
                    if(downInCircle) downInCircle = false;
                    if(downInRect) downInRect = false;
                    if(highlightCenter) highlightCenter = false;
                    if(highlightCenterLittle) highlightCenterLittle = false;
                    invalidate();
                    break;
            }
            return true;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(mWidth, mHeight);
        }

        /**
         * Whether the coordinates are on the color circle
         * @param x coordinates
         * @param y coordinates
         * @param outRadius outer ring radius
         * @param inRadius inner radius of the color circle
         * @return boolean
         */
        private boolean inColorCircle(float x, float y, float outRadius, float inRadius) {
            double outCircle = Math.PI * outRadius * outRadius;
            double inCircle = Math.PI * inRadius * inRadius;
            double fingerCircle = Math.PI * (x * x + y * y);
            return fingerCircle<outCircle && fingerCircle>inCircle;
        }

        /**
         * Whether the coordinates are on the center circle
         * @param x coordinates
         * @param y coordinates
         * @param centerRadius circle radius
         * @return boolean
         */
        private boolean inCenter(float x, float y, float centerRadius) {
            double centerCircle = Math.PI * centerRadius * centerRadius;
            double fingerCircle = Math.PI * (x * x + y * y);
            return fingerCircle < centerCircle;
        }

        /**
         * Whether the coordinates are in the gradient
         * @param x x coordinate
         * @param y y coordinate
         * @return boolean
         */
        private boolean inRect(float x, float y) {
            return x <= rectRight && x >= rectLeft && y <= rectBottom && y >= rectTop;
        }

        /**
         * Get the color on the ring
         * @param colors colors
         * @param unit unit
         * @return color as rgb integer
         */
        private int getCircleColor(int[] colors, float unit) {
            if (unit <= 0) return colors[0];
            if (unit >= 1) return colors[colors.length - 1];

            float p = unit * (colors.length - 1);
            int i = (int) p;
            p -= i;

            // now p is just the fractional part [0...1) and i is the index
            int c0 = colors[i];
            int c1 = colors[i+1];
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);

            return Color.argb(a, r, g, b);
        }

        /**
         * Get the color on the gradient block
         * @param colors colors
         * @param x x
         * @return color as rgb integer
         */
        private int getRectColor(int[] colors, float x) {
            int a, r, g, b, c0, c1;
            float p;
            if (x < 0) {
                c0 = colors[0];
                c1 = colors[1];
                p = (x + rectRight) / rectRight;
            }
            else {
                c0 = colors[1];
                c1 = colors[2];
                p = x / rectRight;
            }
            a = ave(Color.alpha(c0), Color.alpha(c1), p);
            r = ave(Color.red(c0), Color.red(c1), p);
            g = ave(Color.green(c0), Color.green(c1), p);
            b = ave(Color.blue(c0), Color.blue(c1), p);
            return Color.argb(a, r, g, b);
        }
        private int ave(int s, int d, float p) {
            return s + Math.round(p * (d - s));
        }

        Shader updateShader() {
            return new LinearGradient(rectLeft, 0, rectRight, 0, mRectColors,null,
                    Shader.TileMode.MIRROR);
        }
    }

    @NonNull
    public static Point getDisplayDimensions(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        // find out if status bar has already been subtracted from screenHeight
        display.getRealMetrics(metrics);
        int physicalHeight = metrics.heightPixels;
        int statusBarHeight = getStatusBarHeight(context);
        int navigationBarHeight = getNavigationBarHeight(context);
        int heightDelta = physicalHeight - screenHeight;
        if (heightDelta == 0 || heightDelta == navigationBarHeight) {
            screenHeight -= statusBarHeight;
        }
        return new Point(screenWidth, screenHeight);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen",
                "android");
        return (resourceId > 0)? resources.getDimensionPixelSize(resourceId) : 0;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier( "navigation_bar_height", "dimen",
                "android" );
        return ( resourceId > 0 ) ? resources.getDimensionPixelSize( resourceId ) : 0;
    }

    int dpToPx(int paddingDp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (paddingDp * scale + 0.5f);
    }


    TextView initCustomTitle() {
        LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpTitle.setMargins(0, 0, 0, 0);

        TextView textView = new TextView(mContext);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setPadding(0,dpToPx(5),0,dpToPx(5));
        textView.setLayoutParams(lpTitle);
        textView.setText(strTitle);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }

    void showGridPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (!strTitle.isEmpty()) {
            TextView tvTitle = initCustomTitle();
            builder.setCustomTitle(tvTitle);
        }
        if (!strCancel.isEmpty()) {
            builder.setNegativeButton(strCancel, (dialog, id) -> dialog.cancel());
        }
        if (!strMore.isEmpty()) {
            builder.setNeutralButton(strMore, (dialog, id) -> {
                dialog.dismiss();
                showCirclePicker();
            });
        }
        if (!oneTouchSelect) {
            builder.setPositiveButton(strAccept, (dialog, id) -> {
                setSelectedColor(selectedColor);
                dialog.dismiss();
            });
        }

        GridPickerView gridPickerView = new GridPickerView(mContext);
        RelativeLayout rlGridPicker = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        gridPickerView.setLayoutParams(rlParams);
        rlGridPicker.addView(gridPickerView);

        // Set the alert dialog content to GridView (color picker)
        builder.setView(rlGridPicker);

        // Initialize a new AlertDialog object
        cpDialog = builder.create();
        // Show the color picker window
        cpDialog.show();

        Point dimensions = getDisplayDimensions(mContext);
        int height = (int) (dimensions.y * 0.85f);
        int width = dimensions.x;

        cpDialog.getWindow().setLayout(width, height);
    }

    void showCirclePicker() {
        CirclePickerView circlePickerView;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        //dimensiones del dialogo
        Point dimensions = getDisplayDimensions(mContext);
        int height = (int) (dimensions.y * 0.6f);
        int width = (int) (dimensions.x * 0.95f);

        //revisamos la orientacion de la pantalla
        int orientation = mContext.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            circlePickerView = new CirclePickerView(mContext, height, width);
        //Configuration.ORIENTATION_LANDSCAPE
        else circlePickerView = new CirclePickerView(mContext, width, height);

        if (!strTitle.isEmpty()) {
            TextView tvTitle = initCustomTitle();
            builder.setCustomTitle(tvTitle);
            //builder.setTitle(strTitle);
        }
        if (!strCancel.isEmpty()) {
            builder.setNegativeButton(strCancel, (dialog, id) -> dialog.cancel());
        }
        builder.setPositiveButton(strAccept, (dialog, id) -> {
            setSelectedColor(selectedColor);
            dialog.dismiss();
        });

        // Creating a new RelativeLayout
        RelativeLayout rlCirclePicker = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        circlePickerView.setLayoutParams(rlParams);
        rlCirclePicker.addView(circlePickerView);
        builder.setView(rlCirclePicker);

        cpDialog = builder.create();
        cpDialog.show();
    }

    void setSelectedColor(int color) {
        if (mListener!=null) {
            selectedColor = color;
            mListener.onColorSelected(selectedColor);
            cpDialog.dismiss();
        }
    }

    /**
     * Callback interface
     * @author <a href="cmpoggi@gmail.com">cmpoggi</a>
     *
     * Create on 2021-7-28 12:58:00 AM
     *
     */
    public interface onColorSelectedListener {
        /**
         * Callback
         * @param color selected color
         */
        void onColorSelected(int color);
    }
}
