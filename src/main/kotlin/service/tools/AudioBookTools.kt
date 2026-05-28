package service.tools

import org.slf4j.LoggerFactory.getLogger
import org.springaicommunity.mcp.annotation.McpTool
import org.springaicommunity.mcp.annotation.McpToolParam
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class AudioBookTools {

    private val log = getLogger(javaClass)

    // in-memory state
    private var currentAudioBook: String? = null

    @McpTool(name = "get_current_audiobook", description = "Returns the current audiobook the user is listening to.")
    fun get(): String? {
        logUser()
        log.info("getting current audio book $currentAudioBook")
        return currentAudioBook
    }

    @McpTool(name = "set_current_audiobook", description = "Changes the audiobook the user is listening to.")
    fun set(
        @McpToolParam(description = "book name", required = true) name: String
    ) {
        logUser()
        log.info("set current audio book to $name")
        currentAudioBook = name.takeUnless { it.isBlank() }
    }

    private fun logUser() {
        val jwt = (SecurityContextHolder.getContext().authentication as JwtAuthenticationToken).token
        log.info("User: ${jwt.subject}")
        log.info("Claims: ${jwt.claims}")
    }
}
