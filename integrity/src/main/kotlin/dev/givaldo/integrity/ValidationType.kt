package dev.givaldo.integrity

enum class ValidationType(
    val description: String
) {
    AppSignature("App signature"),
    AppPackageName("App package name"),
    DeveloperModeEnabled("Developer mode enabled"),
    Emulator("App is running on emulator"),
    CloningAppsInstalled("Any clone app installed"),
    Root("Is device rooted"),
    RootAppsInstalled("Any root app installed"),
    AdbEnabled("Android Debug Bridge enabled"),
    PackageVerifierDisabled("Package verifier disabled"),
    VirtualizationInstalledApps("Any virtual app installed"),
    InstallationDir("Installation directory"),
    VirtualLibraryPresent("Virtual library present"),
}