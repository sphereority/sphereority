package server;

import common.*;

import java.util.*;

class UserNames {
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

    Hashtable userNames;
    UserNames (){
	userNames = new Hashtable(3);
        /*
	try {
	    userNames.put("user1","password1");
	    userNames.put("user2","password2");
	    userNames.put("user3","password3");
	}
	catch (NullPointerException npe){
	    System.out.println("You tried to insert a user name that was null into the hashtable");
	}
        */
    }
    boolean checkUserPass(String username,String passwd){
	return userNames.containsKey(username) && (userNames.get(username)).equals(passwd);
    }
    // PRE:  assigned(username) & String(username) & username != ""
    //       & assigned(passwd) & String(passwd)
    // POST: <username, passwd> is part of self.UserNames
    //       & FCTVAL == (username was not previously in self.userNames)
    boolean addUser(String username, String passwd){
	if (!userNames.containsKey(username)) {
	    userNames.put(username,passwd);
            return true;
        }
        else
            return false;
    }
    public static void main (String [] args){
	UserNames un = new UserNames();
	System.out.println(un.checkUserPass("user1","password1"));
    }
}
