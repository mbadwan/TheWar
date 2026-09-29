import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class CopyAndroidNativesTask extends DefaultTask {
    @InputFiles
    final ConfigurableFileCollection natives = project.files()

    @OutputDirectory
    final DirectoryProperty outputDir = project.objects.directoryProperty()

    CopyAndroidNativesTask() {
        // Configure the output directory during the configuration phase
        outputDir.set(project.layout.buildDirectory.dir('natives'))
        // Assume sourceDir is configured elsewhere or passed in as a task property
    }

    @TaskAction
    def copy() {
        println 'Copying natives from $sourceDir to $outputDir...'
        project.copy {
            from sourceDir
            into outputDir
            include '**/*.so'
        }
    }
}