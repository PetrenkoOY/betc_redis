package redis.servicies;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import redis.entity.User;
import redis.repo.RedisUserRepository;
import redis.repo.RedisVariableRepository;
import redis.repo.UserRepo;

@Component
public class FetchingData implements ApplicationContextAware {
	private UserRepo userRepo;
	private RedisUserRepository redisUserRepository;
	private RedisVariableRepository redisVariableRepository;
	private ApplicationContext context;
	@Value("${worktime:30000}")
	long worktime;
	
	public FetchingData(UserRepo userRepo, RedisUserRepository redisUserRepository,
			RedisVariableRepository redisVariableRepository) {
		this.userRepo = userRepo;
		this.redisUserRepository = redisUserRepository;
		this.redisVariableRepository = redisVariableRepository;
	}

	private Integer lastUserId = 0;
	private List<User> newUsers = new ArrayList<User>();
	private int userNotFound = 0;
	private int userWasFound = 0;
	
	@Bean
	private void getNewUsers() throws InterruptedException {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < worktime) {
			newUsers = userRepo.findByIdGreaterThan(lastUserId);
			if (newUsers != null && newUsers.size() != 0) {
				addToHash(newUsers);
				userWasFound++;
				redisVariableRepository.save("userWasFound", userWasFound);
			} else {
				userNotFound++;
				redisVariableRepository.save("userNotFound", userNotFound);
			}
			Thread.sleep(5000);
		}
		((AbstractApplicationContext)context).registerShutdownHook();
	}

	private void addToHash(List<User> users) {
		for (User user : users) {
			if (lastUserId < user.getId())
				lastUserId = user.getId();
			redisUserRepository.save(user);
		}
	}

	@PreDestroy
	public void onDestroy() {
		System.out.println("----------------------------------------------------------------");
		System.out.println("New Users Was Found " + redisVariableRepository.get("userWasFound"));
		System.out.println("New Users Wasn't Found " + redisVariableRepository.get("userNotFound"));
		System.out.println("----------------------------------------------------------------");
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;		
	}
}
