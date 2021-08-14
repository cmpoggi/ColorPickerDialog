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

    /* ***************************************************************************** */
    /* ******************************* Constructores ******************************* */
    /* ***************************************************************************** */
    public ColorPickerDialog(Context context, int initColor, String title,
                             onColorSelectedListener listener) {
        this.mContext = context;
        this.strTitle = title;
        this.strAccept = "Ok";
        this.strCancel = "Cancel";
        this.strMore = "More";
        this.initialColor = initColor;
        this.selectedColor = initColor;
        this.mListener = listener;
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
    }

    /* ***************************************************************************** */
    /* ******************************* GridPickerView ****************************** */
    /* ***************************************************************************** */
    private class GridPickerView extends GridView {
        private final int columnWidthDp;
        private final int itemRadiusDp;
        private final int itemBorderDp;
        private final int gridColumns;
        private final int horSpacing;
        private final int verSpacing;

        private final String[] defaultColors = {
            //gray
            "#212121", "#424242", "#616161", "#757575", "#9E9E9E", "#BDBDBD", "#EEEEEE", "#F5F5F5",
            //red
            "#B71C1C", "#C62828", "#D32F2F", "#E53935", "#F44336", "#EF5350", "#E57373", "#EF9A9A",
            //deep orange
            "#BF360C", "#D84315", "#E64A19", "#F4511E", "#FF5722", "#FF7043", "#FF8A65", "#FFAB91",
            //orange
            "#E65100", "#EF6C00", "#F57C00", "#FB8C00", "#FF9800", "#FFA726", "#FFB74D", "#FFCC80",
            //amber
            "#FF6F00", "#FF8F00", "#FFA000", "#FFB300", "#FFC107", "#FFCA28", "#FFD54F", "#FFD54F",
            //yellow
            "#F57F17", "#F9A825", "#FBC02D", "#FDD835", "#FFEB3B", "#FFEE58", "#FFF176", "#FFF59D",
            //lime
            "#827717", "#9E9D24", "#AFB42B", "#C0CA33", "#CDDC39", "#D4E157", "#DCE775", "#E6EE9C",
            //green
            "#1B5E20", "#2E7D32", "#388E3C", "#43A047", "#4CAF50", "#66BB6A", "#81C784", "#A5D6A7",
            //teal
            "#004D40", "#00695C", "#00796B", "#00897B", "#009688", "#26A69A", "#4DB6AC", "#80CBC4",
            //cyan
            "#006064", "#00838F", "#0097A7", "#00ACC1", "#00BCD4", "#26C6DA", "#4DD0E1", "#80DEEA",
            //light blue
            "#01579B", "#0277BD", "#0288D1", "#039BE5", "#03A9F4", "#29B6F6", "#4FC3F7", "#81D4FA",
            //blue
            "#0D47A1", "#1565C0", "#1976D2", "#1E88E5", "#2196F3", "#42A5F5", "#64B5F6", "#90CAF9",
            //indigp
            "#1A237E", "#283593", "#303F9F", "#3949AB", "#3F51B5", "#5C6BC0", "#7986CB", "#9FA8DA",
            //purple
            "#4A148C", "#6A1B9A", "#7B1FA2", "#8E24AA", "#9C27B0", "#AB47BC", "#BA68C8", "#CE93D8",
            //pink
            "#880E4F", "#AD1457", "#C2185B", "#D81B60", "#E91E63", "#EC407A", "#F06292", "#F48FB1",
            //brown
            "#3E2723", "#4E342E", "#5D4037", "#6D4C41", "#795548", "#8D6E63", "#A1887F", "#BCAAA4",
            //blue grey
            "#263238", "#37474F", "#455A64", "#546E7A", "#607D8B", "#78909C", "#90A4AE", "#B0BEC5"
        };

        //Constructor por defecto
        public GridPickerView(Context context) {
            super(context);
            this.columnWidthDp = 45;
            this.itemRadiusDp = 10;
            this.itemBorderDp = 1;
            this.horSpacing = 1;
            this.verSpacing = 2;

            this.gridColumns = GridView.AUTO_FIT; //GridView.AUTO_FIT (-1) //8
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
                this.setColumnWidth(dpToPx(columnWidthDp));
            }

            // Define addition settings of GridView for design purpose
            this.setHorizontalSpacing(horSpacing);
            this.setVerticalSpacing(verSpacing);
            this.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            this.setBackgroundColor(Color.TRANSPARENT);
            this.setPadding(0, 0, 0, 0);
            this.setGravity(Gravity.CENTER);

            // Create an ArrayAdapter using colors list
            ArrayList<Integer> colorList = new ArrayList<>();
            for (String color : defaultColors) {
                colorList.add(Color.parseColor(color));
            }
            ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(mContext,
                    android.R.layout.simple_list_item_1, colorList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Cast the current view as a TextView
                    TextView view = (TextView) super.getView(position, convertView, parent);

                    // Get the current color from list
                    int currentColor = colorList.get(position);

                    //border for textview
                    GradientDrawable gd = new GradientDrawable();
                    // Changes this drawbale to use a single color instead of a gradient
                    gd.setColor(currentColor);
                    gd.setCornerRadius(dpToPx(itemRadiusDp));
                    if (currentColor == initialColor)
                        gd.setStroke(dpToPx(itemBorderDp+2), borderColor);
                    else gd.setStroke(dpToPx(itemBorderDp), borderColor);

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
                    if (gridColumns!=GridView.AUTO_FIT){
                        int offset=dpToPx(1);
                        if (itemBorderDp>0) offset += dpToPx(itemBorderDp);
                        if (horSpacing>0) offset += dpToPx(horSpacing);
                        columnWidth = (getColumnWidth() - offset);
                        //columnWidth = (gridView.getColumnWidth() - offset);
                    }
                    else columnWidth = dpToPx(columnWidthDp);
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
                setSelectedColor((int)parent.getItemAtPosition(position));
            });
        }
    }

    /* ***************************************************************************** */
    /* ******************************* CirclePickerView **************************** */
    /* ***************************************************************************** */
    private class CirclePickerView extends View {
        //bordes
        private final Paint mBorderPaint; //ring brush border
        private final Paint mCursorPaint; //cursor brush border
        private final Paint mLinePaint; // divider brush
        //pinceles
        private final Paint mPaint; //gradient color ring brush
        private final Paint mCenterPaint; //Intermediate Circle Brush
        private final Paint mRectPaint; //gradient square brush
        //coordenadas
        private final float rectLeft; // gradient square left x coordinate
        private final float rectTop; //gradient square right x coordinate
        private final float rectRight; //y coordinate on the gradient block
        private final float rectBottom; //y coordinate under the gradient box
        //gradientes
        private final int[] mCircleColors; //gradient color circle color
        private final int[] mRectColors; //gradation block color
        //medidas
        private final int mHeight;//View high
        private final int mWidth;//View wide
        private final float r;// color circle radius (paint middle)
        private final float centerRadius;// center circle radius
        //banderas
        private boolean downInCircle = true; // press on the gradient ring
        private boolean downInRect; // press on the gradient box
        private boolean highlightCenter; //highlight
        private boolean highlightCenterLittle; //light

        private float curCircleX;
        private float curCircleY;
        private float curRectX;
        private float curRectY;
        private final float rectOffset;
        private Shader rectShader;
        private final RectF rectF;

        public CirclePickerView(Context context, int height, int width) {
            super(context);
            this.mHeight = height;
            this.mWidth = width;
            setMinimumHeight(height);
            setMinimumWidth(width);

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
            int circleStroke = (int) (width * 0.15f);
            mPaint.setStrokeWidth(circleStroke);
            //radio externo (0.9f = 90% del dialogo)
            float viewPercent = 0.85f;
            r = ((float) (width / 2) * viewPercent) - (mPaint.getStrokeWidth() * 0.5f);

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
