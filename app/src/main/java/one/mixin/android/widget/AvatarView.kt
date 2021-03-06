package one.mixin.android.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ViewAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.view_avatar.view.*
import one.mixin.android.R
import one.mixin.android.extension.loadCircleImage
import one.mixin.android.extension.loadImage

class AvatarView(context: Context, attrs: AttributeSet?) : ViewAnimator(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_avatar, this, true)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
        if (ta != null) {
            if (ta.hasValue(R.styleable.CircleImageView_border_width)) {
                avatar_simple.borderWidth = ta.getDimensionPixelSize(R.styleable.CircleImageView_border_width, 0)
                avatar_simple.borderColor = ta.getColor(R.styleable.CircleImageView_border_color,
                    ContextCompat.getColor(context, android.R.color.white))
                avatar_tv.setBorderInfo(avatar_simple.borderWidth.toFloat(), avatar_simple.borderColor)
            }
            ta.recycle()
        }
    }

    companion object {
        const val POS_TEXT = 0
        const val POS_AVATAR = 1
    }

    fun setGroup(url: String?) {
        displayedChild = POS_AVATAR
        Glide.with(this)
            .load(url)
            .apply(RequestOptions().dontAnimate().placeholder(R.drawable.ic_group_place_holder))
            .into(avatar_simple)
    }

    fun setUrl(url: String?, placeHolder: Int) {
        displayedChild = POS_AVATAR
        avatar_simple.loadCircleImage(url, placeHolder)
    }

    fun setInfo(text: Char, url: String?, id: String) {
        avatar_tv.text = text.toString()
        try {
            avatar_tv.setBackgroundResource(getAvatarPlaceHolderById(id.toInt()))
        } catch (e: NumberFormatException) {
        }
        displayedChild = if (url != null && url.isNotEmpty()) {
            avatar_simple.loadImage(url, R.drawable.ic_avatar_place_holder)
            POS_AVATAR
        } else {
            POS_TEXT
        }
    }

    fun setTextSize(size: Float) {
        avatar_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    private fun getAvatarPlaceHolderById(id: Int): Int {
        try {
            val num = id.rem(24) + 1
            return resources.getIdentifier("bg_avatar_$num", "drawable", context.packageName)
        } catch (e: Exception) {
        }
        return R.drawable.default_avatar
    }
}