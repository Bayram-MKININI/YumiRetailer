package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft

class ProfileDataParentView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var shimmerView: ShimmerFrameLayout
    private lateinit var profileDataView: ProfileDataView
    val getProfileDataView get() = profileDataView

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        shimmerView = findViewById(R.id.shimmer_view)
        profileDataView = shimmerView.findViewById(R.id.profile_data_view)
    }

    fun setLoadingVisible(visible: Boolean) {
        shimmerView.setShimmer(
            Shimmer.AlphaHighlightBuilder()
                .setBaseAlpha(if (visible) 0.4f else 1f)
                .setDuration(resources.getInteger(R.integer.shimmer_animation_duration_ms).toLong())
                .build()
        )
        if (visible) {
            shimmerView.startShimmer()
        } else {
            shimmerView.stopShimmer()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        shimmerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        shimmerView.layoutToTopLeft(0, 0)
    }
}