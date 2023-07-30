package api.page

class PagePath
internal constructor(private val path: String) {
    public fun getPath() = path
}

fun ofPath(path: String): PagePath {
    assert(path.startsWith("/")) {
        "Path: '$path' should start with '/'"
    }

    assert(path.endsWith('/')) {
        "Path: '$path' should end with '/'"
    }

    assert(path.isNotBlank()) {
        "Input path is empty"
    }

    return PagePath(path)
}