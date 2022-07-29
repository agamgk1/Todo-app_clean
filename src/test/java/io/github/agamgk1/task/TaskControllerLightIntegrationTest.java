package io.github.agamgk1.task;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskRepository repo;

    @MockBean
    private TaskQueryRepository taskQueryRepository;

    @Test
    void httpGet_returnsGivenTask() {
        //given
        String description = "foo";
        when(repo.findById(anyInt()))
            .thenReturn(Optional.of(new Task(description, LocalDateTime.now())));

        //when
        try {
            mockMvc.perform(get("/tasks/123"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(description)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
