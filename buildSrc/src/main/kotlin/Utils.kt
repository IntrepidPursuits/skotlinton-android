import org.gradle.api.Project
import java.util.*

private var localProperties: Properties? = null

fun getLocalProperty(propertyName: String, project: Project): String {
    // lazy loading so that we don't open the file multiple times
    if (localProperties == null) {
        localProperties = Properties().apply {
            val localPropertiesFile = project.rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                load(localPropertiesFile.inputStream())
            }
        }
    }

    val property = localProperties?.getProperty(propertyName)
    return property ?: ""
}

fun Project.getLocalProperty(propertyName: String): String {
    return getLocalProperty(propertyName, this)
}

fun String.executeShell(): String {
    return Runtime.getRuntime().exec(this).inputStream.bufferedReader().readText()
}
