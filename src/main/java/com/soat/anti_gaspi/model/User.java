package com.soat.anti_gaspi.model;

import java.util.ArrayList;
import java.util.List;


public class User {

	private List<Offer> offers = new ArrayList<Offer>();
	private List<User> friends = new ArrayList<User>();
	
	public List<User> getFriends() {
		return friends;
	}
	
	public void addFriend(User user) {
		friends.add(user);
	}

	public void addOffer(Offer trip) {
		offers.add(trip);
	}
	
	public List<Offer> trips() {
		return offers;
	}

}
