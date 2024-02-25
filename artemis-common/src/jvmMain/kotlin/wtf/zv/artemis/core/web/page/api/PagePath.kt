package wtf.zv.artemis.core.web.page.api

data class PagePath internal constructor(private val path: String) {
    fun getPath() = path

    override fun toString() = path

    init {
        assert(path.startsWith("/")) {
            "Path: '$path' should start with '/'"
        }

        assert(path.isNotBlank()) {
            "Input path is empty"
        }
    }
}

fun ofPath(path: String) = PagePath(path)