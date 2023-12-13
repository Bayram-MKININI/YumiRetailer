package net.noliaware.yumi_retailer.feature_scan.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent

class HomeMenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MaterialCardView(context, attrs, defStyle) {

    private lateinit var homeImageView: ImageView
    private lateinit var profileImageView: ImageView
    private lateinit var mailImageView: ImageView
    private lateinit var mailBadgeTextView: TextView
    private lateinit var notificationImageView: ImageView
    private lateinit var notificationBadgeTextView: TextView
    var callback: HomeMenuViewCallback? = null

    interface HomeMenuViewCallback {
        fun onHomeButtonClicked()
        fun onProfileButtonClicked()
        fun onMailButtonClicked()
        fun onNotificationButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {

        homeImageView = findViewById(R.id.home_image_view)
        homeImageView.setOnClickListener(onButtonClickListener)

        profileImageView = findViewById(R.id.profile_image_view)
        profileImageView.setOnClickListener(onButtonClickListener)

        mailImageView = findViewById(R.id.mail_image_view)
        mailImageView.setOnClickListener(onButtonClickListener)
        mailBadgeTextView = findViewById(R.id.mail_badge_text_view)

        notificationImageView = findViewById(R.id.notification_image_view)
        notificationImageView.setOnClickListener(onButtonClickListener)
        notificationBadgeTextView = findViewById(R.id.notification_badge_text_view)
    }

    private val onButtonClickListener: OnClickListener by lazy {
        OnClickListener {
            when (it.id) {
                R.id.home_image_view -> {
                    if (homeImageView.isSelected) {
                        return@OnClickListener
                    }
                    resetAllBackgrounds()
                    selectHomeButton()
                    callback?.onHomeButtonClicked()
                }
                R.id.profile_image_view -> {
                    if (profileImageView.isSelected) {
                        return@OnClickListener
                    }
                    resetAllBackgrounds()
                    profileImageView.setBackgroundResource(R.drawable.circle_primary)
                    profileImageView.isSelected = true
                    callback?.onProfileButtonClicked()
                }
                R.id.mail_image_view -> {
                    if (mailImageView.isSelected) {
                        return@OnClickListener
                    }
                    resetAllBackgrounds()
                    mailImageView.setBackgroundResource(R.drawable.circle_primary)
                    mailImageView.isSelected = true
                    callback?.onMailButtonClicked()
                }
                R.id.notification_image_view -> {
                    if (notificationImageView.isSelected) {
                        return@OnClickListener
                    }
                    resetAllBackgrounds()
                    notificationImageView.setBackgroundResource(R.drawable.circle_primary)
                    notificationImageView.isSelected = true
                    callback?.onNotificationButtonClicked()
                }
            }
        }
    }

    fun performClickOnHomeButton() {
        onButtonClickListener.onClick(homeImageView)
    }

    fun selectHomeButton() {
        homeImageView.setBackgroundResource(R.drawable.circle_primary)
        homeImageView.isSelected = true
    }

    fun setBadgeForMailButton(number: Int) {
        displayBadgeAnimated(mailBadgeTextView)
        mailBadgeTextView.text = number.toString()
    }

    fun hideMailButtonBadge() {
        hideBadgeAnimated(mailBadgeTextView)
    }

    fun setBadgeForNotificationButton(number: Int) {
        displayBadgeAnimated(notificationBadgeTextView)
        notificationBadgeTextView.text = number.toString()
    }

    fun hideNotificationButtonBadge() {
        hideBadgeAnimated(notificationBadgeTextView)
    }

    private fun displayBadgeAnimated(badge: View) {
        ScaleAnimation(
            0f,
            1f,
            0f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            1f
        ).apply {
            duration = 600
            interpolator = OvershootInterpolator(3f)
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    badge.isVisible = true
                }
                override fun onAnimationRepeat(animation: Animation) = Unit
                override fun onAnimationEnd(animation: Animation) = Unit
            })
            badge.startAnimation(this)
        }
    }

    private fun hideBadgeAnimated(badge: View) {
        if (badge.isGone) {
            return
        }
        ScaleAnimation(
            1f,
            0f,
            1f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.2f,
            Animation.RELATIVE_TO_SELF,
            0.7f
        ).apply {
            duration = 500
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) = Unit
                override fun onAnimationRepeat(animation: Animation) = Unit
                override fun onAnimationEnd(animation: Animation) {
                    badge.isGone = true
                }
            })
            badge.startAnimation(this)
        }
    }

    private fun resetAllBackgrounds() {

        homeImageView.setBackgroundResource(0)
        homeImageView.isSelected = false

        profileImageView.setBackgroundResource(0)
        profileImageView.isSelected = false

        mailImageView.setBackgroundResource(0)
        mailImageView.isSelected = false

        notificationImageView.setBackgroundResource(0)
        notificationImageView.isSelected = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        measureMenuButton(homeImageView)
        measureMenuButton(profileImageView)
        measureMenuButton(mailImageView)
        mailBadgeTextView.measureWrapContent()
        measureMenuButton(notificationImageView)
        notificationBadgeTextView.measureWrapContent()

        viewWidth = homeImageView.measuredWidth * 13 / 2

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(64), MeasureSpec.EXACTLY)
        )
    }

    private fun measureMenuButton(menuButton: View) {
        menuButton.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(48), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(48), MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val viewWidth = right - left
        val viewHeight = bottom - top

        val emptySpace = homeImageView.measuredWidth / 2

        homeImageView.layoutToTopLeft(
            emptySpace,
            (viewHeight - homeImageView.measuredHeight) / 2
        )

        profileImageView.layoutToTopLeft(
            homeImageView.right + emptySpace,
            (viewHeight - profileImageView.measuredHeight) / 2
        )

        mailImageView.layoutToTopLeft(
            profileImageView.right + emptySpace,
            (viewHeight - mailImageView.measuredHeight) / 2
        )

        mailBadgeTextView.layoutToTopLeft(
            mailImageView.right - mailImageView.measuredWidth * 4 / 10,
            mailImageView.top + mailBadgeTextView.measuredHeight * 3 / 10
        )

        notificationImageView.layoutToTopLeft(
            mailImageView.right + emptySpace,
            (viewHeight - notificationImageView.measuredHeight) / 2
        )

        notificationBadgeTextView.layoutToTopLeft(
            notificationImageView.right - notificationImageView.measuredWidth * 5 / 10,
            notificationImageView.top + notificationBadgeTextView.measuredHeight * 3 / 10
        )
    }
}