package com.armaanaki;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Random;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDA;

public class WaifuBot extends ListenerAdapter {
	
	//hashmap to store directories and files inside it
	private static HashMap<String, File[]> waifuMap = new HashMap<String, File[]>();
	
	//RNG for picture files
	private static Random rng = new Random();
	
	//filter for images only
	private final static FilenameFilter IMAGES = new FilenameFilter() {
		public boolean accept(File dir, String name) {
				String[] extensions = {"gif", "png", "jpg", "bmp"};
				for (String ext : extensions) 
					if (name.endsWith(ext)) { return true; }
				return false;
		}
	};
	
	//String array used for random function
	private static String[] waifuIndexes;

	public static void main(String[] args) throws IllegalArgumentException, LoginException, RateLimitedException { 
		//choose the picture folder, then get all directories from the folder
		File waifusFolder = new File("./waifus");
		File[] waifus = waifusFolder.listFiles(File::isDirectory);
		
		//insert a directory with an array of all files inside it inside a hashmap
		for (File waifu : waifus) 
			waifuMap.put(waifu.getName(), waifu.listFiles(IMAGES));
		
		waifuIndexes = waifuMap.keySet().toArray(new String[waifuMap.size()]);
		
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
			File[] waifus = waifuMap.get(msg);
			event.getChannel().sendFile(waifus[rng.nextInt(waifus.length)]).queue();
		} else if (msg.equals("rand")) {
			String theChosenOne = waifuIndexes[rng.nextInt(waifuIndexes.length)];
			File[] waifus = waifuMap.get(theChosenOne);
			event.getChannel().sendFile(waifus[rng.nextInt(waifus.length)]).queue();
		}
	}
}
