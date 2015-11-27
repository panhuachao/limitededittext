package com.example.limitededittext.control;

import com.example.limitededittext.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
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
	//输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    
	private int errorColor=Color.RED;
	private int defColor=Color.BLACK;
	private boolean IsShowEmotion=false;
	
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
	    Log.i("textSize",textSize+"");
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
	    if(textSize!=0.0f)
	    {
	    	//除以density目的解决dimen自动乘以density问题http://blog.csdn.net/icewst/article/details/40651829
	    	etText.setTextSize(textSize/density);
	    }
	    if(tiptextSize!=0.0f)
	    {
	    	curText.setTextSize(tiptextSize/density);
	    	signText.setTextSize(tiptextSize/density);
	    	maxText.setTextSize(tiptextSize/density);
	    }
	    
	    maxText.setText(maxLength.toString());
	    curText.setText("0");
	    etText.setHint(hints);
	    etText.addTextChangedListener(new EditTextWatcher());
	}
	
	
	private class EditTextWatcher implements TextWatcher
	{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			 //if (!resetText) 
			 {
                 cursorPos = etText.getSelectionEnd();
                 // 这里用s.toString()而不直接用s是因为如果用s，
                 // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                 // inputAfterText也就改变了，那么表情过滤就失败了
                 inputAfterText= s.toString();
                 Log.i("inputAfterText", inputAfterText);
             }
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
                if (count >= 1&&IsShowEmotion==false) {
                	//表情符号的字符长度最小为2,是否开启表情显示
                	try
                	{
	                    CharSequence input = s.subSequence(cursorPos, cursorPos + count);
	                    Log.i("input", input.toString());
	                    if (containsEmoji(input.toString())) {
	                        resetText = true;
	                        //是表情符号就将文本还原为输入表情符号之前的内容
	                        etText.setText(inputAfterText);
	                        Log.i("inputAfterText", inputAfterText);
	                        CharSequence text = etText.getText();
	                        if (text instanceof Spannable) {
	                            Spannable spanText = (Spannable) text;
	                            Selection.setSelection(spanText, text.length());
	                        }
	                    }
                	}catch(Exception e)
                	{
                		e.printStackTrace();
                	}
                }
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
	 * 设置是否显示系统emotion
	 * @param _islimited
	 */
	public void setEmotionEnable(boolean _islimited)
	{
		IsShowEmotion=_islimited;
	}
	
	/***
	 * 设置默认背景
	 * @param background
	 */
	public void setBackgroundColor(int bgcolor)
	{
		etText.setBackgroundColor(bgcolor);
	}
	
	
	/**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
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
