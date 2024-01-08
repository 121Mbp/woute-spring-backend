package xyz.heetaeb.Woute.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;

import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

public interface EmailService {
	
	String sendSimpleMessage(String to)throws Exception;
}
