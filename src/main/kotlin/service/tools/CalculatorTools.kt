package service.tools

import org.slf4j.LoggerFactory.getLogger
import org.springaicommunity.mcp.annotation.McpTool
import org.springaicommunity.mcp.annotation.McpToolParam
import org.springframework.stereotype.Component

@Component
class CalculatorTools {

    private val log = getLogger(javaClass)

    @McpTool(name = "add", description = "Add two integer numbers together")
    fun add(
        @McpToolParam(description = "First number", required = true) a: Int,
        @McpToolParam(description = "Second number", required = true) b: Int,
    ): Int {
        log.info("adding $a + $b")
        return a + b
    }
}
