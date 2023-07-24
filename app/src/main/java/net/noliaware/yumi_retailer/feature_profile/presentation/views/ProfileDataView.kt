package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopRight
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.weak
import kotlin.math.max

class ProfileDataView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var myDataTextView: TextView
    private lateinit var loginTitleTextView: TextView
    private lateinit var loginValueTextView: TextView
    private lateinit var retailerTitleTextView: TextView
    private lateinit var retailerValueTextView: TextView
    private lateinit var emailTitleTextView: TextView
    private lateinit var emailValueTextView: TextView
    private lateinit var phoneTitleTextView: TextView
    private lateinit var phoneValueTextView: TextView
    private lateinit var addressTitleTextView: TextView
    private lateinit var addressValueTextView: TextView

    private lateinit var separatorView: View
    private lateinit var boAccessTextView: TextView
    private lateinit var boAccessDescriptionTextView: TextView
    private lateinit var accessButtonLayout: LinearLayoutCompat
    private lateinit var privacyPolicyLinkTextView: TextView

    var callback: ProfileDataViewCallback? by weak()

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
        loginValueTextView = findViewById(R.id.login_value_text_view)
        retailerTitleTextView = findViewById(R.id.retailer_title_text_view)
        retailerValueTextView = findViewById(R.id.retailer_value_text_view)
        emailTitleTextView = findViewById(R.id.email_title_text_view)
        emailValueTextView = findViewById(R.id.email_value_text_view)
        phoneTitleTextView = findViewById(R.id.phone_title_text_view)
        phoneValueTextView = findViewById(R.id.phone_value_text_view)
        addressTitleTextView = findViewById(R.id.address_title_text_view)
        addressValueTextView = findViewById(R.id.address_value_text_view)

        separatorView = findViewById(R.id.separator_view)
        boAccessTextView = findViewById(R.id.bo_access_text_view)
        boAccessDescriptionTextView = findViewById(R.id.bo_access_description_text_view)
        accessButtonLayout = findViewById(R.id.access_button_layout)
        accessButtonLayout.setOnClickListener {
            callback?.onGetCodeButtonClicked()
        }
        privacyPolicyLinkTextView = findViewById(R.id.privacy_policy_link_text_view)
        privacyPolicyLinkTextView.setOnClickListener {
            callback?.onPrivacyPolicyButtonClicked()
        }
    }

    fun fillViewWithData(profileViewAdapter: ProfileViewAdapter) {

        loginValueTextView.text = profileViewAdapter.login
        retailerValueTextView.text = profileViewAdapter.retailer
        emailValueTextView.text = profileViewAdapter.email
        phoneValueTextView.text = profileViewAdapter.phone
        addressValueTextView.text = profileViewAdapter.address

        boAccessDescriptionTextView.text = profileViewAdapter.twoFactorAuthModeText
        if (profileViewAdapter.twoFactorAuthModeActivated) {
            boAccessDescriptionTextView.gravity = Gravity.CENTER
        }
        accessButtonLayout.isVisible = profileViewAdapter.twoFactorAuthModeActivated
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        myDataTextView.measureWrapContent()

        loginTitleTextView.measureWrapContent()
        loginValueTextView.measureWrapContent()

        retailerTitleTextView.measureWrapContent()
        retailerValueTextView.measureWrapContent()

        emailTitleTextView.measureWrapContent()
        emailValueTextView.measureWrapContent()

        phoneTitleTextView.measureWrapContent()
        phoneValueTextView.measureWrapContent()

        addressTitleTextView.measureWrapContent()
        addressValueTextView.measureWrapContent()

        separatorView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        boAccessTextView.measureWrapContent()
        boAccessDescriptionTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth - convertDpToPx(40), MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        if (accessButtonLayout.isVisible) {
            accessButtonLayout.measureWrapContent()
        }

        privacyPolicyLinkTextView.measureWrapContent()

        val viewHeight = myDataTextView.measuredHeight + loginValueTextView.measuredHeight +
                retailerValueTextView.measuredHeight + emailValueTextView.measuredHeight +
                phoneValueTextView.measuredHeight + addressValueTextView.measuredHeight +
                separatorView.measuredHeight + boAccessTextView.measuredHeight +
                boAccessDescriptionTextView.measuredHeight +
                if (accessButtonLayout.isVisible) {
                    accessButtonLayout.measuredHeight + convertDpToPx(15)
                } else {
                    0
                } +
                privacyPolicyLinkTextView.measuredHeight + convertDpToPx(125)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
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

        loginValueTextView.layoutToTopLeft(
            loginTitleTextView.right + convertDpToPx(15),
            loginTitleTextView.top
        )

        retailerTitleTextView.layoutToTopRight(
            edge,
            loginTitleTextView.bottom + convertDpToPx(10)
        )

        retailerValueTextView.layoutToTopLeft(
            retailerTitleTextView.right + convertDpToPx(15),
            retailerTitleTextView.top
        )

        emailTitleTextView.layoutToTopRight(
            edge,
            retailerTitleTextView.bottom + convertDpToPx(10)
        )

        emailValueTextView.layoutToTopLeft(
            emailTitleTextView.right + convertDpToPx(15),
            emailTitleTextView.top
        )

        phoneTitleTextView.layoutToTopRight(
            edge,
            emailValueTextView.bottom + convertDpToPx(10)
        )

        phoneValueTextView.layoutToTopLeft(
            phoneTitleTextView.right + convertDpToPx(15),
            phoneTitleTextView.top
        )

        addressTitleTextView.layoutToTopRight(
            edge,
            phoneValueTextView.bottom + convertDpToPx(10)
        )

        addressValueTextView.layoutToTopLeft(
            addressTitleTextView.right + convertDpToPx(15),
            addressTitleTextView.top
        )

        separatorView.layoutToTopLeft(
            (viewWidth - separatorView.measuredWidth) / 2,
            max(addressTitleTextView.bottom, addressValueTextView.bottom) + convertDpToPx(15)
        )

        boAccessTextView.layoutToTopLeft(
            myDataTextView.left,
            separatorView.bottom + convertDpToPx(15)
        )

        boAccessDescriptionTextView.layoutToTopLeft(
            myDataTextView.left,
            boAccessTextView.bottom + convertDpToPx(10)
        )

        val boAccessBottom = if (accessButtonLayout.isVisible) {
            accessButtonLayout.layoutToTopLeft(
                (viewWidth - accessButtonLayout.measuredWidth) / 2,
                boAccessDescriptionTextView.bottom + convertDpToPx(15)
            )
            accessButtonLayout.bottom
        } else {
            boAccessDescriptionTextView.bottom
        }

        privacyPolicyLinkTextView.layoutToTopLeft(
            (viewWidth - privacyPolicyLinkTextView.measuredWidth) / 2,
            boAccessBottom + convertDpToPx(30)
        )
    }
}