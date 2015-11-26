package com.example.limitededittext.control;

import com.example.limitededittext.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LimitedEditTextControl extends RelativeLayout {
	private Context mContext;
	private RelativeLayout baseView;
	private LimitedEditTextControl curThis;
	private EditText etText;
	private TextView curText;
	private TextView maxText;
	private TextView signText;
	
	private Integer maxLength;
	private Integer curLength;
	
	private int errorColor=Color.RED;
	private int defColor=Color.BLACK;
	
	public LimitedEditTextControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext=context;
		curThis=this;
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		baseView=(RelativeLayout)inflater.inflate(R.layout.control_limitededittext,this,true);
		etText=(EditText)baseView.findViewById(R.id.control_limitededittext_edit);
		curText=(TextView)baseView.findViewById(R.id.control_limitededittext_curlen);
		maxText=(TextView)baseView.findViewById(R.id.control_limitededittext_maxlen);
		signText=(TextView)baseView.findViewById(R.id.control_limitededittext_sign);
		
		TypedArray typeArray = context.obtainStyledAttributes(attrs,R.styleable.limitededittext);     
        /*这里从集合里取出相对应的属性值,第二参数是如果使用者没用配置该属性时所用的默认值*/  
	    String hints = typeArray.getString(R.styleable.limitededittext_hint);    
	    boolean isSingleLine = typeArray.getBoolean(R.styleable.limitededittext_singleLine,false);
	    int minline = typeArray.getInteger(R.styleable.limitededittext_minLines,0);
	    int maxline = typeArray.getInteger(R.styleable.limitededittext_maxLines,0);
	    int background = typeArray.getResourceId(R.styleable.limitededittext_background,0);
	    float textSize = typeArray.getDimension(R.styleable.limitededittext_textSize,0.0f);
	    float tiptextSize = typeArray.getDimension(R.styleable.limitededittext_tipTextSize,0.0f);
	    
	    maxLength = typeArray.getInteger(R.styleable.limitededittext_maxLength,0);
	    curLength=0;
	    etText.setSingleLine(isSingleLine);
	    if(minline!=0)
	    {
	    	etText.setMinLines(minline);
	    }
	    if(maxline!=0)
	    {
	    	etText.setMinLines(maxline);
	    }
	    if(background!=0)
	    {
	    	etText.setBackgroundResource(background);
	    }
	    //获取屏幕密度
	    float density=DisPlayMetricsUtils.getDensity(mContext);
	    if(textSize!=0)
	    {
	    	etText.setTextSize(textSize/density);
	    }
	    if(tiptextSize!=0)
	    {
	    	curText.setTextSize(tiptextSize/density);
	    	signText.setTextSize(tiptextSize/density);
	    	maxText.setTextSize(tiptextSize/density);
	    }
	    
	    maxText.setText(maxLength.toString());
	    curText.setText("0");
	    curText.setHint(hints);
	    etText.addTextChangedListener(new EditTextWatcher());
	}
	
	
	private class EditTextWatcher implements TextWatcher
	{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			curLength=getText().length();
			if(curLength>maxLength)
			{
				//超过限制字数，字体变红
				curThis.setError();
			}else
			{
				curThis.setDefColor();
			}
			curText.setText(curLength.toString());
		}
		
	}
	
	
	public String getText()
	{
		return etText.getText().toString();
	}
	
	public Boolean isValid()
	{
		if(curLength>maxLength)
		{
			return false;
		}else
		{
			return true;
		}
	}
	
	/***
	 * 设置红色
	 */
	private void setError()
	{
		curText.setTextColor(errorColor);
		signText.setTextColor(errorColor);
		maxText.setTextColor(errorColor);
		etText.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.limitedet_error));
	}
	/**
	 * 设置默认字体颜色
	 */
	private void setDefColor()
	{
		curText.setTextColor(defColor);
		signText.setTextColor(defColor);
		maxText.setTextColor(defColor);
		etText.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.limitedet_normal));
	}
}
