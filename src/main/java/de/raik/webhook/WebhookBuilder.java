package de.raik.webhook;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.raik.webhook.elements.AllowedMentions;
import de.raik.webhook.elements.Embed;

import java.util.ArrayList;

/**
 * Class that easily creates
 * a webhook object
 *
 * @author Raik
 * @version 1.0
 */
public class WebhookBuilder {

    /**
     * The url the message will be posted to
     */
    private final String url;

    /**
     * The message content of the webhook
     */
    private String content;

    /**
     * The username of the webhook
     */
    private String username;

    /**
     * The url to the avatar the
     * webhook will have
     */
    private String avatarURL;

    /**
     * Is the message is a
     * tts message
     */
    private boolean tts = false;

    /**
     * The embeds the message
     * will contain
     */
    private final ArrayList<Embed> embeds = new ArrayList<>();

    /**
     * The allowed mentions
     */
    private AllowedMentions allowedMentions;

    /**
     * Constructor which creates the
     * webhook builder
     *
     * @param url The url the webhook will be sent to
     */
    public WebhookBuilder(String url) {
        this.url = url;
    }

    /**
     * Set the message content
     *
     * @param content The content the message content will be set to
     * @return Itself to continuing modifying
     */
    public WebhookBuilder content(String content) {
        this.content = content;
        return this;
    }

    /**
     * Set the username of the webhook
     *
     * @param username The username of the webhook
     * @return Itself to continuing modifying
     */
    public WebhookBuilder username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Set the avatar url of the webhook
     *
     * @param avatarURL The url of the webhook
     * @return Itself to continuing modifying
     */
    public WebhookBuilder avatar(String avatarURL) {
        this.avatarURL = avatarURL;
        return this;
    }

    /**
     * Enable tts
     *
     * @return Itself to continuing modifying
     */
    public WebhookBuilder tts() {
        this.tts = true;
        return this;
    }

    /**
     * Adding an embed to the message
     *
     * @param embed The embed which will be added
     * @return Itself to continuing modifying
     */
    public WebhookBuilder addEmbed(Embed embed) {
        /*
         * Only adding embed if the count is smaller then 10
         * Because they are limited to 10
         */
        if (this.embeds.size() < 10)
            this.embeds.add(embed);


        return this;
    }

    /**
     * Setting the allowed mentions
     *
     * @param allowedMentions The allowed mentions it will be set to
     * @return Itself to continuing modifying
     */
    public WebhookBuilder allowedMentions(AllowedMentions allowedMentions) {
        this.allowedMentions = allowedMentions;
        return this;
    }

    /**
     * Building the webhook
     * which was set up in this builder
     * Using JsonObject to create the payload
     * @see com.google.gson.JsonObject
     * @see Webhook
     *
     * @return The Webhook which was created
     *          return null on not buildable webhook
     */
    public Webhook build() {
        //Cancelling if not buildable
        if (!this.isBuildable())
            return null;

        // Creating json payload
        JsonObject payload = new JsonObject();

        if (this.content != null)
            payload.addProperty("content", this.content);
        if (this.username != null)
            payload.addProperty("username", this.username);
        if (this.avatarURL != null)
            payload.addProperty("avatar_url", this.avatarURL);
        payload.addProperty("tts", this.tts);

        JsonArray embedArray = new JsonArray();
        this.embeds.forEach(embed -> {
            JsonObject embedObject = embed.exportJson();
            if (embedObject.size() > 0)
                embedArray.add(embedObject);
        });

        payload.add("embeds", embedArray);

        if (this.allowedMentions != null)
            payload.add("allowed_mentions", this.allowedMentions.exportToJson());

        //Returning new webhook
        return new Webhook(this.url, payload);
    }

    /**
     * Check if the builder can build the webhook
     * Not buildable if content and embed is empty
     *
     * Also not buildable if url is none
     *
     * @return The value if it is buildable
     */
    private boolean isBuildable() {
        return (this.content != null || !this.embeds.isEmpty()) && this.url != null;
    }
}
