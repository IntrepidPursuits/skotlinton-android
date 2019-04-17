object Version {
    val gitShortHash: String = "git rev-parse --short HEAD".executeShell().trim()
    val gitHash = "git rev-parse HEAD".executeShell()
    val buildNumber: String? = System.getenv("BUILD_NUMBER")

    fun versionSuffix(): String {
        if (!buildNumber.isNullOrBlank()) {
            return "." + buildNumber
        }

        if (!gitShortHash.isBlank()) {
            return "." + gitShortHash
        }

        return ""
    }
}
