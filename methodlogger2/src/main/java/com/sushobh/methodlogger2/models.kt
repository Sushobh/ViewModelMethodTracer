import java.time.Instant

internal data class LogViewItem(
    override var className: String,
    override var methodName: String,
    var count: Int
) : LogItem(className, methodName)

internal fun LogViewItem.displayableClassName() : String {
    return className.split("/").last()
}
open class LogItem(
    open val className: String,
    open val methodName: String,
    open val loggedTime: Instant = Instant.now()
)
