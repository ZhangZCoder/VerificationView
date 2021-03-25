package com.zz.View

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.zz.verificationcodeview.R
import java.util.*

/**
 * Created by zz on 3/22/21.
 */
class VerificationCodeView : RelativeLayout {
    private var mContext: Context? = null
    private var mEtNumber = 0
    private var mEditText: EditText? = null
    private var mLinearLayout: LinearLayout? = null
    private val mCodes: MutableList<String> =
        ArrayList()
    private lateinit var mTextView: Array<TextView?>
    private lateinit var mCursorViews: Array<View?>
    private var valueAnimator: ValueAnimator? = null
    private lateinit var mUnderLineViews: Array<View?>
    private lateinit var mRelativeLayout: Array<RelativeLayout?>
    private var onInputListener: OnInputListener? = null
    private var inputType = 0
    private var mEtWidth = 0

    /**
     * 输入框的高度
     */
    private var mEtHeight = 0

    /**
     * 文字颜色
     */
    private var mEtTextColor = 0

    /**
     * 文字大小
     */
    private var mEtTextSize = 0f

    /**
     * 输入框间距
     */
    private var mEtSpacing = 0

    /**
     * 平分后的间距
     */
    private var mEtBisectSpacing = 0

    /**
     * 判断是否平分,默认平分
     */
    private var isBisect = false

    /**
     * 输入框宽度
     */
    private var mViewWidth = 0

    /**
     * 下划线默认颜色,焦点颜色,高度,是否展示
     */
    private var mEtUnderLineDefaultColor = 0
    private var mEtUnderLineFocusColor = 0
    private var mEtUnderLineHeight = 0
    private var mEtUnderLineShow = false

    /**
     * 光标宽高,颜色
     */
    private var mEtCursorWidth = 0
    private var mEtCursorHeight = 0
    private var mEtCursorColor = 0

    /**
     * 输入框的背景色、焦点背景色、是否有焦点背景色
     */
    private var mEtBackground = 0
    private var mEtFocusBackground = 0
    private var isFocusBackground = false

    enum class ZZInputType {
        NUMBER, NUMBERPASSWORD, TEXT, TEXTPASSWORD
    }

