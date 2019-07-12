import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findPlugin
import java.util.Properties

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

fun allVariants(project: Project): DomainObjectSet<out BaseVariant>? {
    val plugins = project.plugins
    plugins.findPlugin(AppPlugin::class)?.let {
        val appExtension = it.extension as AppExtension
        return appExtension.applicationVariants
    }
    plugins.findPlugin(LibraryPlugin::class)?.let {
        val libraryExtension = it.extension as LibraryExtension
        return libraryExtension.libraryVariants
    }
    throw Exception("The project must use either android or android-library plugin")
}
