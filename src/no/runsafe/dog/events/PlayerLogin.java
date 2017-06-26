package no.runsafe.dog.events;

import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.player.IPlayerJoinEvent;
import no.runsafe.framework.api.event.player.IPlayerPreLoginEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerJoinEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerLogin implements IPlayerPreLoginEvent, IPlayerJoinEvent
{
	public PlayerLogin(Speech speechCenter, IScheduler scheduler)
	{
		this.speechCenter = speechCenter;
		this.scheduler = scheduler;
	}

	@Override
	public void OnBeforePlayerLogin(RunsafePlayerPreLoginEvent event)
	{
		IPlayer player = event.getPlayer();

		if (player.isNew())
			this.playersToWelcome.add(player);
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event)
	{
		final IPlayer player = event.getPlayer();
		if (this.playersToWelcome.contains(player))
			this.scheduler.startSyncTask(() -> welcomePlayer(player), 2);
	}

	private void welcomePlayer(IPlayer player)
	{
		this.speechCenter.Speak(String.format("Welcome to the server, %s.", player.getPrettyName()));
		this.playersToWelcome.remove(player);
	}

	private final Speech speechCenter;
	private final List<IPlayer> playersToWelcome = new ArrayList<>();
	private final IScheduler scheduler;
}