    private var etInputType: ZZInputType? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?
    ) {
        mContext = context
        val typedArray =
            context.obtainStyledAttributes(attrs,
                R.styleable.VerificationCodeView
            )
        mEtNumber = typedArray.getInteger(R.styleable.VerificationCodeView_zz_et_number, 4)
        inputType = typedArray.getInt(
            R.styleable.VerificationCodeView_zz_et_inputType,
            ZZInputType.NUMBER.ordinal
        )
        etInputType =
            ZZInputType.values()[inputType]
        mEtWidth = typedArray.getDimensionPixelSize(
            R.styleable.VerificationCodeView_zz_et_width,
            dip2px(mContext, 45f)
        )
        mEtHeight = typedArray.getDimensionPixelSize(
            R.styleable.VerificationCodeView_zz_et_height,
            dip2px(mContext, 33f)
        )
        mEtTextColor = typedArray.getColor(
            R.styleable.VerificationCodeView_zz_et_textColor,
            Color.parseColor("#757F8D")
        )
        mEtTextSize = typedArray.getDimension(R.styleable.VerificationCodeView_zz_et_textSize, 24f)
        isBisect = typedArray.hasValue(R.styleable.VerificationCodeView_zz_et_spacing)
        mEtCursorColor = typedArray.getColor(
            R.styleable.VerificationCodeView_zz_et_cursor_color,
            Color.parseColor("#FF3A3A")
        )
        mEtCursorWidth = typedArray.getDimensionPixelOffset(
            R.styleable.VerificationCodeView_zz_et_cursor_width,
            dip2px(mContext, 1f)
        )
        mEtCursorHeight = typedArray.getDimensionPixelOffset(
            R.styleable.VerificationCodeView_zz_et_cursor_height,
            dip2px(mContext, 22f)
        )
        isFocusBackground =
            typedArray.hasValue(R.styleable.VerificationCodeView_zz_et_focus_background)
        mEtFocusBackground =
            typedArray.getResourceId(R.styleable.VerificationCodeView_zz_et_focus_background, -1)
        if (isBisect) {
            mEtSpacing = typedArray.getDimensionPixelSize(
                R.styleable.VerificationCodeView_zz_et_spacing,
                dip2px(mContext, 12f)
            )
        }
        mEtBackground =
            typedArray.getResourceId(R.styleable.VerificationCodeView_zz_et_background, -1)
        mEtUnderLineShow =
            typedArray.getBoolean(R.styleable.VerificationCodeView_zz_et_underline_show, true)
        isFocusBackground =
            typedArray.hasValue(R.styleable.VerificationCodeView_zz_et_focus_background)
        mEtUnderLineDefaultColor = typedArray.getColor(
            R.styleable.VerificationCodeView_zz_et_underline_default_color,
            Color.parseColor("#E9E5E5")
        )
        mEtUnderLineFocusColor = typedArray.getColor(
            R.styleable.VerificationCodeView_zz_et_underline_focus_color,
            Color.parseColor("#FF3A3A")
        )
        mEtUnderLineHeight = typedArray.getDimensionPixelOffset(
            R.styleable.VerificationCodeView_zz_et_underline_height,
            dip2px(mContext, 0.5f)
        )
        mEtUnderLineShow =
            typedArray.getBoolean(R.styleable.VerificationCodeView_zz_et_underline_show, false)
        initView()
        typedArray.recycle()
    }

    private fun initView() {
        mRelativeLayout = arrayOfNulls(mEtNumber)
        mTextView = arrayOfNulls(mEtNumber)
        mUnderLineViews = arrayOfNulls(mEtNumber)
        mCursorViews = arrayOfNulls(mEtNumber)
        mLinearLayout = LinearLayout(mContext)
        mLinearLayout!!.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.addRule(CENTER_VERTICAL)
        mLinearLayout!!.layoutParams = layoutParams
        for (i in 0 until mEtNumber) {
            val relativeLayout = RelativeLayout(mContext)
            relativeLayout.layoutParams = getETLayoutParams(i)
            setEtBackground(relativeLayout, mEtBackground)
            mRelativeLayout[i] = relativeLayout
            val textView = TextView(mContext)
            initTextView(textView)
            relativeLayout.addView(textView)
            mTextView[i] = textView
            if (mEtUnderLineShow) {
                val ulView = View(mContext)
                initUnderLineView(ulView)
                relativeLayout.addView(ulView)
                mUnderLineViews[i] = ulView
            }
            val cursorView = View(mContext)
            initCursorView(cursorView)
            mCursorViews[i] = cursorView
            relativeLayout.addView(cursorView)
            mLinearLayout!!.addView(relativeLayout)
        }
        addView(mLinearLayout)
        mEditText = EditText(mContext)
        initEditText(mEditText!!)
        addView(mEditText)
        setCursorColor()
    }

    private fun getETLayoutParams(i: Int): LinearLayout.LayoutParams {
        val layoutParams = LinearLayout.LayoutParams(mEtWidth, mEtHeight)
        val space: Int
        //        不平分
        space = if (!isBisect) {
            mEtBisectSpacing
        } else {
            mEtSpacing
        }
        if (i == 0) {
            layoutParams.leftMargin = 0
            layoutParams.rightMargin = 0
        } else {
            layoutParams.leftMargin = space
            layoutParams.rightMargin = 0
        }
        return layoutParams
    }

    private fun setEtBackground(rl: RelativeLayout, background: Int) {
        if (background > 0) {
            rl.setBackgroundResource(background)
        } else {
            rl.setBackgroundColor(background)
        }
    }

    @SuppressLint("NewApi")
    private fun initTextView(textView: TextView) {
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.layoutParams = layoutParams
        textView.gravity = Gravity.CENTER
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.setTextColor(mEtTextColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mEtTextSize)
        setInputType(textView)
        textView.setPadding(0, 0, 0, 0)
    }

    private fun initUnderLineView(underLineView: View) {
        val layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mEtUnderLineHeight)
        layoutParams.addRule(ALIGN_PARENT_BOTTOM)
        if (mEtUnderLineShow) {
            underLineView.layoutParams = layoutParams
            underLineView.setBackgroundColor(mEtUnderLineDefaultColor)
        }
    }

    private fun initCursorView(cursorView: View) {
        val layoutParams =
            LayoutParams(mEtCursorWidth, mEtCursorHeight)
        layoutParams.addRule(CENTER_IN_PARENT)
        cursorView.layoutParams = layoutParams
    }

    private fun initEditText(editText: EditText) {
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.addRule(ALIGN_TOP, mLinearLayout!!.id)
        layoutParams.addRule(ALIGN_BOTTOM, mLinearLayout!!.id)
        editText.layoutParams = layoutParams
        setInputType(editText)
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.setTextColor(Color.TRANSPARENT)
        editText.isCursorVisible = false
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s != null && s.length > 0) {
                    editText.setText("")
                    sendCode(s.toString())
                }
            }
        })
        editText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && mCodes.size > 0) {
                mCodes.removeAt(mCodes.size - 1)
                showCode()
            }
            false
        }
    }

    private fun sendCode(s: String) {
        if (TextUtils.isEmpty(s)) {
            return
        }
        for (i in 0 until s.length) {
            if (mCodes.size < mEtNumber) {
                mCodes.add(s[i].toString())
            }
        }
        showCode()
        if (onInputListener == null) {
            return
        } else {
            if (mCodes.size == mEtNumber) {
                onInputListener!!.complete(code)
            } else {
                onInputListener!!.onInput()
            }
        }
    }

    private fun showCode() {
        for (i in 0 until mEtNumber) {
            val textView = mTextView[i]
            if (mCodes.size > i) {
                textView!!.text = mCodes[i]
            } else {
                textView!!.text = ""
            }
        }
        setCursorColor()
    }

    private fun setCursorColor() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
        }
        for (i in 0 until mEtNumber) {
            val cursorView = mCursorViews[i]
            cursorView!!.setBackgroundColor(Color.TRANSPARENT)
            if (mEtUnderLineShow) {
                mUnderLineViews[i]!!.setBackgroundColor(mEtUnderLineDefaultColor)
            }
            if (mEtBackground > 0) {
                mRelativeLayout[i]
                    ?.setBackground(mContext!!.resources.getDrawable(mEtBackground))
            }
        }
        if (mCodes.size < mEtNumber) {
            setCursorView(mCursorViews[mCodes.size])
            if (mEtUnderLineShow) {
                mUnderLineViews[mCodes.size]!!.setBackgroundColor(mEtUnderLineFocusColor)
            }
            if (isFocusBackground) {
                mRelativeLayout[mCodes.size]
                    ?.setBackground(mContext!!.resources.getDrawable(mEtFocusBackground))
            }
        }
    }

    private fun setInputType(textView: TextView) {
        when (etInputType) {
            ZZInputType.NUMBERPASSWORD -> {
                textView.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                textView.transformationMethod = PasswordTransformationMethod()
            }
            ZZInputType.TEXT -> textView.inputType = InputType.TYPE_CLASS_TEXT
            ZZInputType.TEXTPASSWORD -> {
                textView.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                textView.transformationMethod = PasswordTransformationMethod()
            }
            else -> textView.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    private fun setCursorView(view: View?) {
        valueAnimator = ObjectAnimator.ofInt(
            view,
            "backgroundColor",
            mEtCursorColor,
            Color.TRANSPARENT
        )
        (valueAnimator as ObjectAnimator?)?.setDuration(1000)
        (valueAnimator as ObjectAnimator?)?.setRepeatCount(-1)
        (valueAnimator as ObjectAnimator?)?.setRepeatMode(ValueAnimator.RESTART)
        (valueAnimator as ObjectAnimator?)?.setEvaluator({ fraction: Float, startValue: Any?, endValue: Any? -> if (fraction <= 0.5f) startValue else endValue })
        (valueAnimator as ObjectAnimator?)?.start()
    }

    fun dip2px(context: Context?, dpValue: Float): Int {
        val scale = context!!.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    //      回调
    interface OnInputListener {
        fun complete(code: String?)
        fun onInput()
    }

    fun setOnInputListener(onInputListener: OnInputListener?) {
        this.onInputListener = onInputListener
    }

    val code: String
        get() = if (mCodes.size == mEtNumber) {
            val sb = StringBuffer()
            for (code in mCodes) {
                sb.append(code)
            }
            sb.toString()
        } else {
            ""
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewWidth = measuredWidth
        mEtBisectSpacing = (mViewWidth - mEtNumber * mEtWidth) / (mEtNumber - 1)
        for (i in 0 until mEtNumber) {
            mLinearLayout!!.getChildAt(i).layoutParams = getETLayoutParams(i)
        }
    }

    //    清空验证码
    fun clearCode() {
        mCodes.clear()
        showCode()
    }
}