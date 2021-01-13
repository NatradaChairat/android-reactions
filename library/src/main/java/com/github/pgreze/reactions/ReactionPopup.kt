package com.github.pgreze.reactions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow

/**
 * Entry point for reaction popup.
 */
class ReactionPopup @JvmOverloads constructor(
    context: Context,
    var reactionsConfig: ReactionsConfig,
    var reactionClickListener: ReactionClickListener? = null,
    var reactionSelectedListener: ReactionSelectedListener? = null
) : PopupWindow(context), View.OnClickListener, View.OnLongClickListener {

    private val rootView = FrameLayout(context).also {
        it.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
    private val view: ReactionViewGroup by lazy(LazyThreadSafetyMode.NONE) {
        // Lazily inflate content during first display
        ReactionViewGroup(context, reactionsConfig).also {
            it.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )

            it.reactionSelectedListener = reactionSelectedListener

            // Just add the view,
            // it will position itself depending on the display preference.
            rootView.addView(it)

            it.dismissListener = ::dismiss
        }
    }

    init {
        contentView = rootView
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun dismiss() {
        view.dismiss()
        super.dismiss()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onLongClick(v: View?): Boolean {
        if (!isShowing) {
            // Show fullscreen with button as context provider
            showAtLocation(v, Gravity.NO_GRAVITY, 0, 0)
            if (v != null) {
                view.show(v)
            }
        }
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onClick(v: View?) {
        reactionClickListener?.invoke()
    }

}
