package com.yc.utils;

import java.util.Random;
import java.util.regex.Pattern;

public class Utils
{
	static private Utils singleton;
	
	private static final String lCase = "abcdefghijklmnopqrstuvwxyz";
	private static final String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String sChar = "@#$%&_*";
	private static final String intChar = "0123456789";
	
	public static final String ALLOWED_SPL_CHARACTERS = "!@#$%^&*()_+";
	public static final String ERROR_CODE = "ERRONEOUS_SPECIAL_CHARS";

	private static StringBuilder pass = new StringBuilder();
	
	private Utils()
	{
		
	}
	
	public static synchronized Utils getInstance()
	{
		if (singleton == null)
		{
			singleton = new Utils();
		}
		
		return singleton;
	}
	
	public String generateHash()
	{
		while (pass.length() != 16)
		{
			Random r = new Random();

			int rPick = r.nextInt(4);
			if (rPick == 0)
			{
				pass.append(lCase.charAt(r.nextInt(26)));
			} 
			else if (rPick == 1)
			{
				pass.append(uCase.charAt(r.nextInt(26)));
			} 
			else if (rPick == 2)
			{
				pass.append(sChar.charAt(r.nextInt(6)));
			} 
			else
			{
				pass.append(intChar.charAt(r.nextInt(10)));
			}
		}
		
		return pass.toString();
	}
	
	public String generateAlphaNumericHash()
	{
		while (pass.length() != 16)
		{
			Random r = new Random();
			
			int rPick = r.nextInt(4);
			if (rPick == 0)
			{
				pass.append(lCase.charAt(r.nextInt(26)));
			} 
			else if (rPick == 1)
			{
				pass.append(uCase.charAt(r.nextInt(26)));
			} 
			else
			{
				pass.append(intChar.charAt(r.nextInt(10)));
			}
		}
		
		return pass.toString();
	}
	
	public String snakeToCamelCase(String phrase)
	{
		String [] tokens = phrase.split(Pattern.quote("_"));
		StringBuffer buffer = new StringBuffer(tokens[0]);
		for (int index = 1; index < tokens.length; index++)
		{
			String token = tokens[index];
			buffer.append(String.valueOf(token.charAt(index)).concat(token.substring(1)));
		}
		return buffer.toString();
	}
	
	public boolean isOnlyAlph(String str)
	{
		return ((str != null) && (!str.equals("")) && (str.matches("^[a-zA-Z]*$")));
	}
    
    public boolean isOnlyAlphNum(String str)
    {
        return ((str != null) && (!str.equals("")) && (str.matches("^[a-z0-9]*$")));
    }
	
	public Boolean namingMatch(String name) throws Exception
	{
		if (name.length() == 0)
		{
			return false;
		}
		
		if (Pattern.compile("[^a-z0-9 ]").matcher(name).find())
		{
			return false;
		}
		
		return true;
	}

	public String generatePass()
	{
		//Randomly generate passwords total number of times
		String randomPassword = "";
		int randomPasswordLength = 16;
		int nonAlphaNumeric = new Random().nextInt(1, 14);
		//Randomly generate a character for the password length number of times
		for (int j = 0; j < randomPasswordLength; j++) 
		{
			//Add a random lowercase or UPPERCASE character to our randomPassword String
			if (nonAlphaNumeric == j) {
			    randomPassword += "-" + randomCharacter();
			}
			else randomPassword += randomCharacter();
		}
		return randomPassword;
	}

    public String generateRandomHash(int hashLength)
    {
        //Randomly generate passwords total number of times
        String randomHasg = "";

        //Randomly generate a character for the password length number of times
        for (int j = 0; j < hashLength; j++) 
        {
            //Add a random lowercase or UPPERCASE character to our randomPassword String
            randomHasg += randomCharacter();
        }

        return randomHasg;
    }

   //Create a function that randomly generates a letter (lowercase or uppercase) or number (0-9) using ASCII
   //'0' - '9' => 48-57 in ASCII
   //'A' - 'Z' => 65-90 in ASCII
   //'a' - 'z' => 97-122 in ASCII
   public char randomCharacter() {
      //We multiply Math.random() by 62 because there are 26 lowercase letters, 26 uppercase letters, and 10 numbers, and 26 + 26 + 10 = 62
      //This random number has values between 0 (inclusive) and 62 (exclusive)
      int rand = (int)(Math.random()*62);
      //0-61 inclusive = all possible values of rand
      //0-9 inclusive = 10 possible numbers/digits
      //10-35 inclusive = 26 possible uppercase letters
      //36-61 inclusive = 26 possible lowercase letters
      //If rand is between 0 (inclusive) and 9 (inclusive), we can say it's a number/digit
      //If rand is between 10 (inclusive) and 35 (inclusive), we can say it's an uppercase letter
      //If rand is between 36 (inclusive) and 61 (inclusive), we can say it's a lowercase letter
      if (rand <= 9) {
         //Number (48-57 in ASCII)
         //To convert from 0-9 to 48-57, we can add 48 to rand because 48-0 = 48
         int number = rand + 48;
         //This way, rand = 0 => number = 48 => '0'
         //Remember to cast our int value to a char!
         return (char)(number);
      } else if(rand <= 35) {
         //Uppercase letter (65-90 in ASCII)
         //To convert from 10-35 to 65-90, we can add 55 to rand because 65-10 = 55
         int uppercase = rand + 55;
         //This way, rand = 10 => uppercase = 65 => 'A'
         //Remember to cast our int value to a char!
         return (char)(uppercase);
      } else {
         //Lowercase letter (97-122 in ASCII)
         //To convert from 36-61 to 97-122, we can add 61 to rand because 97-36 = 61
         int lowercase = rand + 61;
         //This way, rand = 36 => lowercase = 97 => 'a'
         //Remember to cast our int value to a char!
         return (char)(lowercase);
      }
   }
}
