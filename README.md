# waifu-bot
Basic Discord bot to send photos if a command matches an existing folder.

## Quick Compilation
Compiled with the following command after changing into src directory: `javac -cp ../jars/JDA-4.4.0_350-withDependencies-min.jar: com/armaanaki/WaifuBot.java`
Wrap into a standalone jar as follows from src directory: `unzip ../jars/JDA-4.4.0_350-withDependencies-min.jar ; jar cvmf ../manifest.txt ../waifu_bot.jar *` 
The jar should be located in the root of the project directory.

## Instructions
Download the latest release or compile it yourself.
Create a folder named "waifus" in the working directory
Run the jar with a command line arg of your bot's token: `java -jar waifu_bot.jar <token>`
