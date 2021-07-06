package us.ajg0702.queue.api;

import com.google.common.collect.ImmutableList;
import us.ajg0702.queue.api.players.AdaptedPlayer;
import us.ajg0702.queue.api.players.QueuePlayer;
import us.ajg0702.queue.api.queues.QueueServer;

public interface QueueManager {

    /**
     * Adds a player to a queue
     * @param player The player to be added
     * @param server The server or group to add the player to
     * @return True if adding was successfull, false if not.
     */
    boolean addToQueue(AdaptedPlayer player, QueueServer server);

    /**
     * Adds a player to a queue
     * @param player The player to be added
     * @param serverName The name of the server or group to add the player too
     * @return True if adding was successfull, false if not.
     */
    boolean addToQueue(AdaptedPlayer player, String serverName);

    /**
     * Gets a list of QueueServers (servers and groups)
     * @return A list of QueueServerss
     */
    ImmutableList<QueueServer> getServers();

    /**
     * Gets a list of QueueServer (servers and groups) names
     * @return A list of QueueServer names
     */
    ImmutableList<String> getServerNames();

    /**
     * Get a single server the player is queued for. Depends on the multi-server-queue-pick option in the config
     * @param player The player
     * @return The server that was chosen that the player is queued for.
     */
    QueueServer getSingleServer(AdaptedPlayer player);

    /**
     * Get the name of the server the player is queued for.
     * If multiple servers are queued for, it will use the multi-server-queue-pick option in the config
     * @param player The player
     * @return The name of the server, the placeholder none message if not queued
     */
    String getQueuedName(AdaptedPlayer player);

    /**
     * Checks servers that are in bungeecord and adds any it doesnt
     * know about.
     *
     * Also creates/edits server groups
     */
    void reloadServers();

    /**
     * Sends queue status action bars to players in queues
     */
    void sendActionBars();

    /**
     * Tell the spigot sides to call the queue scoreboard event
     */
    void sendQueueEvents();

    /**
     * Sends chat queue status messages to players in queues
     */
    void sendMessages();

    /**
     * Send a chat queue status message to a specific player in a specific queue
     * @param player The player that is in the queue
     */
    void sendMessage(QueuePlayer player);

    /**
     * Find a server by its name
     * @param name The name to look for
     * @return The QueueServer if found, null if not
     */
    QueueServer findServer(String name);

    /**
     * Attempts to send the first player in all queues to the server they are queued for
     */
    void sendPlayers();

    /**
     * Attempts to send the first player in a specific queue
     * @param server The queue that we should try to send.
     */
    void sendPlayers(QueueServer server);

    /**
     * Finds QueuePlayers that represent this player
     * @param p The player to look up
     * @return A list of QueuePlayers that represent this player
     */
    ImmutableList<QueuePlayer> findPlayerInQueues(AdaptedPlayer p);

    /**
     * Gets all of the queues the player is currently queued for
     * @param p The player
     * @return A list of QueueServers that this player is queued for
     */
    ImmutableList<QueueServer> getPlayerQueues(AdaptedPlayer p);
}
