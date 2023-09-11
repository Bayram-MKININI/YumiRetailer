package net.noliaware.yumi_retailer.commun

import net.noliaware.yumi_retailer.BuildConfig

object ApiConstants {
    const val BASE_ENDPOINT = "https://api.noliaware.net/yumi/retailer/"
    const val INIT = "init"
    const val CONNECT = "connect"
    const val SET_PRIVACY_POLICY_READ_STATUS = "setPrivacyPolicyReadStatus"
    const val USE_VOUCHER = "useVoucher"
    const val GET_ACCOUNT = "getAccount"
    const val GET_BACK_OFFICE_SIGN_IN_CODE = "getBackOfficeSignInCode"
    const val GET_VOUCHER_DATA_PER_CATEGORY = "getVoucherDataPerCategory"
    const val GET_PRODUCT_DATA_PER_CATEGORY = "getProductDataPerCategory"
    const val GET_PRODUCT_LIST_BY_CATEGORY = "getProductListByCategory"
    const val GET_AVAILABLE_VOUCHER_LIST_BY_CATEGORY = "getAvailableVoucherListByCategory"
    const val GET_USED_VOUCHER_LIST_BY_CATEGORY = "getUsedVoucherListByCategory"
    const val GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY = "getCanceledVoucherListByCategory"
    const val GET_ALERT_LIST = "getAlertList"
    const val GET_INBOX_MESSAGE_LIST = "getInboxMessageList"
    const val GET_INBOX_MESSAGE = "getInboxMessage"
    const val DELETE_INBOX_MESSAGE = "delInboxMessage"
    const val GET_OUTBOX_MESSAGE_LIST = "getOutboxMessageList"
    const val GET_OUTBOX_MESSAGE = "getOutboxMessage"
    const val DELETE_OUTBOX_MESSAGE = "delOutboxMessage"
    const val SEND_MESSAGE = "sendMessage"
}

object ApiParameters {
    const val LOGIN = "login"
    const val PASSWORD = "password"
    const val APP_VERSION = "appVersion"
    const val DEVICE_ID = "deviceId"
    const val PUSH_TOKEN = "devicePushToken"
    const val DEVICE_TYPE = "deviceType"
    const val DEVICE_OS = "deviceOS"
    const val DEVICE_UUID = "deviceUUID"
    const val DEVICE_LABEL = "deviceLabel"
    const val SESSION_ID = "sessionId"
    const val SESSION_TOKEN = "sessionToken"
    const val TIMESTAMP = "timestamp"
    const val SALT_STRING = "saltString"
    const val TOKEN = "token"
    const val VOUCHER_USE_ID = "voucherUseId"
    const val LIMIT = "limit"
    const val LIST_PAGE_SIZE = 20
    const val OFFSET = "offset"
    const val MESSAGE_ID = "messageId"
    const val MESSAGE_PRIORITY = "messagePriority"
    const val MESSAGE_SUBJECT_ID = "messageSubjectId"
    const val MESSAGE_BODY = "messageBody"
    const val MESSAGE = "message"
    const val TIMESTAMP_OFFSET = "timestampOffset"
}

object Args {
    const val DATA_SHOULD_REFRESH = "dataShouldRefresh"
    const val ACCOUNT_DATA = "account_data"
    const val PRIVACY_POLICY_URL = "privacy_policy_url"
    const val PRIVACY_POLICY_CONFIRMATION_REQUIRED = "privacy_policy_confirmation_required"
    const val MESSAGE_SUBJECTS_DATA = "message_subjects_data"
    const val CATEGORY = "category"
    const val CATEGORY_ID = "categoryId"
    const val CATEGORY_COLOR = "categoryColor"
    const val CATEGORY_ICON = "categoryIcon"
    const val VOUCHER_LIST_TYPE = "voucherListType"
}

object Push {
    const val ACTION_PUSH_DATA = BuildConfig.APPLICATION_ID + ".action.PUSH"
    const val PUSH_TITLE = "title"
    const val PUSH_BODY = "body"
}

object DateTime {
    const val DATE_TIME_SOURCE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS"
    const val DATE_SOURCE_FORMAT = "yyyy-MM-dd"
    const val TIME_SOURCE_FORMAT = "HH:mm:ss"
    const val NUMERICAL_DATE_FORMAT = "dd/MM/yyyy"
    const val SINGLE_DAY_DATE_FORMAT = "EEE"
    const val DAY_OF_MONTH_NUMERICAL_DATE_FORMAT = "dd/MM"
    const val LONG_DATE_WITH_DAY_FORMAT = "EEEE dd LLLL yyyy"
    const val SHORT_DATE_FORMAT = "dd LLL yyyy"
    const val HOURS_TIME_FORMAT = "HH:mm"
    const val MINUTES_TIME_FORMAT = "mm:ss"
}

object RemoteConfig {
    const val KEY_CURRENT_VERSION = "android_force_update_current_version"
    const val KEY_FORCE_UPDATE_REQUIRED = "android_force_update_required"
    const val KEY_FORCE_UPDATE_URL = "android_force_update_store_url"
}

object FragmentTags {
    const val PRIVACY_POLICY_FRAGMENT_TAG = "privacy_policy_fragment"
    const val PRODUCTS_LIST_FRAGMENT_TAG = "products_list_fragment"
    const val BO_SIGN_IN_FRAGMENT_TAG = "bo_sign_in_fragment"
    const val VOUCHERS_OVERVIEW_FRAGMENT_TAG = "vouchers_overview_fragment"
    const val READ_MESSAGE_FRAGMENT_TAG = "read_message_fragment"
    const val SEND_MESSAGES_FRAGMENT_TAG = "send_messages_fragment"
}

object UI {
    const val GOLDEN_RATIO = 1.6180339887
}