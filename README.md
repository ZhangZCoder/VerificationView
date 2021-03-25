# VerificationView
##自定义验证码输入框，支持自定义背景样式，输入框间距，输入格式，输入框个数，焦点样式，光标样式，字体等
###效果如下
![image](https://github.com/ZhangZCoder/VerificationView/blob/master/example.png)
```
<resources>

    <declare-styleable name="VerificationCodeView">
        <attr name="zz_et_number" format="integer" />
        <attr name="zz_et_inputType">
            <enum name="number" value="0" />
            <enum name="numberPassword" value="1" />
            <enum name="text" value="2" />
            <enum name="textPassword" value="3" />
        </attr>
        <attr name="zz_et_width" format="dimension" />
        <attr name="zz_et_height" format="dimension" />
        <attr name="zz_et_textColor" format="color" />
        <attr name="zz_et_textSize" format="dimension" />
        <!--输入框间距，不输入则代表平分-->
        <attr name="zz_et_spacing" format="dimension" />
        <attr name="zz_et_background" format="color|reference" />
        <attr name="zz_et_focus_background" format="reference|color" />
        <attr name="zz_et_cursor_width" format="dimension" />
        <attr name="zz_et_cursor_height" format="dimension" />
        <attr name="zz_et_cursor_color" format="color" />
        <attr name="zz_et_underline_height" format="dimension" />
        <attr name="zz_et_underline_default_color" format="color" />
        <attr name="zz_et_underline_focus_color" format="color" />
        <attr name="zz_et_underline_show" format="boolean" />
    </declare-styleable>

</resources>
```

