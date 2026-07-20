package com.trio.presentation.launcher.components.shared

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.annotation.Keep
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

@Keep
data class LaunchableApp(
    val label: String,
    val packageName: String,
    val intent: Intent
)

fun LaunchableApp.loadIcon(context: Context): ImageBitmap? {
    return try {
        context.packageManager.getApplicationIcon(packageName)
            .toBitmap(96, 96)
            .asImageBitmap()
    } catch (_: Exception) {
        null
    }
}

fun queryLaunchableApps(context: Context): List<LaunchableApp> {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    return context.packageManager.queryIntentActivities(intent, 0)
        .sortedBy { it.loadLabel(context.packageManager).toString().lowercase() }
        .mapNotNull { resolveInfo ->
            val packageName = resolveInfo.activityInfo.packageName
            val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
                ?: buildFallbackIntent(context, resolveInfo)
                ?: return@mapNotNull null

            LaunchableApp(
                label = resolveInfo.loadLabel(context.packageManager).toString(),
                packageName = packageName,
                intent = launchIntent
            )
        }
}

private fun buildFallbackIntent(context: Context, resolveInfo: ResolveInfo): Intent? {
    return Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
        setClassName(
            resolveInfo.activityInfo.packageName,
            resolveInfo.activityInfo.name
        )
    }
}
