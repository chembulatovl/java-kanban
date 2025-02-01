
import model.Task;
import service.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class InMemoryHistoryManagerTest {

    private InMemoryTaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        historyManager = Managers.getDefaultHistoryManager();
    }

    @Test
    void shouldAddAndGetHistory() {
        Task task = new Task("Задача", "Описание задачи",
                LocalDateTime.of(2025, 1, 1, 12, 0), Duration.ofMinutes(30));
        taskManager.createTask(task);
        historyManager.addInHistory(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть null");
        assertEquals(1, history.size());
        assertEquals(task, history.get(0), "История должна содержать задачу");
    }


    @Test
    void shouldRemoveFromHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1",
                LocalDateTime.of(2025, 1, 1, 12, 0), Duration.ofMinutes(30));
        Task task2 = new Task("Задача 2", "Описание задачи 2",
                LocalDateTime.of(2025, 1, 1, 12, 30), Duration.ofMinutes(30));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        historyManager.addInHistory(task1);
        historyManager.addInHistory(task2);
        historyManager.removeFromHistory(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
        assertFalse(history.contains(task1), "Задача 1 должна быть удалена из истории");
    }

    @Test
    void shouldNotAddDuplicateToHistory() {
        Task task = new Task("Задача", "Описание задачи",
                LocalDateTime.of(2025, 1, 1, 12, 0), Duration.ofMinutes(30));
        taskManager.createTask(task);
        historyManager.addInHistory(task);
        historyManager.addInHistory(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "В истории должна быть одна задача");
        assertEquals(task, history.get(0));
    }


    @Test
    void shouldGetEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть null");
        assertTrue(history.isEmpty(), "История должна быть пуста");
    }
}