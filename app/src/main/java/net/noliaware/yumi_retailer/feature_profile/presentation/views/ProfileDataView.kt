package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.views.FillableTextWidget
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.getColorCompat
import net.noliaware.yumi_retailer.commun.util.getFontFromResources
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopRight
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.sizeForVisible
import kotlin.math.max

class ProfileDataView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var myDataTextView: TextView
    private lateinit var loginTitleTextView: TextView
    private lateinit var loginValueFillableTextWidget: FillableTextWidget
    private lateinit var retailerTitleTextView: TextView
    private lateinit var retailerValueFillableTextWidget: FillableTextWidget
    private lateinit var emailTitleTextView: TextView
    private lateinit var emailValueFillableTextWidget: FillableTextWidget
    private lateinit var phoneTitleTextView: TextView
    private lateinit var phoneValueFillableTextWidget: FillableTextWidget
    private lateinit var addressTitleTextView: TextView
    private lateinit var addressValueFillableTextWidget: FillableTextWidget

    private lateinit var separatorView: View
    private lateinit var boAccessTextView: TextView
    private lateinit var boAccessDescriptionFillableTextWidget: FillableTextWidget
    private lateinit var accessButtonLayout: LinearLayoutCompat
    private lateinit var privacyPolicyLinkTextView: TextView
    var callback: ProfileDataViewCallback? = null

    data class ProfileViewAdapter(
        val login: String = "",
        val retailer: String = "",
        val email: String = "",
        val phone: String = "",
        val address: String = "",
        val twoFactorAuthModeText: String = "",
        val twoFactorAuthModeActivated: Boolean = false
    )

    interface ProfileDataViewCallback {
        fun onGetCodeButtonClicked()
        fun onPrivacyPolicyButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        myDataTextView = findViewById(R.id.my_data_text_view)

        loginTitleTextView = findViewById(R.id.login_title_text_view)
        loginValueFillableTextWidget = findViewById(R.id.login_value_fillable_text_view)
        loginValueFillableTextWidget.setUpValueTextView()

        retailerTitleTextView = findViewById(R.id.retailer_title_text_view)
        retailerValueFillableTextWidget = findViewById(R.id.retailer_value_fillable_text_view)
        retailerValueFillableTextWidget.setUpValueTextView()

        emailTitleTextView = findViewById(R.id.email_title_text_view)
        emailValueFillableTextWidget = findViewById(R.id.email_value_fillable_text_view)
        emailValueFillableTextWidget.setUpValueTextView()

        phoneTitleTextView = findViewById(R.id.phone_title_text_view)
        phoneValueFillableTextWidget = findViewById(R.id.phone_value_fillable_text_view)
        phoneValueFillableTextWidget.setUpValueTextView()

        addressTitleTextView = findViewById(R.id.address_title_text_view)
        addressValueFillableTextWidget = findViewById(R.id.address_value_fillable_text_view)
        addressValueFillableTextWidget.setUpValueTextView()

        separatorView = findViewById(R.id.separator_view)
        boAccessTextView = findViewById(R.id.bo_access_text_view)
        boAccessDescriptionFillableTextWidget = findViewById(R.id.bo_access_description_fillable_text_view)
        boAccessDescriptionFillableTextWidget.setUpValueTextView()
        boAccessDescriptionFillableTextWidget.setFixedWidth(true)

        accessButtonLayout = findViewById(R.id.access_button_layout)
        accessButtonLayout.setOnClickListener {
            callback?.onGetCodeButtonClicked()
        }
        privacyPolicyLinkTextView = findViewById(R.id.privacy_policy_link_text_view)
        privacyPolicyLinkTextView.setOnClickListener {
            callback?.onPrivacyPolicyButtonClicked()
        }
    }

    private fun FillableTextWidget.setUpValueTextView() {
        textView.apply {
            typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
            setTextColor(context.getColorCompat(R.color.grey_2))
            textSize = 15f
        }
    }

    fun fillViewWithData(profileViewAdapter: ProfileViewAdapter) {

        loginValueFillableTextWidget.setText(profileViewAdapter.login)
        retailerValueFillableTextWidget.setText(profileViewAdapter.retailer)
        emailValueFillableTextWidget.setText(profileViewAdapter.email)
        phoneValueFillableTextWidget.setText(profileViewAdapter.phone)
        addressValueFillableTextWidget.setText(profileViewAdapter.address)

        boAccessDescriptionFillableTextWidget.setText(profileViewAdapter.twoFactorAuthModeText)
        if (profileViewAdapter.twoFactorAuthModeActivated) {
            boAccessDescriptionFillableTextWidget.textView.gravity = Gravity.CENTER
        }
        accessButtonLayout.isVisible = profileViewAdapter.twoFactorAuthModeActivated
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        myDataTextView.measureWrapContent()

        loginTitleTextView.measureWrapContent()
        loginValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 4 / 10)

        retailerTitleTextView.measureWrapContent()
        retailerValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 2 / 10)

        emailTitleTextView.measureWrapContent()
        emailValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 5 / 10)

        phoneTitleTextView.measureWrapContent()
        phoneValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        addressTitleTextView.measureWrapContent()
        addressValueFillableTextWidget.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 6 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(35), MeasureSpec.EXACTLY)
        )

        separatorView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        boAccessTextView.measureWrapContent()
        boAccessDescriptionFillableTextWidget.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(40), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(35), MeasureSpec.EXACTLY)
        )
        if (accessButtonLayout.isVisible) {
            accessButtonLayout.measureWrapContent()
        }

        privacyPolicyLinkTextView.measureWrapContent()

        val viewHeight = myDataTextView.measuredHeight +
                max(
                    loginTitleTextView.measuredHeight,
                    loginValueFillableTextWidget.measuredHeight
                ) +
                max(
                    retailerTitleTextView.measuredHeight,
                    retailerValueFillableTextWidget.measuredHeight
                ) +
                max(
                    emailTitleTextView.measuredHeight,
                    emailValueFillableTextWidget.measuredHeight
                ) +
                max(
                    phoneTitleTextView.measuredHeight,
                    phoneValueFillableTextWidget.measuredHeight
                ) +
                max(
                    addressTitleTextView.measuredHeight,
                    addressValueFillableTextWidget.measuredHeight
                ) +
                separatorView.measuredHeight + boAccessTextView.measuredHeight +
                boAccessDescriptionFillableTextWidget.measuredHeight +
                accessButtonLayout.sizeForVisible {
                    accessButtonLayout.measuredHeight + convertDpToPx(15)
                } + privacyPolicyLinkTextView.measuredHeight + convertDpToPx(125)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    private fun View.measureTextWidgetWithWidth(width: Int) {
        measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(18), MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        myDataTextView.layoutToTopLeft(
            convertDpToPx(20),
            0
        )

        val edge = viewWidth * 1 / 3

        loginTitleTextView.layoutToTopRight(
            edge,
            myDataTextView.bottom + convertDpToPx(15)
        )

        loginValueFillableTextWidget.layoutToTopLeft(
            loginTitleTextView.right + convertDpToPx(15),
            loginTitleTextView.top + (loginTitleTextView.measuredHeight - loginValueFillableTextWidget.measuredHeight) / 2
        )

        retailerTitleTextView.layoutToTopRight(
            edge,
            loginTitleTextView.bottom + convertDpToPx(10)
        )

        retailerValueFillableTextWidget.layoutToTopLeft(
            retailerTitleTextView.right + convertDpToPx(15),
            retailerTitleTextView.top + (retailerTitleTextView.measuredHeight - retailerValueFillableTextWidget.measuredHeight) / 2
        )

        emailTitleTextView.layoutToTopRight(
            edge,
            retailerTitleTextView.bottom + convertDpToPx(10)
        )

        emailValueFillableTextWidget.layoutToTopLeft(
            emailTitleTextView.right + convertDpToPx(15),
            emailTitleTextView.top + (emailTitleTextView.measuredHeight - emailValueFillableTextWidget.measuredHeight) / 2
        )

        phoneTitleTextView.layoutToTopRight(
            edge,
            emailValueFillableTextWidget.bottom + convertDpToPx(10)
        )

        phoneValueFillableTextWidget.layoutToTopLeft(
            phoneTitleTextView.right + convertDpToPx(15),
            phoneTitleTextView.top + (phoneTitleTextView.measuredHeight - phoneValueFillableTextWidget.measuredHeight) / 2
        )

        addressTitleTextView.layoutToTopRight(
            edge,
            phoneValueFillableTextWidget.bottom + convertDpToPx(10)
        )

        addressValueFillableTextWidget.layoutToTopLeft(
            addressTitleTextView.right + convertDpToPx(15),
            addressTitleTextView.top
        )

        separatorView.layoutToTopLeft(
            (viewWidth - separatorView.measuredWidth) / 2,
            max(addressTitleTextView.bottom, addressValueFillableTextWidget.bottom) + convertDpToPx(
                15
            )
        )

        boAccessTextView.layoutToTopLeft(
            myDataTextView.left,
            separatorView.bottom + convertDpToPx(15)
        )

        boAccessDescriptionFillableTextWidget.layoutToTopLeft(
            myDataTextView.left,
            boAccessTextView.bottom + convertDpToPx(10)
        )

        val boAccessBottom = if (accessButtonLayout.isVisible) {
            accessButtonLayout.layoutToTopLeft(
                (viewWidth - accessButtonLayout.measuredWidth) / 2,
                boAccessDescriptionFillableTextWidget.bottom + convertDpToPx(15)
            )
            accessButtonLayout.bottom
        } else {
            boAccessDescriptionFillableTextWidget.bottom
        }

        privacyPolicyLinkTextView.layoutToTopLeft(
            (viewWidth - privacyPolicyLinkTextView.measuredWidth) / 2,
            boAccessBottom + convertDpToPx(30)
        )
    }
}