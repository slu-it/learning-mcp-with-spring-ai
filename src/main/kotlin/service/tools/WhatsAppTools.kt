package service.tools

import org.slf4j.LoggerFactory.getLogger
import org.springaicommunity.mcp.annotation.McpTool
import org.springaicommunity.mcp.annotation.McpToolParam
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class WhatsAppTools {

    private val log = getLogger(javaClass)
    private val contactNumbers = mapOf(
        "john" to "555 123456",
        "jane" to "555 654321",
    )

    @McpTool(
        name = "send_whatsapp_message",
        title = "Send WhatsApp Message",
        description = "Sends a message to a WhatsApp contact by phone number."
    )
    fun sendMessage(
        @McpToolParam(description = "Recipient's phone number.", required = true) phoneNumber: String,
        @McpToolParam(description = "Message to send.", required = true) message: String,
    ): String {
        logUser()
        log.info("send message to $phoneNumber: $message")
        return "Message was sent."
    }

    @McpTool(
        name = "get_phone_number_of_contact",
        title = "Get Phone Number of Contact",
        description = "Look up the phone number for a contact by name. Returns a not-found message if the contact has no number."
    )
    fun getPhoneNumberByName(
        @McpToolParam(description = "Contact name to look up", required = true) name: String,
    ): String {
        logUser()
        val number = contactNumbers[name.trim().lowercase()]
        log.info("returning number for \"$name\": $number")
        return number ?: "No phone number found for \"$name\""
    }

    private fun logUser() {
        val jwt = (SecurityContextHolder.getContext().authentication as JwtAuthenticationToken).token
        log.debug("User: {}", jwt.subject)
        log.debug("Claims: {}", jwt.claims)
    }
}
