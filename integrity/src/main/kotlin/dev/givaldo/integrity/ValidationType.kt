package dev.givaldo.integrity

enum class ValidationType {
    AppSignature,
    AppPackageName,
    DeveloperModeEnabled,
    Emulator,
    CloningAppsInstalled,
    Root,
    RootAppsInstalled,
    AdbEnabled,
    PackageVerifierDisabled,
    VirtualizationInstalledApps,
    InstallationDir,
    VirtualLibraryPresent
}