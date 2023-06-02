package net.noliaware.yumi_retailer.feature_login.presentation.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.ViewAnimator
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.getLocationRectOnScreen
import net.noliaware.yumi_retailer.commun.util.layoutToBottomLeft

open class LoginLayout(
    context: Context,
    attrs: AttributeSet?
) : ViewAnimator(context, attrs) {

    private lateinit var loginViewContainer: View
    private val visibleRect = Rect()

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        loginViewContainer = findViewById(R.id.login_view_container)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val viewWidth = right - left
        post {
            getWindowVisibleDisplayFrame(visibleRect)
            val loginViewRect = loginViewContainer.getLocationRectOnScreen()
            if (loginViewRect.bottom > visibleRect.bottom) {
                loginViewContainer.layoutToBottomLeft(
                    (viewWidth - loginViewContainer.measuredWidth) / 2,
                    visibleRect.bottom + convertDpToPx(30)
                )
            }
        }
    }
}