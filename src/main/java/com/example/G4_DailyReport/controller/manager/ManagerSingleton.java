package com.example.G4_DailyReport.controller.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.UserRepository;

@Component
public class ManagerSingleton {
	private static User user;
	@Autowired
	private UserRepository userRepository;

	public User getUser() {
		if (user == null) {
			List<User> users = userRepository.findAll();
			for (User user : users) {
				if (user.getPosition().getName().equals("Manager")) {
					return user;

				}
			}
		}
		return user;
	}
}
