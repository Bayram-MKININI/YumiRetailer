package net.noliaware.yumi_retailer.commun.util

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.noliaware.yumi_retailer.BuildConfig
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.ApiParameters.APP_VERSION
import net.noliaware.yumi_retailer.commun.ApiParameters.DEVICE_ID
import net.noliaware.yumi_retailer.commun.ApiParameters.LOGIN
import net.noliaware.yumi_retailer.commun.ApiParameters.SESSION_ID
import net.noliaware.yumi_retailer.commun.ApiParameters.SESSION_TOKEN
import net.noliaware.yumi_retailer.commun.DateTime.DATE_SOURCE_FORMAT
import net.noliaware.yumi_retailer.commun.DateTime.MINUTES_TIME_FORMAT
import net.noliaware.yumi_retailer.commun.DateTime.TIME_SOURCE_FORMAT
import net.noliaware.yumi_retailer.commun.data.remote.dto.AppMessageDTO
import net.noliaware.yumi_retailer.commun.data.remote.dto.ErrorDTO
import net.noliaware.yumi_retailer.commun.data.remote.dto.SessionDTO
import net.noliaware.yumi_retailer.commun.domain.model.AppMessageType
import net.noliaware.yumi_retailer.commun.domain.model.SessionData
import retrofit2.HttpException
import java.io.IOException
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun isNetworkReachable(
    context: Context
): Boolean {
    val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}

fun currentTimeInMillis() = System.currentTimeMillis().toString()

fun Exception.recordNonFatal() {
    if (BuildConfig.DEBUG) {
        printStackTrace()
    } else {
        FirebaseCrashlytics.getInstance().recordException(this)
    }
}

fun randomString(
    len: Int = 36
): String {
    val random = SecureRandom()
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    return (1..len).map { chars[random.nextInt(chars.size)] }.joinToString("")
}

fun generateToken(
    timestamp: String,
    methodName: String,
    randomString: String
) = "noliaware|$timestamp|${methodName}|${timestamp.reversed()}|$randomString".sha256()

fun String.isLoginNotValid() = contains("[^A-Za-z0-9@_.-]".toRegex())

fun getCommonWSParams(
    sessionData: SessionData,
    tokenKey: String
) = mapOf(
    LOGIN to sessionData.login,
    APP_VERSION to BuildConfig.VERSION_NAME,
    DEVICE_ID to sessionData.deviceId,
    SESSION_ID to sessionData.sessionId,
    SESSION_TOKEN to sessionData.sessionTokens[tokenKey].toString()
)

suspend fun <T> FlowCollector<Resource<T>>.handleSessionWithNoFailure(
    session: SessionDTO?,
    sessionData: SessionData,
    tokenKey: String,
    appMessage: AppMessageDTO?,
    error: ErrorDTO?
): Boolean {
    val errorType = session?.let { sessionDTO ->
        sessionData.apply {
            sessionId = sessionDTO.sessionId
            sessionTokens[tokenKey] = sessionDTO.sessionToken
        }
        ErrorType.RECOVERABLE_ERROR
    } ?: run {
        ErrorType.SYSTEM_ERROR
    }
    error?.let { errorDTO ->
        emit(
            Resource.Error(
                errorType = errorType,
                errorMessage = errorDTO.errorMessage,
                appMessage = appMessage?.toAppMessage()
            )
        )
        return false
    } ?: run {
        return true
    }
}

fun resolvePaginatedListErrorIfAny(
    session: SessionDTO?,
    sessionData: SessionData,
    tokenKey: String
): ErrorType {
    val errorType = session?.let { sessionDTO ->
        sessionData.apply {
            sessionId = sessionDTO.sessionId
            sessionTokens[tokenKey] = sessionDTO.sessionToken
        }
        ErrorType.RECOVERABLE_ERROR
    } ?: run {
        ErrorType.SYSTEM_ERROR
    }
    return errorType
}

inline fun <reified T : Any, reified K : Any> handlePagingSourceError(
    ex1: Exception
): PagingSource.LoadResult.Error<T, K> {
    ex1.recordNonFatal()
    try {
        when (ex1) {
            is HttpException -> ErrorType.SYSTEM_ERROR
            is IOException -> ErrorType.NETWORK_ERROR
            else -> null
        }?.let { errorType ->
            throw PaginationException(errorType)
        } ?: run {
            return PagingSource.LoadResult.Error(ex1)
        }
    } catch (ex2: Exception) {
        return PagingSource.LoadResult.Error(ex2)
    }
}

