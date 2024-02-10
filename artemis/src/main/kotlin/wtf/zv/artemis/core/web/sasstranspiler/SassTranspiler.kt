package wtf.zv.artemis.core.web.sasstranspiler

import kotlinx.css.output
import java.lang.ProcessBuilder.Redirect.PIPE

/** Transpiles Sass markup into browser-compliant CSS. */
object SassTranspiler {
    fun transpileSass(input: String): String {
        require(checkSassBinaryExistence()) {
            SassTranspilationException("'sass' binary not found in \$PATH")
        }

        val process = ProcessBuilder(SASS_BINARY_LOCATION, SASS_BINARY_ARGUMENTS)
            .redirectInput(PIPE)
            .redirectOutput(PIPE)
            .start()

        // Use the extension function to write the input string to the process
        process.outputStream.use { it.write(input.toByteArray()) }

        val exitCode = process.waitFor()

        return if (exitCode == SUCCESS_EXIT_CODE) {
            process.inputStream.bufferedReader().readText()
        } else {
            // Handle compilation error
            val errorOutput = process.errorStream.bufferedReader().readText()

            throw SassTranspilationException("Sass compilation failed with exit code $exitCode:\n$errorOutput")
        }
    }

    class SassTranspilationException(message: String) : RuntimeException(message)

    private fun checkSassBinaryExistence(): Boolean {
        return true
//        return Files.exists(sassPath) && Files.isExecutable(sassPath)
    }

    private const val SASS_BINARY_LOCATION = "sass"
    private const val SASS_BINARY_ARGUMENTS = "--stdin"
    private const val SUCCESS_EXIT_CODE = 0;
}