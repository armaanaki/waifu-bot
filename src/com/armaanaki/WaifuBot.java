package com.armaanaki;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDA;

public class WaifuBot extends ListenerAdapter {
	
	// hashmap to store directories and files inside it
	private static HashMap<String, File[]> waifuMap = new HashMap<String, File[]>();

    // Store help message so it only has to be created once
    private static String helpMessage;
	
	// RNG for picture files
	private static Random rng = new Random();
	
	// filter for images only
	private final static FilenameFilter IMAGES = new FilenameFilter() {
		public boolean accept(File dir, String name) {
				String[] extensions = {"gif", "png", "jpg", "bmp"};
				for (String ext : extensions) 
					if (name.endsWith(ext)) { return true; }
				return false;
		}
	};
	
	// String array used for random function
	private static String[] waifuIndexes;

    // HashMap to avoid repeating waifus on each iteration
    private static HashMap<String, ArrayList<Integer>> repeatCounter = new HashMap<String, ArrayList<Integer>>();

    // max number of different images before a repeat can occur
    private static final int repeatCounterSize = 2;

	public static void main(String[] args) throws IllegalArgumentException, LoginException, RateLimitedException { 
		//choose the picture folder, then get all directories from the folder
		File waifusFolder = new File("./waifus");
		File[] waifus = waifusFolder.listFiles(File::isDirectory);
		
		//insert a directory with an array of all files inside it inside a hashmap and initialize repeatCounter
		for (File waifu : waifus) { 
			waifuMap.put(waifu.getName(), waifu.listFiles(IMAGES));
            repeatCounter.put(waifu.getName(), new ArrayList<Integer>());
        }
		
		waifuIndexes = waifuMap.keySet().toArray(new String[waifuMap.size()]);
		
        // create the help message
        helpMessage = "**waifu bot at your service**\n**Commands:**\n\t**w!help**: Print this help page.\n\t**w!rand**: Print a random waifu.";
        for (String waifu : waifuMap.keySet())
            helpMessage += "\n\t**w!" + waifu + "**";

		//setup bot to listen
	    JDA api = JDABuilder.createDefault(args[0])
			.addEventListeners(new WaifuBot())
			.build();
			
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		//return if a bot sent the message
		if (event.getAuthor().isBot()) return;
		
		//return if the message does not have the correct prefix
		String msg = event.getMessage().getContentDisplay();
		if (!msg.startsWith("w!")) return;
		
		//remove the w! from the message and make it all lowercase
		msg = msg.substring(2).toLowerCase();
		
		//if a waifu exists after the prefix, send it
		if (waifuMap.containsKey(msg)) {
            sendWaifu(event, msg);
		} else if (msg.equals("rand")) {
			String theChosenOne = waifuIndexes[rng.nextInt(waifuIndexes.length)];
            sendWaifu(event, theChosenOne);
		} else if (msg.equals("help")) {
            event.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage(helpMessage)).queue();
        }
	}

    // method to send images
    private static void sendWaifu(GuildMessageReceivedEvent event, String waifu) {
        File[] waifus = waifuMap.get(waifu);
        File waifuToSend;

        // loop until a not previously selected image is chosen
        while (true) {
            int random = rng.nextInt(waifus.length);
            ArrayList<Integer> repeats = repeatCounter.get(waifu); 
            if (repeats.contains(random)) continue; 
            waifuToSend = waifus[random];
            repeats.add(random);
            if (repeats.size() > repeatCounterSize) repeats.remove(0); 
            break; 
        }
        event.getChannel().sendFile(waifuToSend).queue();
    }
}
