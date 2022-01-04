package mockito.animal;

import pulson.junit_mockito.mockito.MyFinalClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import static org.assertj.core.api.Assertions.*;

import org.apache.commons.lang.RandomStringUtils;
import pulson.junit_mockito.mockito.animal.Animal;
import pulson.junit_mockito.mockito.animal.AnimalRepository;
import pulson.junit_mockito.mockito.animal.AnimalService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AnimalServiceTest {

//Metoda jest niepotrzebna ponieważ MockitoExtension.class inicjalizuje @Mock, @Spy, @Captor i @InjectMocks
//    @Before
//    public void initializeMocks() {
//      MockitoAnnotations.openMocks(this);
//    }

    private List<Animal> getMockData() {
        return Arrays.asList(new Animal("Cat", 3), new Animal("Dog", 5),
                new Animal("Lion", 10), new Animal("Elephant", 50),
                new Animal("Tortoise", 70), new Animal("Greenland shark", 200));
    }


    @Mock
    AnimalRepository animalRepositoryMock;

    @InjectMocks//tworzy instancję AnimalService i wstrzykuje do niego mocka AnimalRepository
    //równoznaczne z AnimalService animalService = new AnimalService(animalRepoMock);
    private AnimalService animalService;


    @Test
    @DisplayName("Return greenland shark when getOldestAnimal() works properly")
    void Should_ReturnGreenlandShark_When_GetOldestAnimalIsWorkingProperly() {
        when(animalRepositoryMock.findAll()).thenReturn(getMockData());

        String oldestAnimalName = animalService.getOldestAnimal().getName();

        verify(animalRepositoryMock, times(1)).findAll();

        assertEquals("Greenland shark", oldestAnimalName);
    }

    @Test
    void testMethodsCreatingMocks() {
        //Simple mocking - public static <T> T mock(Class<T> classToMock) - Equal to @Mock
        List<String> simpleMock = mock(ArrayList.class);
        when(simpleMock.add(anyString())).thenReturn(false);
        assertFalse(simpleMock.add(RandomStringUtils.randomAlphabetic(6)));

        //With Mock's name - public static <T> T mock(Class<T> classToMock, String name)
        List<String> namedMock = mock(ArrayList.class, "MyFancyMock");

        //With Answer - public static <T> T mock(Class<T> classToMock, Answer defaultAnswer)
        List<String> answerMock = mock(ArrayList.class, invocation -> {
            //Complicated calculations :D
            return 5 > 10;
        }); //przy każdym wywołaniu .add() mock zwróci wartość false
        assertFalse(simpleMock.add(RandomStringUtils.randomAlphabetic(6)));

        //With MockSettings
        MockSettings customSettings = withSettings().defaultAnswer(invocation -> {
            return 5 > 10;
        });
        List<String> settingsMock = mock(ArrayList.class, customSettings);
        assertFalse(settingsMock.add(RandomStringUtils.randomAlphabetic(6)));
    }

    @Test
    void twoWaysOfUsingMockito(@Mock List<String> listMock) {
        when(listMock.add(anyString())).thenReturn(false);
        doReturn(false).when(listMock).add(anyString());
        //doReturn() używana jest z obiektami Spy
        when(listMock.get(anyInt())).thenThrow(NullPointerException.class);
        doThrow(NullPointerException.class).when(listMock.get(anyInt()));
        //a doThrow() z metodami void
        when(listMock.add(anyString())).thenAnswer(invocationOnMock -> false);
        doAnswer(invocationOnMock -> false).when(listMock.add(anyString()));

        when(listMock.add(anyString())).thenCallRealMethod();
        doCallRealMethod().when(listMock.add(anyString()));
        //doCallRealMethod() i doNothing() używane są z metodami void
    }

    @Test
    void thenReturnMultipleValues() {
        when(animalRepositoryMock.getGOATString()).thenReturn("Kanye", "West", "Ye");
        //pierwsze wywołanie metody getGOATString() zwróci Kanye, drugie - West, a trzecie i każde kolejne - Ye
        System.out.println(animalRepositoryMock.getGOATString());//Kanye
        System.out.println(animalRepositoryMock.getGOATString());//West
        System.out.println(animalRepositoryMock.getGOATString());//Ye
        assertEquals("Ye", animalRepositoryMock.getGOATString());// Ye = Ye
    }

    @Test
    void callingRealMethodOnMock() {
        //assertEquals("Ye", animalRepositoryMock.getGOATString()); //Expected:Ye, actual:null
        when(animalRepositoryMock.getGOATString()).thenCallRealMethod();
        //bez wcześniejszego stubowania metody zwracana jest prawidłowa wartość
        assertEquals("Ye", animalRepositoryMock.getGOATString());
    }

    @Test
    void chainingMethodCalls(@Mock List<Integer> integerList) {
        //Łączenie wywołań metod
        when(integerList.get(0)).thenReturn(5).thenReturn(6).thenThrow(IllegalStateException.class);
        when(integerList.get(1)).thenReturn(10);

        assertEquals(5, integerList.get(0));
        assertEquals(6, integerList.get(0));
        assertThrows(IllegalStateException.class, () -> integerList.get(0));
        assertEquals(10, integerList.get(1));
    }

    @Test
    void testThrowingExceptions() {
        when(animalRepositoryMock.getById(1000L)).thenThrow(
                new EntityNotFoundException("Animal with id=1000 not found!"));

        Throwable ex = assertThrows(EntityNotFoundException.class, () -> animalRepositoryMock.getById(1000L));
        assertEquals("Animal with id=1000 not found!", ex.getMessage());

        doThrow(IllegalStateException.class).when(animalRepositoryMock).someVoidMethod(anyString());
        assertThrows(IllegalStateException.class, () -> animalRepositoryMock.someVoidMethod("ok"));
    }

    @Test
    void testVoidMethods() {
        //default void someVoidMethod(String str) { int a = 5; };
        //Metody void mogą być łączone z metodami doNothing(), doThrow(), doAnswer() i doCallRealMethod()
        doNothing().when(animalRepositoryMock).someVoidMethod(anyString());
        animalRepositoryMock.someVoidMethod("ok");
        verify(animalRepositoryMock, atMostOnce()).someVoidMethod(anyString());

        doThrow(SecurityException.class).when(animalRepositoryMock).someVoidMethod("exception");
        assertThrows(SecurityException.class, () -> animalRepositoryMock.someVoidMethod("exception"));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        doNothing().when(animalRepositoryMock).someVoidMethod(captor.capture());
        animalRepositoryMock.someVoidMethod("captured");
        assertEquals("captured", captor.getValue());

        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            assertEquals("fine", arg0);
            return null;
        }).when(animalRepositoryMock).someVoidMethod(anyString());
        animalRepositoryMock.someVoidMethod("fine");

        doCallRealMethod().when(animalRepositoryMock).someVoidMethod(anyString());
    }

    @Test
    void testMockingFinalMethodsAndClasses() {
        MyFinalClass myFinalClass = new MyFinalClass();
        MyFinalClass finalClassMock = mock(MyFinalClass.class);
        when(finalClassMock.finalMethodReturningString(anyString())).thenReturn("let's see");

        assertEquals(finalClassMock.finalMethodReturningString("mockable"),
                myFinalClass.finalMethodReturningString("let's see"));
    }

    @Test
    void testMockingStaticMethods() {
        //MockedStatic reprezentuje mocka mającego scope wątku, który go posiada - a nie scope metody czy klasy
        //dlatego muszą zostać zamknięte. MockedStatic implementuje AutoCloseable, co umożliwia użycie
        //try-with-resources i automatyczne zamknięcie mocka.
        assertThat(AnimalService.staticMethodReturningOK()).isEqualTo("OK");
        assertThat(AnimalService.staticMethodAdd(2,2)).isEqualTo(4);
        try (MockedStatic<AnimalService> service = mockStatic(AnimalService.class)) {
            //service.when(AnimalService::staticMethodReturningOK).thenReturn("aesthetics");
            when(AnimalService.staticMethodReturningOK()).thenReturn("aesthetics");
            assertThat(AnimalService.staticMethodReturningOK()).isEqualTo("aesthetics");

            when(AnimalService.staticMethodAdd(2,2)).thenReturn(15);
            assertThat(AnimalService.staticMethodAdd(2,2)).isEqualTo(15);
        }//Po zamknięciu Mocka klasa wraca do swojego oryginalnego działania i zwraca prawidłowy wynik
        assertThat(AnimalService.staticMethodReturningOK()).isEqualTo("OK");
        assertThat(AnimalService.staticMethodAdd(2,2)).isEqualTo(4);
    }

    //Mockito pozwala na behaviour testing, czyli sprawdzenie, z jakimi parametrami i ile razy metoda została wywołana,
    //a nie jaki wynik zwróci. Używana jest do tego statyczna metoda verify() klasy Mockito.
    @Test
    void testVerify(@Mock AnimalService animalServiceMock) {
        when(animalServiceMock.getCustomString()).thenReturn("Ye");

        animalServiceMock.setCustomString("Donda");
        animalServiceMock.setCustomString("My Beautiful Dark Twisted Fantasy");

        System.out.println(animalServiceMock.getCustomString());//Ye
        System.out.println(animalServiceMock.getCustomString());//Ye


        //Czy metoda setCustomString() została wywołana z argumentem "Donda"?
        verify(animalServiceMock).setCustomString("Donda");

        //Czy metoda została wywołana 2 razy?
        verify(animalServiceMock, times(2)).getCustomString();

        //Inne metody sprawdzające ilość wywołań metod
        //Jeśli nie interesują nas wywołania z konkretnymi argumentami można użyć
        //konstrukcji any np. anyInt(), anyList(), anyString(), any(MyClass.class)
        verify(animalServiceMock, never()).addAnimal(any(Animal.class));
        verify(animalServiceMock, atLeastOnce()).getCustomString();
        verify(animalServiceMock, atMost(2)).getCustomString();

        //Weryfikacja kolejności wywołań - zamiana linii miejscami spowoduje błąd
        InOrder order = inOrder(animalServiceMock);
        order.verify(animalServiceMock).setCustomString("Donda");
        order.verify(animalServiceMock).setCustomString("My Beautiful Dark Twisted Fantasy");
        order.verify(animalServiceMock, times(2)).getCustomString();

        //Weryfikacja argumentów przy użyciu ArgumentCaptor
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(animalServiceMock, times(2)).setCustomString(captor.capture());
        System.out.println(captor.getAllValues());//[Donda, My Beautiful Dark Twisted Fantasy]
        //Jeśli metoda jest wywoływana więcej niż raz - trzeba użyć times()

        //Czy na obiekcie nie została wywołana żadna metoda?
        //verifyNoInteractions(animalServiceMock);
        //Czy na mocku są wywoływane jeszcze jakieś metody?
        verifyNoMoreInteractions(animalServiceMock);
    }

    @Test
    void testArgumentMatchers(@Mock AnimalService animalServiceMock) {
        //Zwraca obiekt równy podanemu
        when(animalServiceMock.methodReturningPassedString(eq("ok"))).thenReturn("<3");
        //Obiekt równy podanemu, porównując jego atrybuty refleksją (przydatne gdy obiekty źle implementują equals)
        when(animalServiceMock.methodReturningPassedString(refEq("ok"))).thenReturn("<3");
        //Zwraca obiekt określonego typu - String.class, Animal.class itp
        when(animalServiceMock.methodReturningPassedString(any(String.class))).thenReturn("<3");
        //Zwraca obiekt implementujący określony typ
        when(animalServiceMock.methodReturningPassedString(isA(String.class))).thenReturn("<3");
        //Zwraca dowolną wartość dla prymitywów -anyString(), anyInt(), anyBoolean itp
        when(animalServiceMock.methodReturningPassedString(anyString())).thenReturn("<3");
        //Zwraca String spełniający warunek contains(), matches() lub startsWith()
        when(animalServiceMock.methodReturningPassedString(contains(""))).thenReturn("<3");
        //Custom ArgumentMatcher, zwraca własną implementację ArgumentMatcher<T>
        when(animalServiceMock.methodReturningPassedString(argThat(argument -> {
            return argument.equals("well");
        }))).thenReturn("<3");

        //Jeśli metoda przyjmuje więcej niż 1 argument, nie możemy mieszać matcherów ze standardowymi argumentami
        //Invalid use of argument matchers! 2 matchers expected, 1 recorded
        //when(animalServiceMock.methodTakingTwoArguments(anyInt(), "This will fail"));

        assertEquals("<3", animalServiceMock.methodReturningPassedString("well"));//<3
    }

    @Test
    void testAnswers(@Mock AnimalService animalServiceMock) {
        List<String> list = new ArrayList<>(Arrays.asList("1", "2", "3"));
        when(animalServiceMock.methodReturningPassedString(eq("well"))).thenAnswer(invocation -> {
            //Lambda 'invocation' to implementacja interfejsu Answer z metodą T answer, która zwraca wartość
            //Sam obiekt ‘invocation’ przechowuje mocka na którym wywołaliśmy metodę i jej argumenty
            String answer = (Integer.valueOf(list.get(0)) + 9 + "! <3").toUpperCase();
            return answer;
        });
        assertEquals("10! <3", animalServiceMock.methodReturningPassedString("well"));
    }

    //ArgumentCaptor pozwala uzyskać dostęp do argumentów, z którymi została wywołana dana metoda
    @Captor
    private ArgumentCaptor<List<String>> captor;
    @Test
    void testArgumentCaptor(@Mock List<String> mockedList) {
        mockedList.addAll(Arrays.asList("Donda", "2021"));
        //Przechwyć argumenty podane metodzie addAll()
        verify(mockedList).addAll(captor.capture());
        System.out.println(captor.getValue()); //[Donda, 2021]
    }

    @Spy
    List<String> stringListSpy = new ArrayList<>();
    @Test
    void testSpy() {
        //List<String> spyList = spy(new ArrayList<>()); //same result

        //when(spyStringList.get(99)).thenReturn("Well");
        //assertEquals("Well", spyStringList.get(99));
        //Używając obiektów typu Spy należy korzystać z konstrukcji doReturn().when() zamiast when().thenReturn()!
        //Używając when().thenReturn() z obiektami Spy dostaniemy:
        //java.lang.IndexOutOfBoundsException: Index 99 out of bounds for length 0
        doThrow(NullPointerException.class).when(stringListSpy).add(anyString());
        assertThrows(NullPointerException.class, () -> stringListSpy.add("fine"));

        doReturn("ok").when(stringListSpy).get(99);
        assertEquals("ok", stringListSpy.get(99));
        verify(stringListSpy, times(1)).get(99);

        stringListSpy.add("1");
        stringListSpy.add("2");
        stringListSpy.add("3");
        assertEquals(3, stringListSpy.size());
    }

    @Test
    void testBDDMockito() {
        //import static org.mockito.BDDMockito.*;
        //pure Mockito
        when(animalRepositoryMock.findAll()).thenReturn(getMockData());
        String oldestAnimalName1 = animalService.getOldestAnimal().getName();
        verify(animalRepositoryMock, times(1)).findAll();
        assertEquals("Greenland shark", oldestAnimalName1);

        //BDDMockito
        given(animalRepositoryMock.findAll()).willReturn(getMockData());
        String oldestAnimalName2 = animalService.getOldestAnimal().getName();
        then(animalRepositoryMock).should(times(2)).findAll();
        assertEquals("Greenland shark", oldestAnimalName2);
    }
}
