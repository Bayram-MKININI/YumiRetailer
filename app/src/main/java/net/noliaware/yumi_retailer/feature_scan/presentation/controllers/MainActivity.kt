package net.noliaware.yumi_retailer.feature_scan.presentation.controllers

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.ACCOUNT_DATA
import net.noliaware.yumi_retailer.commun.ACTION_PUSH_DATA
import net.noliaware.yumi_retailer.commun.PUSH_BODY
import net.noliaware.yumi_retailer.commun.PUSH_TITLE
import net.noliaware.yumi_retailer.commun.util.getSerializableExtraCompat
import net.noliaware.yumi_retailer.feature_login.domain.model.AccountData

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent.getSerializableExtraCompat<AccountData>(ACCOUNT_DATA)?.let { accountData ->
            supportFragmentManager.beginTransaction().run {
                replace(R.id.main_fragment_container, HomeFragment.newInstance(accountData))
                commit()
            }
        }
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            intent.extras?.let {
                val title = it.getString(PUSH_TITLE)
                val body = it.getString(PUSH_BODY)
                context?.let {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle(title)
                        .setMessage(body)
                        .setPositiveButton(R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            messageReceiver,
            IntentFilter(ACTION_PUSH_DATA)
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }

    /**
     * 2. This function will check if the CAMERA permission has been granted.
     * If so, it will call the function responsible to initialize the camera preview.
     * Otherwise, it will raise an alert.
     */
    fun checkIfCameraPermissionIsGranted() =
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) { // Permission granted
            true
        } else {
            // Permission denied
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.permission_required)
                .setMessage(R.string.camera_permission_needed)
                .setPositiveButton(R.string.ok) { _, _ ->
                    // Keep asking for permission until granted
                    checkCameraPermission()
                }
                .setCancelable(false)
                .create()
                .apply {
                    setCanceledOnTouchOutside(false)
                    show()
                }

            false
        }

    private fun checkCameraPermission() {
        try {
            val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, requiredPermissions, 0)
        } catch (e: IllegalArgumentException) {
            checkIfCameraPermissionIsGranted()
        }
    }

    /**
     * 3. This function is executed once the user has granted or denied the missing permission
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkIfCameraPermissionIsGranted()
    }
}