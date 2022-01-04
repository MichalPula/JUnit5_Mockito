package mockito.animal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pulson.junit_mockito.mockito.animal.AnimalController;
import pulson.junit_mockito.mockito.animal.AnimalService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.*;

@WebMvcTest(AnimalController.class)
class AnimalControllerTest {
    //TESTY INTEGRACYJNE

    //https://stackoverflow.com/questions/44200720/difference-between-mock-mockbean-and-mockito-mock
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    AnimalService animalServiceMock;
    @Test
    @DisplayName("should return Hello, World")
    void should_return_default_message() throws Exception {
        when(animalServiceMock.getGreeting()).thenReturn("Hello, Mock");

        this.mockMvc.perform(get("/animals/greeting"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Mock"));
    }
}
