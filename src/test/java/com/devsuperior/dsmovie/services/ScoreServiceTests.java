package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {

	@InjectMocks
	private ScoreService scoreService;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;
	
	private Long existingMovieId;
	private Long nonExistingMovieId;
	private UserEntity userEntity;
	private MovieEntity movieEntity;
	private ScoreEntity scoreEntity;
	private ScoreDTO scoreDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingMovieId = 1L;
		nonExistingMovieId = 2L;
		userEntity = UserFactory.createUserEntity();
		movieEntity = MovieFactory.createMovieEntity();
		scoreEntity = ScoreFactory.createScoreEntity();
		movieEntity.getScores().add(scoreEntity);
		scoreDTO = ScoreFactory.createScoreDTO();
		Mockito.doReturn(userEntity).when(userService).authenticated();
		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movieEntity));
		Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());
		Mockito.when(movieRepository.save(any())).thenReturn(movieEntity);
		Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);
	}

	@Test
	public void saveScoreShouldReturnMovieDTO() {
		MovieDTO result = scoreService.saveScore(scoreDTO);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(scoreDTO.getMovieId(), result.getId());
	}

	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		ScoreDTO dto = new ScoreDTO(nonExistingMovieId, 5.0);
		Assertions.assertThrows(ResourceNotFoundException.class, () -> scoreService.saveScore(dto));
	}
}
