package dev.givaldo.integrity.util

import android.content.Context
import android.content.pm.PackageManager

internal fun getInstalledPackages(context: Context): Set<String> {
    val packages = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        .map { it.packageName }
    return packages.toSet()
}

internal fun checkIsAnyInstalled(context: Context, packages: Set<String>): List<String> {
    return getInstalledPackages(context).filter { packages.contains(it) }
}