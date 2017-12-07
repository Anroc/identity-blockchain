package de.iosl.blockchain.identity.core.provider.user;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/claims")
public class LoginController {

	@GetMapping
	public List<String> testmehtod() {
		return Lists.newArrayList("Vim ist bessel als java lol.");
	}
}
