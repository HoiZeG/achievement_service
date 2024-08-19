package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.BadRequestException;
import faang.school.achievement.filter.achievement.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private AchievementProgressMapper achievementProgressMapper;
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementMapper achievementMapper;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private UserAchievementMapper userAchievementMapper;
    @Mock
    private List<AchievementFilter> achievementFilters;

    @InjectMocks
    private AchievementService achievementService;

    @BeforeEach
    void setUp() {
        achievementService = new AchievementService(
                achievementProgressRepository,
                achievementProgressMapper,
                achievementRepository,
                achievementMapper,
                achievementFilters,
                userAchievementRepository,
                userAchievementMapper
        );
    }

    @Test
    @DisplayName("Получение всех достижений с фильтрацией: тест успешного выполнения")
    void testGetAchievements() {
        AchievementFilterDto filterDto = new AchievementFilterDto();
        Achievement achievement = new Achievement();
        AchievementDto achievementDto = new AchievementDto();

        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);
        when(achievementFilters.stream()).thenReturn(Stream.of());

        List<AchievementDto> result = achievementService.getAchievements(filterDto);

        assertEquals(1, result.size());
        assertEquals(achievementDto, result.get(0));
    }

    @Test
    @DisplayName("Получение достижения по ID: тест успешного выполнения")
    void testGetAchievementById() {
        long id = 1L;
        Achievement achievement = new Achievement();
        AchievementDto achievementDto = new AchievementDto();

        when(achievementRepository.findById(id)).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto result = achievementService.getAchievementById(id);

        assertEquals(achievementDto, result);
    }

    @Test
    @DisplayName("Получение достижения по ID: тест на случай отсутствия достижения")
    void testGetAchievementById_NotFound() {
        long id = 1L;

        when(achievementRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> achievementService.getAchievementById(id));
    }

    @Test
    @DisplayName("Получение достижений пользователя: тест успешного выполнения")
    void testGetUserAchievements() {
        long userId = 1L;
        UserAchievement userAchievement = new UserAchievement();
        UserAchievementDto userAchievementDto = new UserAchievementDto();

        when(userAchievementRepository.findByUserId(userId)).thenReturn(List.of(userAchievement));
        when(userAchievementMapper.toListDto(List.of(userAchievement))).thenReturn(List.of(userAchievementDto));

        List<UserAchievementDto> result = achievementService.getUserAchievements(userId);

        assertEquals(1, result.size());
        assertEquals(userAchievementDto, result.get(0));
    }

    @Test
    @DisplayName("Получение достижений пользователя в процессе выполнения: тест успешного выполнения")
    void testGetUserAchievementsInProgress() {
        long userId = 1L;
        AchievementProgress achievementProgress = new AchievementProgress();
        AchievementProgressDto achievementProgressDto = new AchievementProgressDto();

        when(achievementProgressRepository.findByUserId(userId)).thenReturn(List.of(achievementProgress));
        when(achievementProgressMapper.toListDto(List.of(achievementProgress))).thenReturn(List.of(achievementProgressDto));

        List<AchievementProgressDto> result = achievementService.getUserAchievementsInProgress(userId);

        assertEquals(1, result.size());
        assertEquals(achievementProgressDto, result.get(0));
    }
}