suspend inline fun <reified T : Any> FlowCollector<Resource<T>>.handleRemoteCallError(
    ex: Exception
) {
    ex.recordNonFatal()
    when (ex) {
        is HttpException -> ErrorType.SYSTEM_ERROR
        is IOException -> ErrorType.NETWORK_ERROR
        else -> null
    }?.let {
        emit(Resource.Error(errorType = it))
    }
}

fun String.parseDateToFormat(
    destFormat: String
) = parseDateStringToFormat(this, DATE_SOURCE_FORMAT, destFormat).orEmpty()

fun String.parseTimeToFormat(
    destFormat: String
) = parseDateStringToFormat(this, TIME_SOURCE_FORMAT, destFormat).orEmpty()

private fun parseDateStringToFormat(
    sourceDate: String,
    sourceFormat: String,
    destFormat: String
): String? {
    val sourceFormatter = SimpleDateFormat(sourceFormat, Locale.FRANCE)
    val date = sourceFormatter.parse(sourceDate)
    val destFormatter = SimpleDateFormat(destFormat, Locale.FRANCE)
    date?.let {
        return destFormatter.format(it)
    }
    return null
}

fun Int.parseSecondsToMinutesString(): String = SimpleDateFormat(
    MINUTES_TIME_FORMAT,
    Locale.FRANCE
).format(this * 1000L)

fun Fragment.handleSharedEvent(
    sharedEvent: UIEvent
) = context?.let {
    when (sharedEvent) {
        is UIEvent.ShowAppMessage -> {
            val appMessage = sharedEvent.appMessage
            when (appMessage.type) {
                AppMessageType.POPUP -> {
                    MaterialAlertDialogBuilder(it)
                        .setTitle(appMessage.title)
                        .setMessage(appMessage.body)
                        .setPositiveButton(R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .create()
                        .apply {
                            setCanceledOnTouchOutside(false)
                            show()
                        }
                }
                AppMessageType.SNACKBAR -> {
                    Snackbar.make(
                        requireView(),
                        appMessage.body,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                AppMessageType.TOAST -> {
                    context.toast(appMessage.body)
                }
                else -> Unit
            }
        }
        is UIEvent.ShowError -> {
            context.toast(sharedEvent.errorStrRes)
        }
    }
}

fun Fragment.redirectToLoginScreenFromSharedEvent(
    sharedEvent: UIEvent
) {
    if (sharedEvent is UIEvent.ShowError) {
        if (sharedEvent.errorType == ErrorType.SYSTEM_ERROR) {
            redirectToLoginScreenInternal()
        }
    }
}

fun Fragment.handlePaginationError(
    loadState: CombinedLoadStates
): Boolean {
    when {
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        else -> null
    }?.let {
        if (it.error is PaginationException) {
            if ((it.error as PaginationException).errorType == ErrorType.SYSTEM_ERROR) {
                redirectToLoginScreenInternal()
                return true
            } else if ((it.error as PaginationException).errorType == ErrorType.NETWORK_ERROR) {
                handleSharedEvent(
                    UIEvent.ShowError(
                        errorType = ErrorType.NETWORK_ERROR,
                        errorStrRes = R.string.error_no_network
                    )
                )
                return true
            }
        }
    }
    return false
}

fun <T> Flow<T>.collectLifecycleAware(
    owner: LifecycleOwner,
    action: suspend (value: T) -> Unit
) {
    owner.lifecycleScope.launch {
        this@collectLifecycleAware.flowWithLifecycle(owner.lifecycle).collectLatest {
            action.invoke(it)
        }
    }
}

private fun Fragment.redirectToLoginScreenInternal() {
    (activity?.supportFragmentManager?.findFragmentById(
        R.id.app_nav_host_fragment
    ) as? NavHostFragment)?.findNavController()?.setGraph(R.navigation.app_nav_graph)
}

fun NavController.safeNavigate(
    direction: NavDirections
) = currentDestination?.getAction(direction.actionId)?.run {
    navigate(direction)
}

fun Fragment.navDismiss() {
    findNavController().navigateUp()
}

fun <T : Fragment> T.withArgs(
    vararg pairs: Pair<String, Any?>
) = apply { arguments = bundleOf(*pairs) }

fun ViewGroup.inflate(
    layoutRes: Int,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun Context.drawableIdByName(
    resIdName: String?
): Int {
    resIdName?.let {
        return resources.getIdentifier(it, "drawable", packageName)
    }
    throw Resources.NotFoundException()
}

fun View.getStatusBarHeight() = convertDpToPx(24)

fun View.measureWrapContent() {
    measure(
        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    )
}

fun View.translateYByValue(
    value: Float
): ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", value)

fun Context?.toast(
    text: CharSequence,
    duration: Int = Toast.LENGTH_LONG
) = this?.let {
    Toast.makeText(it, text, duration).show()
}

fun Context?.toast(
    @StringRes textId: Int,
    duration: Int = Toast.LENGTH_LONG
) = this?.let {
    Toast.makeText(it, textId, duration).show()
}

fun View.layoutToTopLeft(
    left: Int,
    top: Int
) {
    val right = left + measuredWidth
    val bottom = top + measuredHeight
    layout(left, top, right, bottom)
}

fun View.layoutToTopRight(
    right: Int,
    top: Int
) {
    val left = right - measuredWidth
    val bottom = top + measuredHeight
    layout(left, top, right, bottom)
}

fun View.layoutToBottomLeft(
    left: Int,
    bottom: Int
) {
    val right = left + measuredWidth
    val top = bottom - measuredHeight
    layout(left, top, right, bottom)
}

fun View.layoutToBottomRight(
    right: Int,
    bottom: Int
) {
    val left = right - measuredWidth
    val top = bottom - measuredHeight
    layout(left, top, right, bottom)
}

fun View.getLocationRectOnScreen(): Rect {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return Rect().apply {
        left = location[0]
        top = location[1]
        right = left + measuredWidth
        bottom = top + measuredHeight
    }
}

fun ShimmerFrameLayout.activateShimmer(
    activated: Boolean
) {
    Shimmer.AlphaHighlightBuilder()
        .setBaseAlpha(if (activated) 0.4f else 1f)
        .setDuration(resources.getInteger(R.integer.shimmer_animation_duration_ms).toLong())
        .build().apply {
            setShimmer(this)
        }
    if (activated) {
        startShimmer()
    } else {
        stopShimmer()
    }
}

fun ViewPager2.removeOverScroll() {
    (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
}

fun View.convertDpToPx(
    dpValue: Int
) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dpValue.toFloat(), resources.displayMetrics
).toInt()

@JvmOverloads
@Dimension(unit = Dimension.PX)
fun Number.dpToPx(
    metrics: DisplayMetrics = Resources.getSystem().displayMetrics
): Float {
    return toFloat() * metrics.density
}

@JvmOverloads
@Dimension(unit = Dimension.DP)
fun Number.pxToDp(
    metrics: DisplayMetrics = Resources.getSystem().displayMetrics
): Float {
    return toFloat() / metrics.density
}

fun Context.showKeyboard() {
    (this as? Activity)?.let {
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.ime())
    }
}

fun Context.hideKeyboard() {
    (this as? Activity)?.let {
        WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
    }
}

inline fun <reified T : View> View.find(id: Int): T = findViewById(id)
inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id)
inline fun <reified T : View> Fragment.find(id: Int): T = view?.findViewById(id) as T
inline fun <reified T : View> RecyclerView.ViewHolder.find(
    id: Int
): T = itemView.findViewById(id) as T
inline fun <reified T : View> View.findOptional(id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Activity.findOptional(id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Fragment.findOptional(id: Int): T? = view?.findViewById(id) as? T
inline fun <reified T : View> RecyclerView.ViewHolder.findOptional(
    id: Int
): T? = itemView.findViewById(id) as? T

fun String.sha256(): String {
    return try {
        val md = MessageDigest.getInstance("SHA-256")
        val messageDigest = md.digest(this.toByteArray(StandardCharsets.UTF_8))
        val number = BigInteger(1, messageDigest)
        val hexString = StringBuilder(number.toString(16))
        while (hexString.length < 64) {
            hexString.insert(0, '0')
        }
        hexString.toString()

    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
    }
}

@ColorInt
fun Context.getColorCompat(
    @ColorRes colorRes: Int
) = ContextCompat.getColor(this, colorRes)

@ColorInt
fun String.parseHexColor() = if (isEmpty()) {
    Color.TRANSPARENT
} else {
    Color.parseColor(this)
}

@CheckResult
fun Drawable.tint(
    @ColorInt color: Int
): Drawable {
    val tintedDrawable = DrawableCompat.wrap(this).mutate()
    DrawableCompat.setTint(tintedDrawable, color)
    return tintedDrawable
}

fun Number.formatNumber(): String = NumberFormat.getNumberInstance(Locale.getDefault()).format(this)

fun Context.startWebBrowserAtURL(
    url: String
) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }.run {
        startActivity(this)
    }
}